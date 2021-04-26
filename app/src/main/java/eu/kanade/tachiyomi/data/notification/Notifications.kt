package eu.kanade.tachiyomi.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import eu.kanade.tachiyomi.R
import eu.kanade.tachiyomi.util.notificationManager

/**
 * Class to manage the basic information of all the notifications used in the app.
 */
object Notifications {

    /**
     * Common notification channel and ids used anywhere.
     */
    const val CHANNEL_COMMON = "common_channel"
    const val ID_UPDATER = 1
    const val ID_DOWNLOAD_IMAGE = 2

    /**
     * Notification channel and ids used by the library updater.
     */
    const val CHANNEL_LIBRARY = "library_channel"
    const val ID_LIBRARY_PROGRESS = 101
    const val ID_LIBRARY_RESULT = 102

    /**
     * Notification channel and ids used by the downloader.
     */
    const val CHANNEL_DOWNLOADER = "downloader_channel"
    const val ID_DOWNLOAD_CHAPTER = 201
    const val ID_DOWNLOAD_CHAPTER_ERROR = 202
    const val ID_DOWNLOAD_CHAPTER_COMPLETE = 203

    /**
     * Creates the notification channels introduced in Android Oreo.
     *
     * @param context The application context.
     */
    fun createChannels(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channels = listOf(
                NotificationChannel(CHANNEL_COMMON, context.getString(R.string.channel_common),
                        NotificationManager.IMPORTANCE_LOW),
                NotificationChannel(CHANNEL_LIBRARY, context.getString(R.string.channel_library),
                        NotificationManager.IMPORTANCE_LOW),
                NotificationChannel(CHANNEL_DOWNLOADER, context.getString(R.string.channel_downloader),
                        NotificationManager.IMPORTANCE_LOW)
        )
        context.notificationManager.createNotificationChannels(channels)
    }
}