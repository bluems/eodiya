package kr.ac.sunmoon.gijoe.camaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailAdapter extends BaseAdapter {
    Context mContext = null;
    LayoutInflater mLayoutInflater = null;
    ArrayList<FacilityData> detailData;

    public DetailAdapter(Context mContext, ArrayList<FacilityData> detailData) {
        this.mContext = mContext;
        this.detailData = detailData;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        return detailData.size();
    }

    @Override
    public FacilityData getItem(int position) {
        return detailData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.detail_sub_layout, null);

        TextView detailNameValue = (TextView)view.findViewById(R.id.detailNameValue);
        TextView detailOpenTimeValue = (TextView)view.findViewById(R.id.detailOpenTimeValue);
        TextView detailClosedValue = (TextView)view.findViewById(R.id.detailClosedValue);

        detailNameValue.setText(detailData.get(position).getFacility_name());
        detailOpenTimeValue.setText("평일: " + detailData.get(position).getWeekday() + "\n주말: " + detailData.get(position).getWeekend());
        detailClosedValue.setText(detailData.get(position).getClosed());

        return view;
    }
}
