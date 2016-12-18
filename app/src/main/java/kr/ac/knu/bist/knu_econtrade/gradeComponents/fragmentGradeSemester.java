package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;


/**
 * Created by Vertx on 2016-08-04.
 */
public class fragmentGradeSemester extends Fragment {
    private View activityView;

    private RecyclerView recylerViewContainer;
    private adapterGradeItem mAdapter;

    private ArrayList<listgradeItem> listChildItems = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activityView = inflater.inflate(R.layout.fragment_grade, container, false);
        setDesignComponents();
        return activityView;
    }

    // CUSTOM METHODS
    private void setDesignComponents() {
        recylerViewContainer = (RecyclerView) activityView.findViewById(R.id.grade_board_list);
        mAdapter = new adapterGradeItem(this.getContext(), listChildItems);
        recylerViewContainer.setAdapter(mAdapter);

    }
}
