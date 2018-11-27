package co.astrnt.kyck.features.register

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import co.astrnt.kyck.R
import co.astrnt.kyck.features.base.BaseMvpActivity
import co.astrnt.kyck.features.takepicture.TakePictureActivity
import com.orhanobut.hawk.Hawk
import com.tbruyelle.rxpermissions2.Permission
import com.tbruyelle.rxpermissions2.RxPermissions
import io.reactivex.Observer
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.toolbar.*
import javax.inject.Inject

class RegisterActivity : BaseMvpActivity(), RegisterMvpView {

    @Inject
    lateinit var presenter: RegisterPresenter

    private var isMemoryOk = true
    private var isSoundOk = true
    private var isCameraOk = true
    private var permissionCounter = 0

    override fun layoutId() = R.layout.activity_register

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initToolbar(toolbar, "Registration")

        activityComponent().inject(this)
        attachView()

        val candidateId = Hawk.get<String>("CandidateId")

        if (!candidateId.isNullOrBlank()) {
            moveToTakePicture()
            return
        }

        btn_start.setOnClickListener {

            if (permissionCounter == 3) {
                validateInput()
            } else {
                Toast.makeText(context, "Some permission not granted", Toast.LENGTH_LONG).show()
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Handler().postDelayed({ checkingPermission() }, 1000)
        } else {
            permissionCounter = 3
        }
    }

    override fun attachView() {
        presenter.attachView(this)
    }

    override fun detachPresenter() {
        presenter.detachView()
    }

    private fun checkingPermission() {
        val rxPermissions = RxPermissions(this)

        rxPermissions
                .requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA,
                        Manifest.permission.RECORD_AUDIO)
                .subscribe(object : Observer<Permission> {
                    override fun onSubscribe(d: Disposable) {

                    }

                    override fun onNext(permission: Permission) {
                        when {
                            permission.granted -> // All permissions are granted !
                                permissionChecking(permission, true)
                            permission.shouldShowRequestPermissionRationale -> {

                                Toast.makeText(context, permission.name + " is not Granted", Toast.LENGTH_SHORT).show()
                                permissionChecking(permission, false)
                            }
                            else -> {
                                permissionChecking(permission, false)
                                Toast.makeText(context, permission.name + " is not Granted and never Ask Again, Please go to Settings", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onError(e: Throwable) {
                        Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    }

                    override fun onComplete() {
                        permissionCounter = 3
                    }
                })
    }

    private fun permissionChecking(permission: Permission, granted: Boolean) {
        when (permission.name) {
            Manifest.permission.CAMERA -> isCameraOk = granted
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> isMemoryOk = granted
            Manifest.permission.RECORD_AUDIO -> isSoundOk = granted
        }
    }

    private fun validateInput() {
        val name = inpName.text.toString()
        val email = inpEmail.text.toString()

        if (name.isBlank()) {
            inpName.error = "Name is still empty"
            inpName.isFocusable = true
            return
        }

        if (email.isBlank()) {
            inpEmail.error = "Email is still empty"
            inpEmail.isFocusable = true
            return
        }

        presenter.doRegister(name, email)
    }

    override fun showResult() {
        moveToTakePicture()
    }

    private fun moveToTakePicture() {
        TakePictureActivity.start(context)
        finish()
    }
}