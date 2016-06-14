package com.sam_chordas.android.stockhawk.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.sam_chordas.android.stockhawk.R;
import com.sam_chordas.android.stockhawk.data.QuoteColumns;
import com.sam_chordas.android.stockhawk.data.QuoteProvider;
import com.sam_chordas.android.stockhawk.rest.Utils;

import java.util.ArrayList;

/**
 * Created by durga on 6/14/16.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class DetailWidgetRemoteViewService extends RemoteViewsService
{
    public final String LOG_TAG = DetailWidgetRemoteViewService.class.getSimpleName();
    ArrayList<Stock> stocksList;
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent)
    {
        return new RemoteViewsFactory()
        {
            @Override
            public void onCreate()
            {

            }
            @Override
            public void onDataSetChanged()
            {

                Cursor cursor = getContentResolver().query(QuoteProvider.Quotes.CONTENT_URI,
                        new String[]{QuoteColumns._ID, QuoteColumns.SYMBOL, QuoteColumns.BIDPRICE,
                                QuoteColumns.PERCENT_CHANGE, QuoteColumns.CHANGE, QuoteColumns.ISUP},
                        QuoteColumns.ISCURRENT + " = ?",
                        new String[]{"1"},
                        null);
                if(cursor.getCount() > 0) {
                    stocksList = new ArrayList<>(cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {
                            stocksList.add(new Stock(
                                    cursor.getString(cursor.getColumnIndex("symbol")),
                                    cursor.getString(cursor.getColumnIndex("bid_price")),
                                    Utils.showPercent ? cursor.getString(cursor.getColumnIndex("percent_change")) : cursor.getString(cursor.getColumnIndex("change")),
                                    cursor.getInt(cursor.getColumnIndex("is_up")) == 1
                            ));
                        } while (cursor.moveToNext());
                    }
                }
                cursor.close();
            }

            @Override
            public void onDestroy()
            {

            }

            @Override
            public int getCount()
            {
                if(stocksList == null)
                {
                    return -1;
                }
                else
                {
                    return stocksList.size();
                }
            }

            @Override
            public RemoteViews getViewAt(int position)
            {
                RemoteViews row = new RemoteViews(getPackageName(),
                        R.layout.list_item_quote);

                String symbol = stocksList.get(position).symbol;
                String bidPrice = stocksList.get(position).bidPrice;
                //setRemoteContentDescription(row, getResources().getString(R.string.detailed_stock_message) + symbol);
                //setRemoteContentDescription(row,  symbol);
                String change = stocksList.get(position).change;
                row.setTextViewText(R.id.stock_symbol, symbol);
                row.setTextViewText(R.id.bid_price, bidPrice);
                row.setTextViewText(R.id.change, change);
                if (stocksList.get(position).isUp) {
                    row.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_green);
                } else {
                    row.setInt(R.id.change, "setBackgroundResource", R.drawable.percent_change_pill_red);
                }
                String selectedsymbolStr = getResources().getString(R.string.selectedsymbol);
                final Intent fillInIntent = new Intent();
                fillInIntent.putExtra(selectedsymbolStr, symbol);
                row.setOnClickFillInIntent(R.id.list_item, fillInIntent);
                return row;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.stock_symbol, description);
            }

            @Override
            public RemoteViews getLoadingView()
            {
                return null;
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position)
            {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }
        };
    }
    static class Stock {
        public final String symbol;
        public final String bidPrice;
        public final String change;
        public final boolean isUp;

        public Stock(String symbol, String bidPrice, String change, boolean isUp) {
            this.symbol = symbol;
            this.bidPrice = bidPrice;
            this.change = change;
            this.isUp = isUp;
        }
    }

}
