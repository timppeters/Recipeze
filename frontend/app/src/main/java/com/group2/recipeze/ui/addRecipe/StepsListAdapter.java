package com.group2.recipeze.ui.addRecipe;

import android.app.AlertDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StepsListAdapter extends RecyclerView.Adapter<StepsListAdapter.ViewHolder> {

    private ArrayList<String> steps;

    public static class StepDialog extends AlertDialog {
        AlertDialog dialogBuilder;

        public StepDialog(View view, StepsListAdapter stepsListAdapter) {
            super(view.getContext());
            dialogBuilder = new AlertDialog.Builder(view.getContext()).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_step, null);

            EditText instruction = dialogView.findViewById(R.id.instructionEdit);
            Button button1 = dialogView.findViewById(R.id.buttonSubmit);
            Button button2 = dialogView.findViewById(R.id.buttonCancel);


            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stepsListAdapter.addItem(instruction.getText().toString());
                    dialogBuilder.dismiss();
                }
            });

            dialogBuilder.setView(dialogView);
        }

        public void show() {
            dialogBuilder.show();
        }
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView cross;
        private final TextView instruction;
        private int position;
        private StepsListAdapter parent;

        public ViewHolder(View view, StepsListAdapter parent) {
            super(view);
            this.parent = parent;
            instruction = view.findViewById(R.id.step);
            cross = view.findViewById(R.id.cross);

            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.removeItem(position);
                }
            });
        }

        public TextView getInstruction() {
            return this.instruction;
        }

        public void setPosition(int position) {
            this.position = position;
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param steps String[] containing the data to populate views to be used
     *                    by RecyclerView.
     */
    public StepsListAdapter(ArrayList<String> steps) {
        this.steps = steps;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public StepsListAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_step, viewGroup, false);


        return new StepsListAdapter.ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(StepsListAdapter.ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setIsRecyclable(false);
        viewHolder.setPosition(position);
        viewHolder.getInstruction().setText(String.valueOf(position+1) + ". " + this.steps.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return steps.size();
    }

    public void newStep(View view) {
        StepsListAdapter.StepDialog dialog = new StepsListAdapter.StepDialog(view, this);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void addItem(String instruction) {
        steps.add(instruction);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        steps.remove(position);
        notifyDataSetChanged();
    }

    public HashMap<String, String> getStepsForRecipeSave() {
        HashMap<String, String> result = new HashMap<>();
        for (int i = 0; i < steps.size(); i++) {
            result.put(String.valueOf(i+1), steps.get(i));
        }
        return result;
    }
}