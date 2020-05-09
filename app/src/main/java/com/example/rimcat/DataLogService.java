package com.example.rimcat;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class DataLogService extends IntentService {
    private static final String TAG = "DataLogService";
    private static final String HEADER = "patient_id,question,response,start_time,end_time,date";
    private static final String EXTRA_FILE_DESTINATION = "rimcat.file_destination";
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

    public static void log(Context context, File file, String data) {
        Intent intent = new Intent(context, DataLogService.class);
        intent.putExtra(EXTRA_FILE_DESTINATION, file.getAbsolutePath());
        intent.putExtra(EXTRA_DATA, data);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || !intent.hasExtra(EXTRA_DATA)) {
            return;
        }

        // if this file is new, create the file and flag as new.
        boolean newFile = false;
        File file = new File(intent.getStringExtra(EXTRA_FILE_DESTINATION));
        if (!file.exists()) {
            newFile = true;
            file.getParentFile().mkdirs();
            try {
                if (!file.createNewFile()) {
                    return;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // write to the file
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file, true);
            if (newFile) {
                // if flagged as a new file, apply the header before the data.
                fileOutputStream.write(HEADER.getBytes());
                fileOutputStream.write("\n".getBytes());
            }
            fileOutputStream.write(intent.getStringExtra(EXTRA_DATA).getBytes());
            fileOutputStream.write("\n".getBytes());
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.i(TAG, "Data successfully logged.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
