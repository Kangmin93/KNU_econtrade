package kr.ac.knu.bist.knu_econtrade.noticeComponents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.gradeComponents.listGradeItem;

/**
 * Created by liliilli on 2016-11-17.
 */

public class noticeItemViewHolder extends RecyclerView.ViewHolder {
    public TextView itemTitle;
    public TextView itemWriter;
    public TextView itemDate;

    public LinearLayout childLayout;

    public Info_ListData referenceItem;

    public noticeItemViewHolder(View itemView) {
        super(itemView);
        itemTitle = (TextView) itemView.findViewById(R.id.item_title);
        itemWriter = (TextView) itemView.findViewById(R.id.item_writer);
        itemDate = (TextView) itemView.findViewById(R.id.item_date);

        childLayout = (LinearLayout) itemView.findViewById(R.id.card_view);
    }

    public void bind(Info_ListData item) {
        this.referenceItem = item;
        this.itemTitle.setText(item.getmTitle());
        this.itemWriter.setText(item.getmWriter());
        this.itemDate.setText(item.getmDate());
    }
}