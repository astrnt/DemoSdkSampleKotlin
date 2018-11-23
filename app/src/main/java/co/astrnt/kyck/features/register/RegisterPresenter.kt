package co.astrnt.kyck.features.register

import co.astrnt.kyck.data.DataManager
import co.astrnt.kyck.features.base.BasePresenter
import co.astrnt.kyck.injection.ConfigPersistent
import javax.inject.Inject

@ConfigPersistent
class RegisterPresenter @Inject
constructor(private val dataManager: DataManager) : BasePresenter<RegisterMvpView>() {

    fun doRegister(name: String, email: String) {
        checkViewAttached()
        mvpView?.showProgress(true)

//        addDisposable(
//                dataManager.getPokemonList(limit)
//                        .compose(SchedulerUtils.ioToMain<List<String>>())
//                        .subscribe({ pokemons ->
//                            mvpView?.apply {
//                                showProgress(false)
//                                showResult()
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