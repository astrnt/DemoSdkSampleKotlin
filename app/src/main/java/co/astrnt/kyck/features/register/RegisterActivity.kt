package co.astrnt.kyck.features.register

import android.os.Bundle
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseMvpActivity
import co.astrnt.kyck.features.takepicture.TakePictureActivity
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class RegisterActivity : BaseMvpActivity(), RegisterMvpView {

    @Inject
    lateinit var presenter: RegisterPresenter

    override fun layoutId() = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(toolbar, "Registration")

        activityComponent().inject(this)
        presenter.attachView(this)

        btn_start.setOnClickListener {
            TakePictureActivity.start(context)
            finish()
        }
    }

    override fun attachView() {
        presenter.attachView(this)
    }

    override fun detachPresenter() {
        presenter.detachView()
    }

    override fun showResult() {
    }
}