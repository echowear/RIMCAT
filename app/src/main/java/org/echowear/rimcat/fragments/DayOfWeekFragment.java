package org.echowear.rimcat.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import org.echowear.rimcat.MainActivity;
import com.example.echowear.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DayOfWeekFragment extends QuestionFragment {

    private static final String TAG = "DayOfWeekFragment";
    private Spinner dayOfWeekSpinner;
    private ArrayAdapter<CharSequence> adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day_of_week, container, false);
        dayOfWeekSpinner = view.findViewById(R.id.day_of_week_spinner);
        adapter = ArrayAdapter.createFromResource(getActivity(), R.array.day_of_week_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        dayOfWeekSpinner.setAdapter(adapter);

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        if (dayOfWeekSpinner.getSelectedItem().toString().equals(""))
            return false;
        logEndTimeAndData(getActivity().getApplicationContext(), "day_of_week," + dayOfWeekSpinner.getSelectedItem().toString());
        return true;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new SeasonFragment(), "SeasonFragment");
    }

    @Override
    public String getCorrectAnswer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE");
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
