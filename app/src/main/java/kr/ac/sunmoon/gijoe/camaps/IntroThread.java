package kr.ac.sunmoon.gijoe.camaps;

import android.os.Handler;
import android.os.Message;

// 인트로 뷰 제어를 위한 쓰레드
public class IntroThread extends Thread {

    private Handler handler;

    IntroThread(Handler handler) {
        this.handler = handler;
    }

    @Override
    public void run() {
        Message msg = new Message();

        try {
            Thread.sleep(3000); //3초 대기 후 콜백
            msg.what = 1;
            handler.sendEmptyMessage(msg.what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
