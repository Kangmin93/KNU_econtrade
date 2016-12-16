package kr.ac.knu.bist.knu_econtrade.noticeComponents;

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

public class adapterNoticeinfoItem extends noticeRecyclerView.Adapter<noticeItemViewHolder> {
    public Context context;
    private LayoutInflater mInflater;
    private ArrayList<Info_ListData> listChild = new ArrayList<>();

    public adapterNoticeinfoItem(Context context, ArrayList<Info_ListData> arrayChild) {
        super();
        this.context = context;
        this.listChild = arrayChild;

        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public void onBindViewHolder(noticeItemViewHolder holder, int position) {
        holder.bind(listChild.get(position));
    }

    @Override
    public noticeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        view = mInflater.inflate(R.layout.itemstyle, parent, false);

        return new noticeItemViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return listChild.size();
    }
}
