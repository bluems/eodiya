package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.Objects;

public class DetailMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private PublicData data;
    private String TAG = "DetailMapActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(this);

        Intent intent = getIntent();

        data = (PublicData) Objects.requireNonNull(intent.getExtras()).get("publicData");

        if (data == null) {
            Log.d(TAG, "onCreate: data is null");
        }

        drawDescription();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng gps = new LatLng(data.getLatitude(), data.getLongitude());
        markerOptions.position(gps).title(data.getFacility());

        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gps, 15));
    }

    private void drawDescription() {
        TextView name = findViewById(R.id.detailNameValue);
        TextView addr = findViewById(R.id.detailAddressValue);
        TextView openTime = findViewById(R.id.detailOpenTimeValue);
        TextView closedTime = findViewById(R.id.detailClosedValue);

        name.setText(data.getFacility());
        addr.setText(data.getAddress());
        openTime.setText(String.format("평일: %s\n주말: %s", data.getWeekday(), data.getWeekend()));
        closedTime.setText(data.getClosed());
    }
}
