package com.amanotes.beathop.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.amanotes.beathop.data.model.game.Cell
import com.amanotes.beathop.databinding.ItemTicTacBinding
import com.amanotes.beathop.interfaces.TicTacToeAdapterListener
import com.amanotes.beathop.utils.enums.CellState
import com.amanotes.beathop.utils.oMarkIdResource
import com.amanotes.beathop.utils.xMarkIdResource

class TicTacToeAdapter(private val listener: TicTacToeAdapterListener) :
    ListAdapter<Cell, TicTacToeAdapter.ViewHolder>(DiffUtilItemCallback()) {

    inner class ViewHolder(private val binding: ItemTicTacBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Cell) {
            var crossed = false
            setCellDrawable(
                when (item.state) {
                    CellState.BLANK -> 0
                    CellState.X_MARK -> xMarkIdResource
                    CellState.O_MARK -> oMarkIdResource
                    CellState.X_CROSSED -> xMarkIdResource.also { crossed = true }
                    CellState.O_CROSSED -> oMarkIdResource.also { crossed = true }
                },
                crossed
            )
            binding.mCvParent.setOnClickListener {
                listener.onClick(item)
            }
        }

        private fun setCellDrawable(@DrawableRes id: Int, crossed: Boolean) {
            binding.viewMark.visibility = if (crossed) {
                View.VISIBLE
            } else {
                View.INVISIBLE
            }
            binding.ivDrawable.setImageResource(id)
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(ItemTicTacBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }


    class DiffUtilItemCallback : DiffUtil.ItemCallback<Cell>() {
        override fun areItemsTheSame(oldItem: Cell, newItem: Cell): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cell, newItem: Cell): Boolean {
            return oldItem == newItem
        }
    }
}