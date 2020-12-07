package com.example.rimcat.data_log;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class LogcatExportService extends IntentService {
    private static final String TAG = "LogcatExportService";
    private static final String EXTRA_FILE_DESTINATION = "rimcat.file_destination";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public LogcatExportService(String name) {
        super(name);
    }

    public LogcatExportService() {
        super("LogcatExportService");
    }

    public static void log(Context context, File file) {
        Log.d(TAG, "log: Sending intent...");
        Intent intent = new Intent(context, LogcatExportService.class);
        intent.putExtra(EXTRA_FILE_DESTINATION, file.getAbsolutePath());
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        File logFile = new File(intent.getStringExtra(EXTRA_FILE_DESTINATION));
        if (!logFile.exists()) {
            logFile.getParentFile().mkdirs();
            try {
                if (!logFile.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            // clear the previous logcat and then write the new one to the file
            Process process = Runtime.getRuntime().exec("logcat -c");
            process = Runtime.getRuntime().exec("logcat -f " + logFile);
            Log.i(TAG, "onHandleIntent: Logcat file successfully created.");
        } catch ( IOException e ) {
            Log.d(TAG, "onHandleIntent: Error");
            e.printStackTrace();
        }

    }
}
