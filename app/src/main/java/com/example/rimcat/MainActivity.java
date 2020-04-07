package com.example.rimcat;

import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ProgressBar;
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
import com.example.rimcat.fragments.ReadingCompFragment;
import com.example.rimcat.fragments.RecallResponseFragment;
import com.example.rimcat.fragments.SeasonFragment;
import com.example.rimcat.fragments.TodayDateFragment;
import com.example.rimcat.fragments.VerbalRecallFragment;


public class MainActivity extends AppCompatActivity {
    private static final String     TAG = "MainActivity";
    private static final int        BACKGROUND_TRANSITION_TIME = 2000;
    private static final int        NUM_SCREENS = 18;
    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private String                  fragmentTag;
    private int                     viewNumber = 0;
    private ConstraintLayout        appBackground;
    private FloatingActionButton    nextButton;
    private TextView                nextText;
    private ProgressBar             appProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        appBackground = findViewById(R.id.app_background);

        // Initially change view to home fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTag = "HomeFragment";
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new HomeFragment(), "HomeFragment");
        fragmentTransaction.commit();

        // Initialize views and model
        nextButton = findViewById(R.id.floatingActionButton);
        nextText = findViewById(R.id.nextText);
        appProgress = findViewById(R.id.app_progress);
        appProgress.setMax(NUM_SCREENS);
    }

    public void getFragmentData(View view) {
        QuestionFragment fragment = (QuestionFragment) fragmentManager.findFragmentByTag(fragmentTag);
        if (fragment.loadDataModel()) {
            changeBackground();
            fragment.startAnimation(false);
            // Checks to hide or show the Next button
            viewButtonVisibility();
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

    public void changeBackground() {
        TransitionDrawable trans = (TransitionDrawable) appBackground.getBackground();
        if (viewNumber == 0 || viewNumber % 2 == 0)
            trans.startTransition(BACKGROUND_TRANSITION_TIME);
        else
            trans.reverseTransition(BACKGROUND_TRANSITION_TIME);
    }

    public int getViewNumber() {
        return viewNumber;
    }

    public void incrementViewNumber() {
        viewNumber++;
        appProgress.setProgress(viewNumber);
        Log.d(TAG, "incrementViewNumber: View number is " + viewNumber);
    }

    private void viewButtonVisibility() {
        Log.d(TAG, "viewButtonVisibility: View Number: " + viewNumber);
        if (    viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_2 ||
                viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_3 ||
                viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_4 ||
                viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_5 ||
                viewNumber == DataLogModel.INSTRUCTIONS_SCREEN_6 ||
                viewNumber == DataLogModel.VERBAL_RECALL_SCREEN_1 ||
                viewNumber == DataLogModel.VERBAL_RECALL_SCREEN_2) {
            nextText.setVisibility(View.INVISIBLE);
            nextButton.hide();
        }
        else if (nextText.getVisibility() == View.INVISIBLE) {
            nextText.setVisibility(View.VISIBLE);
            nextButton.show();
        }
    }

    //TODO: Make this shorter!!
    private void debugScreenSelect(int itemID) {
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (itemID) {
            case R.id.screen_home_om:
                this.viewNumber = DataLogModel.HOME_SCREEN;
                fragmentTag = "HomeFragment";
                fragmentTransaction.replace(R.id.container, new HomeFragment(), "HomeFragment");
                break;
            case R.id.screen_inst_1_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_1;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_education_om:
                this.viewNumber = DataLogModel.EDUCATION_SCREEN;
                fragmentTag = "EducationFragment";
                fragmentTransaction.replace(R.id.container, new EducationFragment(), "EducationFragment");
                break;
            case R.id.screen_date_om:
                this.viewNumber = DataLogModel.TODAYS_DATE_SCREEN;
                fragmentTag = "TodayDateFragment";
                fragmentTransaction.replace(R.id.container, new TodayDateFragment(), "TodayDateFragment");
                break;
            case R.id.screen_day_om:
                this.viewNumber = DataLogModel.DAY_OF_WEEK_SCREEN;
                fragmentTag = "DayOfWeekFragment";
                fragmentTransaction.replace(R.id.container, new DayOfWeekFragment(), "DayOfWeekFragment");
                break;
            case R.id.screen_season_om:
                this.viewNumber = DataLogModel.SEASON_SCREEN;
                fragmentTag = "SeasonFragment";
                fragmentTransaction.replace(R.id.container, new SeasonFragment(), "SeasonFragment");
                break;
            case R.id.screen_inst_2_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_2;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_1_om:
                this.viewNumber = DataLogModel.VERBAL_RECALL_SCREEN_1;
                fragmentTag = "VerbalRecallFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "VerbalRecallFragment");
                break;
            case R.id.screen_recall_1_om:
                this.viewNumber = DataLogModel.RECALL_RESPONSE_SCREEN_1;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new RecallResponseFragment(), "RecallResponseFragment");
                break;
            case R.id.screen_inst_3_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_3;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_2_om:
                this.viewNumber = DataLogModel.RECALL_RESPONSE_SCREEN_2;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new RecallResponseFragment(), "RecallResponseFragment");
                break;
            case R.id.screen_recall_2_om:
                this.viewNumber = DataLogModel.VERBAL_RECALL_SCREEN_2;
                fragmentTag = "VerbalRecallFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "VerbalRecallFragment");
                break;
            case R.id.screen_inst_4_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_4;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_img_nm_om:
                this.viewNumber = DataLogModel.IMAGE_NAME_SCREEN;
                fragmentTag = "ImageNameFragment";
                fragmentTransaction.replace(R.id.container, new ImageNameFragment(), "ImageNameFragment");
                break;
            case R.id.screen_inst_5_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_5;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_fig_study_om:
                this.viewNumber = DataLogModel.FIGURE_STUDY_SCREEN;
                fragmentTag = "FigureStudyFragment";
                fragmentTransaction.replace(R.id.container, new FigureStudyFragment(), "FigureStudyFragment");
                break;
            case R.id.screen_fig_select_om:
                this.viewNumber = DataLogModel.FIGURE_SELECT_SCREEN;
                fragmentTag = "FigureSelectFragment";
                fragmentTransaction.replace(R.id.container, new FigureSelectFragment(), "FigureSelectFragment");
                break;
            case R.id.screen_inst_6_om:
                this.viewNumber = DataLogModel.INSTRUCTIONS_SCREEN_6;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_read_comp:
                this.viewNumber = DataLogModel.READING_COMP_SCREEN;
                fragmentTag = "ReadingCompFragment";
                fragmentTransaction.replace(R.id.container, new ReadingCompFragment(), "ReadingCompFragment");
                break;
        }
        appProgress.setProgress(viewNumber);
        viewButtonVisibility();
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.overflow_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        debugScreenSelect(item.getItemId());
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // Do nothing
    }
}
