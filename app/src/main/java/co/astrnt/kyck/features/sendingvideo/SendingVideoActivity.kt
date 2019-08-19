package co.astrnt.kyck.features.sendingvideo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.BuildConfig
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.success.SuccessActivity
import co.astrnt.kyck.util.DialogFactory
import com.orhanobut.hawk.Hawk
import com.otaliastudios.transcoder.Transcoder
import com.otaliastudios.transcoder.TranscoderListener
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.*
import java.io.File

class SendingVideoActivity : BaseActivity(), UploadStatusDelegate {

    private lateinit var outputPath: String
    private lateinit var candidateId: String
    private lateinit var srcPath: String

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SendingVideoActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_sending_file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent().inject(this)

        initToolbar(toolbar, "Step 2")
        txtMessage.text = "Sending your video"

        candidateId = Hawk.get("CandidateId")
        srcPath = Hawk.get("VideoFilePath")

        doCompressVideo()
    }

    private fun doCompressVideo() {

        val directory = File(context.filesDir, "video")
        if (!directory.exists()) {
            directory.mkdir()
        }

        outputPath = File(directory, "output_" + System.currentTimeMillis() + "_video.mp4").absolutePath

        Transcoder.into(outputPath)
                .addDataSource(srcPath)
                .setListener(object : TranscoderListener {
                    override fun onTranscodeCompleted(successCode: Int) {
                        doUploadVideo()
                    }

                    override fun onTranscodeProgress(progress: Double) {
                    }

                    override fun onTranscodeCanceled() {
                        DialogFactory.createErrorDialog(context, "Video Compress Transcoder canceled").show()
                    }

                    override fun onTranscodeFailed(exception: Throwable) {
                        DialogFactory.createErrorDialog(context, "Video Compress Transcoder error occurred").show()
                    }

                }).transcode()
    }

    private fun doUploadVideo() {

        val notificationConfig = UploadNotificationConfig()
        notificationConfig.isRingToneEnabled = true

        MultipartUploadRequest(context, BuildConfig.API_URL + "astronaut/kyck/video/save")
                .addParameter("candidate_identifier", candidateId)
                .addFileToUpload(outputPath, "file")
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
        doUploadVideo()
        DialogFactory.createErrorDialog(context, "Upload Error").show()
    }

    override fun onCompleted(ctx: Context?, uploadInfo: UploadInfo?, serverResponse: ServerResponse?) {
        SuccessActivity.start(context)
        finish()
    }
}