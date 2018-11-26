package co.astrnt.kyck.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView

import co.astrnt.kyck.R
import co.astrnt.kyck.types.RecordState
import rjsv.circularview.CircleView

class RecordButtonView : LinearLayout {

    private var circleProgress: CircleView? = null
    private var bgCircle: View? = null
    private var txtState: TextView? = null
    private var recordListener: RecordListener? = null
    private var maxTime: Int = 0

    var currentProgress: Int = 0

    var maxProgress: Int = 0
        set(maxProgress) {
            field = maxProgress
            circleProgress?.setMaximumValue(maxProgress)
        }

    @RecordState
    @get:RecordState
    var currentState = STATE_PRE_RECORD
        set(@RecordState currentState) {
            field = currentState
            if (recordListener != null) {
                when (currentState) {
                    STATE_PRE_RECORD -> {
                        circleProgress?.progressValue = 0f
                        txtState?.setText(R.string.start)
                        txtState?.visibility = View.VISIBLE
                        bgCircle?.visibility = View.GONE
                        recordListener?.onPreRecord()
                    }
                    STATE_ON_COUNTDOWN -> {
                        txtState?.visibility = View.GONE
                        circleProgress?.progressValue = 10f
                        maxProgress = 10
                        recordListener?.onCountdown()
                    }
                    STATE_ON_RECORD -> {
                        circleProgress?.progressValue = maxTime.toFloat()
                        maxProgress = maxTime
                        recordListener?.onRecording()
                    }
                    STATE_ON_FINISH -> {
                        circleProgress?.progressValue = 0f
                        recordListener?.onFinished()
                    }
                    else -> {
                    }
                }
            }
        }

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        orientation = LinearLayout.VERTICAL
        gravity = Gravity.CENTER
        val view = LayoutInflater.from(context).inflate(R.layout.view_record_button, this)
        circleProgress = view.findViewById(R.id.circle_progress)
        bgCircle = view.findViewById(R.id.bg_circle)
        txtState = view.findViewById(R.id.txt_state)
        circleProgress?.progressValue = 0f
    }

    fun setRecordListener(recordListener: RecordListener) {
        this.recordListener = recordListener
    }

    fun setCurrentProgress(currentProgress: Long) {
        this.currentProgress = currentProgress.toInt()
        circleProgress?.progressValue = currentProgress.toFloat()
        if (currentState == STATE_ON_RECORD) {
            if (currentProgress > maxTime - 5) {
                bgCircle?.visibility = View.GONE
                txtState?.visibility = View.VISIBLE
                txtState?.text = currentProgress.toString()
            }
            if (currentProgress <= maxTime - 5) {
                isClickable = true
                bgCircle?.visibility = View.VISIBLE
                txtState?.visibility = View.VISIBLE
                txtState?.setText(R.string.finish)
            }
        }
    }

    fun setMaxTime(maxTime: Int) {
        this.maxTime = maxTime
    }

    fun setToNextState() {
        when (currentState) {
            STATE_PRE_RECORD -> {
                isClickable = false
                currentState = STATE_ON_COUNTDOWN
            }
            STATE_ON_COUNTDOWN -> currentState = STATE_ON_RECORD
            STATE_ON_RECORD -> currentState = STATE_ON_FINISH
            STATE_ON_FINISH -> currentState = STATE_PRE_RECORD
            else -> {
            }
        }
    }

    interface RecordListener {
        fun onPreRecord()

        fun onCountdown()

        fun onRecording()

        fun onFinished()
    }

    companion object {

        const val STATE_PRE_RECORD = "pre_record"
        const val STATE_ON_COUNTDOWN = "on_countdown"
        const val STATE_ON_RECORD = "on_record"
        const val STATE_ON_FINISH = "on_finish"
    }
}
