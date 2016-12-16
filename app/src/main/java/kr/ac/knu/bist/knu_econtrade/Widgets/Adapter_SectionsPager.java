package kr.ac.knu.bist.knu_econtrade.Widgets;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.Calendar;
import java.util.Date;

import kr.ac.knu.bist.knu_econtrade.gradeComponents.fragmentGradeIssu;
import kr.ac.knu.bist.knu_econtrade.gradeComponents.fragmentGradeSemester;
import kr.ac.knu.bist.knu_econtrade.gradeComponents.mainGradeActivity;
import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-09-04.
 */

public class Adapter_SectionsPager extends FragmentStatePagerAdapter
        implements slidingTabLayout.TabIconProvider {

    private static int IconRes[] = {
            R.drawable.dev_noitems,
            R.drawable.dev_noitems,
    };

    private static final String IconText[] = {
            "이수성적",
            "학기성적",
    };

    private mainGradeActivity.MENU_TYPE SwitchMenutype;

    public Adapter_SectionsPager(FragmentManager fm) {
        super(fm);

        Calendar Local_Calendar = Calendar.getInstance();
        Long Local_TimeInMillis = new Date().getTime();
        Local_Calendar.setTimeInMillis(Local_TimeInMillis);
    }

    public interface OnDataSetChangedListener {
        void onDataSetChangeFinished();
    }
    OnDataSetChangedListener listener;

    public void setOnDataSetChangedListener(OnDataSetChangedListener listener) {
        this.listener = listener;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        if (listener != null)  {
            listener.onDataSetChangeFinished();
        }
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch (position) {
            case 0:
                return Call_Refresh_Fragment();
            case 1:
                return Call_Daily_Fragment();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        if (SwitchMenutype == mainGradeActivity.MENU_TYPE.TAB_IMAGE) {
            return IconRes.length;
        } else {
            return IconText.length;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return IconText[position];
    }

    @Override
    public int getPageIconResId(int position) {
        return IconRes[position];
    }

    public void SetMenuType(mainGradeActivity.MENU_TYPE menuType) {
        this.SwitchMenutype = menuType;
    }

    private Fragment Call_Refresh_Fragment() {
        return new fragmentGradeIssu();
    }
    private Fragment Call_Daily_Fragment() {
        return new fragmentGradeSemester();
    }
}