package com.example.notificationreader.notifications

import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.time.LocalDate
import java.time.format.DateTimeFormatter


class YnabTransactionApi(
    val ctx: Context,
    val accountId: String,
    val budgetId: String,
    val accessToken: String,
) {
    private val TAG = this::class.simpleName

    private val url: String
        get() = "https://api.youneedabudget.com/v1/budgets/$budgetId/transactions"

    private val method = Request.Method.POST
    private val requestBodyPattern =
        """
        {
            "transaction": {
                "account_id": "$accountId",
                "amount": <?amount>,
                "date": "${LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)}",
                "memo": "From NotificationReader",
                "payee_name": "<?payee>"
            }
        }
        """.trimIndent()


    private fun customizeValue(key: String, value: String): String {
        return when (key) {
            "amount" -> (value.toFloat() * -1000).toInt().toString()
            else -> value
        }
    }

    private fun replaceVariables(pattern: String) =
        { map: Map<String, String> ->
            map.toList().fold(pattern)
            { acc, (key, value) ->
                acc.replace("<?${key}>", customizeValue(key, value))
            }
        }

    val getRequestBody = replaceVariables(requestBodyPattern)

    internal class YNABRequest(
        method: Int,
        url: String,
        jsonRequest: JSONObject,
        private val accessToken: String,
        listener: Response.Listener<JSONObject?>?,
        errorListener: Response.ErrorListener?,
    ) : JsonObjectRequest(method, url, jsonRequest, listener, errorListener) {

        override fun getHeaders(): Map<String, String>? {
            return mapOf("Authorization" to "Bearer $accessToken")
        }
    }

    fun sendRequest(map: Map<String, String>, onSuccess: () -> Any, onFailure: () -> Any) {
        val queue = Volley.newRequestQueue(ctx)

        Log.i(TAG, map.toList().toString())
        val requestBody = getRequestBody(map)

        queue.add(
            YNABRequest(
                method, url, JSONObject(requestBody), accessToken,
                {
                    Log.i(TAG, "Successfully send data!")
                    onSuccess()
                },
                {
                    Log.i(TAG, "Failed to send data!")
                    Log.e(TAG, requestBody)
                    onFailure()
                })
        )

    }
}
