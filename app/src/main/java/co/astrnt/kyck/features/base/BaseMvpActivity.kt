package co.astrnt.kyck.features.base

abstract class BaseMvpActivity : BaseActivity(), MvpView {

    protected abstract fun attachView()

    protected abstract fun detachPresenter()

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
    }

    override fun showProgress(show: Boolean) {
    }

    override fun showError(error: Throwable) {
    }
}