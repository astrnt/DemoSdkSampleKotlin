package co.astrnt.kyck.features.takepicture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.sendingfile.SendingFileActivity
import kotlinx.android.synthetic.main.activity_take_picture.*
import kotlinx.android.synthetic.main.toolbar.*

class TakePictureActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, TakePictureActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_take_picture

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(toolbar, "Step 1")

        activityComponent().inject(this)

        btn_ok.setOnClickListener {
            SendingFileActivity.start(context, "TakePicture")
            finish()
        }
    }

}