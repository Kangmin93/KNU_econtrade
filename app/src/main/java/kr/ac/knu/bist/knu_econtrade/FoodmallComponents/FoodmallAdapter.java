package kr.ac.knu.bist.knu_econtrade.FoodmallComponents;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by dykim on 2016-09-09.
 */

// <리스트 적용부분


public class FoodmallAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<ListData> mListData;

    public FoodmallAdapter(Context mContext, ArrayList<ListData> mListData) {
        this.mContext = mContext;
        this.mListData = mListData;
    }


    @Override
    public int getCount() {
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mListData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.itemstyle, null);

            holder.mWriter = (TextView) convertView.findViewById(R.id.item_writer);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ListData mData = mListData.get(position);



        mData.mWriter = mData.mType.replace("<br />","");

        holder.mWriter.setText(mData.mWriter);

        return convertView;

    }
    class ViewHolder {

        public TextView mType;
        public TextView mTitle;
        public TextView mUrl;
        public TextView mWriter;
        public TextView mDate;
    }
}