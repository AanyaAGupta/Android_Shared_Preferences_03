package com.example.p7l03guptaaanya;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.snackbar.Snackbar;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    String TAG = "com.example.p7l03guptaaanya.sharedprefs";

    Button bRight, bLeft;
    TextView tLeft, tRight;
    SeekBar seekBar;
    TextView[] views;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ConstraintLayout layout;
    long startTime, clicks;
    float cPS;
    Button submitB;
    TextView welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bRight=findViewById(R.id.bottomright_button);
        bRight.setBackgroundResource(R.drawable.frog);
        bLeft=findViewById(R.id.bottomleft_button);
        bLeft.setBackgroundResource(R.drawable.fish);
        tLeft=findViewById(R.id.topleft_textview);
        tLeft.setBackgroundResource(R.drawable.dog);
        tRight=findViewById(R.id.topright_textview);
        tRight.setBackgroundResource(R.drawable.cat);
        submitB = findViewById(R.id.exit_button);
        welcome = findViewById(R.id.welcome);
        welcome.setText(getString(R.string.welcome_msg));
        seekBar=findViewById(R.id.seekbar);
        views= new TextView[]{bLeft, bRight, tLeft, tRight};
        layout = findViewById(R.id.activity_main_layout);
        tLeft.setOnClickListener(this);
        bLeft.setOnClickListener(this);
        tRight.setOnClickListener(this);
        bRight.setOnClickListener(this);

        submitB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        sharedPreferences = getSharedPreferences(TAG, MODE_PRIVATE);
        editor = sharedPreferences.edit();
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int lastProgress;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                for (TextView x: views) {
                    x.setTextSize(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {//record state
                lastProgress = seekBar.getProgress();
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {//pop snackbar
                Snackbar snackbar = Snackbar.make(layout, "Font Size Changed To" +
                        seekBar.getProgress() + "sp", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                seekBar.setProgress(lastProgress);
                                for (TextView x: views) {
                                    x.setTextSize(lastProgress);
                                }
                                Snackbar.make(layout,"Font Size Reverted Back To "+
                                        lastProgress + "sp",Snackbar.LENGTH_LONG).show();

                            }
                        }
                );
                snackbar.setActionTextColor(Color.BLUE);
                View snackBarView = snackbar.getView();
                //TextView textView = snackBarView.findViewById(R.id.snackbar_text);
                TextView textView = snackBarView.findViewById(com.google.android.material.R.id.snackbar_text);
                textView.setTextColor(Color.WHITE);

                snackbar.show();
            }

        });

        layout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                editor.clear().apply();
                setInitialValue();
                return true;
            }
        });

        setInitialValue();
        startTime=System.currentTimeMillis();


    }
    private void setInitialValue() {
        for (TextView x: views) {
            x.setText(sharedPreferences.getString(x.getTag().toString(),"0") );
        }
        seekBar.setProgress(30);

    }

    @Override
    public void onClick(View view) {
        TextView x = (TextView) view;
        x.setText("" + (Integer.parseInt(x.getText().toString()) + 1));
        editor.putString(x.getTag().toString(), x.getText().toString());
        editor.apply();
        cPS = ++clicks/((System.currentTimeMillis()-startTime)/1000f);
        Toast.makeText(this,"" +cPS,Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setInitialValue();
    }
}

