package io.github.bersoncrios.minutesapp

import android.app.Application

class CustomApplication : Application() {
    var username: String? = null
    var userId: String? = null

    companion object{
        var instance: CustomApplication? = null
            get() {
                if (field == null){
                    field = CustomApplication()
                }
                return field
            }
            private set
    }
}