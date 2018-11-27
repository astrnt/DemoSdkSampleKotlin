package co.astrnt.kyck.features.sendingidcard

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.MediaStore
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseMvpActivity
import co.astrnt.kyck.features.takepicture.TakePictureActivity
import co.astrnt.kyck.features.takerecord.TakeRecordActivity
import co.astrnt.kyck.util.DialogFactory
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.*
import java.io.File
import javax.inject.Inject

class SendingIdCardActivity : BaseMvpActivity(), SendingIdCardMvpView, UploadStatusDelegate {

    @Inject
    lateinit var presenter: SendingIdCardPresenter

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SendingIdCardActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_sending_file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent().inject(this)
        attachView()

        initToolbar(toolbar, "Step 1")
        txtMessage.text = "Sending your ID Card picture"

        val candidateId = Hawk.get<String>("CandidateId")
        val idCardPath = Hawk.get<String>("IdCardPath")
        val bitmap = MediaStore.Images.Media.getBitmap(context.contentResolver, Uri.fromFile(File(idCardPath)))

        Handler().postDelayed({
            //            doUploadIdCard(candidateId, Utils.imageToBase64(bitmap), idCardPath)
            doUploadIdCard(candidateId, idCardPath)
//            presenter.saveIdCard(candidateId, bitmap)
        }, 1000)
    }

    override fun attachView() {
        presenter.attachView(this)
    }

    override fun detachPresenter() {
        presenter.detachView()
    }

    override fun showResult() {
        showToast("Success")
        TakeRecordActivity.start(context)
        finish()
    }

    override fun showError(error: Throwable) {
        super.showError(error)
        TakePictureActivity.start(context)
        finish()
    }

    //    private fun doUploadIdCard(candidateId: String, photo: String, idCardPath: String) {
    private fun doUploadIdCard(candidateId: String, idCardPath: String) {

        val notificationConfig = UploadNotificationConfig()
        notificationConfig.isRingToneEnabled = true

        MultipartUploadRequest(context, "http://beta.astrnt.co/api/astronaut/kyck/card-id/save")
                .addParameter("candidate_identifier", candidateId)
//                .addParameter("file", photo)
                .addFileToUpload(File(idCardPath).absolutePath, "file")
                .setUtf8Charset()
                .setNotificationConfig(notificationConfig)
                .setAutoDeleteFilesAfterSuccessfulUpload(false)
                .setDelegate(this)
                .startUpload()

    }

    override fun onCancelled(ctx: Context?, uploadInfo: UploadInfo?) {
        DialogFactory.createErrorDialog(context, "Upload canceled")
    }

    override fun onProgress(ctx: Context?, uploadInfo: UploadInfo?) {
    }

    override fun onError(ctx: Context?, uploadInfo: UploadInfo?, serverResponse: ServerResponse?, exception: java.lang.Exception?) {
        DialogFactory.createErrorDialog(context, "Upload Error")
    }

    override fun onCompleted(ctx: Context?, uploadInfo: UploadInfo?, serverResponse: ServerResponse?) {
        TakeRecordActivity.start(context)
        finish()
    }
}