package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;
import org.echowear.rimcatbeta.data_log.CorrectAnswerDictionary;

public class ImageNameFragment extends QuestionFragment {

    private static final String TAG = "ImageNameFragment";
    private static final int NUM_CHOICES = 3;
    private static final int[] IMAGE_NAMES = new int[] {
            R.drawable.tree,
            R.drawable.bicycle,
            R.drawable.house,
            R.drawable.butterfly,
            R.drawable.giraffe,
            R.drawable.kayak,
            R.drawable.pear,
            R.drawable.hippo,
            R.drawable.watermelon,
            R.drawable.daisy,
            R.drawable.grapefruit,
            R.drawable.accordion
    };
    private String[] buttonOptions;
    private int stringArrCount = 0, imageCount = 0;
    private boolean isFadingOut = true;
    private ConstraintLayout imageNamePage;
    private ImageView imageView;
    private Button btn1, btn2, btn3;
    private View.OnClickListener recordImageChoice;
    private Animation.AnimationListener animationListener;
    private boolean isAnimationActive;

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
                else {
                    isAnimationActive = false;
                    logStartTime();
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
                if (!isAnimationActive) {
                    isAnimationActive = true;
                    Button selectedButton = (Button) view;
                    // This is where the answer will be recorded
                    Log.d(TAG, "onClick: Button value: " + selectedButton.getText().toString());
                    logEndTimeAndData(getActivity().getApplicationContext(), "image_name_" + (imageCount + 1) + "," + selectedButton.getText().toString());

                    imageCount++;
                    if (imageCount == IMAGE_NAMES.length) {
                        ((MainActivity)getActivity()).getFragmentData(null);
                    }
                    else {
                        Animation fadeOutAnimation =  AnimationUtils.loadAnimation(getActivity(), R.anim.fade_out);
                        fadeOutAnimation.setAnimationListener(animationListener);
                        imageNamePage.startAnimation(fadeOutAnimation);
                    }
                }
            }
        };

        btn1.setOnClickListener(recordImageChoice);
        btn2.setOnClickListener(recordImageChoice);
        btn3.setOnClickListener(recordImageChoice);

        btn1.setText(buttonOptions[0]);
        btn2.setText(buttonOptions[1]);
        btn3.setText(buttonOptions[2]);

        cardView = view.findViewById(R.id.image_name_page);

        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }


    @Override
    public boolean loadDataModel() { return true; }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        return CorrectAnswerDictionary.IMAGE_NAME_ANSWERS.get(imageCount);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
