package kr.ac.knu.bist.knu_econtrade.gradeComponents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by liliilli on 2016-11-17.
 */

public class gradeItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemName;
    public TextView itemSort;
    public TextView itemCredit;
    public TextView itemScore;

    public LinearLayout childLayout;

    public listGradeItem referenceItem;

    public gradeItemViewHolder(View itemView) {
        super(itemView);
        itemName = (TextView) itemView.findViewById(R.id.grade_text_subject);
        itemSort = (TextView) itemView.findViewById(R.id.grade_text_sort);
        itemCredit = (TextView) itemView.findViewById(R.id.grade_text_credit);
        itemScore = (TextView) itemView.findViewById(R.id.grade_text_score);

        childLayout = (LinearLayout) itemView.findViewById(R.id.grade_item_layout);
    }

    public void bind(listGradeItem item) {
        this.referenceItem = item;
        this.itemName.setText(item.getItemName());
        this.itemSort.setText(item.getItemSort());
        this.itemCredit.setText(item.getItemUnit());
        this.itemScore.setText(item.getItemRank());
    }
}