package com.tencent.joox.sdk.utils.ui

import android.content.ContentResolver
import android.content.Context
import android.content.res.Resources
import android.net.Uri

object ResourceUtil {

    fun resIdToUrl(context: Context, resId: Int): String {
        val resources = context.resources
        return try {
            Uri.parse(
                ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
                        + resources.getResourcePackageName(resId) + '/'
                        + resources.getResourceTypeName(resId) + '/'
                        + resources.getResourceEntryName(resId)
            ).toString()
        } catch (e: Resources.NotFoundException) {
            ""
        }
    }
}