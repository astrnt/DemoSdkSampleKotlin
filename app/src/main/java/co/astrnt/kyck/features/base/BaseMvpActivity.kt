package co.astrnt.kyck.features.base

import co.astrnt.kyck.util.DialogFactory
import com.derohimat.sweetalertdialog.SweetAlertDialog

abstract class BaseMvpActivity : BaseActivity(), MvpView {

    private var progressDialog: SweetAlertDialog? = null

    protected abstract fun attachView()

    protected abstract fun detachPresenter()


    override fun onPause() {
        super.onPause()
        showProgress(false)
    }

    override fun onStop() {
        super.onStop()
        showProgress(false)
    }

    override fun onDestroy() {
        detachPresenter()
        super.onDestroy()
        if (progressDialog != null) {
            progressDialog?.dismiss()
            progressDialog = null
        }
    }

    override fun showProgress(show: Boolean) {
        if (!isFinishing) {
            if (show) {
                progressDialog = DialogFactory.createProgressDialog(context)
                if (progressDialog != null) progressDialog?.show()
            } else {
                if (progressDialog != null) progressDialog?.dismissWithAnimation()
            }
        }
    }

    override fun showError(error: Throwable) {
        if (!isFinishing) {
            error.message?.let { DialogFactory.createErrorDialog(context, it).show() }
        }
    }
}