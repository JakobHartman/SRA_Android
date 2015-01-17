package com.special;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

public class DataCollect extends FragmentActivity {

    private int numQuestions;
    private QuestionSet questionSet;

    private TextView progressView;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collect);

        Intent intent = getIntent();
//        int area = intent.getIntExtra("area", 0);
//        int house = intent.getIntExtra("household", 0);
//        if (area < 0) System.out.println("Given area index (" + area + ") is less than 0");
////        if (house < 0) System.out.println("Given household index (" + household + ") is less than 0");
//        getRegion(area, house);
//        String questionSetName = intent.getStringExtra("questionSetName");
//        if (household != null) {
//            questionSet = household.getQuestionSet(questionSetName);
//            if (questionSet == null) {
//                questionSet = CRUDFlinger.getQuestionSet(questionSetName);
//            }
//        }
        questionSet = CRUDFlinger.getQuestionSets().get(0);
        numQuestions = questionSet.getQuestions().size();

        progressView = (TextView) findViewById(R.id.question_progress_view);
        TextView pageTitle = (TextView) findViewById(R.id.question_header_view);
        pageTitle.setText(questionSet.getName());

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.question_view_pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new ZoomOutPageTransformer());
        mPager.setOnPageChangeListener(new OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {}
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}
            public void onPageSelected(int position) {
                sliderChanged(position);
            }
        });

        sliderChanged(0);
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
//            CRUDFlinger.saveRegion();
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void done(MenuItem mi) {
//        CRUDFlinger.saveRegion();
        finish();
    }

    public void sliderChanged(int position) {
        String newLabel = "" + (position + 1) + "/" + numQuestions;
        if (numQuestions == 0) newLabel = "0/0";
        progressView.setText(newLabel);
    }

    public Question getQuestion(int position) {
        return questionSet.getQuestions().get(position);
    }


    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Bundle arguments = new Bundle();
            arguments.putInt("questionIndex", position);
            DataCollectQuestionFragment fragment = new DataCollectQuestionFragment();
            fragment.setArguments(arguments);
            return fragment;
        }

        @Override
        public int getCount() {
            return numQuestions;
        }
    }

    public class ZoomOutPageTransformer implements ViewPager.PageTransformer {
        private static final float MIN_SCALE = 0.85f;
        private static final float MIN_ALPHA = 0.5f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();
            int pageHeight = view.getHeight();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0);

            } else if (position <= 1) { // [-1,1]
                // Modify the default slide transition to shrink the page as well
                float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));
                float vertMargin = pageHeight * (1 - scaleFactor) / 2;
                float horzMargin = pageWidth * (1 - scaleFactor) / 2;
                if (position < 0) {
                    view.setTranslationX(horzMargin - vertMargin / 2);
                } else {
                    view.setTranslationX(-horzMargin + vertMargin / 2);
                }

                // Scale the page down (between MIN_SCALE and 1)
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

                // Fade the page relative to its size.
                view.setAlpha(MIN_ALPHA +
                        (scaleFactor - MIN_SCALE) /
                                (1 - MIN_SCALE) * (1 - MIN_ALPHA));

            } else { // (1,+Infinity]
                // This page is way off-screen to the right.
                view.setAlpha(0);
            }
        }
    }
}
