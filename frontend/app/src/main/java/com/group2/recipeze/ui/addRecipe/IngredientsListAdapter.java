package com.group2.recipeze.ui.addRecipe;


import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.group2.recipeze.R;

import java.util.ArrayList;

public class IngredientsListAdapter extends RecyclerView.Adapter<IngredientsListAdapter.ViewHolder> {

    private ArrayList<String> ingredients;
    private ArrayList<String> ingredientsAmounts;

    public static class IngredientDialog extends AlertDialog {
        AlertDialog dialogBuilder;

        public IngredientDialog(View itemView, ViewHolder viewHolder) {
            super(itemView.getContext());
            dialogBuilder = new AlertDialog.Builder(itemView.getContext()).create();
            LayoutInflater inflater = this.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.dialog_ingredient, null);

            EditText amount = dialogView.findViewById(R.id.amountEdit);
            EditText ingredient = dialogView.findViewById(R.id.ingredientEdit);
            Button button1 = dialogView.findViewById(R.id.buttonSubmit);
            Button button2 = dialogView.findViewById(R.id.buttonCancel);

            ArrayList<String> alreadySetValues = viewHolder.getSeperateAmountAndIngredientStrings();
            amount.setText(alreadySetValues.get(0));
            ingredient.setText(alreadySetValues.get(1));

            boolean newItem = false;
            if (alreadySetValues.get(0).length() == 0) {
                newItem = true;
            }

            boolean finalNewItem = newItem;
            button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialogBuilder.dismiss();
                    if (finalNewItem) {
                        viewHolder.cross.performClick();
                    }
                }
            });
            button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    viewHolder.updateIngredient(amount.getText().toString(), ingredient.getText().toString());
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
        private final TextView ingredient;
        private final TextView cross;
        private int position;
        private IngredientsListAdapter parent;

        public ViewHolder(View view, IngredientsListAdapter parent) {
            super(view);
            this.parent = parent;
            ViewHolder thisViewHolder = this;
            ingredient = view.findViewById(R.id.ingredient);
            cross = view.findViewById(R.id.cross);

            cross.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    parent.removeItem(position);
                }
            });

            ingredient.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IngredientDialog dialog = new IngredientDialog(view, thisViewHolder);
                    dialog.show();
                }
            });

            ingredient.performClick();
        }

        public TextView getIngredient() {
            return ingredient;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        public void updateIngredient(String amount, String ingredient) {
            parent.updateItem(position, amount, ingredient);
        }

        public ArrayList<String> getSeperateAmountAndIngredientStrings() {
            return parent.getItem(position);
        }

    }

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param ingredients String[] containing the data to populate views to be used
     * by RecyclerView.
     */
    public IngredientsListAdapter(ArrayList<String> ingredients, ArrayList<String> ingredientsAmounts) {
        this.ingredients = ingredients;
        this.ingredientsAmounts = ingredientsAmounts;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_ingredient, viewGroup, false);

        return new ViewHolder(view, this);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.setPosition(position);
        viewHolder.getIngredient().setText(this.ingredientsAmounts.get(position) + " " + this.ingredients.get(position));
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return ingredients.size();
    }

    public void addItem(String amount, String ingredient) {
        ingredients.add(ingredient);
        ingredientsAmounts.add(amount);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        ingredients.remove(position);
        ingredientsAmounts.remove(position);
        notifyDataSetChanged();
    }

    public void updateItem(int position, String amount, String ingredient) {
        ingredients.remove(position);
        ingredients.add(position, ingredient);
        ingredientsAmounts.remove(amount);
        ingredientsAmounts.add(position, amount);
        notifyDataSetChanged();
    }

    public ArrayList<String> getItem(int position) {
        ArrayList<String> result = new ArrayList<>();
        result.add(ingredientsAmounts.get(position));
        result.add(ingredients.get(position));
        return result;
    }
}

