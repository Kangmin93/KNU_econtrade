package kr.ac.knu.bist.knu_econtrade.Adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.noticeComponents.Info_ListData;
import kr.ac.knu.bist.knu_econtrade.R;

/**
 * Created by dykim on 2016-09-09.
 * 16-10-28 : 도서관 및 공지사항 등에 공통적으로 쓰이는 어댑터인듯.
 */

// <리스트 적용부분


public class BBSListAdapter extends BaseAdapter {
    private Context mContext = null;
    private ArrayList<Info_ListData> mInfoListData;

    public BBSListAdapter(Context mContext, ArrayList<Info_ListData> mInfoListData) {
        this.mContext = mContext;
        this.mInfoListData = mInfoListData;
    }


    @Override
    public int getCount() {
        return mInfoListData.size();
    }

    @Override
    public Object getItem(int position) {
        return mInfoListData.get(position);
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

            holder.mTitle = (TextView) convertView.findViewById(R.id.item_title);
            holder.mWriter = (TextView) convertView.findViewById(R.id.item_writer);
            holder.mDate = (TextView) convertView.findViewById(R.id.item_date);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        Info_ListData mData = mInfoListData.get(position);


        if (mData.getmType().equals("공지")) {
            holder.mTitle.setText(Html.fromHtml("[공지]" + mData.getmTitle()));
        } else {
            holder.mTitle.setText("[" + mData.getmType() + "]" + mData.getmTitle());
        }

        holder.mWriter.setText(mData.getmWriter());
        holder.mDate.setText(mData.getmDate());

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