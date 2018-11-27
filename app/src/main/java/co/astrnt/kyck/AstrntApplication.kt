package co.astrnt.kyck

import android.content.Context
import androidx.multidex.MultiDexApplication
import co.astrnt.demosdk.DemoSDK
import co.astrnt.demosdk.core.DemoSDKApi
import co.astrnt.kyck.injection.component.AppComponent
import co.astrnt.kyck.injection.component.DaggerAppComponent
import co.astrnt.kyck.injection.module.AppModule
import com.facebook.stetho.Stetho
import com.orhanobut.hawk.Hawk
import com.singhajit.sherlock.core.Sherlock
import com.squareup.leakcanary.LeakCanary
import com.tspoon.traceur.Traceur
import timber.log.Timber

class AstrntApplication : MultiDexApplication() {

    private var appComponent: AppComponent? = null

    companion object {
        private lateinit var demoSDK: DemoSDK

        operator fun get(context: Context): AstrntApplication {
            return context.applicationContext as AstrntApplication
        }

        fun getApi(): DemoSDKApi {
            demoSDK = DemoSDK(BuildConfig.API_URL, BuildConfig.DEBUG)
            return demoSDK.api
        }
    }

    override fun onCreate() {
        super.onCreate()

        Hawk.init(this).build()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Stetho.initializeWithDefaults(this)
            LeakCanary.install(this)
            Sherlock.init(this)
            Traceur.enableLogging()
        }
    }

    // Needed to replace the component with a test specific one
    var component: AppComponent
        get() {
            if (appComponent == null) {
                appComponent = DaggerAppComponent.builder()
                        .appModule(AppModule(this))
                        .build()
            }
            return appComponent as AppComponent
        }
        set(appComponent) {
            this.appComponent = appComponent
        }

}