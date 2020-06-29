package kr.ac.sunmoon.gijoe.camaps;

import androidx.annotation.NonNull;
import androidx.annotation.RawRes;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private GpsTracker gpsTracker;
    //private ArrayList<PublicData> arrayList;
    private ArrayList<BuildingData> arrayBuildingList = new ArrayList<>();
    private ArrayList<FacilityData> arrayFacilitiesList;
    private Button corona;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    private LoginData userLoginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        // 필요 권한이 부여되었는지 확인.
        // 권한 확인 과정에서 앱이 먼저 종료되는 버그가 있다.
        // 권한 부여 후 다시 실행하면 문제 없지만 확인 필요함.
        if (!checkLocationServicesStatus()) {
            showDialogForLocationServiceSetting();
        }else {
            checkRunTimePermission();
        }

        Intent intent = getIntent();
        userLoginData = (LoginData) Objects.requireNonNull(intent.getExtras()).get("userData");

        //GPS 수신 쓰레드 연결
        gpsTracker = new GpsTracker(MapsActivity.this);

        // 구글 지도 및 콜백 연결
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        // 테스트를 위해 내장된 xml 연결
        //arrayList = xml_parse(R.raw.cheonan);
        //Log.d("MapsActivity", "onCreate: Array Size: " + arrayList.size());

        //getBuildingList();//arrayList 채워짐
        //getFacilityList();

        corona = findViewById(R.id.corona_btn);

        corona.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse("https://coronamap.site/"));
            startActivity(intent1);
        });
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // GPS Tracker로부터 수신
        LatLng MyPoint = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

        // 마커 클릭 시 나오는 InfoWindows 연결
        // 안드로이드 기본 윈도우로는 줄내림 처리등의 미흡함이 있다.
        // 따라서 해당 처리가 되어 있는 별도 레이아웃을 연결할 필요가 있다.
        mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
            @Override
            public View getInfoWindow(Marker marker) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {
                // 레이아웃 연결
                @SuppressLint("InflateParams") View v = getLayoutInflater().inflate(R.layout.activity_marker, null);

                // 객체 연결
                TextView infoTitle = v.findViewById(R.id.infoTitle);
                TextView info = v.findViewById(R.id.info);

                // 데이터 바인딩
                // FAKE_BOLD_TEXT_FLAG: 한글 문자는 이 Flag가 별도로 없으면 Bold처리가 안됨.
                infoTitle.setText(marker.getTitle());
                infoTitle.setPaintFlags(infoTitle.getPaintFlags() | Paint.FAKE_BOLD_TEXT_FLAG);
                info.setText(marker.getSnippet());

                return v;
            }
        });

        // 마커 클릭으로 등장한 InfoWindow 클릭 시 DetailMapActivity로 연결하기 위한 이벤트 리스너 추가
        mMap.setOnInfoWindowClickListener(marker -> {
            Intent intent = new Intent(MapsActivity.this, DetailMapActivity.class);

            // 함께 넘길 데이터 취득 및 전달
            //PublicData data = find_xml_data(marker.getTitle());
            //intent.putExtra("publicData", data);

            startActivity(intent);
        });

        // 현재 위치로 카메라 이동
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(MyPoint,15));

        // 내 위치 표시 활성화
        mMap.setMyLocationEnabled(true);

        // 내 위치 찾기 버튼 활성화
        mMap.getUiSettings().setMyLocationButtonEnabled(true);

        // 천안시 전역의 등록된 공공시설 마커 추가
        // 서버 연동 시 별도 수신받은 데이터에 대해 지속적으로 반복될 예정임.

        String buildingUrl = getString(R.string.baseUrl) + "api/build/get";
        JSONObject buildingJson = new JSONObject();

        try {
            final RequestQueue requestQueue = Volley.newRequestQueue(MapsActivity.this);
            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
            //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, buildingUrl,buildingJson, (JSONObject response) -> {
                try {

                    JSONObject jsonObject = new JSONObject(response.toString());
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    ArrayList<BuildingData> buildingDataList = new ArrayList<>();

                    for (int i=0; i< dataArray.length(); i++) {
                        JSONObject tmpObject = dataArray.getJSONObject(i);
                        BuildingData tmpBuildingData = new BuildingData(
                                tmpObject.getInt("build_id"),
                                tmpObject.getString("build_name"),
                                tmpObject.getDouble("latitude"),
                                tmpObject.getDouble("longitude"),
                                tmpObject.getString("address_street")
                        );
                        buildingDataList.add(tmpBuildingData);
                    }

                    //arrayBuildingList = buildingDataList;

                    Iterator<BuildingData> iterator = buildingDataList.iterator();
                    BuildingData buildingData;

                    while(iterator.hasNext()) {
                        buildingData = iterator.next();
                        //ArrayList<FacilityData> facilityData = getFacilityofBuilding(data.getBuild_id());
                        //Log.d("Iterator", String.format("onMapReady: 개방장소명: %s, %f M", data.getPlace(), dist));

                        String facilityUrl = getString(R.string.baseUrl) + "api/facility/get/"+buildingData.getBuild_id();
                        JSONObject facilityJson = new JSONObject();

                        try {
                            final RequestQueue requestQueueFacility = Volley.newRequestQueue(MapsActivity.this);
                            //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                            //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                            BuildingData finalBuildingData = buildingData;
                            final JsonObjectRequest jsonObjectRequestFacility = new JsonObjectRequest(Request.Method.GET, facilityUrl,facilityJson, responseFacility -> {
                                try {

                                    //받은 json형식의 응답을 받아
                                    JSONObject jsonObjectFacility = new JSONObject(responseFacility.toString());
                                    JSONArray dataArrayFacility = jsonObjectFacility.getJSONArray("data");
                                    ArrayList<FacilityData> facilityDataList = new ArrayList<>();

                                    for (int i=0; i< dataArrayFacility.length(); i++) {
                                        JSONObject tmpObject = dataArrayFacility.getJSONObject(i);
                                        FacilityData tmpFacilityData = new FacilityData(
                                                tmpObject.optInt("facility_id"),
                                                tmpObject.optString("facility_name"),
                                                tmpObject.optString("closed"),
                                                tmpObject.optString("weekday_start"),
                                                tmpObject.optString("weekday_end"),
                                                tmpObject.optString("weekend_start"),
                                                tmpObject.optString("weekend_end"),
                                                tmpObject.optString("paid"),
                                                tmpObject.optInt("paid_std_time"),
                                                tmpObject.optInt("usage_fee"),
                                                tmpObject.optInt("overtime_std"),
                                                tmpObject.optInt("overtime_usage_fee"),
                                                tmpObject.optString("application"),
                                                tmpObject.optString("tel"),
                                                tmpObject.optString("website"),
                                                tmpObject.optInt("buildingBuildId")
                                        );
                                        facilityDataList.add(tmpFacilityData);
                                    }

                                    //arrayFacilitiesList = facilityDataList;
                                    //Log.d("Build", jsonObject.toString());

                                    // 앱 내 노출되는 부분
                                    LatLng item_position = new LatLng(finalBuildingData.getLatitude(), finalBuildingData.getLongitude());

                                    Iterator<FacilityData> iteratorFacility = facilityDataList.iterator();

                                    FacilityData facilityData;
                                    String description = "";

                                    String min_week_start = "";
                                    String max_week_end = "";
                                    String min_weekend_start = "";
                                    String max_weekend_end = "";


                                    while (iteratorFacility.hasNext()) {
                                        facilityData = iteratorFacility.next();
                                        description += facilityData.getFacility_name()+"\n";
                                        if (min_week_start.equals("")) facilityData.getWeekday_start();
                                        if (max_week_end.equals("")) facilityData.getWeekday_end();
                                        if (min_weekend_start.equals("")) facilityData.getWeekend_start();
                                        if (max_weekend_end.equals("")) facilityData.getWeekend_end();

                                        String buf = "";
                                        int splitedOrigin = Integer.valueOf(min_week_start.split(":")[0]);
                                        int splitedNew = Integer.valueOf(facilityData.getWeekday_start().split(":")[0]);

                                        if (splitedNew < splitedOrigin) min_week_start = facilityData.getWeekday_start();
                                    }
                                    description = description.substring(0, description.length()-1);

                                    //description = "test";//data.getTel() + "\n평일: " + data.getWeekday() + "\n주말: " + data.getWeekend();
                                    mMap.addMarker(new MarkerOptions().position(item_position).title(finalBuildingData.getBuild_name()).snippet(description));

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }, Throwable::printStackTrace);
                            jsonObjectRequestFacility.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            requestQueueFacility.add(jsonObjectRequestFacility);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(jsonObjectRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }






    }

    //권한 설정 연결
    private void showDialogForLocationServiceSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
        builder.setTitle("위치 서비스 비활성화");
        builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.");
        builder.setCancelable(true);
        builder.setPositiveButton("설정", (dialog, id) -> {
            Intent callGPSSettingIntent
                    = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
        });
        builder.setNegativeButton("취소", (dialog, id) -> dialog.cancel());
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

/*
    // XML Parser
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

    // DetailMapActivity로 넘기기 위한 클릭된 마커 정보 검색 함수
    private PublicData find_xml_data(String title) {
        for (PublicData data: arrayList) {
            if (data.getFacility().equals(title)) return data;
        }

        return null;
    }*/

    private ArrayList<FacilityData> getFacilityofBuilding(int building) {
        ArrayList<FacilityData> arrayList = new ArrayList<>();

        for(int i=0; i< arrayFacilitiesList.size(); i++ ) {
            if (arrayFacilitiesList.get(i).getBuild_id() == building)
                arrayList.add(arrayFacilitiesList.get(i));
        }

        return arrayList;
    }

    // 거리 계산을 위한 함수들
    // 추후 반경 계산시 사용될 것임.
    private double calcDistance(double origin_lat, double origin_lng, double diff_lat, double diff_lng) {
        double theta = origin_lng - diff_lng;
        double dist = Math.sin(deg2rad(origin_lat)) * Math.sin(deg2rad(diff_lat)) + Math.cos(deg2rad(origin_lat)) * Math.cos(deg2rad(diff_lat)) * Math.cos(deg2rad(theta));

        dist = rad2deg(Math.acos(dist));
        dist = dist * 60 * 1.1515;

        return (dist * 1609.344);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
