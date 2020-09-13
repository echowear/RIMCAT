package com.example.rimcat.fragments;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.VideoView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.Objects;

public class VideoFragment extends QuestionFragment {

    private static final String TAG = "VideoFragment";
    private VideoView videoView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video, container, false);

        videoView = view.findViewById(R.id.videoView);
        String path = "android.resource://" + Objects.requireNonNull(getActivity()).getPackageName() + "/" + R.raw.keyboard_entry_training;
        videoView.setVideoURI(Uri.parse(path));
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ((MainActivity)getActivity()).getFragmentData(null);
            }
        });
        videoView.start();

        cardView = view.findViewById(R.id.card);
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
        ((MainActivity)getActivity()).addFragment(new KeyboardFragment(), "KeyboardFragment");
    }
}