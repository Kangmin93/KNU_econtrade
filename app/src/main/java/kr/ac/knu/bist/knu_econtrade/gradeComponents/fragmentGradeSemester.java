package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;


/**
 * Created by Vertx on 2016-08-04.
 */
public class fragmentGradeSemester extends Fragment {
    private View activityView;
    private View emptyView;

    private RecyclerView recylerViewContainer;
    private adapterGradeItem mAdapter;

    private ArrayList<listGradeItem> listChildItems = new ArrayList<>();

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

        emptyView = activityView.findViewById(R.id.emptyview);
        setRecycleViewIsEmpty();
    }

    private void setRecycleViewIsEmpty() {
        if (listChildItems.isEmpty()) {
            recylerViewContainer.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }
        else {
            recylerViewContainer.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
    }
}
