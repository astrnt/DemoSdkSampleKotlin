package co.astrnt.kyck.util

import android.content.Context
import androidx.core.content.ContextCompat
import co.astrnt.kyck.R
import com.derohimat.sweetalertdialog.SweetAlertDialog

object DialogFactory {

    fun createProgressDialog(context: Context): SweetAlertDialog {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = ContextCompat.getColor(context, R.color.colorPrimary)
        pDialog.titleText = context.getString(R.string.loading)
        pDialog.setCancelable(false)
        return pDialog
    }

    fun createProgressDialog(context: Context, title: String): SweetAlertDialog {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = ContextCompat.getColor(context, R.color.colorPrimary)
        pDialog.titleText = title
        pDialog.setCancelable(false)
        return pDialog
    }

    fun createProgressDialog(context: Context, title: String, subTitle: String): SweetAlertDialog {
        val pDialog = SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE)
        pDialog.progressHelper.barColor = ContextCompat.getColor(context, R.color.colorPrimary)
        pDialog.titleText = title
        pDialog.contentText = subTitle
        pDialog.setCancelable(false)
        return pDialog
    }

    fun createWarningDialog(context: Context, title: String, content: String, confirmText: String, cancelText: String,
                            confirmListener: SweetAlertDialog.OnSweetClickListener): SweetAlertDialog {
        return SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(confirmText)
                .setConfirmClickListener(confirmListener)
                .setCancelText(cancelText)
    }

    fun createWarningDialog(context: Context, title: String, content: String, confirmText: String,
                            confirmListener: SweetAlertDialog.OnSweetClickListener): SweetAlertDialog {
        return SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(content)
                .setConfirmText(confirmText)
                .setConfirmClickListener(confirmListener)
    }

    fun createLoadingDialog(context: Context, title: String, content: String): SweetAlertDialog {
        return SweetAlertDialog(context, SweetAlertDialog.CUSTOM_IMAGE_TYPE)
                //                .setCustomImage(R.drawable.ic_loading_circle_purple)
                .setTitleText(title)
                .showConfirmButton(false)
                .setContentText(content)
    }

    fun createErrorDialog(context: Context, message: String): SweetAlertDialog {
        return SweetAlertDialog(context, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(context.getString(R.string.oops))
                .setContentText(message)
    }

    fun createDialog(context: Context, title: String, message: String): SweetAlertDialog {
        return SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE)
                .setTitleText(title)
                .setContentText(message)
    }

}