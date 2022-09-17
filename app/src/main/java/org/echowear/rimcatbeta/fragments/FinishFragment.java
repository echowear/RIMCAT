package org.echowear.rimcatbeta.fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;

import org.echowear.rimcatbeta.R;

public class FinishFragment extends QuestionFragment {

    private boolean isAnimationActive;

    public FinishFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_finish, container, false);
        mediaPlayer = MediaPlayer.create(requireActivity().getApplicationContext(), R.raw.instfinish);

        CardView endCard = view.findViewById(R.id.end_card);
        cardView = view.findViewById(R.id.finish_page);
        Button quitBtn = view.findViewById(R.id.quit_btn);
        endCard.setVisibility(View.VISIBLE);
        cardView.setVisibility(View.VISIBLE);

        quitBtn.setOnClickListener(v -> {
            if (!isAnimationActive)
                requireActivity().finishAndRemoveTask();
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
