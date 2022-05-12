package com.amanotes.beathop.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.amanotes.beathop.adapter.TicTacToeAdapter
import com.amanotes.beathop.data.model.game.Cell
import com.amanotes.beathop.databinding.FragmentGameBinding
import com.amanotes.beathop.interfaces.TicTacToeAdapterListener
import com.amanotes.beathop.utils.enums.GameState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameFragment : Fragment(), TicTacToeAdapterListener {
    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private val viewModel: GameViewModel by viewModels()

    private lateinit var mAdapter: TicTacToeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAdapter = TicTacToeAdapter(this)

        viewModel.gameState.observe(viewLifecycleOwner) { state ->
            lifecycleScope.launch {
                when (state) {
                    GameState.IDLE -> {}
                    GameState.WON -> navigateToScoreFragment(state)
                    GameState.LOSE -> navigateToScoreFragment(state)
                    GameState.DRAW -> navigateToScoreFragment(state)
                    null -> {}
                }
            }
        }

        viewModel.boardCells.observe(viewLifecycleOwner) { list ->
            mAdapter.submitList(list.map { cell ->
                cell.copy()
            })
        }

        initRv()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onClick(item: Cell) {
        viewModel.setCell(item)
    }

    private fun initRv() {
        binding.rvBoard.apply {
            adapter = mAdapter
            layoutManager =
                GridLayoutManager(requireContext(), 3, LinearLayoutManager.VERTICAL, false)
        }
    }

    private suspend fun navigateToScoreFragment(state: GameState) {
        delay(2000)
        val action = GameFragmentDirections.actionGameFragmentToScoreFragment(
            gameResult = state
        )
        findNavController().navigate(action)
    }
}