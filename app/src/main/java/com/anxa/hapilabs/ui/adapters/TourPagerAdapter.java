package com.anxa.hapilabs.ui.adapters;

import android.content.Context;
import android.os.Parcelable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;

import com.hapilabs.R;

/**
 * Created by jas on 06/01/2016.
 */
public class TourPagerAdapter extends PagerAdapter{

        public int getCount() {
            return 3;
        }

        public Object instantiateItem(View collection, int position) {

            LayoutInflater inflater = (LayoutInflater) collection.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int resId = 0;


            switch (position) {
                case 0:
                    resId = R.layout.tour_page1;
                    break;
                case 1:
                    resId = R.layout.tour_page2;
                    break;
                case 2:
                    resId = R.layout.tour_page3;
                    break;
                default:
                    resId = R.layout.tour_page1;
                    break;
            }

            View view = inflater.inflate(resId, null);

            ((ViewPager) collection).addView(view, 0);

            return view;
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);

        }


        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == ((View) arg1);

        }

        @Override
        public Parcelable saveState() {
            return null;
        }



}
