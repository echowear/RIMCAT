package com.example.rimcat;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.Gravity;
import android.widget.TextView;

public class RetryDialog extends DialogFragment {
    private static final String TAG =  "RetryDialog";
    private static final int    PADDING = 15;

    public interface RetryDialogListener {
        void onRetryDialogPositiveClick(DialogFragment dialog);
    }

    RetryDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (RetryDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView myMsg = new TextView(getContext());
        myMsg.setText(R.string.vresponse_message1);
        myMsg.setTextSize(25);
        myMsg.setPadding(PADDING,PADDING,PADDING,PADDING);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(myMsg)
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.d(TAG, "onClick: Positive button clicked");
                        listener.onRetryDialogPositiveClick(RetryDialog.this);
                        RetryDialog.this.getDialog().cancel();
                    }
                });
        return builder.create();
    }
}

/** In case you need to do any button modifications...
 *
 * dialog.setOnShowListener( new OnShowListener() {
 *     @Override
 *     public void onShow(DialogInterface arg0) {
 *         dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(COLOR_I_WANT);
 *     }
 * });
 */
