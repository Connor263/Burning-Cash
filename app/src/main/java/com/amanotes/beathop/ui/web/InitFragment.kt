package com.amanotes.beathop.ui.web

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.amanotes.beathop.R
import com.amanotes.beathop.data.repo.LinkRepositoryImpl
import com.amanotes.beathop.databinding.FragmentInitBinding
import com.amanotes.beathop.di.MainApplication
import com.amanotes.beathop.utils.isInternetAvailable
import com.appsflyer.AppsFlyerLib
import com.facebook.FacebookSdk
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class InitFragment : Fragment() {
    private var _binding: FragmentInitBinding? = null
    private val binding get() = _binding!!

    private val viewModel: InitViewModel by viewModels()

    private lateinit var linkRepository: LinkRepositoryImpl

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInitBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startInitLoading()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun startInitLoading() {
        binding.pBar.visibility = View.VISIBLE
        linkRepository = LinkRepositoryImpl(requireContext())
        val link = linkRepository.link

        if (isInternetAvailable(requireContext())) {
            if (link != "") {
                navigateToWebFragment(link)
                binding.pBar.visibility = View.GONE
            } else {
                setupFirebaseKeys()
            }
        } else {
            showNoInternetDialog()
            binding.pBar.visibility = View.GONE
        }
    }

    private fun setupFirebaseKeys() {
        val remoteConfig = FirebaseRemoteConfig.getInstance()
        val settings =
            FirebaseRemoteConfigSettings.Builder()
                .setMinimumFetchIntervalInSeconds(2500)
                .build()
        remoteConfig.setConfigSettingsAsync(settings)

        remoteConfig.fetchAndActivate().addOnCompleteListener {
            val url = remoteConfig.getString(resources.getString(R.string.firebase_root_url))
            val organic = remoteConfig.getBoolean(resources.getString(R.string.firebase_organic))
            viewModel.setOrganic(organic)
            viewModel.setUrl(url)
            if (url.contains("http")) {
                beginWork()
            } else {
                val action = InitFragmentDirections.actionGlobalMenuFragment()
                findNavController().navigate(action)
            }
        }
    }

    private fun beginWork() = lifecycleScope.launch(Dispatchers.IO) {
        beginInitFacebook()
        setGoogleAID()

        lifecycleScope.launch(Dispatchers.Main) {
            setAfParams()
        }
    }

    private fun setAfParams() {
        val uid = AppsFlyerLib.getInstance().getAppsFlyerUID(requireContext())
        viewModel.setAFUserID(uid)
        MainApplication.appsFlyerLiveData.observe(viewLifecycleOwner) {
            for (inform in it) {
                when (inform.key) {
                    "af_status" -> {
                        viewModel.setAfStatus(inform.value.toString())
                    }
                    "campaign" -> {
                        viewModel.setCampaign(inform.value.toString())
                    }
                    "media_source" -> {
                        viewModel.setMediaSource(inform.value.toString())
                    }
                    "af_channel" -> {
                        viewModel.setAfChannel(inform.value.toString())
                    }
                }
            }
            val pair = viewModel.getAFMediaSourceAndOrganic()
            val mediaSource = pair.first
            val organicAccess = pair.second

            if (mediaSource == "organic" && organicAccess == false) {
                val action = InitFragmentDirections.actionGlobalMenuFragment()
                findNavController().navigate(action)
                return@observe
            }
            buildLink()
        }
    }

    private fun setGoogleAID() {
        val adIdInfo = AdvertisingIdClient.getAdvertisingIdInfo(requireContext())
        viewModel.setGoogleID(adIdInfo.id.toString())
    }

    private fun beginInitFacebook() {
        FacebookSdk.setAutoInitEnabled(true)
        FacebookSdk.fullyInitialize()
        AppLinkData.fetchDeferredAppLinkData(requireContext()) {
            val uri = it?.targetUri
            viewModel.setFBDeepLink(uri)
        }
    }

    private fun buildLink() {
        val link = viewModel.buildLink(requireContext())
        linkRepository.link = link
        navigateToWebFragment(link)
    }

    private fun showNoInternetDialog(): AlertDialog =
        MaterialAlertDialogBuilder(requireContext()).setTitle("No internet connection")
            .setMessage("Check your internet connection and try again later")
            .setCancelable(false)
            .setPositiveButton("Try again") { dialog, _ ->
                startInitLoading()
                dialog.dismiss()
            }
            .show()

    private fun navigateToWebFragment(link: String) {
        val action = InitFragmentDirections.actionInitFragmentToWebFragment(link)
        findNavController().navigate(action)
    }
}