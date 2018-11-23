package co.astrnt.kyck.injection.component

import co.astrnt.kyck.injection.PerFragment
import co.astrnt.kyck.injection.module.FragmentModule
import dagger.Subcomponent

/**
 * This component inject dependencies to all Fragments across the application
 */
@PerFragment
@Subcomponent(modules = [FragmentModule::class])
interface FragmentComponent