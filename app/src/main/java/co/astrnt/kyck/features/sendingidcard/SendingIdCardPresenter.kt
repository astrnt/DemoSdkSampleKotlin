package co.astrnt.kyck.features.sendingidcard

import android.graphics.Bitmap
import co.astrnt.kyck.data.DataManager
import co.astrnt.kyck.features.base.BasePresenter
import co.astrnt.kyck.injection.ConfigPersistent
import javax.inject.Inject

@ConfigPersistent
class SendingIdCardPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<SendingIdCardMvpView>() {

    fun saveIdCard(candidateId: String, bitmap: Bitmap) {
        checkViewAttached()
        mvpView?.showProgress(true)

//        addDisposable(
//                dataManager.saveIdCard(candidateId, bitmap)
//                        .compose(SchedulerUtils.ioToMain<BaseApiDao>())
//                        .subscribe({ response ->
//                            mvpView?.apply {
//                                showProgress(false)
//                                if (response.status == "SUCCESS") {
//                                    showResult()
//                                } else {
//                                    showError(Throwable(response.message))
//                                }
//                            }
//                        }) { throwable ->
//                            mvpView?.apply {
//                                showProgress(false)
//                                showError(throwable)
//                            }
//                        }
//        )
    }
}