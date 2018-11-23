package co.astrnt.kyck.features.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseMvpFragment : Fragment(), MvpView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        attachView()
    }

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