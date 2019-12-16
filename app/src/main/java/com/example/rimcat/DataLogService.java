package com.example.rimcat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class DataLogService extends IntentService {
    private static final String TAG = "DataLogService";
    private static final String HEADER = "rimcat.data";
    private static final String EXTRA_DATA = "rimcat.data";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public DataLogService(String name) {
        super(name);
    }

    public DataLogService() {
        super("DataLogService");
    }

    public static void log(Context context, String data) {
        Intent intent = new Intent(context, DataLogService.class);
        intent.putExtra(EXTRA_DATA, data);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || !intent.hasExtra(EXTRA_DATA)) {
            return;
        }
        Log.d(TAG, "onHandleIntent: Here");
    }
}
