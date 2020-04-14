package com.example.rimcat.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.TextView;

import com.example.rimcat.R;

public class RecallFinishDialog extends DialogFragment {
    private static final String TAG =  "RecallFinishDialog";
    private static final int    PADDING = 15;

    public interface RecallFinishDialogListener {
        void onFinishDialogPositiveClick(DialogFragment dialog);
    }

    RecallFinishDialog.RecallFinishDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RecallFinishDialog.RecallFinishDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView myMsg = new TextView(getContext());
        myMsg.setText(R.string.vresponse_message2);
        myMsg.setTextSize(25);
        myMsg.setPadding(PADDING,PADDING,PADDING,PADDING);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(myMsg)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Positive button clicked");
                        listener.onFinishDialogPositiveClick(RecallFinishDialog.this);
                        RecallFinishDialog.this.getDialog().cancel();
                    }
                })
                .setNegativeButton("Keep Trying", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        RecallFinishDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}
