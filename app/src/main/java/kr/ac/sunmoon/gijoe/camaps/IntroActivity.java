package kr.ac.sunmoon.gijoe.camaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.content.Intent;

// 인트로 뷰를 위한 액티비티
public class IntroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        //IntroThread 에 핸들러를 넘기고 쓰레드 시작.
        IntroThread introThread = new IntroThread(handler);
        introThread.start();
    }

    // 쓰레드에 넘길 핸들러
    // 쓰레드로부터 콜백을 받아 로그인 뷰로 보낸다.
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message msg) {
            if (msg.what == 1) {
                Intent intent = new Intent(IntroActivity.this, LoginActivity.class);

                // Flag를 추가로 뒤로가기 시 앱 종료률 유도함.
                // 안드로이드는 액티비티간 전환 내역을 Task 라는 스택으로 관리함.
                // 하여, 해당 Task를 초기화함으로써 인트로 화면이 더이상 나타나지 않고,
                // 첫 화면이 메인이 되게끔 유도할 수 있다.
                // Clear Task: 기존 작업 내역 삭제
                // New Task: 새 작업 내역 시작
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

            return true;
        }
    });
}
