package com.example.rimcat;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

public class GenerateDirectory  extends Application {

    private static String PATIENT_ID;

    public static void setPatientID(String id) {
        PATIENT_ID = id;
    }

    public static File getRootFile(Context context) {
        File root;
        root = new File("/storage/sdcard1");
        if (!root.exists() || !root.canWrite()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS);
            } else {
                root = new File(Environment.getExternalStorageDirectory(), "Documents");
            }
        }
        File directory;
        if (PATIENT_ID == null || PATIENT_ID.equals("")) {
            directory = new File(root, ".RIMCAT");
        } else {
            directory = new File(root, ".RIMCAT/" + PATIENT_ID);
        }
        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.d("MAIN", "Made parent directories");
            }
        }
        return directory;
    }
}
