package com.example.rimcat.data_log;

import android.util.Log;

import java.util.HashMap;

public class CorrectAnswerDictionary {
    private static final String TAG = "CorrectAnswerDictionary";
    public static final HashMap<Integer, String> IMAGE_NAME_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> FIGURE_SELECT_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> DIGIT_SPAN_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> COMPUTATION_ANSWERS = new HashMap<>();

    public static void loadAnswers() {
        Log.d(TAG, "loadAnswers: loading correct answer dictionaries...");
        loadImageNameAnswers();
        loadFigureSelectAnswers();
        loadDigitSpanAnswers();
        loadComputationAnswers();
    }

    private static void loadImageNameAnswers() {
        IMAGE_NAME_ANSWERS.put(0, "Tree");
        IMAGE_NAME_ANSWERS.put(1, "Bicycle");
        IMAGE_NAME_ANSWERS.put(2, "House");
        IMAGE_NAME_ANSWERS.put(3, "Butterfly");
        IMAGE_NAME_ANSWERS.put(4, "Giraffe");
        IMAGE_NAME_ANSWERS.put(5, "Kayak");
        IMAGE_NAME_ANSWERS.put(6, "Pear");
        IMAGE_NAME_ANSWERS.put(7, "Hippopotamus");
        IMAGE_NAME_ANSWERS.put(8, "Watermelon");
        IMAGE_NAME_ANSWERS.put(9, "Daisy");
        IMAGE_NAME_ANSWERS.put(10, "Grapefruit");
        IMAGE_NAME_ANSWERS.put(11, "Accordion");
    }

    private static void loadFigureSelectAnswers() {
        FIGURE_SELECT_ANSWERS.put(0, "Tree");
        FIGURE_SELECT_ANSWERS.put(1, "Bicycle");
        FIGURE_SELECT_ANSWERS.put(2, "House");
        FIGURE_SELECT_ANSWERS.put(3, "Butterfly");
        FIGURE_SELECT_ANSWERS.put(4, "Giraffe");
        FIGURE_SELECT_ANSWERS.put(5, "Kayak");
    }

    private static void loadDigitSpanAnswers() {
        DIGIT_SPAN_ANSWERS.put(0, "5238");
        DIGIT_SPAN_ANSWERS.put(1, "9713");
        DIGIT_SPAN_ANSWERS.put(2, "15739");
        DIGIT_SPAN_ANSWERS.put(3, "62847");
        DIGIT_SPAN_ANSWERS.put(4, "371205");
        DIGIT_SPAN_ANSWERS.put(5, "481392");
        DIGIT_SPAN_ANSWERS.put(6, "8431792");
        DIGIT_SPAN_ANSWERS.put(7, "2851064");
        DIGIT_SPAN_ANSWERS.put(8, "95");
        DIGIT_SPAN_ANSWERS.put(9, "53");
        DIGIT_SPAN_ANSWERS.put(10, "971");
        DIGIT_SPAN_ANSWERS.put(11, "524");
        DIGIT_SPAN_ANSWERS.put(12, "8192");
        DIGIT_SPAN_ANSWERS.put(13, "2806");
        DIGIT_SPAN_ANSWERS.put(14, "63419");
        DIGIT_SPAN_ANSWERS.put(15, "26851");
    }

    private static void loadComputationAnswers() {
        COMPUTATION_ANSWERS.put(0, "17");
        COMPUTATION_ANSWERS.put(1, "43");
        COMPUTATION_ANSWERS.put(2, "36");
        COMPUTATION_ANSWERS.put(3, "26");
        COMPUTATION_ANSWERS.put(4, "36");
        COMPUTATION_ANSWERS.put(5, "48");
        COMPUTATION_ANSWERS.put(6, "4");
        COMPUTATION_ANSWERS.put(7, "34");
    }
}
