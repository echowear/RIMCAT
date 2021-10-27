package com.example.rimcat;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Handler;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rimcat.data_log.CorrectAnswerDictionary;
import com.example.rimcat.data_log.GenerateDirectory;
import com.example.rimcat.data_log.LogcatExportService;
import com.example.rimcat.dialogs.RecallFinishDialog;
import com.example.rimcat.dialogs.RetryDialog;
import com.example.rimcat.fragments.ComputationFragment;
import com.example.rimcat.fragments.DayOfWeekFragment;
import com.example.rimcat.fragments.DigitSpanFragment;
import com.example.rimcat.fragments.EducationFragment;
import com.example.rimcat.fragments.FigureSelectFragment;
import com.example.rimcat.fragments.FigureStudyFragment;
import com.example.rimcat.fragments.FinishFragment;
import com.example.rimcat.fragments.HomeFragment;
import com.example.rimcat.fragments.ImageNameFragment;
import com.example.rimcat.fragments.InstructionsFragment;
import com.example.rimcat.fragments.KeyboardFragment;
import com.example.rimcat.fragments.QuestionFragment;
import com.example.rimcat.fragments.ReactionFragment;
import com.example.rimcat.fragments.StoryLearningFragment;
import com.example.rimcat.fragments.StoryMemoryFragment;
import com.example.rimcat.fragments.VerbalRecallFragment;
import com.example.rimcat.fragments.SeasonFragment;
import com.example.rimcat.fragments.SemanticChoiceFragment;
import com.example.rimcat.fragments.SemanticRelatednessFragment;
import com.example.rimcat.fragments.TodayDateFragment;
import com.example.rimcat.fragments.VerbalLearningFragment;
import com.example.rimcat.fragments.VerbalRecognitionFragment;
import com.example.rimcat.fragments.VideoFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements RetryDialog.RetryDialogListener, RecallFinishDialog.RecallFinishDialogListener {
    private static final String     TAG = "MainActivity";
    private static final int        MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1400;
    private static final int        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1401;
    private static final int        MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 1402;
    private static final int        RESULT_SPEECH = 65676;
    private static final int        BACKGROUND_TRANSITION_TIME = 2000;
    private static final int        NUM_SCREENS = 43;
    private static final int        BLINKING_START_SECS = 5;
    private FrameLayout             container;
    private FragmentManager         fragmentManager;
    private FragmentTransaction     fragmentTransaction;
    private String                  fragmentTag;
    private int                     viewNumber = 0;
    private ConstraintLayout        appBackground;
    private Button                  nextButton;
    private ImageView               blinkingArrow;
    private int                     currentArrowDrawable;
    private long                    startTime;
    private Handler                 timerHandler;
    private Runnable                timerRunnable;
    private boolean                 isNextButtonReady;
    private ProgressBar             appProgress;
    protected TextToSpeech          textToSpeech;
    protected boolean               isTTSInitialized;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create log file and start logging
        LogcatExportService.log(this, new File(GenerateDirectory.getRootFile(this), "logcat_" + System.currentTimeMillis() + ".txt"));

        // Set up text to speech. Main screen will be set up after text to speech initialization.
        setUpTextToSpeech();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setInitialHomeFragment() {
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        CorrectAnswerDictionary.loadAnswers();
        appBackground = findViewById(R.id.app_background);

        // Initialize views and model
        nextButton = findViewById(R.id.next_button);
        nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundColor)));
        appProgress = findViewById(R.id.app_progress);
        appProgress.setMax(NUM_SCREENS);
        container = findViewById(R.id.container);
        container.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideSoftKeyboard(MainActivity.this);
                return false;
            }
        });

        // Initialize blinking arrow logic
        blinkingArrow = findViewById(R.id.blinkingArrow);
        blinkingArrow.setVisibility(View.INVISIBLE);
        blinkingArrow.setImageResource(R.drawable.arrow_color);
        currentArrowDrawable = R.drawable.arrow_color;
        timerHandler = new Handler();
        timerRunnable = new Runnable() {
            @Override
            public void run() {
                timerRunnableOnTick();
            }
        };

        // Initially change view to home fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTag = "HomeFragment";
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, new HomeFragment(), "HomeFragment");
        fragmentTransaction.commit();
    }

    /** nextButton onClick function
     *
     * @param activity
     */
    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        if(inputMethodManager.isAcceptingText()){
            inputMethodManager.hideSoftInputFromWindow(
                    activity.getCurrentFocus().getWindowToken(),
                    0
            );
        }
    }

    public void getFragmentData(View view) {
        if (isNextButtonReady) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);
            } else if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MY_PERMISSIONS_REQUEST_RECORD_AUDIO);
            } else {
                QuestionFragment fragment = (QuestionFragment) fragmentManager.findFragmentByTag(fragmentTag);
                if (fragment.loadDataModel()) {
                    stopBlinkingArrows();
                    isNextButtonReady = false;
                    nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.backgroundColor)));
//                    nextText.setTextColor(getResources().getColor(R.color.backgroundColor));
//                    changeBackground();
                    fragment.startAnimation(false);
                    // Checks to hide or show the Next button
                    viewButtonVisibility();
                } else {
                    Toast toast = Toast.makeText(this, "Please fill out all fields before proceeding.", Toast.LENGTH_SHORT);
                    ViewGroup group = (ViewGroup) toast.getView();
                    TextView toastTV = (TextView) group.getChildAt(0);
                    toastTV.setTextSize(20);
                    toast.show();
                }
            }
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
        if (    viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_1 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_3 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_4 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_5 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_6 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_7 ||
//                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_8 ||
//                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_9 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_10 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_11 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_12 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_13 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_14 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_15 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_16 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_17 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_18 ||
                viewNumber == ActivitiesModel.INSTRUCTIONS_SCREEN_19 ||
                viewNumber == ActivitiesModel.VERBAL_LEARNING_SCREEN_1 ||
                viewNumber == ActivitiesModel.VERBAL_LEARNING_SCREEN_2 ||
                viewNumber == ActivitiesModel.VERBAL_LEARNING_SCREEN_3 ||
//                viewNumber == ActivitiesModel.VERBAL_LEARNING_SCREEN_4 ||
                viewNumber == ActivitiesModel.VIDEO_SCREEN ||
                viewNumber == ActivitiesModel.FIGURE_STUDY_SCREEN ||
                viewNumber == ActivitiesModel.SEMANTIC_RELATEDNESS_SCREEN) {
            nextButton.setVisibility(View.INVISIBLE);
        }
        else if (nextButton.getVisibility() == View.INVISIBLE) {
            nextButton.setVisibility(View.VISIBLE);
        }
    }

    private void startBlinkingArrows() {
        startTime = System.currentTimeMillis();
        timerHandler.postDelayed(timerRunnable, 1000);
    }

    private void stopBlinkingArrows() {
        timerHandler.removeCallbacks(timerRunnable);
        blinkingArrow.setVisibility(View.INVISIBLE);
        blinkingArrow.setImageResource(R.drawable.arrow_color);
    }

    private void timerRunnableOnTick() {
        long millis = System.currentTimeMillis() - startTime;
        int seconds = (int) (millis / 1000);
        if (nextButton.getVisibility() == View.VISIBLE) {
            if (seconds > BLINKING_START_SECS) {
                currentArrowDrawable = currentArrowDrawable == R.drawable.arrow_color ? R.drawable.arrow_blank : R.drawable.arrow_color;
                blinkingArrow.setImageResource(currentArrowDrawable);
                blinkingArrow.setVisibility(View.VISIBLE);
            }
            timerHandler.postDelayed(timerRunnable, 1000);
        }
    }

    public void nextButtonReady() {
        nextButton.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
//        nextText.setTextColor(getResources().getColor(R.color.colorAccent));
        isNextButtonReady = true;
        startBlinkingArrows();
    }

    public void showRetryDialog() {
        RetryDialog dialog = new RetryDialog();
        dialog.show(getSupportFragmentManager(), "RetryDialog");
    }

    public void showRecallFinishDialog() {
        RecallFinishDialog dialog = new RecallFinishDialog();
        dialog.show(getSupportFragmentManager(), "RecallFinishDialog");
    }

    public void hideSoftKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (this.getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
            inputManager.hideSoftInputFromInputMethod(this.getCurrentFocus().getWindowToken(), 0);
        }
    }

    private void setUpTextToSpeech() {
        textToSpeech = new TextToSpeech(this.getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(Locale.US);
                    textToSpeech.setSpeechRate(0.7f);
                    isTTSInitialized = true;
                    // wait a little for the initialization to complete
                    Handler h = new Handler();
                    h.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // run your code here
                            useTextToSpeech("Ready");
                        }
                    }, 400);
                    setInitialHomeFragment();
                } else if (status == TextToSpeech.ERROR) {
                    Log.e(TAG, "TTS Initialization Failed!");
                    isTTSInitialized = false;
                }
            }
        });
    }

    public void useTextToSpeech(String text) {
        Log.d(TAG, "useTextToSpeech: " + text);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && isTTSInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
        } else if (isTTSInitialized) {
            textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    public void pauseTextToSpeech() {
        if (textToSpeech.isSpeaking()) {
            textToSpeech.stop();
        }
    }

    @Override
    public void onRetryDialogPositiveClick(DialogFragment dialog) {
        VerbalRecallFragment fragment = (VerbalRecallFragment) fragmentManager.findFragmentByTag(fragmentTag);
        fragment.executePostMessageSetup();
    }

    @Override
    public void onFinishDialogPositiveClick(DialogFragment dialog) {
        getFragmentData(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ArrayList<String> speechText = null;

        if (resultCode == RESULT_OK && data != null) {
            speechText = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
        }
        if (speechText != null) {
            switch (viewNumber) {
                case ActivitiesModel.VERBAL_RECALL_SCREEN_1:
                case ActivitiesModel.VERBAL_RECALL_SCREEN_2:
                case ActivitiesModel.VERBAL_RECALL_SCREEN_3:
//                case ActivitiesModel.VERBAL_RECALL_SCREEN_4:
//                case ActivitiesModel.VERBAL_RECALL_SCREEN_5:
                case ActivitiesModel.VERBAL_RECALL_SCREEN_6:
                    VerbalRecallFragment verbalRecallFragment = (VerbalRecallFragment) fragmentManager.findFragmentByTag(fragmentTag);
                    verbalRecallFragment.setResponseTextToSpeechText(speechText.get(0));
                    break;
                case ActivitiesModel.KEYBOARD_SCREEN:
                    KeyboardFragment keyboardFragment = (KeyboardFragment) fragmentManager.findFragmentByTag(fragmentTag);
                    keyboardFragment.setResponseTextToSpeechText(speechText.get(0));
                    break;
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        // Start logging data to log file now that permission has been granted
        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE)
            LogcatExportService.log(this, new File(GenerateDirectory.getRootFile(this), "logcat_" + System.currentTimeMillis() + ".txt"));

        // Calls getFragmentData again to attempt to move to the next page if all required permissions are granted
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            getFragmentData(null);
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

    private void debugScreenSelect(int itemID) {
        fragmentTransaction = fragmentManager.beginTransaction();

        switch (itemID) {
            case R.id.screen_home_om:
                this.viewNumber = ActivitiesModel.HOME_SCREEN;
                fragmentTag = "HomeFragment";
                fragmentTransaction.replace(R.id.container, new HomeFragment(), "HomeFragment");
                break;
            case R.id.screen_inst_1_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_1;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_video_om:
                this.viewNumber = ActivitiesModel.VIDEO_SCREEN;
                fragmentTag = "VideoFragment";
                fragmentTransaction.replace(R.id.container, new VideoFragment(), "VideoFragment");
                break;
            case R.id.screen_keyboard_om:
                this.viewNumber = ActivitiesModel.KEYBOARD_SCREEN;
                fragmentTag = "KeyboardFragment";
                fragmentTransaction.replace(R.id.container, new KeyboardFragment(), "KeyboardFragment");
                break;
            case R.id.screen_inst_2_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_2;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_education_om:
                this.viewNumber = ActivitiesModel.EDUCATION_SCREEN;
                fragmentTag = "EducationFragment";
                fragmentTransaction.replace(R.id.container, new EducationFragment(), "EducationFragment");
                break;
            case R.id.screen_date_om:
                this.viewNumber = ActivitiesModel.TODAYS_DATE_SCREEN;
                fragmentTag = "TodayDateFragment";
                fragmentTransaction.replace(R.id.container, new TodayDateFragment(), "TodayDateFragment");
                break;
            case R.id.screen_day_om:
                this.viewNumber = ActivitiesModel.DAY_OF_WEEK_SCREEN;
                fragmentTag = "DayOfWeekFragment";
                fragmentTransaction.replace(R.id.container, new DayOfWeekFragment(), "DayOfWeekFragment");
                break;
            case R.id.screen_season_om:
                this.viewNumber = ActivitiesModel.SEASON_SCREEN;
                fragmentTag = "SeasonFragment";
                fragmentTransaction.replace(R.id.container, new SeasonFragment(), "SeasonFragment");
                break;
            case R.id.screen_inst_3_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_3;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_reaction_om:
                this.viewNumber = ActivitiesModel.REACTION_SCREEN;
                fragmentTag = "ReactionFragment";
                fragmentTransaction.replace(R.id.container, new ReactionFragment(), "ReactionFragment");
                break;
            case R.id.screen_inst_4_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_4;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_img_nm_om:
                this.viewNumber = ActivitiesModel.IMAGE_NAME_SCREEN;
                fragmentTag = "ImageNameFragment";
                fragmentTransaction.replace(R.id.container, new ImageNameFragment(), "ImageNameFragment");
                break;
            case R.id.screen_inst_5_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_5;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_1_om:
                this.viewNumber = ActivitiesModel.VERBAL_LEARNING_SCREEN_1;
                fragmentTag = "VerbalRecallFragment";
                fragmentTransaction.replace(R.id.container, new VerbalLearningFragment(), "VerbalRecallFragment");
                break;
            case R.id.screen_recall_1_om:
                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_1;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
                break;
            case R.id.screen_inst_6_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_6;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_2_om:
                this.viewNumber = ActivitiesModel.VERBAL_LEARNING_SCREEN_2;
                fragmentTag = "VerbalRecallFragment";
                fragmentTransaction.replace(R.id.container, new VerbalLearningFragment(), "VerbalRecallFragment");
                break;
            case R.id.screen_recall_2_om:
                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_2;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
                break;
            case R.id.screen_inst_7_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_7;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_3_om:
                this.viewNumber = ActivitiesModel.VERBAL_LEARNING_SCREEN_3;
                fragmentTag = "VerbalRecallFragment";
                fragmentTransaction.replace(R.id.container, new VerbalLearningFragment(), "VerbalRecallFragment");
                break;
            case R.id.screen_recall_3_om:
                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_3;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
                break;
//            case R.id.screen_inst_8_om:
//                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_8;
//                fragmentTag = "InstructionsFragment";
//                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
//                break;
//            case R.id.screen_verbal_4_om:
//                this.viewNumber = ActivitiesModel.VERBAL_LEARNING_SCREEN_4;
//                fragmentTag = "VerbalRecallFragment";
//                fragmentTransaction.replace(R.id.container, new VerbalLearningFragment(), "VerbalRecallFragment");
//                break;
//            case R.id.screen_recall_4_om:
//                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_4;
//                fragmentTag = "RecallResponseFragment";
//                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
//                break;
//            case R.id.screen_inst_9_om:
//                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_9;
//                fragmentTag = "InstructionsFragment";
//                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
//                break;
//            case R.id.screen_recall_5_om:
//                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_5;
//                fragmentTag = "RecallResponseFragment";
//                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
//                break;
            case R.id.screen_inst_10_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_10;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_fig_study_om:
                this.viewNumber = ActivitiesModel.FIGURE_STUDY_SCREEN;
                fragmentTag = "FigureStudyFragment";
                fragmentTransaction.replace(R.id.container, new FigureStudyFragment(), "FigureStudyFragment");
                break;
            case R.id.screen_fig_select_om:
                this.viewNumber = ActivitiesModel.FIGURE_SELECT_SCREEN;
                fragmentTag = "FigureSelectFragment";
                fragmentTransaction.replace(R.id.container, new FigureSelectFragment(), "FigureSelectFragment");
                break;
            case R.id.screen_inst_11_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_11;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_digit_span_om:
                this.viewNumber = ActivitiesModel.DIGIT_SPAN_SCREEN;
                fragmentTag = "DigitSpanFragment";
                fragmentTransaction.replace(R.id.container, new DigitSpanFragment(), "DigitSpanFragment");
                break;
            case R.id.screen_inst_12_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_12;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_read_comp_story_om:
                this.viewNumber = ActivitiesModel.READ_COMP_STORY_SCREEN;
                fragmentTag = "ReadingCompFragment";
                fragmentTransaction.replace(R.id.container, new StoryLearningFragment(), "ReadingCompFragment");
                break;
            case R.id.screen_inst_13_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_13;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_computation_om:
                this.viewNumber = ActivitiesModel.COMPUTATION_SCREEN;
                fragmentTag = "ComputationFragment";
                fragmentTransaction.replace(R.id.container, new ComputationFragment(), "ComputationFragment");
                break;
            case R.id.screen_inst_14_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_14;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_recall_6_om:
                this.viewNumber = ActivitiesModel.VERBAL_RECALL_SCREEN_6;
                fragmentTag = "RecallResponseFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecallFragment(), "RecallResponseFragment");
                break;
            case R.id.screen_inst_15_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_15;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_verbal_rec_om:
                this.viewNumber = ActivitiesModel.VERBAL_RECOGNITION_SCREEN;
                fragmentTag = "VerbalRecognitionFragment";
                fragmentTransaction.replace(R.id.container, new VerbalRecognitionFragment(), "VerbalRecognitionFragment");
                break;
            case R.id.screen_inst_16_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_16;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_semantic_choice_om:
                this.viewNumber = ActivitiesModel.SEMANTIC_CHOICE_SCREEN;
                fragmentTag = "SemanticChoiceFragment";
                fragmentTransaction.replace(R.id.container, new SemanticChoiceFragment(), "SemanticChoiceFragment");
                break;
            case R.id.screen_inst_17_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_17;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_fig_select_2_om:
                this.viewNumber = ActivitiesModel.FIGURE_SELECT_SCREEN_2;
                fragmentTag = "FigureSelectFragment";
                fragmentTransaction.replace(R.id.container, new FigureSelectFragment(), "FigureSelectFragment");
                break;
            case R.id.screen_inst_18_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_18;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_read_comp_test_om:
                this.viewNumber = ActivitiesModel.READ_COMP_TEST_SCREEN;
                fragmentTag = "ReadCompTestFragment";
                fragmentTransaction.replace(R.id.container, new StoryMemoryFragment(), "ReadCompTestFragment");
                break;
            case R.id.screen_inst_19_om:
                this.viewNumber = ActivitiesModel.INSTRUCTIONS_SCREEN_19;
                fragmentTag = "InstructionsFragment";
                fragmentTransaction.replace(R.id.container, new InstructionsFragment(), "InstructionsFragment");
                break;
            case R.id.screen_semantic_relatedness_om:
                this.viewNumber = ActivitiesModel.SEMANTIC_RELATEDNESS_SCREEN;
                fragmentTag = "SemanticRelatedness";
                fragmentTransaction.replace(R.id.container, new SemanticRelatednessFragment(), "SemanticRelatedness");
                break;
            case R.id.screen_finish_om:
                this.viewNumber = ActivitiesModel.FINISH_SCREEN;
                fragmentTag = "FinishFragment";
                fragmentTransaction.replace(R.id.container, new FinishFragment(), "FinishFragment");
                break;
        }
        appProgress.setProgress(viewNumber);
        viewButtonVisibility();
        fragmentTransaction.commit();
    }

    @Override
    protected void onDestroy() {
        if(textToSpeech != null){
            textToSpeech.stop();
            textToSpeech.shutdown();
            isTTSInitialized = false;
        }
        super.onDestroy();
    }
}
