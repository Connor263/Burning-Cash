package com.amanotes.beathop.ui.game.menu

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.amanotes.beathop.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private var _binding: FragmentMenuBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMenuBinding.inflate(layoutInflater)

        binding.mBtnPlay.setOnClickListener {
            navigateToGameFragment()
        }
        binding.mBtnExit.setOnClickListener {
            requireActivity().finish()
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun navigateToGameFragment() {
        val action = MenuFragmentDirections.actionMenuFragmentToGameFragment()
        findNavController().navigate(action)
    }
}