package com.hongxing.utilx

import android.app.Application

/**
 * 初始化UtilX
 */
class UtilX {
    var app: Application? = null
    fun init(application: Application) {
        app = application
    }
}