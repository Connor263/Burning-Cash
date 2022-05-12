package com.amanotes.beathop.data.model.game

import com.amanotes.beathop.utils.enums.CellPosition
import com.amanotes.beathop.utils.enums.CellState

data class Cell(
    val id: Int = 0,
    val position: CellPosition,
    var state: CellState = CellState.BLANK
)
