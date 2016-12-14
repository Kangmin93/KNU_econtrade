package kr.ac.knu.bist.knu_econtrade.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;

import kr.ac.knu.bist.knu_econtrade.Adapters.Adapter_MainSidebar;
import kr.ac.knu.bist.knu_econtrade.R;

public class Main_MainScene extends AppCompatActivity {

    private ExpandableListView View_SideMenu;
    private ArrayList<String> List_GroupMenu = new ArrayList<>();
    private HashMap<String, ArrayList<String>> List_ChildMenu = new HashMap<>();

    private Intent Local_Intent;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private String Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_main);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerLayout = (DrawerLayout) findViewById(R.id.main_main);
        Title = getTitle().toString();
        SetupDrawer();

        CreateListitems();
        View_SideMenu = (ExpandableListView) findViewById(R.id.main_sidebar_list);
        View_SideMenu.setAdapter(new Adapter_MainSidebar(getApplicationContext(), List_GroupMenu, List_ChildMenu));

        // 1. 학교 공지사항, 2. 학부 공지사항
        // 3. 학생회 공지 & 문의
        // 4. 성적 시간표 a. 내 시간표 b. 내 성적 c. 강의 계획서 d. 강의 평가
        // 5. 식단 a. 기숙사 b. 교내식당
        // 6. 도서관 a. 열람실 현황 b. 도서대출 현황
        // 7. 세미나실
        View_SideMenu.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int position, long id) {
                switch (position) {
                    case 0 : // 학교 공지사항
                        Local_Intent = new Intent(getApplicationContext(), Main_UnivNot_MainScene.class);
                        startActivity(Local_Intent);
                        break;
                    case 1 : // 학부 공지사항
                        Local_Intent = new Intent(getApplicationContext(), Main_Notice_MainScene.class);
                        startActivity(Local_Intent);
                        break;
                    case 2 : // 학생회 공지
                        Local_Intent = new Intent(getApplicationContext(), Main_Council_MainScene.class);
                        startActivity(Local_Intent);
                        break;
                    case 6 : // 성적 시간표
                        Local_Intent = new Intent(getApplicationContext(), Main_Seminar_.class);
                        startActivity(Local_Intent);
                        break;
                    default :
                        return false;
                }

                return true;
            }
        });
        View_SideMenu.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int group_pos, int child_pos, long id) {
                switch (group_pos) {
                    case 3 :
                        switch (child_pos) {
                            case 0 :
                                Local_Intent = new Intent(getApplicationContext(), Main_UnivNot_MainScene.class);
                                startActivity(Local_Intent);
                                break;
                            case 1 :
                                break;
                            case 2 :
                                break;
                            case 3 :
                                break;
                        }
                        break;
                    case 4 :
                        switch (child_pos) {
                            case 0 :
                                Local_Intent = new Intent(getApplicationContext(), Main_UnivNot_MainScene.class);
                                startActivity(Local_Intent);
                                break;
                            case 1 :
                        }
                        break;
                    case 5 :
                        switch (child_pos) {
                            case 0 : //TODO : Children Item 클릭 시, 화면 전환이 되도록 할 것.
                                Local_Intent = new Intent(getApplicationContext(), Main_Library_MainScene.class);
                                startActivity(Local_Intent);
                                break;
                            case 1 :
                                break;
                        }
                        break;
                    default :
                        return false;
                }

                return true;
            }
        });
    }

    @Override
    public View onCreateView(View parent, String name, Context context, AttributeSet attrs) {
        return super.onCreateView(parent, name, context, attrs);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        toggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        toggle.onConfigurationChanged(newConfig);
    }

    private void SetupDrawer() {
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                invalidateOptionsMenu();
            }
        };
        toggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(toggle);
    }

    private void CreateListitems() {
        List_GroupMenu.add("학교 공지사항");
        //ArrayList<String> GroupChild_1 = new ArrayList<>();
        //GroupChild_1.add();

        List_GroupMenu.add("학부 공지사항");
        List_GroupMenu.add("학생회 공지 및 문의");
        List_GroupMenu.add("성적 및 시간표");
        ArrayList<String> GroupChild_4 = new ArrayList<>();
        GroupChild_4.add("내 시간표");  GroupChild_4.add("내 성적");
        GroupChild_4.add("강의 계획서");GroupChild_4.add("강의 평가");

        List_GroupMenu.add("식단");
        ArrayList<String> GroupChild_5 = new ArrayList<>();
        GroupChild_5.add("기숙사");    GroupChild_5.add("교내 식당");

        List_GroupMenu.add("도서관");
        ArrayList<String> GroupChild_6 = new ArrayList<>();
        GroupChild_6.add("열람실 현황");
        GroupChild_6.add("도서대출 현황");

        List_GroupMenu.add("세미나실");

        List_ChildMenu.put(List_GroupMenu.get(3), GroupChild_4);
        List_ChildMenu.put(List_GroupMenu.get(4), GroupChild_5);
        List_ChildMenu.put(List_GroupMenu.get(5), GroupChild_6);
    }
}
