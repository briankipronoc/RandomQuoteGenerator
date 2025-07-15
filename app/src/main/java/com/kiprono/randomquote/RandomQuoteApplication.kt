package com.kiprono.randomquote // This package must match the one in AndroidManifest.xml

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RandomQuoteApplication : Application() {
    // You can leave this class body empty or add any application-level initialization here.
}