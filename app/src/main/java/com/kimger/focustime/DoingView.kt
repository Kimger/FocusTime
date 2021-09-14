package com.kimger.focustime

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import com.kimger.focustime.net.NetResource
import com.kimger.focustime.net.RetrofitClient
import com.kimger.focustime.net.vo.Status
import com.kimger.focustime.sql.DatabaseManager
import com.kimger.focustime.sql.entity.TodoListEntity
import com.kimger.focustime.sql.entity.TodoRecordEntity
import com.lzf.easyfloat.EasyFloat
import com.lzf.easyfloat.enums.ShowPattern
import com.orhanobut.hawk.Hawk
import kotlinx.android.synthetic.main.dialog_add_todo.*
import kotlinx.android.synthetic.main.view_doing.view.*

class DoingView : LinearLayout {

    private lateinit var progressCircular: CircleTextProgressbar
    private lateinit var tvDoTitle: TextView
    private lateinit var ivPause: ImageView
    private lateinit var ivStop: ImageView
    private lateinit var tvDoingStatus: TextView
    private lateinit var ivBg: ImageView
    private lateinit var tvTip: TextView
    private lateinit var stopListener: () -> Unit
    private lateinit var fragmentManager: FragmentManager

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
        ivBg = view.findViewById(R.id.iv_bg)
        tvTip = view.findViewById(R.id.tv_tips)
        ivBg.setImageResource(Helper.get().getRandomDoingBackground())
        iv_sun.tag = 0
        iv_sun.setOnClickListener {
            if (iv_sun.tag == 0) {
                iv_sun.tag = 1
                iv_sun.keepScreenOn = true
                iv_sun.setImageResource(R.drawable.ic_wb_sunny_24)
            } else {
                iv_sun.tag = 0
                iv_sun.keepScreenOn = false
                iv_sun.setImageResource(R.drawable.ic_wb_sunny_24_2)
            }
        }
        getOneTip()
    }

    fun setFragmentManager(fragmentManager: FragmentManager) {
        this.fragmentManager = fragmentManager
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

        progressCircular.setCountdownProgressListener(1,
            object : CircleTextProgressbar.OnCountdownProgressListener {
                override fun onProgress(what: Int, progress: Int, time: Long) {
                    progressCircular.text = Helper.get().time2ms(time)
                    Log.d("TodoList", "onProgress: " + progress)
                }

                override fun onFinish() {
                    Log.d("TodoList", "onFinish: ")
                    saveRecord(1, data)
                    data.finishNumber += 1
                    DatabaseManager.dataBase.todoDao().updateTodo(data)
                    if (this@DoingView::stopListener.isInitialized) {
                        stopListener()
                    }
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
            if (progressCircular.lastTime <= 0) {
                if (this::stopListener.isInitialized) {
                    stopListener()
                }
                return@setOnClickListener
            }
            if (progressCircular.timeMillis - progressCircular.lastTime <= 5) {
                progressCircular.stop()
                if (this::stopListener.isInitialized) {
                    stopListener()
                }
                Toast.makeText(context, "小于5秒不记录", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            progressCircular.stop()
            val lockMode = Hawk.get("lockMode", 0)
            if (lockMode == 0) {
                val exitDoingDialog = ExitDoingDialog()
                exitDoingDialog.show(fragmentManager)
                exitDoingDialog.setOnPositiveClick {
                    progressCircular.resume()
                }
                exitDoingDialog.setOnNegativeClick {
                    if (this::stopListener.isInitialized) {
                        stopListener()
                        saveRecord(2, data)
                        exitDoingDialog.dismiss()
                    }
                }
            } else {
                EasyFloat.with(context)
                    .setTag("exit")
                    .setGravity(Gravity.CENTER)
                    .setAnimator(null)
                    .setLayout(R.layout.dialog_exit_doing) {
                        val tvPositive = it.findViewById<TextView>(R.id.tv_positive)
                        val tvNegative = it.findViewById<TextView>(R.id.tv_negative)
                        tvPositive.setOnClickListener {
                            EasyFloat.dismiss("exit")
                            progressCircular.resume()
                        }
                        tvNegative.setOnClickListener {
                            if (this::stopListener.isInitialized) {
                                stopListener()
                                saveRecord(2, data)
                            }
                            EasyFloat.dismiss("exit")
                        }
                    }
                    .setImmersionStatusBar(true)
                    .setShowPattern(ShowPattern.ALL_TIME)
                    .show()
            }
        }
    }

    fun setOnStopListener(listener: () -> Unit) {
        this.stopListener = listener
    }

    private fun saveRecord(status: Int, todoListEntity: TodoListEntity) {
        val todoRecordEntity = TodoRecordEntity()
        todoRecordEntity.status = status
        todoRecordEntity.todoId = todoListEntity.id
        todoRecordEntity.time = todoListEntity.time
        todoRecordEntity.title = todoListEntity.title
        todoRecordEntity.backgroundId = todoListEntity.backgroundId
        todoRecordEntity.userTime = progressCircular.timeMillis - progressCircular.lastTime
        DatabaseManager.dataBase.todoRecordDao().insert(todoRecordEntity)
    }

    private fun getOneTip() {
        NetResource.get<OneTipBean>().fetchFromNetwork(RetrofitClient.userServiceApi.getOneTip())
            .observeForever {
                when (it.status) {
                    Status.SUCCESS -> {
                        val hitokoto = it.data?.hitokoto
                        tvTip.text = hitokoto
                    }
                }
            }
    }
}