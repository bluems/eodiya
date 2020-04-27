package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        TextView tvTitle = findViewById(R.id.tvTitle_text);
        TextView tvAddress = findViewById(R.id.tvAddress_text);
        TextView tvClosed = findViewById(R.id.tvClosed_text);
        TextView tvTime = findViewById(R.id.tvTime_text);

        MapView mMap = findViewById(R.id.mapView);

        Intent intent = getIntent();

        String title = Objects.requireNonNull(intent.getExtras()).getString("name");
        LatLng gps = (LatLng) intent.getExtras().get("gps");
        ArrayList<PublicData> arrayList = (ArrayList<PublicData>) intent.getExtras().get("publicData");

        if (arrayList != null) {
            Iterator<PublicData> iterator = arrayList.iterator();
            PublicData data;
            while(iterator.hasNext()) {
                data = iterator.next();
                //double dist = calcDistance(gpsTracker.getLatitude(), gpsTracker.getLongitude(), data.getLatitude(), data.getLongitude());
                //Log.d("Iterator", String.format("onMapReady: 개방장소명: %s, %f M", data.getPlace(), dist));

                LatLng item_position = new LatLng(data.getLatitude(), data.getLongitude());
                String description = data.getTel() + "\n평일: " + data.getWeekday() + "\n주말: " + data.getWeekend();
                //mMap.addMarker(new MarkerOptions().position(item_position).title(data.getPlace()).snippet(description));
            }
        }
    }
}
