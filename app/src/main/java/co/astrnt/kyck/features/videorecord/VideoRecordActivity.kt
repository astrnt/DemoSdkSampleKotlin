package co.astrnt.kyck.features.videorecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.view.View
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.sendingfile.SendingFileActivity
import co.astrnt.kyck.widget.RecordButtonView
import co.astrnt.kyck.widget.RecordButtonView.Companion.STATE_ON_FINISH
import co.astrnt.kyck.widget.RecordButtonView.Companion.STATE_ON_RECORD
import com.otaliastudios.cameraview.CameraListener
import kotlinx.android.synthetic.main.activity_video_record.*
import java.io.File

class VideoRecordActivity : BaseActivity(), RecordButtonView.RecordListener {

    private var countDownTimer: CountDownTimer? = null
    private var recordFile: File? = null
    private var maxTime: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        maxTime = 30

        btnRecord.setRecordListener(this)
        btnRecord.setMaxTime(maxTime)

        btnRecord.setOnClickListener { btnRecord.setToNextState() }

        setUpCamera()
    }

    override fun layoutId() = R.layout.activity_video_record

    private fun setUpCamera() {
        cameraView?.addCameraListener(object : CameraListener() {

            override fun onVideoTaken(video: File?) {
                recordFile = video
                moveToPreview()
            }
        })
    }

    private fun startRecording() {
        val directory = File(context.filesDir, "video")
        if (!directory.exists()) {
            directory.mkdir()
        }
        recordFile = File(directory, System.currentTimeMillis().toString() + "_video.mp4")
        cameraView?.videoMaxDuration = maxTime * 1000
        cameraView?.startCapturingVideo(recordFile)
    }

    override fun onPreRecord() {
        txtTitle.text = "Instruction"
        txtQuestion.text = "Record Instruction"
    }

    override fun onCountdown() {
        txtTitle.text = "Question"
        //        txtQuestion.setText(currentQuestion.getTitle());
        txtCountDown.text = btnRecord.maxProgress.toString()
        txtCountDown.visibility = View.VISIBLE
        countDownTimer = object : CountDownTimer((btnRecord.maxProgress * 1000).toLong(), 1000) {

            override fun onTick(millisUntilFinished: Long) {
                val currentProgress = millisUntilFinished / 1000
                txtCountDown.text = (currentProgress + 1).toString()
                btnRecord.setCurrentProgress(currentProgress)
            }

            override fun onFinish() {
                txtCountDown.text = 1.toString()
                txtCountDown.visibility = View.GONE
                btnRecord.currentProgress = 0
                btnRecord.currentState = STATE_ON_RECORD
            }
        }.start()
    }

    override fun onRecording() {
        txtTitle.text = "Recording"
        startRecording()

        Handler().postDelayed({
            countDownTimer = object : CountDownTimer((maxTime * 1000).toLong(), 1000) {

                override fun onTick(millisUntilFinished: Long) {
                    val currentProgress = millisUntilFinished / 1000
                    btnRecord.setCurrentProgress(currentProgress)
                    if (currentProgress < 5) {
                        if (txtCountDown.visibility == View.GONE) {
                            txtCountDown.visibility = View.VISIBLE
                        }
                        txtCountDown.text = (currentProgress + 1).toString()
                    }
                }

                override fun onFinish() {
                    btnRecord.currentProgress = 0
                    btnRecord.currentState = STATE_ON_FINISH
                }
            }.start()
        }, 2000)
    }

    override fun onFinished() {
        if (countDownTimer != null) {
            countDownTimer?.cancel()
        }
        cameraView.stopCapturingVideo()
        txtCountDown.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        cameraView.start()
    }

    override fun onPause() {
        super.onPause()
        cameraView.stop()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraView.destroy()
        if (countDownTimer != null) countDownTimer?.cancel()
    }

    private fun moveToPreview() {
        if (recordFile != null) {
            //            VideoPreviewRecordActivity.start(context, Uri.fromFile(recordFile));
            //            finish();
            SendingFileActivity.start(context, "TakeRecord")
            finish()
        }
    }

    companion object {

        fun start(context: Context) {
            val intent = Intent(context, VideoRecordActivity::class.java)
            context.startActivity(intent)
        }
    }
}
