package org.echowear.rimcatbeta.data_log;

import android.util.Log;

import java.util.HashMap;

public class CorrectAnswerDictionary {
    private static final String TAG = "CorrectAnswerDictionary";
    public static final HashMap<Integer, String> IMAGE_NAME_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> FIGURE_SELECT_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> DIGIT_SPAN_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> COMPUTATION_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> STORY_MEMORY_ANSWERS = new HashMap<>();
    public static final HashMap<Integer, String> SEMANTIC_RELATEDNESS_ANSWERS = new HashMap<>();
    public static final String[] SEMANTIC_CHOICE_ANSWERS = {
            "Lynx Hyena Buffalo Whale Frog Cobra",
            "Wolf Moose Chipmunk Turtle Otter Penguin",
            "Beaver Sloth Fox Ferret Lizard Eel",
            "Cranberry Tangerine Papaya Pineapple Cherry",
            "Plum Melon Raspberry Pear Apricot Lime",
            "Grape Pomegranate Lemon Nectarine Blackberry Strawberry",
            "Pants Belt Slippers Poncho Skirt Tie",
            "Hat Gown Blazer Gloves Robe Purse Jacket",
            "Scarf Shorts Bikini Stockings Cardigan Blouse"
    };
    public static final String[] TRIAL_LIST_ONE = new String[] {
            "Drum", "Curtain", "Bell", "Coffee", "School",
            "Parent", "Moon", "Garden", "Hat", "Farmer",
            "Nose", "Turkey"
    };
    public static final String[] TRIAL_LIST_TWO = new String[] {
            "Desk", "Ranger", "Bird", "Shoe", "Mountain", "Stove",
            "Glasses", "Towel", "Cloud", "Boat", "Lamb", "Gum"
    };

    public static void loadAnswers() {
        Log.d(TAG, "loadAnswers: loading correct answer dictionaries...");
        loadImageNameAnswers();
        loadFigureSelectAnswers();
        loadDigitSpanAnswers();
        loadComputationAnswers();
        loadSemanticRelatednessAnswer();
        loadStoryMemoryQuestions();
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
        FIGURE_SELECT_ANSWERS.put(0, "figure_a_1");
        FIGURE_SELECT_ANSWERS.put(1, "figure_b_1");
        FIGURE_SELECT_ANSWERS.put(2, "figure_c_1");
        FIGURE_SELECT_ANSWERS.put(3, "figure_d_1");
        FIGURE_SELECT_ANSWERS.put(4, "figure_e_1");
        FIGURE_SELECT_ANSWERS.put(5, "figure_f_1");
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
        DIGIT_SPAN_ANSWERS.put(8, "59");
        DIGIT_SPAN_ANSWERS.put(9, "35");
        DIGIT_SPAN_ANSWERS.put(10, "179");
        DIGIT_SPAN_ANSWERS.put(11, "425");
        DIGIT_SPAN_ANSWERS.put(12, "2918");
        DIGIT_SPAN_ANSWERS.put(13, "6082");
        DIGIT_SPAN_ANSWERS.put(14, "91436");
        DIGIT_SPAN_ANSWERS.put(15, "15862");
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

    private static void loadSemanticRelatednessAnswer() {
        SEMANTIC_RELATEDNESS_ANSWERS.put(0, "Garden");
        SEMANTIC_RELATEDNESS_ANSWERS.put(1, "Desert");
        SEMANTIC_RELATEDNESS_ANSWERS.put(2, "Money");
        SEMANTIC_RELATEDNESS_ANSWERS.put(3, "Rose");
        SEMANTIC_RELATEDNESS_ANSWERS.put(4, "Shine");
        SEMANTIC_RELATEDNESS_ANSWERS.put(5, "Hopelessness");
        SEMANTIC_RELATEDNESS_ANSWERS.put(6, "Silence");
        SEMANTIC_RELATEDNESS_ANSWERS.put(7, "Truth");
        SEMANTIC_RELATEDNESS_ANSWERS.put(8, "Flame");
        SEMANTIC_RELATEDNESS_ANSWERS.put(9, "Rattle");
        SEMANTIC_RELATEDNESS_ANSWERS.put(10, "Thunder");
        SEMANTIC_RELATEDNESS_ANSWERS.put(11, "Hunger");
        SEMANTIC_RELATEDNESS_ANSWERS.put(12, "Wisdom");
        SEMANTIC_RELATEDNESS_ANSWERS.put(13, "Sour");
        SEMANTIC_RELATEDNESS_ANSWERS.put(14, "Moon");
    }

    private static void loadStoryMemoryQuestions() {
        STORY_MEMORY_ANSWERS.put(0, "Monday morning");
        STORY_MEMORY_ANSWERS.put(1, "No");
        STORY_MEMORY_ANSWERS.put(2, "Sarah");
        STORY_MEMORY_ANSWERS.put(3, "Yes");
        STORY_MEMORY_ANSWERS.put(4, "At lunch time");
        STORY_MEMORY_ANSWERS.put(5, "No");
        STORY_MEMORY_ANSWERS.put(6, "She was distracted");
        STORY_MEMORY_ANSWERS.put(7, "Eggs");
        STORY_MEMORY_ANSWERS.put(8, "8");
        STORY_MEMORY_ANSWERS.put(9, "Blueberries");
    }
}
