package com.group2.recipeze.ui.recipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SliderAdapter extends PagerAdapter {
    Context mContext;
    ArrayList<Bitmap> images = new ArrayList<>();

    SliderAdapter(Context context, HashMap<Integer, Bitmap> images) {
        this.mContext = context;
        for (Map.Entry<Integer, Bitmap> entry : images.entrySet()) {
            this.images.add(entry.getValue());
        }
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((ImageView) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageBitmap(images.get(position));
        ((ViewPager) container).addView(imageView, 0);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((ImageView) object);
    }

    @Override
    public int getCount() {
        return images.size();
    }
}