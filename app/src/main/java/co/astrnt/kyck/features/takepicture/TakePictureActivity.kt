package co.astrnt.kyck.features.takepicture

import android.content.Context
import android.content.Intent
import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseActivity
import co.astrnt.kyck.features.sendingfile.SendingFileActivity
import com.esafirm.imagepicker.features.ImagePicker
import com.esafirm.imagepicker.model.Image
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.activity_take_picture.*
import kotlinx.android.synthetic.main.toolbar.*

class TakePictureActivity : BaseActivity() {

    private var images = ArrayList<Image>()

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
            ImagePicker.cameraOnly().start(this)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            images = ImagePicker.getImages(data) as ArrayList<Image>

            val image = images.first()
            Hawk.put("IdCardPath", image.path)

            SendingFileActivity.start(context, "TakePicture")
            finish()
            return
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}