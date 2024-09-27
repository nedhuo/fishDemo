package com.nedhuo.custom.utils

import java.util.concurrent.Executors

object ThreadUtils {

    fun runOnUiThread(runnable: Runnable) {
        Executors.newCachedThreadPool()

    }

}