package kr.ac.knu.bist.knu_econtrade.libraryComponents;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by stype on 2016-12-19.
 */

public class bookItemViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener {
    public TextView itemTitle;
    public TextView itemAuthor;
    public TextView itemAvailable;
    public CardView childLayout;
    public bookInfo referenceItem;

    @Override
    public void onClick(View v) {

    }

    public bookItemViewHolder(View itemView) {
        super(itemView);
        itemTitle = (TextView) itemView.findViewById(R.id.book_title);
        itemAuthor = (TextView) itemView.findViewById(R.id.book_author);
        itemAvailable = (TextView) itemView.findViewById(R.id.book_available);
        childLayout = (CardView) itemView.findViewById(R.id.book_view);
    }

    public void bind(bookInfo item) {
        this.referenceItem = item;
        this.itemTitle.setText(item.getmTitle());
        this.itemAuthor.setText(item.getmAuthor());
        this.itemAvailable.setText(item.getmAvailable());
    }
}