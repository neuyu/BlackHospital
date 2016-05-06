package com.neu.blackheartedhospital.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.neu.blackheartedhospital.R;

import java.util.List;

/**
 * Created by neu on 16/5/5.
 */
public class BlackHeartAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private List<String> mHospitalList;
    private Context mContext;

    public BlackHeartAdapter(Context context, List<String> hospitalList) {
        this.mLayoutInflater = LayoutInflater.from(context);
        mHospitalList = hospitalList;
        mContext = context;

    }

    @Override
    public int getCount() {
        return mHospitalList.size();
    }

    @Override
    public String getItem(int position) {
        return mHospitalList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        String name = getItem(position);
        if (convertView == null){
            convertView = mLayoutInflater.inflate(R.layout.black_heart_adapter,null);
            viewHolder = new ViewHolder();

            viewHolder.mTextView = (TextView)convertView.findViewById(R.id.hospital_item);

            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        viewHolder.mTextView.setText(name);

        return convertView;
    }

    static class ViewHolder{
        private TextView mTextView;
    }
}
