package hu.ait.minesweeper

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import hu.ait.minesweeper.model.MineSweeperModel
import hu.ait.minesweeper.view.MineSweeperView
import android.support.design.widget.Snackbar
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        MineSweeperModel.initialize()
        toggleClicked()
        reset()
    }

    private fun reset() {
        reset.setOnClickListener {
            mineSweeperView.reset()
            notToggle()
        }
    }

    private fun toggleClicked() {
        toggleFlag.setOnCheckedChangeListener { _, isChecked ->
            MineSweeperModel.flagged = isChecked
        }
    }

    private fun notToggle() {
        toggleFlag.isChecked = false
    }

    public fun win(str: String) {
        Snackbar.make(mineSweeperView, str, Snackbar.LENGTH_LONG)
            .setDuration(600)
            .show()
    }

    public fun lose(str: String) {
        Snackbar.make(mineSweeperView, str, Snackbar.LENGTH_LONG)
            .setDuration(600)
            .show()
    }

    public fun startChrono() {
        val systemCurrTime = SystemClock.elapsedRealtime()
        chronometer.base = systemCurrTime
        chronometer.start()
    }

    public fun stopChrono() {
        chronometer.stop()
    }

    public fun restartChrono() {
        val systemCurrTime = SystemClock.elapsedRealtime()
        chronometer.base = systemCurrTime
        chronometer.stop()
    }
}
