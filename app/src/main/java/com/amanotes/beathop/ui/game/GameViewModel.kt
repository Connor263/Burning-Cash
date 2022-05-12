package com.amanotes.beathop.ui.game

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.amanotes.beathop.data.model.game.Cell
import com.amanotes.beathop.data.model.game.TicTacToeBoard
import com.amanotes.beathop.utils.enums.CellState
import com.amanotes.beathop.utils.enums.GameState

class GameViewModel : ViewModel() {
    private val board = TicTacToeBoard()

    val boardCells = MutableLiveData(board.markedCells)
    val gameState = board.gameState

    private var playerCellState = MutableLiveData(CellState.X_MARK)

    fun setCell(item: Cell) {
        if (board.gameState.value == GameState.IDLE) {
            val cellStateMark = playerCellState.value!!

            if (cellStateMark == CellState.X_MARK) {
                board.setCell(item, cellStateMark)
                if (board.hasWon(cellStateMark)) {
                    board.gameState.value = GameState.WON
                    updateBoardCells()
                    return
                }
                playerCellState.value = CellState.O_MARK
                aiTurn()
                updateBoardCells()
            }
        }
    }

    private fun aiTurn() {
        // AI turn
        val aiCellStateMark = CellState.O_MARK
        board.setCellAI(aiCellStateMark)
        if (board.hasWon(aiCellStateMark)) {
            board.gameState.value = GameState.LOSE
            updateBoardCells()
            return
        }

        playerCellState.value = CellState.X_MARK
    }

    private fun updateBoardCells() {
        boardCells.value = board.markedCells
    }
}