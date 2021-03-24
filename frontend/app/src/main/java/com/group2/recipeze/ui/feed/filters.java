package com.group2.recipeze.ui.feed;

import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.group2.recipeze.R;

import org.jetbrains.annotations.NotNull;

public class filters extends DialogFragment {

    @NotNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomAlertDialog);
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.fragment_filter_feed, null);
        //dialogView.setClipToOutline(true);
        //dialogView.setBackground(ContextCompat.getDrawable(getActivity(), R.drawable.dialog_bg));
        //dialogView.setBackground(new ColorDrawable(Color.TRANSPARENT));

        builder.setView(dialogView)
                // Add action buttons
//                .setPositiveButton("test1", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int id) {
//                        // sign in the user ...
//                    }
//                })
//                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                        LoginDialogFragment.this.getDialog().cancel();
//                    }
//                });
        ;
        // Create the AlertDialog object and return it
        return builder.create();
    }

}
