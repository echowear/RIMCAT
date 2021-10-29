package org.echowear.rimcat.fragments;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import org.echowear.rimcat.MainActivity;
import com.example.echowear.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TodayDateFragment extends QuestionFragment {

    private static final String TAG = "TodayDateFragment";
    private static final String ACTIVITY_DATE_FORMAT = "MM/dd/yyyy";
    private static final int INPUT_LENGTH = 2;
    private EditText inputMonth, inputDay, inputYear;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today_date, container, false);
        inputMonth = view.findViewById(R.id.input_today_month);
        inputDay = view.findViewById(R.id.input_today_day);
        inputYear = view.findViewById(R.id.input_today_year);

        inputMonth.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(inputMonth.getText().toString().length() == INPUT_LENGTH)
                {
                    inputDay.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) { }
        });

        inputDay.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(inputDay.getText().toString().length() == INPUT_LENGTH)
                {
                    inputYear.requestFocus();
                }
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void afterTextChanged(Editable s) { }
        });

        cardView = view.findViewById(R.id.card);
        startAnimation(true);
        logStartTime();
        nextButtonReady();
        return view;
    }

    @Override
    public boolean loadDataModel() {
        String todayDateResult = "";
        if  (   (!inputDay.getText().toString().equals("") ||
                !inputMonth.getText().toString().equals("") ||
                !inputYear.getText().toString().equals(""))
                &&
                (inputDay.getText().toString().length() == 2 &&
                inputMonth.getText().toString().length() == 2 &&
                inputYear.getText().toString().length() == 4)
            )
            {
            todayDateResult =   Integer.parseInt(inputMonth.getText().toString()) + "/" +
                                Integer.parseInt(inputDay.getText().toString()) + "/" +
                                Integer.parseInt(inputYear.getText().toString());
            logEndTimeAndData(getActivity().getApplicationContext(), "todays_date," + todayDateResult);
            return true;
        }

        return false;
    }

    @Override
    public void moveToNextPage() {
        ((MainActivity)getActivity()).addFragment(new DayOfWeekFragment(), "DayOfWeekFragment");
    }

    @Override
    public String getCorrectAnswer() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(ACTIVITY_DATE_FORMAT);
        Date today = Calendar.getInstance().getTime();
        return dateFormat.format(today);
    }

    @Override
    public String getTriedMicrophone() {
        return "N/A";
    }
}
