package de.mm20.launcher2.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import de.mm20.launcher2.R
import de.mm20.launcher2.ui.assistant.AssistantActivity

/**
 * A small, tap-to-search home screen widget.
 *
 * This does NOT make Kvaesitso the device's launcher. It only draws a search-bar-shaped
 * widget that any launcher (e.g. Huawei's own launcher) can place on its home screens.
 * Tapping it opens Kvaesitso's existing search/assistant overlay
 * ([AssistantActivity]) on top of whatever launcher is currently active, then returns
 * you to that launcher when you back out or pick a result.
 *
 * One provider can have many independent instances - the user can add one copy of this
 * widget to every home screen page if they want.
 */
class SearchWidgetProvider : AppWidgetProvider() {

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        for (appWidgetId in appWidgetIds) {
            updateWidget(context, appWidgetManager, appWidgetId)
        }
    }

    private fun updateWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int,
    ) {
        val views = RemoteViews(context.packageName, R.layout.widget_search)

        val searchIntent = Intent(context, AssistantActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
        // requestCode = appWidgetId so that multiple widget instances (one per home
        // screen page) each get their own PendingIntent instead of overwriting one another.
        val pendingIntent = PendingIntent.getActivity(
            context,
            appWidgetId,
            searchIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
        views.setOnClickPendingIntent(R.id.widget_search_root, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }
}
