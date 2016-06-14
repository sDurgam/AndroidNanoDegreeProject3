package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.ui.DisplayStockGraphActivity;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by durga on 6/13/16.
 */
public class StockHawkAppWidgetProvider extends AppWidgetProvider
{
    Context mContext;
static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                            int appWidgetId) {
    RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.stockhawk_appwidget);
//        views.setTextViewText(R.id.appwidget_text, widgetText);
// Create an Intent to launch MainActivity
    Intent intent = new Intent(context, MyStocksActivity.class);
    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
    views.setOnClickPendingIntent(R.id.widget, pendingIntent);
    // Set up the collection
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
        setRemoteAdapter(context, views);
    } else {
        setRemoteAdapterV11(context, views);
    }
    Intent clickIntentTemplate = new Intent(context, DisplayStockGraphActivity.class);
    PendingIntent clickPendingIntentTemplate = android.support.v4.app.TaskStackBuilder.create(context)
            .addNextIntentWithParentStack(clickIntentTemplate)
            .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
    views.setPendingIntentTemplate(R.id.stockwidget_list, clickPendingIntentTemplate);
    views.setEmptyView(R.id.stockwidget_list, R.id.widget_empty);
    // Instruct the widget manager to update the widget
    appWidgetManager.updateAppWidget(appWidgetId, views);
}

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds)
    {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private static void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.stockwidget_list,
                new Intent(context, DetailWidgetRemoteViewService.class));
    }

    /**
     * Sets the remote adapter used to fill in the list items
     *
     * @param views RemoteViews to set the RemoteAdapter
     */
    @SuppressWarnings("deprecation")
    private static void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.stockwidget_list,
                new Intent(context, DetailWidgetRemoteViewService.class));
    }
}