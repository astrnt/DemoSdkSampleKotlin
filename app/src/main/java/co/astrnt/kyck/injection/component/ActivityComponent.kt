package co.astrnt.kyck.injection.component

import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.base.BaseMvpActivity
import co.astrnt.kyck.features.register.RegisterActivity
import co.astrnt.kyck.features.sendingidcard.SendingIdCardActivity
import co.astrnt.kyck.features.sendingvideo.SendingVideoActivity
import co.astrnt.kyck.features.success.SuccessActivity
import co.astrnt.kyck.features.takepicture.TakePictureActivity
import co.astrnt.kyck.features.takerecord.TakeRecordActivity
import co.astrnt.kyck.injection.PerActivity
import co.astrnt.kyck.injection.module.ActivityModule
import dagger.Subcomponent

@PerActivity
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {
    fun inject(baseActivity: BaseActivity)
    fun inject(baseMvpActivity: BaseMvpActivity)

    fun inject(registerActivity: RegisterActivity)
    fun inject(takePictureActivity: TakePictureActivity)
    fun inject(sendingIdCardActivity: SendingIdCardActivity)
    fun inject(takeRecordActivity: TakeRecordActivity)
    fun inject(sendingVideoActivity: SendingVideoActivity)
    fun inject(successActivity: SuccessActivity)
}
