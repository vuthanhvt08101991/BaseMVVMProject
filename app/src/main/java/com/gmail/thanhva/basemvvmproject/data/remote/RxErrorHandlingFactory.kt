package com.gmail.thanhva.basemvvmproject.data.remote

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import io.reactivex.*
import io.reactivex.functions.Function
import retrofit2.*
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.io.IOException
import java.lang.reflect.Type

/**
 *  Create by thanhva on 18/08/2020
 *  Class RxErrorHandlingFactory
 */
class RxErrorHandlingFactory : CallAdapter.Factory() {

    private val instance = RxJava2CallAdapterFactory.createAsync()

    @Suppress("UNCHECKED_CAST")
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? =
        RxCallAdapterWrapper(
            instance.get(returnType, annotations, retrofit) as CallAdapter<Any, Any>
        )
}

class RxCallAdapterWrapper<R>(
    private val wrapped: CallAdapter<R, Any>
) : CallAdapter<R, Any> {

    override fun responseType(): Type = wrapped.responseType()

    override fun adapt(call: Call<R>): Any {
        return when (val result = wrapped.adapt(call)) {
            is Single<*> -> {
                result.onErrorResumeNext(Function<Throwable, SingleSource<Nothing>> { throwable ->
                    Single.error(convertToBaseException(throwable))
                })
            }

            is Observable<*> -> {
                result.onErrorResumeNext(Function<Throwable, ObservableSource<Nothing>> { throwable ->
                    Observable.error(convertToBaseException(throwable))
                })
            }

            is Completable -> {
                result.onErrorResumeNext { throwable ->
                    Completable.error(convertToBaseException(throwable))
                }
            }

            is Flowable<*> -> {
                result.onErrorResumeNext(Function<Throwable, Flowable<Nothing>> { throwable ->
                    Flowable.error(convertToBaseException(throwable))
                })
            }

            is Maybe<*> -> {
                result.onErrorResumeNext(Function<Throwable, Maybe<Nothing>> { throwable ->
                    Maybe.error(convertToBaseException(throwable))
                })
            }

            else -> result
        }
    }
}


fun convertToBaseException(throwable: Throwable): BaseException =
    when (throwable) {
        is BaseException -> throwable

        is IOException -> BaseException.toNetworkError(throwable)

        is HttpException -> {
            val response = throwable.response()
            val httpCode = throwable.code()

            if (response.errorBody() == null) {
                BaseException.toHttpError(
                    httpCode = httpCode,
                    response = response
                )
            }

            val serverErrorResponseBody = try {
                throwable.response().errorBody()?.string() ?: ""
            } catch (e: Exception) {
                ""
            }

            val serverErrorResponse =
                try {
                    Gson().fromJson(serverErrorResponseBody, ServerErrorResponse::class.java)
                } catch (e: Exception) {
                    ServerErrorResponse(message = serverErrorResponseBody)
                }

            if (serverErrorResponse != null) {
                BaseException.toServerError(
                    serverErrorResponse = serverErrorResponse,
                    httpCode = httpCode
                )
            } else {
                BaseException.toHttpError(
                    response = response,
                    httpCode = httpCode
                )
            }
        }

        else -> BaseException.toUnexpectedError(throwable)
    }

class BaseException(
    val errorType: ErrorType,
    val serverErrorResponse: ServerErrorResponse? = null,
    val response: Response<*>? = null,
    val httpCode: Int = 0,
    cause: Throwable? = null
) : RuntimeException(cause?.message, cause) {

    override val message: String?
        get() = when (errorType) {
            ErrorType.HTTP -> response?.message()

            ErrorType.NETWORK -> cause?.message

            ErrorType.SERVER -> serverErrorResponse?.message

            ErrorType.UNEXPECTED -> cause?.message
        }

    companion object {
        fun toHttpError(response: Response<*>, httpCode: Int) =
            BaseException(
                errorType = ErrorType.HTTP,
                response = response,
                httpCode = httpCode
            )

        fun toNetworkError(cause: Throwable) =
            BaseException(
                errorType = ErrorType.NETWORK,
                cause = cause
            )

        fun toServerError(serverErrorResponse: ServerErrorResponse, httpCode: Int) =
            BaseException(
                errorType = ErrorType.SERVER,
                serverErrorResponse = serverErrorResponse,
                httpCode = httpCode
            )

        fun toUnexpectedError(cause: Throwable) =
            BaseException(
                errorType = ErrorType.UNEXPECTED,
                cause = cause
            )
    }
}

/**
 * Identifies the error type which triggered a [BaseException]
 */
enum class ErrorType {
    /**
     * An [IOException] occurred while communicating to the server.
     */
    NETWORK,

    /**
     * A non-2xx HTTP status code was received from the server.
     */
    HTTP,

    /**
     * A error server with code & message
     */
    SERVER,

    /**
     * An internal error occurred while attempting to execute a request. It is best practice to
     * re-throw this exception so your application crashes.
     */
    UNEXPECTED
}

// TODO update server error response
data class ServerErrorResponse(
    @SerializedName("message") val message: String? = null,
    @SerializedName("errorCode") val errorCode: Int? = null,
    @SerializedName("error") val error: String? = null,
    @SerializedName("errors") val errors: List<FieldErrorResponse>? = null
)

data class FieldErrorResponse(
    @SerializedName("field") val field: String? = null,
    @SerializedName("message") val message: String? = null
)