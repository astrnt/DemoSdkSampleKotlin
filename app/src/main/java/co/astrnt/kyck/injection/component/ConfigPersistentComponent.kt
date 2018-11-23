package co.astrnt.kyck.injection.component

import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.base.BaseFragment
import co.astrnt.kyck.injection.ConfigPersistent
import co.astrnt.kyck.injection.module.ActivityModule
import co.astrnt.kyck.injection.module.FragmentModule
import dagger.Component

/**
 * A dagger component that will live during the lifecycle of an Activity or Fragment but it won't
 * be destroy during configuration changes. Check [BaseActivity] and [BaseFragment] to
 * see how this components survives configuration changes.
 * Use the [ConfigPersistent] scope to annotate dependencies that need to survive
 * configuration changes (for example Presenters).
 */
@ConfigPersistent
@Component(dependencies = [AppComponent::class])
interface ConfigPersistentComponent {

    fun activityComponent(activityModule: ActivityModule): ActivityComponent

    fun fragmentComponent(fragmentModule: FragmentModule): FragmentComponent

}
