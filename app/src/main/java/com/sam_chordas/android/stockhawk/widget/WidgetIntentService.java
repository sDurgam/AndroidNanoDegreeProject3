package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.ui.MyStocksActivity;

/**
 * Created by durga on 6/14/16.
 */
public class WidgetIntentService extends IntentService
{
    public  WidgetIntentService()
    {
        super("Update Widget Service");
    }
    @Override
    public void onStart(Intent intent, int startId)
    {
        super.onStart(intent, startId);
        onHandleIntent(intent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        onHandleIntent(intent);
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    protected void onHandleIntent(Intent intent)
    {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                StockHawkAppWidgetProvider.class));

        // Perform this loop procedure for each Today widget
        for (int appWidgetId : appWidgetIds)
        {
            int layoutId = R.layout.list_item_quote;
            RemoteViews views = new RemoteViews(getPackageName(), layoutId);
            Cursor c = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                    new String[]{ QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                            QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                    QuoteColumns.ISCURRENT + " = ?",
                    new String[]{"1"},null);

            if (c.getCount() != 0)
            {
                c.moveToFirst();
                do
                {
                    Log.d("tag", c.getString(1) + "," + c.getString(3));
                    views.setTextViewText(R.id.stock_symbol, c.getString(1));
                    views.setTextViewText(R.id.bid_price, c.getString(3));
                }while(c.moveToNext());
            }
            c.close();
            // Create an Intent to launch MainActivity
            Intent launchIntent = new Intent(this, MyStocksActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
//        views.setContentDescription(R.id.widget_icon, description);
    }
}
