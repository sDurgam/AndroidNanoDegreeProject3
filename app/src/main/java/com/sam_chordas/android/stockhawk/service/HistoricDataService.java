package com.sam_chordas.android.stockhawk.service;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by durga on 6/12/16.
 */
public class HistoricDataService extends IntentService
{
    String symbol;
    public HistoricDataService()
    {
        super("HistoricDataService");
    }

    @Override
    public void onCreate()
    {
        super.onCreate();
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
        if(intent.getExtras() != null && intent.getExtras().getString("selectedsymbol") != null)
        {
            symbol = intent.getExtras().getString("selectedsymbol").toString();
        }
    }
}
