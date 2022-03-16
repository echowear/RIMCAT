package org.echowear.rimcatbeta.data_log;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import org.echowear.rimcatbeta.MainActivity;

import java.io.File;
import java.io.IOException;

public class JSONLogcatExportService extends IntentService {
    MainActivity mainActivity = new MainActivity();
    private static final String TAG = "LogcatExportService";
    private static final String EXTRA_FILE_DESTINATION = "rimcat.file_destination";
    private static final String EXTRA_DATA = "rimcat.data";
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public JSONLogcatExportService(String name) {
        super(name);
    }

    public JSONLogcatExportService() {
        super("LogcatExportService");
    }

    public static void log(Context context, File file, String[][][] data) {
        Log.d(TAG, "log: Sending intent...");
        Intent intent = new Intent(context, JSONLogcatExportService.class);
        intent.putExtra(EXTRA_FILE_DESTINATION, file.getAbsolutePath());
        intent.putExtra(EXTRA_DATA, data);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent == null) {
            return;
        }

        File logFile = new File(intent.getStringExtra(EXTRA_FILE_DESTINATION));
        Log.d(TAG, "onHandleIntent: " + logFile);
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
            Log.i(TAG, "DATA: " + mainActivity.coordsJson());
            Log.i(TAG, "DATA: " + mainActivity.buttonCoordsJson());
            process = Runtime.getRuntime().exec( "logcat -f " + logFile + " *:S LogActivity:V LogActivityButton:V");
            Log.i(TAG, "onHandleIntent: Logcat file successfully created.");
        } catch ( IOException e ) {
            Log.d(TAG, "onHandleIntent: Error");
            e.printStackTrace();
        }

    }
}
