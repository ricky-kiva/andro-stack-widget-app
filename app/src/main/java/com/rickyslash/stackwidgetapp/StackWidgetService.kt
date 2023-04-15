package com.rickyslash.stackwidgetapp

import android.content.Intent
import android.widget.RemoteViewsService

// this class provides `RemoteViewsFactory` for `RemoteViewsAdapter`
// `RemoteViewsAdapter` will bind `RemoteViews` to data
// `RemoteViewsFactory` will create the views that make up the `RemoteViewsAdapter`
class StackWidgetService: RemoteViewsService() {
    // this get the custom RemoteViewsFactory
    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory {
        return StackRemoteViewsFactory(this.applicationContext)
    }
}