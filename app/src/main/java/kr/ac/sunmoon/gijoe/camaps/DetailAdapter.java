package kr.ac.sunmoon.gijoe.camaps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView detailWebsiteValue = (TextView) view.findViewById(R.id.detailWebsiteValue);
        TextView detailTelValue = (TextView) view.findViewById(R.id.detailTelValue);
        TextView detailPayValue = (TextView) view.findViewById(R.id.detailPayValue);

        detailNameValue.setText(detailData.get(position).getFacility_name());
        detailOpenTimeValue.setText("평일: " + detailData.get(position).getWeekday() + "\n주말: " + detailData.get(position).getWeekend());
        detailClosedValue.setText(detailData.get(position).getClosed());
        detailWebsiteValue.setText(detailData.get(position).getWebsite());
        detailTelValue.setText(detailData.get(position).getTel());
        String paid = "";
        if (detailData.get(position).getPaid().equals("Y")) {
            paid += "유료시설\n";
            paid += "기본 " + detailData.get(position).getPaid_std_time() + "시간, " + detailData.get(position).getUsage_fee() + "원\n";
            paid += "초과 " + detailData.get(position).getOvertime_std() + "시간 당 " + detailData.get(position).getOvertime_usage_fee() + "원 부과";

        }
        else paid += "무료시설";

        detailPayValue.setText(paid);

        return view;
    }
}
