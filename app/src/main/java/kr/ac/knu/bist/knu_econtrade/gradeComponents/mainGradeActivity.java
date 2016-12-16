package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.os.Bundle;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.Widgets.Adapter_SectionsPager;
import kr.ac.knu.bist.knu_econtrade.Widgets.slidingTabLayout;

/**
 * Created by Vertx on 2016-09-10.
 */
public class mainGradeActivity extends AppCompatActivity {
    private static final String TAG = mainGradeActivity.class.getSimpleName();

    public enum MENU_TYPE {
        TAB_IMAGE,
        TAB_TEXT
    } // DEFAULT로 TAB_TEXT 설정.

    private ViewPager mViewPager;
    private Adapter_SectionsPager mSectionsPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_grade);
        setTitle("내 성적");

        mSectionsPagerAdapter = new Adapter_SectionsPager(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        initViews(MENU_TYPE.TAB_TEXT);
    }

    private void initViews(MENU_TYPE type) {
        mSectionsPagerAdapter.SetMenuType(type);

        slidingTabLayout mSlidingTabLayout = (slidingTabLayout) findViewById(R.id.tabs);
        if (type == MENU_TYPE.TAB_IMAGE) {
            mSlidingTabLayout.setCustomTabView(R.layout.tab_img_layout, R.id.tab_name_img);
        } else if (type == MENU_TYPE.TAB_TEXT){
            mSlidingTabLayout.setCustomTabView(R.layout.tab_txt_layout, R.id.tab_name_txt);
        }

        mSlidingTabLayout.setCustomTabColorizer(new slidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.colorPrimary);
            }
        });
        mSlidingTabLayout.setViewPager(mViewPager);
    }

    public void setCurrentItem(int inp_TabItem, boolean inp_SmoothScroll) {
        mViewPager.setCurrentItem(inp_TabItem, inp_SmoothScroll);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mViewPager.getAdapter().notifyDataSetChanged();
    }

    public ViewPager getmViewPager() {
        return mViewPager;
    }
}