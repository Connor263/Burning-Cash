package com.amanotes.beathop.interfaces

import com.amanotes.beathop.data.model.game.Cell

interface TicTacToeAdapterListener {
    fun onClick(item: Cell)
}