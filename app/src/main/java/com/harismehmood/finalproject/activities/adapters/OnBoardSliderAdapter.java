package com.harismehmood.finalproject.activities.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;

import com.airbnb.lottie.LottieAnimationView;
import com.harismehmood.finalproject.R;

public class OnBoardSliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;

    public OnBoardSliderAdapter(Context context) {
        this.context = context;

    }
    int[] images ={R.raw.hamburger,R.raw.orderfood,R.raw.delivery};
    int[] titles ={R.string.onBoardingTitle1,R.string.onBoardingTitle2,R.string.onBoardingTitle3};
    int[] descriptions ={R.string.onBoardingDesc1,R.string.onBoardingDesc2,R.string.onBoardingDesc3};
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view== (ConstraintLayout) object;
    }
    @Override
    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.onboard_image_design_row,container,false);
        LottieAnimationView imageView = view.findViewById(R.id.onBoardAnimation);
        TextView title = view.findViewById(R.id.onBoardingTitle);
        TextView description = view.findViewById(R.id.onBoardingDescription);
        imageView.setAnimation((images[position]));
        title.setText(titles[position]);
        description.setText(descriptions[position]);
        container.addView(view);
        return view;
    }
    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout)object);
    }
}
