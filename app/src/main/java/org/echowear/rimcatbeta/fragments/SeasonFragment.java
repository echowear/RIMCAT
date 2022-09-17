package org.echowear.rimcatbeta.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.echowear.rimcatbeta.MainActivity;
import org.echowear.rimcatbeta.R;

import java.util.Calendar;
import java.util.Date;

public class SeasonFragment extends BasicQuestionFragment {

    private Spinner seasonSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_season, container, false);

        seasonSpinner = view.findViewById(R.id.season_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireActivity(), R.array.season_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        seasonSpinner.setAdapter(adapter);

        seasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((MainActivity) requireActivity()).viewOnTouch();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                ((MainActivity) requireActivity()).viewOnTouch();
            }
        });

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (!isQuestionAnswered())
            return false;
        logEndTimeAndData(requireActivity().getApplicationContext(), "season," + seasonSpinner.getSelectedItem().toString());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity) requireActivity()).addFragment(new InstructionsFragment(), "InstructionsFragment");
    }

    @Override
    public String getCorrectAnswer() {
        java.util.Date date= new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int month = cal.get(Calendar.MONTH);
        if (month == 12 || month < 3) {
            return "Winter";
        } else if (month < 6) {
            return "Spring";
        } else if (month < 9) {
            return "Summer";
        }
        return "Fall";
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }

    @Override
    public boolean isQuestionAnswered() {
        return !seasonSpinner.getSelectedItem().toString().equals("");
    }
}
