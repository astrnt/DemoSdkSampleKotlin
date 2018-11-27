package co.astrnt.kyck.features.success

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.toolbar.*

class SuccessActivity : BaseActivity() {

    companion object {

        fun start(context: Context) {
            context.startActivity(Intent(context, SuccessActivity::class.java))
        }
    }

    override fun layoutId() = R.layout.activity_success

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initToolbar(toolbar, "Finished")

        activityComponent().inject(this)

        Hawk.deleteAll()
    }

}