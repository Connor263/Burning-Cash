package com.amanotes.beathop.data.model.game

import androidx.lifecycle.MutableLiveData
import com.amanotes.beathop.utils.enums.CellPosition
import com.amanotes.beathop.utils.enums.CellPosition.*
import com.amanotes.beathop.utils.enums.CellState
import com.amanotes.beathop.utils.enums.GameState

data class TicTacToeBoard(val markedCells: MutableList<Cell> = mutableListOf()) {
    val gameState = MutableLiveData(GameState.IDLE)

    init {
        clearBoard()
    }

    fun setCell(cellForSet: Cell, state: CellState): Boolean {
        val listOfBlanks = markedCells.filter { it.state == CellState.BLANK }
        if (listOfBlanks.isEmpty()) {
            return setDraw()
        }
        if (markedCells.find { it.position == cellForSet.position }?.state != CellState.BLANK) {
            return false
        }
        markedCells.find { it.position == cellForSet.position }?.state = state
        return true
    }


    fun setCellAI(aiCellStateMark: CellState): Boolean {
        val listOfBlanks = markedCells.filter { it.state == CellState.BLANK }
        val cellForSet = if (listOfBlanks.isNotEmpty()) {
            listOfBlanks.random()
        } else {
            return setDraw()
        }
        return setCell(cellForSet, aiCellStateMark)
    }

    private fun setDraw(): Boolean {
        gameState.value = GameState.DRAW
        return true
    }

    fun hasWon(stateToWon: CellState): Boolean {
        val crossedState = when {
            stateToWon.name.contains(CellState.X_MARK.name.take(3)) -> CellState.X_CROSSED
            stateToWon.name.contains(CellState.O_MARK.name.take(3)) -> CellState.O_CROSSED
            else -> CellState.X_CROSSED
        }
        val listOfWonCells = mutableListOf<Cell>()

        fun compareCells(vararg cellsForCompare: CellPosition): Boolean {
            val compareResult = cellsForCompare.all { comparePos ->
                if (markedCells.find { it.position == comparePos }?.state == stateToWon) {
                    listOfWonCells.add(markedCells.find { it.position == comparePos }!!)
                    true
                } else {
                    false
                }
            }
            return if (compareResult) {
                listOfWonCells.forEach { wonCell ->
                    markedCells.find { it.position == wonCell.position }?.state = crossedState
                }
                true
            } else {
                listOfWonCells.clear()
                false
            }
        }
        // Diagonal
        return compareCells(TOP_LEFT, CENTER, BOTTOM_RIGHT) ||
                compareCells(BOTTOM_LEFT, CENTER, TOP_RIGHT) ||

                // Vertical
                compareCells(TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT) ||
                compareCells(TOP_CENTER, CENTER, BOTTOM_CENTER) ||
                compareCells(TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT) ||

                // Horizontal
                compareCells(TOP_LEFT, TOP_CENTER, TOP_RIGHT) ||
                compareCells(CENTER_LEFT, CENTER, CENTER_RIGHT) ||
                compareCells(BOTTOM_LEFT, BOTTOM_CENTER, BOTTOM_RIGHT)
    }

    private fun clearBoard() {
        markedCells.clear()
        markedCells.addAll(
            mutableListOf(
                Cell(
                    id = 0,
                    position = TOP_LEFT,
                ),
                Cell(
                    id = 1,
                    position = TOP_CENTER,
                ),
                Cell(
                    id = 2,
                    position = TOP_RIGHT,
                ),
                Cell(
                    id = 3,
                    position = CENTER_LEFT,
                ),
                Cell(
                    id = 4,
                    position = CENTER,
                ),
                Cell(
                    id = 5,
                    position = CENTER_RIGHT,
                ),
                Cell(
                    id = 6,
                    position = BOTTOM_LEFT,
                ),
                Cell(
                    id = 7,
                    position = BOTTOM_CENTER,
                ),
                Cell(
                    id = 8,
                    position = BOTTOM_RIGHT,
                )
            )
        )
    }
}