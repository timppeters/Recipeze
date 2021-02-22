package com.group2.recipeze;

import android.content.Context;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

public class endlessScroll {
    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;

    public endlessScroll(RecyclerView recycler) {
        recyclerView = recycler;
    }

    public void populateData() {
        int i = 0;
        while (i < 10) {
            rowsArrayList.add("Recipe " + i);
            i++;
        }
    }

    public void initAdapter() {

        recyclerViewAdapter = new RecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    public void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();

                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == rowsArrayList.size() - 1) {
                        //bottom of list!
                        loadMore();
                        isLoading = true;
                    }
                }
            }
        });
    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemInserted(rowsArrayList.size() - 1);


        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                int currentSize = scrollPosition;
                int nextLimit = currentSize + 10;

                while (currentSize - 1 < nextLimit) {
                    rowsArrayList.add("Item " + currentSize);
                    currentSize++;
                }

                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);


    }
}
