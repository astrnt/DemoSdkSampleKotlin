package co.astrnt.kyck.injection.component

import android.app.Application
import android.content.Context
import co.astrnt.kyck.data.DataManager
import co.astrnt.kyck.injection.ApplicationContext
import co.astrnt.kyck.injection.module.AppModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    @ApplicationContext
    fun context(): Context

    fun application(): Application

    fun dataManager(): DataManager

}
