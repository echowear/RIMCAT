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

import com.example.rimcat.fragments.DayOfWeekFragment;
import com.example.rimcat.fragments.EducationFragment;
import com.example.rimcat.fragments.FigureSelectFragment;
import com.example.rimcat.fragments.FigureStudyFragment;
import com.example.rimcat.fragments.HomeFragment;
import com.example.rimcat.fragments.ImageNameFragment;
import com.example.rimcat.fragments.InstructionsFragment;
import com.example.rimcat.fragments.QuestionFragment;
import com.example.rimcat.fragments.RecallResponseFragment;
import com.example.rimcat.fragments.SeasonFragment;
import com.example.rimcat.fragments.TodayDateFragment;
import com.example.rimcat.fragments.VerbalRecallFragment;

//TODO: Find a way to log the data for the first section.
public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private FragmentManager     fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private String              fragmentTag;
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
    }

    public void getFragmentData(View view) {
        QuestionFragment fragment = (QuestionFragment) fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment.loadDataModel()) {
            fragment.startAnimation(false);
            // Checks to hide or show the Next button
            if (    viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_2 ||
                    viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_3 ||
                    viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_4 ||
                    viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_5) {
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

    public int getViewNumber() {
        return viewNumber;
    }

    public void incrementViewNumber() {
        viewNumber++;
        Log.d(TAG, "incrementViewNumber: View number is " + viewNumber);
    }

    //TODO: Make this shorter!!
    private void debugScreenSelect(int viewNumber) {
        this.viewNumber = viewNumber;
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (viewNumber) {
            case DataLogModel.HOME_SCREEN:
                fragmentTransaction.replace(R.id.container, new HomeFragment(), "HomeFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_1:
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case DataLogModel.EDUCATION_SCREEN:
                fragmentTransaction.replace(R.id.container, new EducationFragment(), "EducationFragment");
                break;
            case DataLogModel.TODAYS_DATE_SCREEN:
                fragmentTransaction.replace(R.id.container, new TodayDateFragment(), "TodayDateFragment");
                break;
            case DataLogModel.DAY_OF_WEEK_SCREEN:
                fragmentTransaction.replace(R.id.container, new DayOfWeekFragment(), "DayOfWeekFragment");
                break;
            case DataLogModel.SEASON_SCREEN:
                fragmentTransaction.replace(R.id.container, new SeasonFragment(), "SeasonFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_2:
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case DataLogModel.VERBAL_RECALL_SCREEN:
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "VerbalRecallFragment");
                break;
            case DataLogModel.RECALL_RESPONSE_SCREEN_1:
                fragmentTransaction.replace(R.id.container, new RecallResponseFragment(), "RecallResponseFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_3:
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case DataLogModel.RECALL_RESPONSE_SCREEN_2:
                fragmentTransaction.replace(R.id.container, new RecallResponseFragment(), "RecallResponseFragment");
                break;
            case DataLogModel.VERBAL_RECALL_SCREEN_3:
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "VerbalRecallFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_4:
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case DataLogModel.IMAGE_NAME_SCREEN:
                fragmentTransaction.replace(R.id.container, new ImageNameFragment(), "ImageNameFragment");
                break;
            case DataLogModel.INSTRUCTIONS_SCREEN_5:
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case DataLogModel.FIGURE_STUDY_SCREEN:
                fragmentTransaction.replace(R.id.container, new FigureStudyFragment(), "FigureStudyFragment");
                break;
            case DataLogModel.FIGURE_SELECT_SCREEN:
                fragmentTransaction.replace(R.id.container, new FigureSelectFragment(), "FigureSelectFragment");
                break;
        }
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
