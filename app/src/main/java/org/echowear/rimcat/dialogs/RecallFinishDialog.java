package org.echowear.rimcat.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.echowear.R;

public class RecallFinishDialog extends DialogFragment {
    private static final String TAG =  "RecallFinishDialog";
    private static final float  TEXT_SIZE = 20f;
    private static final int    PADDING = 20;

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

        final AlertDialog alert = builder.create();
        alert.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
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

                Button btnNegative = alert.getButton(Dialog.BUTTON_NEGATIVE);
                btnNegative.setTextSize(TEXT_SIZE);
                btnNegative.setTextColor(getResources().getColor(R.color.white));
                btnNegative.setBackground(getResources().getDrawable(R.drawable.roundbutton));
                btnNegative.getBackground().setTint(getResources().getColor(R.color.backgroundColor));
                btnNegative.setLayoutParams(params);
            }
        });

        return alert;
    }
}
