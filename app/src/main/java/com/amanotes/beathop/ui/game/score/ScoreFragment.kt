package com.amanotes.beathop.ui.game.score

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.amanotes.beathop.R
import com.amanotes.beathop.databinding.FragmentScoreBinding
import com.amanotes.beathop.utils.enums.GameState

class ScoreFragment : Fragment() {
    private var _binding: FragmentScoreBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScoreBinding.inflate(layoutInflater)

        binding.mBtnPlayAgain.setOnClickListener {
            navigateToGameFragment()
        }
        binding.mBtnMenu.setOnClickListener {
            navigateToMenuFragment()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val navArgs: ScoreFragmentArgs by navArgs()
        when (navArgs.gameResult) {
            GameState.IDLE -> {}
            GameState.WON -> binding.tvGameState.text = resources.getString(R.string.you_win)
            GameState.LOSE -> binding.tvGameState.text = resources.getString(R.string.you_lose)
            GameState.DRAW -> binding.tvGameState.text = resources.getString(R.string.draw)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToGameFragment() {
        val action = ScoreFragmentDirections.actionScoreFragmentToGameFragment()
        findNavController().navigate(action)
    }

    private fun navigateToMenuFragment() {
        val action = ScoreFragmentDirections.actionGlobalMenuFragment()
        findNavController().navigate(action)
    }
}