package com.amanotes.beathop.data.model.web

import android.content.Context
import com.amanotes.beathop.R

data class Link(
    var googleId: String? = null,
    var appsFlyerUserId: String? = null,
    var subAll: List<String> = listOf("", "", "", "", "", "", "", "", "", ""),
    var deepLink: String? = null,
    var mediaSource: String? = null,
    var afChannel: String? = null,
    var campaign: String? = null,
    var url: String? = null,
    var organicAccess: Boolean? = null
) {
    fun collect(context: Context): String {
        val resources = context.resources
        val packageName = context.packageName
        val appsFlyerDevKey = resources.getString(R.string.apps_dev_key)
        val fbToken = resources.getString(R.string.fb_at)
        val fbAppId = resources.getString(R.string.fb_app_id)

        val link = "${this.url}?media_source=${this.mediaSource}" +
                "&google_adid=${this.googleId}" +
                "&af_userid=${this.appsFlyerUserId}" +
                "&bundle=${packageName}" +
                "&dev_key=${appsFlyerDevKey}" +
                "&fb_at=${fbToken}" +
                "&fb_app_id=${fbAppId}" +
                "&af_channel=${this.afChannel}" +
                "&campaign=${this.campaign}" +
                "&sub1=${
                    try {
                        this.subAll[0]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub2=${
                    try {
                        this.subAll[1]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub3=${
                    try {
                        this.subAll[2]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub4=${
                    try {
                        this.subAll[3]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub5=${
                    try {
                        this.subAll[4]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub6=${
                    try {
                        this.subAll[5]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub7=${
                    try {
                        this.subAll[6]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub8=${
                    try {
                        this.subAll[7]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub9=${
                    try {
                        this.subAll[8]
                    } catch (e: Exception) {
                        ""
                    }
                }" +
                "&sub10=${
                    try {
                        this.subAll[9]
                    } catch (e: Exception) {
                        ""
                    }
                }"
        return link
    }
}
