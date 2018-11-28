package co.astrnt.kyck.features.sendingidcard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.takerecord.TakeRecordActivity
import co.astrnt.kyck.util.DialogFactory
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.*

class SendingIdCardActivity : BaseActivity(), UploadStatusDelegate {

    private lateinit var candidateId: String
    private lateinit var idCardPath: String

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SendingIdCardActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_sending_file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent().inject(this)

        initToolbar(toolbar, "Step 1")
        txtMessage.text = "Sending your ID Card picture"

        candidateId = Hawk.get<String>("CandidateId")
        idCardPath = Hawk.get<String>("IdCardPath")

        doUploadIdCard()
    }

    private fun doUploadIdCard() {

        val notificationConfig = UploadNotificationConfig()
        notificationConfig.isRingToneEnabled = true

        MultipartUploadRequest(context, "http://beta.astrnt.co/api/astronaut/kyck/card-id/save")
                .addParameter("candidate_identifier", candidateId)
                .addFileToUpload(idCardPath, "file")
                .setUtf8Charset()
                .setNotificationConfig(notificationConfig)
                .setAutoDeleteFilesAfterSuccessfulUpload(false)
                .setDelegate(this)
                .startUpload()

    }

    override fun onCancelled(ctx: Context?, uploadInfo: UploadInfo?) {
        DialogFactory.createErrorDialog(context, "Upload canceled").show()
    }

    override fun onProgress(ctx: Context?, uploadInfo: UploadInfo?) {
    }

    override fun onError(ctx: Context?, uploadInfo: UploadInfo?, serverResponse: ServerResponse?, exception: java.lang.Exception?) {
        doUploadIdCard()
        DialogFactory.createErrorDialog(context, "Upload Error").show()
    }

    override fun onCompleted(ctx: Context?, uploadInfo: UploadInfo?, serverResponse: ServerResponse?) {
        TakeRecordActivity.start(context)
        finish()
    }
}