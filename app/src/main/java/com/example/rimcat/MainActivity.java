package com.example.rimcat;

import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimcat.fragments.HomeFragment;
import com.example.rimcat.fragments.QuestionFragment;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FragmentManager     fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String              fragmentTag;
    private DataLogModel        logData;
    private int                 viewNumber = 0;
    private FloatingActionButton nextButton;
    private TextView            nextText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // Initially change view to home fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTag = "HomeFragment";
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new HomeFragment(), "HomeFragment");
        fragmentTransaction.commit();

        // Initialize views and model
        nextButton = findViewById(R.id.floatingActionButton);
        nextText = findViewById(R.id.nextText);
        logData = new DataLogModel();
    }

    public void getFragmentData(View view) {
        QuestionFragment fragment = (QuestionFragment) fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment.loadDataModel(logData)) {
            fragment.startAnimation(false);
            // TODO: Make method for checking this stuff. Final Screen, Verbal screen for hiding next button, etc.
            if (viewNumber == DataLogModel.FINAL_SCREEN)
                callLogData();
            else if (viewNumber == DataLogModel.VERBAL_LEARNING_INST_VIEW) {
                nextText.setVisibility(View.INVISIBLE);
                nextButton.hide();
            }
            else if (nextText.getVisibility() == View.INVISIBLE) {
                nextText.setVisibility(View.VISIBLE);
                nextButton.show();
            }
        } else {
            Toast.makeText(this, "Please fill out all fields before proceeding.", Toast.LENGTH_SHORT).show();
        }
    }

    public void addFragment(Fragment nextFragment, String fragmentTag) {
        Log.d(TAG, "addFragment: Moving to new fragment --- " + fragmentTag);
        this.fragmentTag = fragmentTag;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, nextFragment, fragmentTag);
        fragmentTransaction.commit();
    }

    private void callLogData() {
        String resultString = "";
        if (logData != null) {
            // Create log string
            DataLogService.log(this, resultString);
        }
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void incrementViewNumber() {
        viewNumber++;
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
