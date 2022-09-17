package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Random;

public class ReactionFragment extends QuestionFragment {

    private static final String TAG = "ImageNameFragment";
    private static final DecimalFormat df = new DecimalFormat("0.00");
    private static final int NUM_ITERATIONS = 10;
    private static final double RAN_LOWER_BOUND = 1.0;
    private static final double RAN_UPPER_BOUND = 4.0;
    private ConstraintLayout layout1, layout2;
    private TableLayout reactionGrid;
    private CountDownTimer readyCountdown;
    private TextView reactionCountdownText;
    private static final String[] COUNTDOWN_TEXT = { "Ready", "Set", "Go!" };
    private Button[] selectButtons;
    private long reactionStart;
    private int count, timerIndex = 0;
    private double result;
    boolean inIteration;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reaction, container, false);
        cardView = view.findViewById(R.id.reaction_page);
        layout1 = view.findViewById(R.id.reaction_layout1);
        layout2 = view.findViewById(R.id.reaction_layout2);
        layout1.setVisibility(View.VISIBLE);
        layout2.setVisibility(View.INVISIBLE);
        MainActivity mainActivity = new MainActivity();

        Button readyBtn = view.findViewById(R.id.reaction_ready_btn);
        readyBtn.setOnClickListener(v -> {
            layout1.setVisibility(View.INVISIBLE);
            layout2.setVisibility(View.VISIBLE);
            readyCountdown.start();
        });

        selectButtons = new Button[] {
                view.findViewById(R.id.rb1), view.findViewById(R.id.rb2), view.findViewById(R.id.rb3),
                view.findViewById(R.id.rb4), view.findViewById(R.id.rb5), view.findViewById(R.id.rb6),
                view.findViewById(R.id.rb7), view.findViewById(R.id.rb8), view.findViewById(R.id.rb9),
                view.findViewById(R.id.rb10), view.findViewById(R.id.rb11), view.findViewById(R.id.rb12)
        };

        // Set up prompt and add bold text to it
        TextView reactionPrompt = view.findViewById(R.id.reaction_prompt);
        String promptString = getResources().getString(R.string.reaction_prompt);
        int boldLength = "as quickly as you can.".length();
        ForegroundColorSpan fcs = new ForegroundColorSpan(getResources().getColor(R.color.red));
        SpannableString promptSS = new SpannableString(promptString);
        promptSS.setSpan(fcs, promptString.length() - boldLength, promptString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        reactionPrompt.setText(promptSS);

        reactionCountdownText = view.findViewById(R.id.reaction_countdown);
        readyCountdown = new CountDownTimer(3000, 980) {
            @Override
            public void onTick(long millisUntilFinished) {
                Log.d(TAG, "onTick: Tick: " + timerIndex);
//                if (timerIndex > 0)
                if (timerIndex < 3) {
//                    reactionCountdownText.setText("" + timerIndex);
                    reactionCountdownText.setText(COUNTDOWN_TEXT[timerIndex]);
                    timerIndex++;
                } else {
                    Log.d(TAG, "onTick: ready-set-go went out of range");
                }
            }

            @Override
            public void onFinish() {
                timerIndex = 0;
                reactionCountdownText.setVisibility(View.INVISIBLE);
                reactionGrid.setVisibility(View.VISIBLE);
                startNewIteration();
            }
        };

        reactionGrid = view.findViewById(R.id.reaction_grid);
        reactionGrid.setVisibility(View.INVISIBLE);
        initializeGrid();
        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }


    private void initializeGrid() {
        timerIndex = 0;
        final MainActivity mainActivity = new MainActivity();
        View.OnClickListener reactionListener = v -> {
            if (inIteration) {
                AppCompatButton b = (AppCompatButton) v;
                if (b.getVisibility() == View.VISIBLE) {
                    b.setVisibility(View.INVISIBLE);
                    endIteration();
                }
            }
        };
        View.OnTouchListener touchListener = (v, event) -> {
            mainActivity.callTouchEventInButton(event.getRawX(), event.getRawY(), 9);
            return false;
        };
        for (Button btn : selectButtons) {
            btn.setOnClickListener(reactionListener);
            btn.setOnTouchListener(touchListener);
            btn.setVisibility(View.INVISIBLE);
        }
    }

    private void startNewIteration() {
        timerIndex = 0;
        inIteration = true;
        Random random = new Random();
        int buttonToPressIndex = random.nextInt(selectButtons.length);
        selectButtons[buttonToPressIndex].setVisibility(View.VISIBLE);
        Calendar calendar = Calendar.getInstance();
        reactionStart = calendar.getTimeInMillis();
        logStartTime();
    }

    private void endIteration() {
        inIteration = false;
        Calendar calendar = Calendar.getInstance();
        long reactionEnd = calendar.getTimeInMillis();
        if (reactionStart < reactionEnd) {
            result = (reactionEnd - reactionStart) / 1000.0;
            logEndTimeAndData(getActivity(), "reaction_" + (count + 1) + "," + df.format(result));
        } else {
            Log.d(TAG, "endIteration: Error calculating time. Could not log data");
        }
        count++;
        if (count < NUM_ITERATIONS) {
            reactionGrid.setVisibility(View.INVISIBLE);
//            reactionCountdownText.setVisibility(View.VISIBLE);
            startRandomCountdown();
        } else {
            ((MainActivity) requireActivity()).getFragmentData(null);
        }
    }

    private void startRandomCountdown() {
        Random random = new Random();
        double timeInterval = RAN_LOWER_BOUND + (RAN_UPPER_BOUND - RAN_LOWER_BOUND) * random.nextDouble()*1000;
        CountDownTimer readyCountdown = new CountDownTimer((int) timeInterval, (int) timeInterval) {
            @Override
            public void onTick(long millisUntilFinished) {}

            @Override
            public void onFinish() {
                reactionGrid.setVisibility(View.VISIBLE);
                startNewIteration();
            }
        };
        readyCountdown.start();
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "" + df.format(result);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
