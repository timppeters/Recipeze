package com.group2.recipeze.ui.profile;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.google.android.material.snackbar.Snackbar;
import com.group2.recipeze.R;

public class tagApplicationDialog extends DialogFragment {
    AlertDialog dialogBuilder;
    View parentView;

    public tagApplicationDialog(View parentView){
        this.parentView = parentView;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_tag_application, null);
        builder.setView(dialogView);
        AlertDialog tagDialog = builder.create();

        Button addBtn = dialogView.findViewById(R.id.buttonSubmit);
        Button cancelBtn = dialogView.findViewById(R.id.buttonCancel);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(parentView, "Application received, our team will review your new tag.", Snackbar.LENGTH_LONG);
                snackbar.show();
                tagDialog.dismiss();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tagDialog.dismiss();
            }
        });

        return tagDialog;
    }
}
