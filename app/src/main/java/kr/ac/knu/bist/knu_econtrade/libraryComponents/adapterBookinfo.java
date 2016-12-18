package kr.ac.knu.bist.knu_econtrade.libraryComponents;

/**
 * Created by stype on 2016-12-19.
 */

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.noticeRecyclerView;

public class adapterBookinfo extends noticeRecyclerView.Adapter<bookItemViewHolder>
        implements View.OnClickListener {
    public Context context;
    private LayoutInflater mInflater;
    private ArrayList<bookInfo> listChild = new ArrayList<>();

    public adapterBookinfo(Context context, ArrayList<bookInfo> arrayChild) {
        super();
        this.context = context;
        this.listChild = arrayChild;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onBindViewHolder(bookItemViewHolder holder, int position) {
        holder.bind(listChild.get(position));
    }

    @Override
    public bookItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = mInflater.inflate(R.layout.layout_book, parent, false);
        return new bookItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listChild.size();
    }
}
