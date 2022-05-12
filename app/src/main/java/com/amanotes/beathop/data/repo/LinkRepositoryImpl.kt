package com.amanotes.beathop.data.repo

import android.content.Context
import com.amanotes.beathop.interfaces.LinkRepository

const val LINK_SHARED_PREF = "LINK_SHARED_PREF"
const val CACHE_LINK = "CACHE_LINK"

class LinkRepositoryImpl(context: Context) : LinkRepository {
    private val sharedPref = context.getSharedPreferences(LINK_SHARED_PREF, Context.MODE_PRIVATE)
    override var link: String
        get() = sharedPref.getString(CACHE_LINK, "") ?: ""
        set(value) {
            sharedPref.edit().putString(CACHE_LINK, value).apply()
        }
}