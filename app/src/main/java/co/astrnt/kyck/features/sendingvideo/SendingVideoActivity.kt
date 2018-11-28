package co.astrnt.kyck.features.sendingvideo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.demosdk.dao.VideoInfo
import co.astrnt.demosdk.utils.VideoUtils
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.success.SuccessActivity
import co.astrnt.kyck.util.DialogFactory
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.*
import net.ypresto.androidtranscoder.MediaTranscoder
import net.ypresto.androidtranscoder.format.MediaFormatStrategyPresets
import java.io.File

class SendingVideoActivity : BaseActivity(), UploadStatusDelegate {

    private lateinit var outputPath: String
    private var videoInfo: VideoInfo? = null

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

        val candidateId = Hawk.get<String>("CandidateId")
        val srcPath = Hawk.get<String>("VideoFilePath")

        val directory = File(context.filesDir, "video")
        if (!directory.exists()) {
            directory.mkdir()
        }

        outputPath = File(directory, "output_" + System.currentTimeMillis() + "_video.mp4").absolutePath
        videoInfo = VideoUtils.getVideoInfo(srcPath)

        if (videoInfo != null) {

            val listener = object : MediaTranscoder.Listener {
                override fun onTranscodeProgress(progress: Double) {
                }

                override fun onTranscodeCompleted() {
                    doUploadVideo(candidateId, outputPath)
                }

                override fun onTranscodeCanceled() {
                    DialogFactory.createErrorDialog(context, "Video Compress Transcoder canceled")
                }

                override fun onTranscodeFailed(exception: Exception) {
                    DialogFactory.createErrorDialog(context, "Video Compress Transcoder error occurred")
                }
            }

            MediaTranscoder.getInstance().transcodeVideo(srcPath, outputPath,
                    MediaFormatStrategyPresets.createAndroid720pStrategy(), listener)
        } else {
            showToast("Error when compress, video not found")
        }

    }

    fun doUploadVideo(candidateId: String, videoPath: String) {

        val notificationConfig = UploadNotificationConfig()
        notificationConfig.isRingToneEnabled = true

        MultipartUploadRequest(context, "http://beta.astrnt.co/api/astronaut/kyck/video/save")
                .addParameter("candidate_identifier", candidateId)
                .addFileToUpload(videoPath, "file")
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
        SuccessActivity.start(context)
        finish()
    }
}