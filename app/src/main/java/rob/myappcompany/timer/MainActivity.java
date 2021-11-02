package rob.myappcompany.timer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    //Variable
    private static final String TAG = MainActivity.class.getSimpleName();
    private Double timeNum = 0.0;
    private boolean timerStarted = false;

    //Object and Element
    TimeViewModel timeViewModel;
    private Button button;
    private Timer timer;
    private TimerTask timerTask;
    private Button startButton;
    private Button resetButton;
    private TextView timeTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        timeViewModel = ViewModelProviders.of(this).get(TimeViewModel.class);
        timeViewModel.getAllTime().observe(this, new Observer<List<Time>>() {
            @Override
            public void onChanged(List<Time> times) {
                Log.d(TAG, "onChanged: "+ times.toString());

            }
        });

        init();
        insertFunc();
        getAllTimeFunc();
    }
    public void init(){
        button = findViewById(R.id.button);
        timer = new Timer();
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        timeTextView = findViewById(R.id.timeTextView);
    }


    private void getAllTimeFunc() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                LiveData<List<Time>> getAllTime1 = MyRoomDatabase.getInstance(getApplicationContext())
                        .timerDao()
                        .getAllTime();

                getAllTime1.observe(MainActivity.this, new Observer<List<Time>>() {
                    @Override
                    public void onChanged(List<Time> times) {
                        Log.i(TAG, "onChanged: "+ times.toString());
                    }
                });

            }
        });

    }


    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    private void insertFunc() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Resources res = getResources();
                Drawable drawable = res.getDrawable(R.drawable.sqlite_icon);
                Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] bitMapData = stream.toByteArray();


                Time time = new Time("2.11.2021", "First Inserting ", bitMapData);

                InsertAsynTask insertAsynTask = new InsertAsynTask();
                insertAsynTask.execute(time);
            }
        });
    }


    class InsertAsynTask extends AsyncTask<Time, Void, Void>{
        @Override
        protected Void doInBackground(Time... times) {
            MyRoomDatabase.getInstance(getApplicationContext())
                    .timerDao()
                    .insert(times[0]);

            Log.i(TAG, "doInBackground: + Inserted");
            return null;
        }
    }

    public void startTimeTapped(View view) {
        startTimer();
        if (timerStarted == false){
            timerStarted = true;


        }
    }

    public void startTimer(){
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        timeNum++;
                        timeTextView.setText(getTimerText());
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);
    }

    private String getTimerText() {
        int rounded = (int) Math.round(timeNum);
        //Log.i(TAG, "getTimerText: "+ rounded);


        int milliSeconds = ((rounded % 86400) / 3600);

        int seconds = ((rounded % 86400) / 3600);

        int minutes = ((rounded % 86400) % 3600) / 60;
        int hours = ((rounded % 86400) % 3600) % 60;

        return formatTime(seconds, minutes, hours);
    }

    private String formatTime(int seconds, int minutes, int hours) {

        return String.format("%02d", seconds) + " : " + String.format("%02d", minutes) + " : "+ String.format("%02d", hours);
    }

    public void resetTimeTapped(View view) {

    }
}