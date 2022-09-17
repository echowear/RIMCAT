package org.echowear.rimcatbeta.data_log;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.File;

// Commit change
public class GenerateDirectory  extends Application {

    public static File getRootFile() {
        File root;
        root = new File("/storage/sdcard1");
        if (!root.exists() || !root.canWrite()) {
            root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOCUMENTS);
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
