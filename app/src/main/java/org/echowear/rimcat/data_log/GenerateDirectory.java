package org.echowear.rimcat.data_log;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;

// Commit change
public class GenerateDirectory  extends Application {

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
        File directory = new File(root, "RIMCAT");

        if (!directory.exists()) {
            if (directory.mkdirs()) {
                Log.d("MAIN", "Made parent directories");
            }
        }

        return directory;
    }
}
