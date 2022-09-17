package org.echowear.rimcatbeta.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import org.echowear.rimcatbeta.R;

import java.util.Objects;

public class RetryDialog extends DialogFragment {
    private static final String TAG =  "RetryDialog";
    private static final float  TEXT_SIZE = 25f;
    private static final int    PADDING = 20;

    public interface RetryDialogListener {
        void onRetryDialogPositiveClick(DialogFragment dialog);
    }

    RetryDialogListener listener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (RetryDialogListener) context;
        } catch (ClassCastException e) {
            Log.e(TAG, "onAttach: ", e);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        TextView myMsg = new TextView(getContext());
        myMsg.setText(R.string.vresponse_message1);
        myMsg.setTextSize(25);
        myMsg.setPadding(PADDING,PADDING,PADDING,PADDING);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCustomTitle(myMsg)
                .setPositiveButton("Okay", (dialog, which) -> {
                    Log.d(TAG, "onClick: Positive button clicked");
                    listener.onRetryDialogPositiveClick(RetryDialog.this);
                    Objects.requireNonNull(RetryDialog.this.getDialog()).cancel();
                });
        final AlertDialog alert = builder.create();
        alert.setOnShowListener(dialog -> {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );

            params.setMargins(PADDING,0,0,0);
            Button btnPositive = alert.getButton(Dialog.BUTTON_POSITIVE);
            btnPositive.setTextSize(TEXT_SIZE);
            btnPositive.setTextColor(getResources().getColor(R.color.white));
            btnPositive.setBackground(getResources().getDrawable(R.drawable.roundbutton));
            btnPositive.setLayoutParams(params);
        });

        return alert;
    }
}

