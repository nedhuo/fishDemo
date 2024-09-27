package com.nedhuo.custom.manager

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener

class WebSocketManager {

    private var webSocket: WebSocket? = null
    private val url = "ws://echo.websocket.org"

    fun connect() {
        val client = OkHttpClient()
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
                // 连接建立时的回调
                println("WebSocket connection opened")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                // 接收到服务器消息时的回调，处理实时数据推送

            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                // 连接关闭时的回调
                println("Closing WebSocket")
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
                // 连接失败时的回调
                t.printStackTrace()
            }
        })
    }
}