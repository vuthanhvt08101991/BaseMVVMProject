package com.gmail.thanhva.basemvvmproject.ui.base

import android.graphics.Rect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.gmail.thanhva.basemvvmproject.BR
import com.gmail.thanhva.basemvvmproject.R
import com.gmail.thanhva.basemvvmproject.data.constant.ErrorType
import com.gmail.thanhva.basemvvmproject.listener.OnFragmentLifeCycle
import com.gmail.thanhva.basemvvmproject.ui.screen.MainActivity
import com.gmail.thanhva.basemvvmproject.ui.screen.ShareViewModel
import com.gmail.thanhva.basemvvmproject.ui.widgets.DragEdge
import com.gmail.thanhva.basemvvmproject.ui.widgets.SwipeBackLayout
import com.gmail.thanhva.basemvvmproject.utils.getWidthScreen
import com.gmail.thanhva.basemvvmproject.utils.hideKeyboardFromView
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

/**
 *  Create by thanhva on 19/08/2020
 *  Class BaseFragmentBinding
 */
abstract class BaseFragmentBinding<ViewBinding : ViewDataBinding, ViewModel : BaseViewModel> :
    Fragment(), SwipeBackLayout.SwipeBackListener {
    protected val shareViewModel: ShareViewModel by sharedViewModel()

    lateinit var viewBinding: ViewBinding

    abstract val viewModel: ViewModel

    /**
     * Declare layout file
     */
    @get:LayoutRes
    abstract val layoutId: Int
    private var isKeyboardShowing = false
    private var swipeBackLayout: SwipeBackLayout? = null
    private var ivShadow: ImageView? = null
    var onFragmentLifeCycle: OnFragmentLifeCycle? = null
    protected var isInitFragment = false

    /**
     * get TAG of fragment, use when add/replace fragment
     */
    abstract fun getTagFragment(): String

    fun isViewBindingInitialized() = ::viewBinding.isInitialized

    /**
     * call when receiver
     */
    open fun beforeAddContent() {}

    abstract fun initView()

    abstract fun initData()

    abstract fun observeField()

    final override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeAddContent()
    }

    final override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        initView()
        return getContainer(viewBinding.root)
    }

    final override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.viewTreeObserver.addOnGlobalLayoutListener {
            val r = Rect()
            view.getWindowVisibleDisplayFrame(r)
            val screenHeight = view.rootView.height
            // r.bottom is the position above soft keypad or device button.
            // if keypad is shown, the r.bottom is smaller than that before.
            val keypadHeight = screenHeight - r.bottom
            if (keypadHeight > screenHeight * 0.15) { // 0.15 ratio is perhaps enough to determine keypad height.
                // keyboard is opened
                if (!isKeyboardShowing) {
                    isKeyboardShowing = true
                    onKeyboardVisibilityChanged(true)
                }
            } else {
                // keyboard is closed
                if (isKeyboardShowing) {
                    isKeyboardShowing = false
                    onKeyboardVisibilityChanged(false)
                }
            }
        }

        viewBinding.apply {
            // TODO: remove comments
            // setVariable(BR.viewModel, viewModel)
            root.isClickable = true
            root.isFocusable = true
            lifecycleOwner = viewLifecycleOwner
            executePendingBindings()
        }
        isInitFragment = true
        initData()
    }

    private fun getContainer(view: View): View {
        if (isSwipeBack()) {
            val container = RelativeLayout(context)
            context?.let { ctx ->
                swipeBackLayout = SwipeBackLayout(ctx).apply {
                    coordinatesCanSwipe = getWidthScreen(activity) / 5
                    dragEdge = DragEdge.LEFT
                    swipeBackListener = this@BaseFragmentBinding
                    addView(view)
                }

                ivShadow = ImageView(context).apply {
                    setBackgroundColor(
                        resources.getColor(R.color.black_opacity_30)
                    )
                }

                val params = RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT
                )

                container.apply {
                    addView(ivShadow, params)
                    addView(swipeBackLayout)
                }
            }
            return container
        } else {
            return view
        }
    }

    fun showLoadingFragment(cancelable: Boolean) {
        viewBinding.root.hideKeyboardFromView()
        (activity as BaseActivity<*>?)?.showLoading(cancelable)
    }

    fun hideLoadingFragment() {
        (activity as BaseActivity<*>?)?.hideLoading()
    }

    fun showLoadingFragment() {
        showLoadingFragment(false)
    }

    /**
     * use when single click in multi views
     */
    @Synchronized
    fun canNotClick(): Boolean {
        (activity as BaseActivity<*>?)?.thresholdClickTime?.let { thresholdClickTime ->
            return if (thresholdClickTime.isBlockClick()) {
                true
            } else {
                thresholdClickTime.setBlockClick(true)
                false
            }
        }
        return false
    }

    override fun onViewPositionChanged(fractionAnchor: Float, fractionScreen: Float) {
        ivShadow?.alpha = 1 - fractionScreen
    }

    override fun onSwipeComplete() {
        viewBinding.root.hideKeyboardFromView()
        popFragment()
    }

    override fun onSwipeCancel() {
        //Do something in here
    }

    /**
     * callback for parent fragment when click back button
     */
    open fun onBackClick() {
        if (canNotClick()) {
            return
        }
        viewBinding.root.hideKeyboardFromView()
        (activity as MainActivity?)?.onBackPressed()
    }

    fun setEnableSwipe(status: Boolean) {
        swipeBackLayout?.enableSwipe = status
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
                if (isLoading) {
                    showLoadingFragment()
                } else {
                    hideLoadingFragment()
                }
            })

            baseErrorEvent.observe(viewLifecycleOwner, Observer { baseError ->
                baseError?.let {
                    when (baseError) {
                        ErrorType.NO_INTERNET -> {
                            //handle show error message
                            if (!this@BaseFragmentBinding.isResumed) return@Observer
                            (activity as BaseActivity<*>?)?.showErrorDialog(
                                R.string.txt_no_internet_error_message,
                                R.string.ok_btn
                            )
                        }

                        ErrorType.CONNECT_TIME_OUT -> {
                            //handle show error message
                            if (!this@BaseFragmentBinding.isResumed) return@Observer
                            (activity as BaseActivity<*>?)?.showErrorDialog(
                                R.string.txt_connect_timeout_error_message,
                                R.string.ok_btn
                            )
                        }

                        ErrorType.FORCE_UPDATE -> {
                            //handle force update application
                        }

                        ErrorType.UNAVAILABLE -> {
                            if (!this@BaseFragmentBinding.isResumed) return@Observer
                            (activity as BaseActivity<*>?)?.showErrorDialog(
                                R.string.txt_unavailable_error_message,
                                R.string.ok_btn
                            )
                        }

                        ErrorType.UNKNOWN -> {
                            if (!this@BaseFragmentBinding.isResumed) return@Observer
                            (activity as BaseActivity<*>?)?.showErrorDialog(
                                R.string.txt_unknown_error_message,
                                R.string.ok_btn
                            )
                        }
                    }
                }
            })

            isBackClick.observe(viewLifecycleOwner, Observer { isBackClick ->
                if (isBackClick) {
                    onBackClick()
                }
            })
        }
        observeField()
    }

    /**
     * Fragment child override can use
     *
     * @param isShow : true if Keyboard is show
     */
    open fun onKeyboardVisibilityChanged(isShow: Boolean) {}

    /**
     * override if need
     */
    protected open fun isSwipeBack() = true

    override fun onDestroy() {
        viewModel.clearDisposable()
        onFragmentLifeCycle?.let { lifeCycle ->
            if (getTagFragment().isNotEmpty()) {
                lifeCycle.onFragmentDestroy(getTagFragment())
            }
        }
        super.onDestroy()
    }

    fun popFragment() {
        fragmentManager?.let {
            if (it.backStackEntryCount > 1) {
                it.popBackStack()
            } else {
                activity?.finish()
            }
        }
    }
}