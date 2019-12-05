package com.example.rimcat.fragments;

import android.support.v4.app.Fragment;

import com.example.rimcat.DataLogModel;

public abstract class QuestionFragment extends Fragment {

    public abstract boolean loadDataModel(DataLogModel dataLogModel);

    public abstract void startAnimation();
}
