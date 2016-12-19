package kr.ac.knu.bist.knu_econtrade.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kr.ac.knu.bist.knu_econtrade.R;
import kr.ac.knu.bist.knu_econtrade.noticeComponents.SeatListData;

/**
 * Created by dykim on 2016-12-18.
 */
public class Seat_adapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<SeatListData> mInfoListData;
    public Seat_adapter(Context context, ArrayList<SeatListData> seatList){
        mContext = context;
        mInfoListData = seatList;
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
            convertView = inflater.inflate(R.layout.seat_item, null);

            holder.mTitle = (TextView) convertView.findViewById(R.id.seat_item_name);
            holder.mPercentage = (TextView) convertView.findViewById(R.id.seat_item_percentage);
            holder.mNumber = (TextView) convertView.findViewById(R.id.seat_item_number);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        SeatListData mData = mInfoListData.get(position);
        holder.mPercentage.setText(mData.getPercentage());
        holder.mTitle.setText(mData.getName());
        holder.mNumber.setText(mData.getNumber());

        return convertView;
    }
    class ViewHolder {
        public TextView mTitle;
        public TextView mPercentage;
        public TextView mNumber;
    }
}
