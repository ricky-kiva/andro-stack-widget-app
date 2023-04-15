package com.rickyslash.stackwidgetapp

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf

internal class StackRemoteViewsFactory(private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private val mWidgetItems = ArrayList<Bitmap>()

    override fun onCreate() {
    }

    // sets data for the RemoteView
    override fun onDataSetChanged() {
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.darth_vader))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.star_wars_logo))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.storm_trooper))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.starwars))
        mWidgetItems.add(BitmapFactory.decodeResource(mContext.resources, R.drawable.falcon))
    }

    override fun onDestroy() {
    }

    // set data size for the RemoteView
    override fun getCount(): Int = mWidgetItems.size

    // create RemoteViews for each item in StackView
    override fun getViewAt(position: Int): RemoteViews {

        // set widget_item layout for every RemoteViews & pass Bitmap from mWidgetItems for each
        val rv = RemoteViews(mContext.packageName, R.layout.widget_item)
        rv.setImageViewBitmap(R.id.imageView, mWidgetItems[position])

        // bundling in key-value pair method, then pass extras to intent's extras
        val extras = bundleOf(ImageBannerWidget.EXTRA_ITEM to position)
        val fillInIntent = Intent()
        fillInIntent.putExtras(extras)

        // setOnClickFillInIntent() sets `PendingIntent` template to be used by `specified View` when it's clicked
        rv.setOnClickFillInIntent(R.id.imageView, fillInIntent)
        return rv
    }

    // this specifies which view to show when data is loading
    override fun getLoadingView(): RemoteViews? = null

    // this returns number of types that will be used to display data (RemoteViews)
    override fun getViewTypeCount(): Int = 1

    // return unique ID for item at specified position. 0 means items are not uniquely identified
    override fun getItemId(position: Int): Long = 0

    // true if each item's ID remains the same even if item's position changed. False means items do not have stable IDs
    override fun hasStableIds(): Boolean = false
}