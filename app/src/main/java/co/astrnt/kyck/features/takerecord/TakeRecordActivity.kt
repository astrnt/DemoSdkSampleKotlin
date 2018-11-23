package co.astrnt.kyck.features.takerecord

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.sendingfile.SendingFileActivity
import kotlinx.android.synthetic.main.activity_take_record.*
import kotlinx.android.synthetic.main.toolbar.*

class TakeRecordActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            return context.startActivity(Intent(context, TakeRecordActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_take_record

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(toolbar, "Step 2")

        activityComponent().inject(this)

        btn_ok.setOnClickListener {
            SendingFileActivity.start(context, "TakeRecord")
            finish()
        }
    }

}