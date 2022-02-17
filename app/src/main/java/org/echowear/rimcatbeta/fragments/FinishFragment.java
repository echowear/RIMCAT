package org.echowear.rimcatbeta.fragments;

import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import org.echowear.rimcatbeta.R;
import java.util.Objects;

public class FinishFragment extends QuestionFragment {

    private static final String TAG = "FinishFragment";
    private Animation.AnimationListener animationListener;
    private CardView feedbackCard, endCard;
    private EditText feedbackText;
    private Button quitBtn, nextBtn;
    private boolean isFadingOut = true;
    private boolean isAnimationActive;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        mediaPlayer = MediaPlayer.create(getActivity().getApplicationContext(), R.raw.instfinish);

        endCard = view.findViewById(R.id.end_card);
        cardView = view.findViewById(R.id.finish_page);
        quitBtn = view.findViewById(R.id.quit_btn);
        endCard.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);

        quitBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View v) {
                if (!isAnimationActive)
                    Objects.requireNonNull(getActivity()).finishAndRemoveTask();
            }
        });

        cardView = view.findViewById(R.id.finish_page);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() { return false; }

    @Override
    public void moveToNextPage() { }

    @Override
    public String getCorrectAnswer() {
        return null;
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
