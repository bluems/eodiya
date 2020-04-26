package kr.ac.sunmoon.gijoe.camaps;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    private ArrayList<PublicData> arrayList;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(MapsActivity.this);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        arrayList = xml_parse(R.raw.cheonan);
        Log.d("MapsActivity", "onCreate: Array Size: " + arrayList.size());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GPS_ENABLE_REQUEST_CODE) {
            if (checkLocationServicesStatus()) {
                Log.d("@@@", "onActivityResult: GPS 활성화 됨");
                checkRunTimePermission();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int permsRequestCode, @NonNull String[] permissions, @NonNull int[] grandResults) {
        // 요청 코드가 PERMISSIONS_REQUEST_CODE 이고, 요청한 퍼미션 개수만큼 수신되었다면
        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {
            boolean check_result = true;

            // 모든 퍼미션을 허용했는지 체크
            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }

            if ( !check_result ) {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료
                if (shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[0])
                        || shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[1])) {
                    Toast.makeText(MapsActivity.this, "퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.", Toast.LENGTH_LONG).show();
                    finish();
                }else {
                    Toast.makeText(MapsActivity.this, "퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다. ", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        //LatLng cityHall = new LatLng(36.815226, 127.113886);
        LatLng cityHall = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.activity_marker, null);
                TextView infoTitle = v.findViewById(R.id.infoTitle);
                TextView info = v.findViewById(R.id.info);
                infoTitle.setText(marker.getTitle());
                infoTitle.setPaintFlags(infoTitle.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                info.setText(marker.getSnippet());

                return v;
            }
        });
        //mMap.addMarker(new MarkerOptions().position(cityHall).title("천안시청").snippet("1 \n 2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(cityHall,15));
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }

    //권한 설정 연결
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }

    //서비스 사용 가능 여부 판단
    private boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.GPS_PROVIDER)
                || Objects.requireNonNull(locationManager).isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    //서비스 권한 사용 가능 여부 판단
    void checkRunTimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(MapsActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_DENIED ||
                hasCoarseLocationPermission == PackageManager.PERMISSION_DENIED)
        {
            if (shouldShowRequestPermissionRationale(REQUIRED_PERMISSIONS[0])) {
                Toast.makeText(MapsActivity.this, "이 앱을 실행하려면 위치 접근 권한이 필요합니다.", Toast.LENGTH_LONG).show();
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                requestPermissions(REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    private ArrayList<PublicData> xml_parse(@RawRes int id) {
        String TAG = "Parser";
        ArrayList<PublicData> publicDataList = new ArrayList<>();

        InputStream inputStream = getResources().openRawResource(id);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);

        XmlPullParserFactory xmlPullParserFactory;
        XmlPullParser xmlPullParser;

        try {
            xmlPullParserFactory = XmlPullParserFactory.newInstance();
            xmlPullParser = xmlPullParserFactory.newPullParser();
            xmlPullParser.setInput(reader);

            PublicData publicData = null;
            int eventType = xmlPullParser.getEventType();

            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        Log.d(TAG,"xml_parse: Start");
                        break;
                    case XmlPullParser.START_TAG:
                        String startTag = xmlPullParser.getName();
                        Log.d(TAG, "xml_parse: Start TAG: " + xmlPullParser.getName());

                        if(startTag.equals("Row")) {
                            publicData = new PublicData();
                            Log.d(TAG, "xml_parse: 공공데이터 Row 추가");
                        }

                        if (publicData != null)
                            if (startTag.equals("개방시설명")) {
                                publicData.setFacility(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("개방장소명")) {
                                publicData.setPlace(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("휴관일")) {
                                publicData.setClosed(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("평일운영시작시각")) {
                                publicData.setWeekday(xmlPullParser.nextText() + "~" + publicData.getWeekday());
                            }
                            else if (startTag.equals("평일운영종료시각")) {
                                publicData.setWeekday(publicData.getWeekday() + xmlPullParser.nextText());
                            }
                            else if (startTag.equals("주말운영시작시각")) {
                                publicData.setWeekend(xmlPullParser.nextText() + "~" + publicData.getWeekend());
                            }
                            else if (startTag.equals("주말운영종료시각")) {
                                publicData.setWeekend(publicData.getWeekend() + xmlPullParser.nextText());
                            }
                            else if (startTag.equals("유료사용여부")) {
                                publicData.setPaid(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("부대시설정보")) {
                                publicData.setDesc(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("신청방법구분")) {
                                publicData.setApply(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("소재지도로명주소")) {
                                publicData.setAddress(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("사용안내전화번호")) {
                                publicData.setTel(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("홈페이지주소")) {
                                publicData.setUrl(xmlPullParser.nextText());
                            }
                            else if (startTag.equals("위도")) {
                                publicData.setLatitude(Double.parseDouble(xmlPullParser.nextText()));
                            }
                            else if (startTag.equals("경도")) {
                                publicData.setLongitude(Double.parseDouble(xmlPullParser.nextText()));
                            }
                        break;
                    case XmlPullParser.END_TAG:
                        String endTag = xmlPullParser.getName();
                        Log.d(TAG, "xml_parse: End TAG: " + endTag);

                        if(endTag.equals("Row")) {
                            publicDataList.add(publicData);
                        }
                        break;
                }
                eventType = xmlPullParser.next();
            }

            Log.d(TAG, "xml_parse: End");
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
                inputStreamReader.close();
                inputStream.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

        return publicDataList;
    }
}
