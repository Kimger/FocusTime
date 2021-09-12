package com.kimger.focustime

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.kimger.focustime.sql.entity.TodoListEntity

class DoingView : LinearLayout {

    private lateinit var progressCircular: CircleTextProgressbar
    private lateinit var tvDoTitle: TextView
    private lateinit var ivPause: ImageView
    private lateinit var ivStop: ImageView
    private lateinit var tvDoingStatus:TextView
    private lateinit var stopListener: () -> Unit

    constructor(context: Context) : this(context, null) {}
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0) {}
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private fun init() {
        val view = View.inflate(context, R.layout.view_doing, this)
        progressCircular = view.findViewById(R.id.progress_circular)
        tvDoTitle = view.findViewById(R.id.tv_do_title)
        ivPause = view.findViewById(R.id.iv_pause)
        ivStop = view.findViewById(R.id.iv_stop)
        tvDoingStatus = view.findViewById(R.id.tv_do_status)
    }

    fun setTodoData(data: TodoListEntity) {
        progressCircular.progressType = CircleTextProgressbar.ProgressType.COUNT_BACK
        progressCircular.setProgressLineWidth(20)
        progressCircular.setMaxProgress((data.time).toInt())
        progressCircular.setProgress((data.time).toInt())
        progressCircular.timeMillis = data.time
        progressCircular.setProgressColor(Color.parseColor("#ffffff"))
        progressCircular.setOutLineWidth(20)
        progressCircular.setOutLineColor(Color.parseColor("#66ffffff"))
        progressCircular.start()
        tvDoTitle.text = data.title

        progressCircular.setCountdownProgressListener(1,object :CircleTextProgressbar.OnCountdownProgressListener{
            override fun onProgress(what: Int, progress: Int, time: Long) {
                progressCircular.text = Helper.get().time2ms(time)
                Log.d("TodoList", "onProgress: " + progress)
            }

            override fun onFinish() {
                Log.d("TodoList", "onFinish: ")
            }

        })
        ivPause.tag = 0
        ivPause.setOnClickListener {
            if (ivPause.tag == 0) {
                progressCircular.stop()
                ivPause.setImageResource(R.drawable.ic_play)
                ivPause.tag = 1
                tvDoingStatus.text = "暂停中"
            } else {
                progressCircular.resume()
                ivPause.setImageResource(R.drawable.ic_pause)
                ivPause.tag = 0
                tvDoingStatus.text = "进行中"
            }

        }
        ivStop.setOnClickListener {
            progressCircular.stop()
            if (this::stopListener.isInitialized) {
                stopListener()
            }
        }
    }

    public fun setOnStopListener(listener: () -> Unit) {
        this.stopListener = listener
    }
}