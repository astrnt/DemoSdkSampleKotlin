package co.astrnt.kyck.features.sendingfile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.success.SuccessActivity
import co.astrnt.kyck.features.takerecord.TakeRecordActivity
import kotlinx.android.synthetic.main.activity_sending_file.*
import kotlinx.android.synthetic.main.toolbar.*

class SendingFileActivity : BaseActivity() {

    companion object {

        private const val EXTRA_NAME = "EXTRA_NAME"

        fun start(context: Context, extName: String) {
            val intent = Intent(context, SendingFileActivity::class.java)
            intent.putExtra(EXTRA_NAME, extName)
            context.startActivity(intent)
        }
    }

    override fun layoutId() = R.layout.activity_sending_file

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activityComponent().inject(this)

        val extName = intent.getStringExtra(EXTRA_NAME)

        when (extName) {
            "TakePicture" -> {
                initToolbar(toolbar, "Step 1")
                txt_message.text = "Sending your ID Card picture"

                Handler().postDelayed({
                    startActivity(Intent(context, TakeRecordActivity::class.java))
                    finish()
                }, 3000)
            }
            else -> {
                initToolbar(toolbar, "Step 2")
                txt_message.text = "Sending your video"

                Handler().postDelayed({
                    startActivity(Intent(context, SuccessActivity::class.java))
                    finish()
                }, 3000)
            }
        }

    }

}