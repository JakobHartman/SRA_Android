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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.rbdc.sra.R;
import org.rbdc.sra.helperClasses.CRUDFlinger;
import org.rbdc.sra.objects.Area;
import org.rbdc.sra.objects.Household;
import org.rbdc.sra.objects.Interview;
import org.rbdc.sra.objects.Question;
import org.rbdc.sra.objects.QuestionSet;

import java.util.ArrayList;

/*
    This class is called when someone clicks on one
    of the Response sets. This view contains a
    ViewPager which holds multiple or single
    DataCollectQuestionFragments

 */

public class DataCollect extends FragmentActivity {

    private int numQuestions;
    private QuestionSet questionSet;
    private TextView questionNameView;
    private TextView progressView;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_collect);
        CRUDFlinger.setApplication(getApplication());

        Intent intent = getIntent();
        int areaID = intent.getIntExtra("areaID", -1);
        int householdID = intent.getIntExtra("householdID", -1);
        int responseSetIndex = intent.getIntExtra("responseSetIndex", -1);
        String interviewType = intent.getStringExtra("interviewType");
        if (areaID < 0) {
            System.out.println("areaID was either not passed from DataCollect or is invalid");
            return;
        }
        if (householdID < 0) {
            System.out.println("householdID was either not passed from DataCollect or is invalid");
            return;
        }
        if (responseSetIndex < 0) {
            System.out.println("responseSetIndex was either not passed from DataCollect or is invalid");
            return;
        }
        if (interviewType.equals("household")) {
            Household household = CRUDFlinger.getAreas().get(areaID).getResources().get(householdID);
            ArrayList<Interview> interviews = household.getInterviews();
            if (interviews.isEmpty()) interviews.add(new Interview());

            // Right now it isn't clear why we need multiple interviews...for now just grab the first one
            Interview interview = interviews.get(0);
            ArrayList<QuestionSet> responseSets = interview.getQuestionsets();
            questionSet = responseSets.get(responseSetIndex);
            numQuestions = questionSet.getQuestions().size();
        }
        else if (interviewType.equals("area")) {
            Area area = CRUDFlinger.getAreas().get(areaID);
            ArrayList<Interview> interviews = area.getInterviews();
            if (interviews.isEmpty()) interviews.add(new Interview());
            Interview interview = interviews.get(0);
            ArrayList<QuestionSet> responseSets = interview.getQuestionsets();
            questionSet = responseSets.get(responseSetIndex);
            numQuestions = questionSet.getQuestions().size();
        }

        // A title for the question fragment
        questionNameView = (TextView) findViewById(R.id.question_header_view);

        // How far along you are in the response set
        progressView = (TextView) findViewById(R.id.question_progress_view);

        // Question set title
        TextView pageTitle = (TextView) findViewById(R.id.title);
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

        Button finishButton = (Button) findViewById(R.id.finish_button);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CRUDFlinger.saveRegion();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
            CRUDFlinger.saveRegion();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }

    public void sliderChanged(int position) {
        String newLabel = "" + (position + 1) + "/" + numQuestions;
        if (numQuestions == 0) newLabel = "0/0";
        progressView.setText(newLabel);

        if (!questionSet.getQuestions().isEmpty())
            questionNameView.setText(questionSet.getQuestions().get(position).getName());
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
