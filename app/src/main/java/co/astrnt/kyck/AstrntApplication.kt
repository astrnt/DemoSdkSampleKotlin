package co.astrnt.kyck

import android.content.Context
import androidx.multidex.MultiDexApplication
import co.astrnt.kyck.injection.component.AppComponent
import co.astrnt.kyck.injection.component.DaggerAppComponent
import co.astrnt.kyck.injection.module.AppModule
import co.astrnt.kyck.injection.module.NetworkModule
import com.facebook.stetho.Stetho
import com.singhajit.sherlock.core.Sherlock
import com.squareup.leakcanary.LeakCanary
import com.tspoon.traceur.Traceur
import timber.log.Timber

class AstrntApplication : MultiDexApplication() {

    private var appComponent: AppComponent? = null

    companion object {
        operator fun get(context: Context): AstrntApplication {
            return context.applicationContext as AstrntApplication
        }
    }

    override fun onCreate() {
        super.onCreate()

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
                        .networkModule(NetworkModule(this))
                        .build()
            }
            return appComponent as AppComponent
        }
        set(appComponent) {
            this.appComponent = appComponent
        }

}