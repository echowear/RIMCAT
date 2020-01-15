package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

public class ImageNameFragment extends QuestionFragment {

    private static final String TAG = "ImageNameFragment";
    private static final int NUM_CHOICES = 3;
    private static final int[] IMAGE_NAMES = new int[] {
            R.drawable.tree,
            R.drawable.bicycle
    };
    private String[] buttonOptions;
    private int stringArrCount = 0, imageCount = 0;
    private boolean isFadingOut = true;
    private ConstraintLayout imageNamePage;
    private ImageView imageView;
    private Button btn1, btn2, btn3;
    private View.OnClickListener recordImageChoice;
    private Animation.AnimationListener animationListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_name, container, false);

        buttonOptions = getResources().getStringArray(R.array.image_name_array);
        imageNamePage = view.findViewById(R.id.image_name_page);
        btn1 = view.findViewById(R.id.image_nm_btn_1);
        btn2 = view.findViewById(R.id.image_nm_btn_2);
        btn3 = view.findViewById(R.id.image_nm_btn_3);
        imageView = view.findViewById(R.id.image_name_image);

        animationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (isFadingOut) {
                    stringArrCount += NUM_CHOICES;
                    imageCount++;
                    if (imageCount < IMAGE_NAMES.length) {
                        imageView.setImageResource(IMAGE_NAMES[imageCount]);
                        btn1.setText(buttonOptions[stringArrCount]);
                        btn2.setText(buttonOptions[stringArrCount + 1]);
                        btn3.setText(buttonOptions[stringArrCount + 2]);

                        Animation fadeInAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.fade_in);
                        fadeInAnimation.setAnimationListener(animationListener);
                        imageNamePage.startAnimation(fadeInAnimation);
                    }
                }
                isFadingOut = !isFadingOut;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        recordImageChoice = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Button test = (Button) view;
                // This is where the answer will be recorded
                Log.d(TAG, "onClick: Button value: " + test.getText().toString());

                Animation fadeOutAnimation =  AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                fadeOutAnimation.setAnimationListener(animationListener);
                imageNamePage.startAnimation(fadeOutAnimation);
            }
        };

        btn1.setOnClickListener(recordImageChoice);
        btn2.setOnClickListener(recordImageChoice);
        btn3.setOnClickListener(recordImageChoice);

        btn1.setText(buttonOptions[0]);
        btn2.setText(buttonOptions[1]);
        btn3.setText(buttonOptions[2]);

        cardView = view.findViewById(R.id.card);

        startAnimation(true);

        return view;
    }


    @Override
    public boolean loadDataModel() {
        return false;
    }

    @Override
    public void moveToNextPage() {

    }
}
