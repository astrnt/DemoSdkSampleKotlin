package co.astrnt.kyck.features.register

import co.astrnt.demosdk.dao.RegisterApiDao
import co.astrnt.kyck.data.DataManager
import co.astrnt.kyck.features.base.BasePresenter
import co.astrnt.kyck.injection.ConfigPersistent
import co.astrnt.kyck.util.rx.scheduler.SchedulerUtils
import com.orhanobut.hawk.Hawk
import javax.inject.Inject

@ConfigPersistent
class RegisterPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<RegisterMvpView>() {

    fun doRegister(name: String, email: String) {
        checkViewAttached()
        mvpView?.showProgress(true)

        addDisposable(
                dataManager.doRegister(name, email)
                        .compose(SchedulerUtils.ioToMain<RegisterApiDao>())
                        .subscribe({ response ->
                            mvpView?.apply {
                                showProgress(false)
                                if (response.status == "SUCCESS") {
                                    Hawk.put("CandidateId", response.candidateIdentifier)
                                    showResult()
                                } else {
                                    showError(Throwable(response.message))
                                }
                            }
                        }) { throwable ->
                            mvpView?.apply {
                                showProgress(false)
                                showError(throwable)
                            }
                        }
        )
    }
}