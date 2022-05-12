package com.amanotes.beathop.ui.web

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.amanotes.beathop.data.model.web.Link
import com.onesignal.OneSignal

class InitViewModel : ViewModel() {
    private val mainLink = Link()


    fun getAFMediaSourceAndOrganic(): Pair<String?, Boolean?> {
        return mainLink.mediaSource to mainLink.organicAccess
    }

    fun setGoogleID(googleId: String) {
        mainLink.googleId = googleId
        OneSignal.setExternalUserId(googleId)
    }

    fun setOrganic(organicAccess: Boolean) {
        mainLink.organicAccess = organicAccess
    }

    fun setUrl(url: String) {
        mainLink.url = url
    }

    fun setFBDeepLink(targetUri: Uri?) {
        mainLink.deepLink = targetUri?.toString()

        mainLink.deepLink?.let {
            val arrayDeepLink = it.split("//")
            mainLink.subAll = arrayDeepLink[1].split("_")
        }
    }

    fun setAFUserID(id: String?) {
        mainLink.appsFlyerUserId = id
    }


    fun setAfStatus(value: String) {
        if (value == "Organic" && mainLink.deepLink == null) {
            mainLink.mediaSource = "organic"
        }
    }

    fun setCampaign(value: String) {
        mainLink.campaign = value
        mainLink.campaign?.let {
            mainLink.subAll = it.split("_")
        }
    }

    fun setMediaSource(value: String) {
        mainLink.mediaSource = value
    }

    fun setAfChannel(value: String) {
        mainLink.afChannel = value
    }

    fun buildLink(context: Context): String = mainLink.collect(context)
}