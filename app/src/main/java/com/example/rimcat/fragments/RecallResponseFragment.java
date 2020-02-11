package com.example.rimcat.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.rimcat.DataLogModel;
import com.example.rimcat.MainActivity;
import com.example.rimcat.R;

import java.util.ArrayList;

public class RecallResponseFragment extends QuestionFragment {

    private ListView                listView;
    private ArrayList<String>       listItems = new ArrayList<>();
    private ArrayAdapter<String>    adapter;
    private EditText                responseText;
    private Button                  addBtn;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recall_response, container, false);

        // Set up list view
        listView = view.findViewById(R.id.vresponse_list);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.list_white_text, R.id.list_content, listItems);
        listView.setAdapter(adapter);

        // Initialize other views
        responseText = view.findViewById(R.id.vresponse_recall_word);
        addBtn = view.findViewById(R.id.vresponse_addBtn);

        // Set add button on click
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!responseText.getText().toString().equals("")) {
                    listItems.add(responseText.getText().toString());
                    adapter.notifyDataSetChanged();
                    responseText.setText("");
                }
            }
        });

        cardView = view.findViewById(R.id.vresponse_layout);
        startAnimation(true);
        logStartTime();

        return view;
    }

    @Override
    public boolean loadDataModel() {
        for (int i = 0; i < listItems.size(); i++) {
            logEndTimeAndData(getActivity().getApplicationContext(), "word_recall," + listItems.get(i));
        }

        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }
}
