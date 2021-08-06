package mich.hiro.stopwatch

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    val handler = Handler()                      //
    var timeValue = 0                              // 秒カウンター

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Handler(スレット間通信：イベントキュー？)
        val runnable = object : Runnable {
            // メッセージ受信が有った時かな?
            override fun run() {
                timeValue++                      // 秒カウンタ+1
                timeToText(timeValue)?.let {        // timeToText()で表示データを作り
                    timeText.text = it            // timeText.textへ代入(表示)
                }
                handler.postDelayed(this, 1000)  // 1000ｍｓ後に自分にpost
            }
        }

        // startボタン押された時(setOnClickListener)の処理
        start.setOnClickListener {
            handler.post(runnable)                // 最初のキュー登録
        }
        // stopボタン押された時の処理
        stop.setOnClickListener {
            handler.removeCallbacks(runnable)      // キューキャンセル
        }
        // resetボタン押された時の処理
        reset.setOnClickListener {
            handler.removeCallbacks(runnable)      // キューキャンセル
            timeValue = 0                          // 秒カウンタークリア
            timeToText()?.let {                  // timeToText()で表示データを作り
                timeText.text = it                // timeText.textに表示
            }
        }
    }

    // 表示
    private fun timeToText(time: Int = 0): String? {
        return if (time < 0) {
            null                                    // 時刻が0未満の場合 null
        } else if (time == 0) {
            "00:00:00"                            // ０なら
        } else {
            val h = time / 3600
            val m = time % 3600 / 60
            val s = time % 60
            "%1$02d:%2$02d:%3$02d".format(h, m, s)  // 表示に整形
        }
    }
}