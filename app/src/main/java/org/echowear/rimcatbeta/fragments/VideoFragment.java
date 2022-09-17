package org.echowear.rimcatbeta.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

public class VideoFragment extends QuestionFragment {

    private static final String TAG = "VideoFragment";
    private VideoView videoView;
    int stopPosition;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = view.findViewById(R.id.videoView);
        String path = "android.resource://" + requireActivity().getPackageName() + "/" + R.raw.keyboard_entry_training;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnCompletionListener(mp -> {
            logEndTimeAndData(requireActivity().getApplicationContext(), "video,null");
            ((MainActivity) requireActivity()).getFragmentData(null);
        });
        videoView.setOnPreparedListener(mp -> mp.setScreenOnWhilePlaying(true));
        videoView.start();

        cardView = view.findViewById(R.id.main_page);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new KeyboardFragment(), "KeyboardFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return "N/A";
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public void onPause() {
        Log.d(TAG, "onPause called");
        super.onPause();
        stopPosition = videoView.getCurrentPosition(); //stopPosition is an int
        videoView.pause();
    }
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume called");
        videoView.seekTo(stopPosition);
        videoView.start(); //Or use resume() if it doesn't work. I'm not sure
    }
}