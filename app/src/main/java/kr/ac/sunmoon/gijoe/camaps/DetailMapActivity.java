package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class DetailMapActivity extends AppCompatActivity implements OnMapReadyCallback {
    private PublicData data;
    private int build_id;
    private String build_title;
    private LatLng build_position;
    private String TAG = "DetailMapActivity";
    private Button commentSend;
    private EditText commentBox;
    private Context mContext;
    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private String secretKey;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_map);

        FragmentManager fragmentManager = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.detailMap);
        mapFragment.getMapAsync(this);

        Context mContext = this;
        preferences = mContext.getSharedPreferences(
                getString(R.string.preference_file_key),
                Context.MODE_PRIVATE
        );
        editor = preferences.edit();

        secretKey = preferences.getString(getString(R.string.secret), "");

        // 해당 Activity 생성시 Maps에서 할당 받은 Intent
        Intent intent = getIntent();

        // intent 로부터 함께 전달된 DetailMap 에 필요한 데이터 취득
        //data = (PublicData) Objects.requireNonNull(intent.getExtras()).get("publicData");
        build_id = intent.getIntExtra("clickBuildId", 0);
        build_title = intent.getStringExtra("clickTitle");
        build_position = (LatLng) Objects.requireNonNull(intent.getExtras()).get("clickPoint");

        LinearLayout commentLayout = (LinearLayout) findViewById(R.id.add_comment_layout);
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        layoutInflater.inflate(R.layout.detail_comment, commentLayout, true);

        commentSend = findViewById(R.id.btn_comment_send);
        commentBox = findViewById(R.id.comment_box);

        commentSend.setOnClickListener(v -> {
            if (secretKey == "") {
                Intent intent1 = new Intent(DetailMapActivity.this, LoginActivity.class);
                startActivityForResult(intent1, 1);
            } else {
                String commentUrl = getString(R.string.baseUrl) + "api/comment/add";
                JSONObject commentJson = new JSONObject();

                //Toast.makeText(DetailMapActivity.this,"댓글전송", Toast.LENGTH_SHORT).show();

                try {
                    commentJson.put("comment", commentBox.getText());
                    commentJson.put("build_id", build_id);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final RequestQueue requestQueue = Volley.newRequestQueue(DetailMapActivity.this);
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, commentUrl, commentJson, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("code") == "OK") {
                                Toast.makeText(DetailMapActivity.this, "댓글전송 성공", Toast.LENGTH_SHORT);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {
                    @Override
                    public Map<String,String> getHeaders() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String,String>();
                        params.put("x-access-token", secretKey);

                        return params;
                    }
                };

                jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                requestQueue.add(jsonObjectRequest);
            }


        });

        String facilityUrl = getString(R.string.baseUrl) + "api/facility/get/" + this.build_id;
        JSONObject facilityJson = new JSONObject();

        try {
            final RequestQueue requestQueueFacility = Volley.newRequestQueue(DetailMapActivity.this);
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

                    ListView listView = (ListView)findViewById(R.id.detailListView);
                    final DetailAdapter detailAdapter = new DetailAdapter(this,facilityDataList);
                    listView.setAdapter(detailAdapter);

                    String commentUrl = getString(R.string.baseUrl) + "api/comment/get/" + this.build_id;
                    JSONObject commentJson = new JSONObject();

                    try {
                        final RequestQueue requestQueueComment = Volley.newRequestQueue(DetailMapActivity.this);
                        final JsonObjectRequest jsonObjectRequestComment = new JsonObjectRequest(Request.Method.POST, commentUrl,commentJson, responseComment -> {
                            try {
                                //받은 json형식의 응답을 받아
                                JSONObject jsonObjectComment = new JSONObject(responseComment.toString());
                                JSONArray dataArrayComment = jsonObjectComment.getJSONArray("data");
                                ArrayList<CommentData> commentDataList = new ArrayList<>();

                                for (int i=0; i< dataArrayComment.length(); i++) {
                                    JSONObject tmpObject = dataArrayComment.getJSONObject(i);
                                    CommentData tmpCommentData = new CommentData(
                                            tmpObject.getString("comment"),
                                            tmpObject.getInt("user_id"),
                                            tmpObject.getInt("buildingBuildId")
                                    );

                                    String url = getString(R.string.baseUrl) + "api/finder/user/" + tmpObject.getInt("user_id");

                                    JSONObject userJson = new JSONObject();

                                    final RequestQueue requestQueue = Volley.newRequestQueue(DetailMapActivity.this);
                                    //데이터 전달을 끝내고 이제 그 응답을 받을 차례입니다.
//서버로 데이터 전달 및 응답 받기에 실패한 경우 아래 코드가 실행됩니다.
                                    //Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
                                    final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, userJson, response -> {
                                        try {

                                            //받은 json형식의 응답을 받아
                                            JSONObject jsonObject = new JSONObject(response.toString());
                                            tmpCommentData.setNick(jsonObject.getString("nickname"));

                                            ListView commentView = (ListView) findViewById(R.id.detailCommentView);
                                            final CommentAdapter commentAdapter = new CommentAdapter(this, commentDataList);
                                            commentView.setAdapter(commentAdapter);
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }, Throwable::printStackTrace);
                                    jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    requestQueue.add(jsonObjectRequest);

                                    commentDataList.add(tmpCommentData);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }, Throwable::printStackTrace);
                        jsonObjectRequestComment.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        requestQueueComment.add(jsonObjectRequestComment);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, Throwable::printStackTrace);
            jsonObjectRequestFacility.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueueFacility.add(jsonObjectRequestFacility);
        } catch (Exception e) {
            e.printStackTrace();
        }



        /*if (data == null) {
            Log.d(TAG, "onCreate: data is null");
        }*/

        //drawDescription();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1)
            if(resultCode == RESULT_OK) {
                LoginData loginData = (LoginData) Objects.requireNonNull(data.getExtras()).get("userData");
                editor.putString(getString(R.string.secret), loginData.getAccessToken());
                secretKey = loginData.getAccessToken();
            }
    }

    //전달받은 데이터를 기준으로 미니맵 조정
    @Override
    public void onMapReady(GoogleMap googleMap) {
        MarkerOptions markerOptions = new MarkerOptions();
        LatLng gps = this.build_position;
        markerOptions.position(gps).title(this.build_title);

        // 현재 건물 주변만 보기 위해 제스처 기능 제거
        // 사용자는 미니맵의 지도에서 다른 지역으로 이동시킬 수 없음
        googleMap.getUiSettings().setAllGesturesEnabled(false);

        googleMap.addMarker(markerOptions);
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(gps, 15));
    }

    private void drawDescription() {
        TextView name = findViewById(R.id.detailNameValue);
        TextView openTime = findViewById(R.id.detailOpenTimeValue);
        TextView closedTime = findViewById(R.id.detailClosedValue);

        /*name.setText(data.getFacility());
        address.setText(data.getAddress());
        openTime.setText(String.format("평일: %s\n주말: %s", data.getWeekday(), data.getWeekend()));
        closedTime.setText(data.getClosed());*/
    }
}
