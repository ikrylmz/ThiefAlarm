package com.example.thiefalarm;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import com.skyfishjy.library.RippleBackground;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements MovementDetector.IMovement{

    private MovementDetector movementDetector;
    private AlertSoundPlayer player;
    TextView txt_start,txt_stop,txt_active;
    RippleBackground rippleBackground,rippleBackground_alert;
    FrameLayout primary_screen;
    Animation animation_open,animation_close;
    ImageButton btn_close_screen;

    SeekBar seekbar_sensivity;

    boolean isBackScreenOn  = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_main);


        seekbar_sensivity = (SeekBar)findViewById(R.id.seekbar_sensivity);

        seekbar_sensivity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                float value = Math.abs((i /10f) - 10f);
                movementDetector.sensivity = 1 + value;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        primary_screen = (FrameLayout)findViewById(R.id.primary_screen);

        animation_open = AnimationUtils.loadAnimation(this,R.anim.tv_anim);
        animation_close = AnimationUtils.loadAnimation(this,R.anim.tv_anim_close);



        btn_close_screen = (ImageButton)findViewById(R.id.btn_close_screen);


        txt_start = (TextView)findViewById(R.id.txt_start);
        txt_stop = (TextView)findViewById(R.id.txt_stop);
        txt_active = (TextView)findViewById(R.id.txt_active);

        rippleBackground = (RippleBackground)findViewById(R.id.content);
        rippleBackground_alert = (RippleBackground)findViewById(R.id.content_alert);

        movementDetector = new MovementDetector(this);


        btn_close_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rippleBackground.setVisibility(View.GONE);
                primary_screen.setBackgroundResource(R.drawable.background_black);
                isBackScreenOn = true;
            }
        });

        primary_screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isBackScreenOn)
                {
                    rippleBackground.setVisibility(View.VISIBLE);
                    primary_screen.setBackgroundResource(R.drawable.background);
                    isBackScreenOn = false;
                }
            }
        });

        ImageView imageView=(ImageView)findViewById(R.id.centerImage);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setVisibleTextViews();
            }
        });

        setValuesTo();
    }
    private boolean isVisible(View o)
    {
        if(o.getVisibility() == View.VISIBLE)
            return true;
        else return false;
    }
    private void setVisibleTextViews()
    {
        if(isVisible(txt_start))
        {
            rippleBackground.startRippleAnimation();
            txt_start.setVisibility(View.INVISIBLE);
            txt_start.clearAnimation();

            player = new AlertSoundPlayer(this);

            txt_stop.setVisibility(View.VISIBLE);
            txt_stop.setAnimation(animation_open);
            txt_stop.startAnimation(animation_open);

            txt_active.setVisibility(View.VISIBLE);
            txt_active.setAnimation(animation_open);
            txt_active.startAnimation(animation_open);

            movementDetector.startListening(this);

        }
        else
        {
            rippleBackground.stopRippleAnimation();
            rippleBackground_alert.stopRippleAnimation();

            player.stop();

            txt_stop.setVisibility(View.INVISIBLE);
            txt_start.setVisibility(View.VISIBLE);
            txt_start.setAnimation(animation_open);
            txt_start.startAnimation(animation_open);
            txt_stop.clearAnimation();

            txt_active.setVisibility(View.INVISIBLE);
            txt_active.setAnimation(animation_close);
            txt_active.startAnimation(animation_close);

            movementDetector.stopListening();
        }
    }


    @Override
    public void onMovementTrigger() {
        player.play();
        rippleBackground.stopRippleAnimation();
        rippleBackground_alert.startRippleAnimation();
    }
    protected void onStop() {
        super.onStop();
        new ValueSaver(this).setValues(seekbar_sensivity.getProgress(), movementDetector.sensivity);

    }
    private void setValuesTo() {
        Map<String, ?> values = new ValueSaver(this).getValues();

        if (!values.isEmpty()) {
            seekbar_sensivity.setProgress((Integer) (values.get("progressValue")));
            movementDetector.sensivity = ((Float) (values.get("sensivityValue")));
        }
    }
}


