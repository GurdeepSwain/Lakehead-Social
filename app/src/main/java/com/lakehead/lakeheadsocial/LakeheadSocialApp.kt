package com.lakehead.lakeheadsocial

import android.app.Application
import com.google.firebase.FirebaseApp

class LakeheadSocialApp : Application() {
    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)
    }
}