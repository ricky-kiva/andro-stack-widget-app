package com.rickyslash.stackwidgetapp

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import android.widget.Toast
import androidx.core.net.toUri

class ImageBannerWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    // this onReceive is triggered when there is Intent to this class object
    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        if (intent?.action != null) {
            if (intent.action == TOAST_ACTION) {
                val viewIndex = intent.getIntExtra(EXTRA_ITEM, 0)
                Toast.makeText(context, "It's image $viewIndex", Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val TOAST_ACTION = "com.rickyslash.TOAST_ACTION"
        const val EXTRA_ITEM = "com.rickyslash.EXTRA_ITEM"

        internal fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            // making intent for PendingIntent that will be connected to RemoteViews
            val intent = Intent(context, StackWidgetService::class.java)
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
            // this allows the intent to be passed to RemoteViewsFactory, as a Uri
            // Uri (Uniform Resource Identifier) is a string that identify a resource name
            intent.data = intent.toUri(Intent.URI_INTENT_SCHEME).toUri()

            // create RemoteViews instance. It allows to manipulate View hierarchy from different process
            val views = RemoteViews(context.packageName, R.layout.image_banner_widget)
            // sets StackView & defined intent, for RemoteViews
            views.setRemoteAdapter(R.id.stack_view, intent)
            // set View to display when adapter has no data
            views.setEmptyView(R.id.stack_view, R.id.empty_view)

            // set intent for PendingIntent in this widget
            val toastIntent = Intent(context, ImageBannerWidget::class.java)
            toastIntent.action = TOAST_ACTION
            toastIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)

            // starts getting broadcast for PendingIntent in this widget
            val toastPendingIntent = PendingIntent.getBroadcast(context, 0, toastIntent,
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    // this flag allows widget to update it's state on Android S+
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                } else {
                    0
                })

            // this sets PendingIntent for all items on StackView
            views.setPendingIntentTemplate(R.id.stack_view, toastPendingIntent)
            // this will update current layout of the widget
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }
    }

}

