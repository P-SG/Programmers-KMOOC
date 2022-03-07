package com.programmers.kmooc

import android.app.Application
import com.programmers.kmooc.repositories.KmoocRepository

class KmoocApplication : Application() {
    companion object{
        val kmoocRepository = KmoocRepository()
    }
}