package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by Vertx on 2016-08-04.
 *
 */

public class adapterGradeItem extends gradeRecyclerView.Adapter<gradeItemViewHolder> {
    public Context context;
    private LayoutInflater mInfalter;
    private ArrayList<listgradeItem> listChild = new ArrayList<>();

    public adapterGradeItem(Context context, ArrayList<listgradeItem> arrayChild) {
        super();
        this.context = context;
        this.listChild = arrayChild;

        this.mInfalter = LayoutInflater.from(context);

    }

    @Override
    public void onBindViewHolder(gradeItemViewHolder holder, int position) {
        holder.bind(listChild.get(position));
    }

    @Override
    public gradeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = mInfalter.inflate(R.layout.listitem_grade_child, parent, false);

        return new gradeItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listChild.size();
    }
}
