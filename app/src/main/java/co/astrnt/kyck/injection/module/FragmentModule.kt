package co.astrnt.kyck.injection.module

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import co.astrnt.kyck.injection.ActivityContext
import dagger.Module
import dagger.Provides

@Module
class FragmentModule(private val fragment: Fragment) {

    @Provides
    internal fun providesFragment(): Fragment = fragment

    @Provides
    internal fun provideActivity(): FragmentActivity? = fragment.activity

    @Provides
    @ActivityContext
    internal fun providesContext(): Context? = fragment.context

}