package com.example.rimcat.fragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;
import java.io.IOException;

public class StoryLearningFragment extends QuestionFragment {

    private static final String TAG = "ReadingCompFragment";

    private static final int[] READING_COMP_SECTIONS = {
            R.string.reading_comp_prompt_1, R.string.reading_comp_prompt_2, R.string.reading_comp_prompt_3, R.string.reading_comp_prompt_4
    };
    private static final int[] READING_COMP_AUDIO = {
            R.raw.reading_comp_1, R.raw.reading_comp_2, R.raw.reading_comp_3, R.raw.reading_comp_4
    };
    private TextView    storyText;
    private int         reading_comp_sec;
    private MediaPlayer storyMedia;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_story_learning, container, false);

        // Initialize all views
        storyText = view.findViewById(R.id.ready_inst1);

        // Set up media player
        MediaPlayer.OnCompletionListener onCompletionListener = new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                reading_comp_sec++;
                if (storyMedia != null) {
                    storyMedia.reset();
                }
                Log.d(TAG, "onCompletion: Reading comp sec " + reading_comp_sec);
                if (reading_comp_sec < READING_COMP_SECTIONS.length) {
                    storyText.setText(READING_COMP_SECTIONS[reading_comp_sec]);
                    AssetFileDescriptor afd = getActivity().getResources().openRawResourceFd(READING_COMP_AUDIO[reading_comp_sec]);
                    try {
                        storyMedia.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                        storyMedia.setOnCompletionListener(this);
                        storyMedia.prepare();
                        storyMedia.start();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    if (storyMedia != null) {
                        storyMedia.release();
                        storyMedia = null;
                    }
                    logEndTimeAndData(getActivity().getApplicationContext(), "story_learning,null");
                    ((MainActivity)getActivity()).getFragmentData(null);
                }
            }
        };
        storyMedia = MediaPlayer.create(getActivity().getApplicationContext(), READING_COMP_AUDIO[reading_comp_sec]);
        storyMedia.setOnCompletionListener(onCompletionListener);
        storyMedia.start();
        cardView = view.findViewById(R.id.read_main_page);
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
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
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
    public void onDestroy() {
        if (storyMedia != null) {
            if (storyMedia.isPlaying() || storyMedia.isLooping()) {
                storyMedia.stop();
            }
            storyMedia.reset();
            storyMedia.release();
            storyMedia = null;
        }
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (storyMedia != null && storyMedia.isPlaying()) {
            storyMedia.pause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (storyMedia != null) {
            storyMedia.start();
        }
    }
}
