package hu.ait.minesweeper.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import hu.ait.minesweeper.MainActivity
import hu.ait.minesweeper.R
import hu.ait.minesweeper.model.MineSweeperModel

class MineSweeperView(context: Context?, attrs: AttributeSet) : View(context, attrs) {

    private val paintBackground = Paint()
    private val paintLine = Paint()
    private val paintText = Paint()
    private val gridSize = MineSweeperModel.GRID_SIZE
    private var touched: Boolean = false
    private var bombIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_action_record)
    private var flagIcon = BitmapFactory.decodeResource(resources, R.drawable.ic_flag)

    init {
        // background
        paintBackground.color = Color.CYAN
        paintBackground.style = Paint.Style.FILL

        // board line
        paintLine.color = Color.BLACK
        paintLine.style = Paint.Style.STROKE
        paintLine.strokeWidth = 8F

        // field text
        paintText.color = Color.BLACK
        paintText.textSize = 50F
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawGameBoard(canvas)
        drawField(canvas)
    }

    private fun drawGameBoard(canvas: Canvas?) {
        // board background
        canvas?.drawRect(0F,0F,width.toFloat(),height.toFloat(),paintBackground)

        // board border
        canvas?.drawRect(0F, 0F, width.toFloat(), height.toFloat(), paintLine)

        // horizontal lines
        for (x in 1..gridSize) {
            canvas?.drawLine(width.toFloat() * x/gridSize, 0F, width.toFloat() * x/gridSize, height.toFloat(), paintLine)
        }

        // vertical lines
        for (y in 1..gridSize) {
            canvas?.drawLine(0F, height.toFloat() * y/gridSize, width.toFloat(), height.toFloat()*y/gridSize, paintLine)
        }
    }

    private fun drawField(canvas: Canvas) {
        for(x in 0..4) {
            for (y in 0..4) {
                if(MineSweeperModel.isClicked(x,y)) {
                    var clickedField = MineSweeperModel.getField(x,y)
                    if(clickedField == MineSweeperModel.BOMB) {
                        canvas.drawBitmap(bombIcon, x*width / gridSize.toFloat(), y* height / gridSize.toFloat(), null)
                    } else if (clickedField == MineSweeperModel.FLAG) {
                        canvas.drawBitmap(flagIcon, x*width / gridSize.toFloat(), y*height/gridSize.toFloat(), null)
                    } else {
                        val paddingLeft = width/22
                        val paddingTop = height/7
                        canvas.drawText(MineSweeperModel.getField(x,y).toString(),x*width/gridSize.toFloat()+paddingLeft,y*height/gridSize.toFloat()+paddingTop,paintText)
                    }
                }
            }
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (!MineSweeperModel.ended()) {
            if( event?.action == MotionEvent.ACTION_DOWN) {
                startChrono()
                val tX = event.x.toInt() / (width/gridSize)
                val tY = event.y.toInt() / (width/gridSize)
                if(tX < gridSize && tY < gridSize) {
                    if(!MineSweeperModel.isClicked(tX, tY)) {
                        MineSweeperModel.setClicked(tX, tY)
                    }
                    var clickedField = MineSweeperModel.getField(tX,tY)
                    clickControl(clickedField, tX, tY)
                    invalidate()
                }
            }
        }
        return true
    }

    private fun clickControl(clickedField: Short, tX: Int, tY: Int) {
        if(MineSweeperModel.flagged()) {
            flagMode(clickedField, tX, tY)
        } else {
            mineMode(clickedField)
        }
    }

    private fun flagMode(clickedField: Short, tX: Int, tY: Int) {
        if (clickedField == MineSweeperModel.BOMB) {
            MineSweeperModel.setField(tX,tY,MineSweeperModel.FLAG)
            (context as MainActivity).win(context.getString(R.string.correct_flag))
            MineSweeperModel.increaseFlag()

            if(MineSweeperModel.getFlagNum() == MineSweeperModel.getBombNum()) {
                (context as MainActivity).win(context.getString(R.string.win_flag))
                MineSweeperModel.endGame()
                stopChrono()

            }
        } else {
            (context as MainActivity).lose(context.getString(R.string.wrong_flag))
            MineSweeperModel.endGame()
            stopChrono()

        }
    }

    private fun mineMode(clickedField: Short) {
        if (clickedField == MineSweeperModel.BOMB) {
            (context as MainActivity).lose(context.getString(R.string.lost_bomb))
            MineSweeperModel.endGame()
            stopChrono()
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        paintText.textSize = height.toFloat()/6
        bombIcon = Bitmap.createScaledBitmap(bombIcon, width/gridSize, height/gridSize, false)
        flagIcon = Bitmap.createScaledBitmap(flagIcon, width/gridSize, height/gridSize, false)

    }

    public fun reset() {
        MineSweeperModel.restart()
        restartChrono()
        invalidate()
    }

    private fun startChrono() {
        if(!touched) {
            touched = true
            (context as MainActivity).startChrono()
        }
    }

    private fun stopChrono() {
        (context as MainActivity).stopChrono()
        touched = false
    }

    private fun restartChrono() {
        touched = false
        (context as MainActivity).restartChrono()
    }
}