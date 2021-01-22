/*
package com.example.catimages;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.SnapHelper;

import static com.example.catimages.MainActivity.adapter;
import static com.example.catimages.MainActivity.recyclerView;

public class FabClickListner {
    Context context;
    @Override
    public void onClick(View view) {
        int _id = view.getId();
        context = view.getContext();
        switch (_id)
        {
            case R.id.fab1:
                ShowInViewPager();
                break;
            case R.id.fab2:
                ShowInListView();
                break;
            case R.id.fab3:
                ShowInGridView();
                break;
        }

    }

    private void ShowInGridView()
    {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 100);
        recyclerView.setLayoutManager(new GridLayoutManager(context, noOfColumns));
    }


    private void ShowInListView()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    private void ShowInViewPager()
    {
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        SnapHelper snapHelper = new PagerSnapHelper();
        recyclerView.setOnFlingListener(null);
        snapHelper.attachToRecyclerView(recyclerView);
    }
}
*/
