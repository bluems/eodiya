package kr.ac.sunmoon.gijoe.camaps;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Paint;
import android.os.Bundle;
import android.widget.TextView;

// 혹여 마커 이벤트 처리가 필요할까 싶어 미리 빼둔 클래스.
// 현재로선 MapsActivity의 커스텀 마커 처리로만 쓰인다.
public class Marker extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker);
    }
}
