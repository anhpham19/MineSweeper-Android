package hu.ait.minesweeper.model

object MineSweeperModel {

    public val BOMB_NUM = 3
    public var FLAG_NUM = 0
    public val GRID_SIZE = 5
    public val EMPTY: Short = -1
    public val BOMB: Short = -2
    public val FLAG: Short = -3
    public var flagged: Boolean = false
    public var ended: Boolean = false

    private val model = arrayOf(
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY),
        shortArrayOf(EMPTY, EMPTY, EMPTY, EMPTY, EMPTY)
    )

    private val clicked = arrayOf(
        booleanArrayOf(false, false, false, false, false),
        booleanArrayOf(false, false, false, false, false),
        booleanArrayOf(false, false, false, false, false),
        booleanArrayOf(false, false, false, false, false),
        booleanArrayOf(false, false, false, false, false)
    )

    public fun initialize() {
        setBombs()
        setNeighbors()
    }

    private fun setBombs() {
        var currBombNum = BOMB_NUM
        while(currBombNum > 0) {
            val x = (0..4).random()
            val y = (0..4).random()
            if (getField(x,y) != BOMB) {
                setField(x, y, BOMB)
                currBombNum--
            }
        }
    }

    private fun countBombs(x: Int, y: Int) : Int {
        if (x in 0..4 && y in 0..4 && getField(x, y) == BOMB) {
            return 1
        }
        else {
            return 0
        }
    }

    private fun setNeighbors() {
        for (x in 0..4) {
            for (y in 0..4) {
                if(getField(x,y) != BOMB) {
                    var neighborNum: Int = 0
                    for (i in -1..1) {
                        for (j in -1..1) {
                            neighborNum += countBombs(x + i, y + j)
                        }
                    }
                    setField(x, y, neighborNum.toShort())
                }
            }
        }
    }

    private fun clearClick() {
        for (x in 0..4) {
            for (y in 0..4) {
                clearClick(x, y)
            }
        }
    }

    private fun clearField() {
        for (x in 0..4) {
            for (y in 0..4) {
                setField(x, y, EMPTY)
            }

        }
    }

    public fun restart() {
        clearField()
        clearClick()
        restartFlag()
        notFlagged()
        notEndGame()
        setBombs()
        setNeighbors()
    }


    private fun clearClick(x: Int, y: Int) {
        clicked[x][y] = false
    }

    private fun restartFlag() {
        FLAG_NUM = 0
    }

    public fun getField(x: Int, y:Int) = model[x][y]

    public fun setField(x: Int, y: Int, content: Short) {
        model[x][y] = content
    }


    public fun isClicked(x: Int, y:Int) : Boolean {
        return clicked[x][y]
    }

    public fun setClicked(x: Int, y: Int) {
        clicked[x][y] = true
    }

    public fun flagged() : Boolean {
        return flagged
    }

    public fun notFlagged() {
        flagged = false
    }

    public fun ended() : Boolean {
        return ended
    }

    public fun endGame() {
        ended = true
    }

    public fun notEndGame() {
        ended = false
    }

    public fun increaseFlag() {
        FLAG_NUM++
    }

    public fun getFlagNum() : Int {
        return FLAG_NUM
    }

    public fun getBombNum() : Int {
        return BOMB_NUM
    }
}