package co.astrnt.kyck.features.sendingvideo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.demosdk.utils.VideoUtils
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.success.SuccessActivity
import co.astrnt.kyck.util.DialogFactory
import com.orhanobut.hawk.Hawk
import com.zolad.videoslimmer.VideoSlimmer
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*
import net.gotev.uploadservice.*
import java.io.File

class SendingVideoActivity : BaseActivity(), UploadStatusDelegate {

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
        val videoPath = Hawk.get<String>("VideoFilePath")

        val directory = File(context.filesDir, "video")
        if (!directory.exists()) {
            directory.mkdir()
        }

        val videoOutputPath = File(directory, "output_" + System.currentTimeMillis() + "_video.mp4").absolutePath
        val videoInfo = VideoUtils.getVideoInfo(videoPath)

        if (videoInfo != null) {
            VideoSlimmer.convertVideo(videoPath, videoOutputPath, videoInfo.width, videoInfo.height, videoInfo.bitrate, object : VideoSlimmer.ProgressListener {
                override fun onStart() {
                    //convert start
                    showToast("Start Compress")
                }

                override fun onFinish(result: Boolean) {
                    //convert finish,result(true is success,false is fail)
                    if (result) {
                        showToast("Success Compress")
                        doUploadVideo(candidateId, videoOutputPath)
                    } else {
                        showToast("Error when compress")
                    }
                }

                override fun onProgress(percent: Float) {
                    //percent of progress
                }
            })
        } else {
            showToast("Error when compress")
        }

    }

    fun doUploadVideo(candidateId: String, videoPath: String) {

        val notificationConfig = UploadNotificationConfig()
        notificationConfig.isRingToneEnabled = true

        MultipartUploadRequest(context, "http://beta.astrnt.co/api/astronaut/kyck/video/save")
                .addParameter("candidate_identifier", candidateId)
                .addFileToUpload(File(videoPath).absolutePath, "file")
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