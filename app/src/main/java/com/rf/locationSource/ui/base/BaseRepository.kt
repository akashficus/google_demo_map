package com.rf.locationSource.ui.base

import android.content.Context
import android.util.Log
import com.rf.locationSource.R
import com.rf.locationSource.utils.APIResponseCode
import com.rf.locationSource.utils.APIResponseCode.*
import com.rf.locationSource.utils.CallBackKt
import com.rf.locationSource.utils.Constant
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.Objects


/**
 * Akash.Singh
 * RootFicus.
 */
abstract class BaseRepository {

    fun <P> execute(
        call: Call<BaseResponseModel<P>>,
        success: (payload: P) -> Unit,
        fail: (error: String) -> Unit,
        context: Context,
        message: (message: String) -> Unit
    ) {
        val callBack = CallBackKt<BaseResponseModel<P>>().apply {
            onResponse = { response_ ->
                try {
                    when (response_.code()) {
                        ResponseCode200.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    if (body_.status != null) {
                                        when (body_.status) {
                                            200.0 -> {
                                                success.invoke(results_)
                                            }
                                            200 -> {
                                                success.invoke(results_)
                                            }
                                            true -> {
                                                success.invoke(results_)
                                            }
                                            else -> {
                                                fail.invoke(body_.message ?: Constant.KEY_SOMETHING_WENT_WRONG)
                                            }
                                        }
                                    } else if (body_.message?.equals(Constant.KEY_SUCCESS.lowercase()) == true) {
                                        success.invoke(results_)
                                    } else if (body_.message!!.contains(Constant.KEY_SUCCESS)) {
                                        success.invoke(results_)
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(body_.message ?: Constant.KEY_SOMETHING_WENT_WRONG)
                                }
                            } ?: kotlin.run {
                                fail.invoke(Constant.KEY_SOMETHING_WENT_WRONG)
                            }
                        }
                        ResponseCode201.codeValue -> {
                            response_.body()?.let { body_ ->
                                body_.data?.let { results_ ->
                                    when (body_.status) {
                                        200.0 -> {
                                            success.invoke(results_)
                                        }
                                        200 -> {
                                            success.invoke(results_)
                                        }
                                        true -> {
                                            success.invoke(results_)
                                        }
                                        else -> {
                                            fail.invoke(body_.message ?: Constant.KEY_SOMETHING_WENT_WRONG)
                                        }
                                    }
                                } ?: kotlin.run {
                                    fail.invoke(body_.message ?: Constant.KEY_SOMETHING_WENT_WRONG)
                                }
                            } ?: kotlin.run {
                                fail.invoke(Constant.KEY_SOMETHING_WENT_WRONG)
                            }
                        }
                        ResponseCode404.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                        ResponseCode401.codeValue -> {
                            getErrorBody(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                        ResponseCode403.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                        ResponseCode429.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                        ResponseCode500.codeValue -> {
                            getErrorMessage(response_)?.let {
                                fail.invoke(it)
                            }
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    fail.invoke(Constant.KEY_SOMETHING_WENT_WRONG)
                }
            }

            onFailure = { t ->
                t?.printStackTrace()
                if (t.toString().contains("failed to connect")) {
                    fail.invoke(context.getString(R.string.key_no_network))
                } else {
                    fail.invoke(Constant.KEY_SOMETHING_WENT_WRONG)
                    Log.i("Error Something", "::" + t.toString())
                }
            }
        }

        // Enqueue the request
        call.enqueue(callBack)
    }

    open fun getErrorBody(response: Response<*>): String? {
        val errorBody = response.errorBody()
        val errorMessage: String = try {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Objects.isNull(errorBody)
                } else {
                    errorBody == null
                }
            ) {
                response.message()
            } else {
                JSONObject(errorBody?.string()).getString("error")
            }
        } catch (e: IOException) {
            throw Exception("could not read error body", e)
        }
        return errorMessage
    }
    open fun getErrorMessage(response: Response<*>): String? {
        val errorBody = response.errorBody()
        val errorMessage: String = try {
            if (if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    Objects.isNull(errorBody)
                } else {
                    errorBody == null
                }
            ) {
                response.message()
            } else {
                JSONObject(errorBody?.string()).getString("message")
            }
        } catch (e: IOException) {
            throw Exception("could not read error body", e)
        }
        return errorMessage
    }

        fun <P> execute1(
            call: Call<P>,
            success: (P) -> Unit,
            fail: (String) -> Unit,
            context: Context,
            message: (String) -> Unit
        ) {
            Log.d("URL","::"+call.request().url())
            call.enqueue(object : Callback<P> {
                override fun onResponse(call: Call<P>, response: Response<P>) {
                    if (response.isSuccessful && response.body() != null) {
                        success(response.body()!!)
                    } else {
                        fail("Something went wrong")
                    }
                }

                override fun onFailure(call: Call<P>, t: Throwable) {
                    fail("Failed to connect: ${t.message}")
                }
            })
        }

}