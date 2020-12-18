package com.bigfortune.goldseven;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private TextView infoTextView;
    private ImageView[] topImages;
    private ImageView[] midImages;
    private ImageView[] bottomImages;
    private Wheel wheel1, wheel2, wheel3;
    private Button button;
    private boolean isStarted;

    public static final Random RANDOM = new Random();

    public static long randomLong(long lower, long upper) {
        return lower + (long) (RANDOM.nextDouble() * (upper - lower));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topImages = new ImageView[3];
        midImages = new ImageView[3];
        bottomImages = new ImageView[3];

        topImages[0] = findViewById(R.id.image1Top);
        topImages[1] = findViewById(R.id.image2Top);
        topImages[2] = findViewById(R.id.image3Top);

        midImages[0] = findViewById(R.id.image1Mid);
        midImages[1] = findViewById(R.id.image2Mid);
        midImages[2] = findViewById(R.id.image3Mid);

        bottomImages[0] = findViewById(R.id.image1Bottom);
        bottomImages[1] = findViewById(R.id.image2Bottom);
        bottomImages[2] = findViewById(R.id.image3Bottom);

        button = findViewById(R.id.button);
        infoTextView = findViewById(R.id.textView);

        button.setOnClickListener(view -> {
            if (isStarted) {
                wheel1.stopWheel();
                wheelTimeDelay(0);
            } else {
                final int frameDurationMin = 20;
                final int frameDurationMax = 70;

                wheel1 = new Wheel((imgTop, imgCenter, imgBottom, index) -> runOnUiThread(() -> {
                    topImages[0].setImageResource(imgTop);
                    midImages[0].setImageResource(imgCenter);
                    midImages[0].setTag(index);
                    bottomImages[0].setImageResource(imgBottom);
                }), RANDOM.nextInt(frameDurationMax - frameDurationMin) + frameDurationMin, randomLong(0, 200));

                wheel1.start();

                wheel2 = new Wheel((imgTop, imgCenter, imgBottom, index) -> runOnUiThread(() -> {
                    topImages[1].setImageResource(imgTop);
                    midImages[1].setImageResource(imgCenter);
                    midImages[1].setTag(index);
                    bottomImages[1].setImageResource(imgBottom);
                }), RANDOM.nextInt(frameDurationMax - frameDurationMin) + frameDurationMin, randomLong(150, 400));

                wheel2.start();

                wheel3 = new Wheel((imgTop, imgCenter, imgBottom, index) -> runOnUiThread(() -> {
                    topImages[2].setImageResource(imgTop);
                    midImages[2].setImageResource(imgCenter);
                    midImages[2].setTag(index);
                    bottomImages[2].setImageResource(imgBottom);
                }), RANDOM.nextInt(frameDurationMax - frameDurationMin) + frameDurationMin, randomLong(150, 400));

                wheel3.start();

                button.setText("Stop");
                infoTextView.setText("");
                isStarted = true;
            }
        });
    }

    private void wheelTimeDelay(int wheelId) {
        button.setClickable(false);
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    switch (wheelId) {
                        case 0:
                            wheel2.stopWheel();
                            wheelTimeDelay(1);
                            break;
                        case 1:
                            wheel3.stopWheel();
                            new Timer().schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    runOnUiThread(MainActivity.this::gameEnd);
                                }
                            }, 1206);
                            break;
                    }
                });
            }
        }, RANDOM.nextInt(1000) + 200);
    }

    private void gameEnd() {
        console(midImages[0].getTag() + " " + midImages[1].getTag() + " " + midImages[2].getTag());
        if (midImages[0].getTag() == midImages[1].getTag() && midImages[1].getTag() == midImages[2].getTag()) {
            infoTextView.setText(getResources().getString(R.string.win_msg));
        } else {
            infoTextView.setText(getResources().getString(R.string.lose_msg));
        }

        button.setText("Start");
        isStarted = false;
        button.setClickable(true);
    }

    public void console(String log) {
        Log.d("Loger", log);
    }
}