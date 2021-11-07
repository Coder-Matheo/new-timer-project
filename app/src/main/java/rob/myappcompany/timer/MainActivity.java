package rob.myappcompany.timer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
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
import java.util.ArrayList;
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

    public List<TimeAbteil> getTimeList;


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

    }
    public void init(){
        button = findViewById(R.id.button);
        timer = new Timer();
        startButton = findViewById(R.id.startButton);
        resetButton = findViewById(R.id.resetButton);
        timeTextView = findViewById(R.id.timeTextView);
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
            return null;
        }
    }

    public void startTimeTapped(View view) {

        if (timerStarted == false){
            timerStarted = true;
            startTimer();
            startButton.setText("resume");
            resetButton.setEnabled(true);
        }else {
            timerStarted = false;
            timerTask.cancel();
            startButton.setText("start");
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
                        timeTextView.setText(getTimerText().get(0).getSeconds()+" : "+
                                getTimerText().get(0).getMinutes() +" : "+
                                getTimerText().get(0).getMilliseconds());

                    }
                });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 10);
    }

    private List<TimeAbteil> getTimerText() {
        int rounded = (int) Math.round(timeNum);
        //convert time to second
        int milliseconds = ((rounded % 86400) % 3600) % 60;
        int seconds = ((rounded % 86400) % 3600) / 60;
        int minutes = ((rounded % 86400) / 3600);

        return formatTime(milliseconds, seconds, minutes);
    }

    //format result time
    private List<TimeAbteil> formatTime(int milliseconds, int seconds, int minutes) {
        getTimeList = new ArrayList<>();
        getTimeList.add(new TimeAbteil(Integer.parseInt(String.format("%02d", milliseconds)), Integer.parseInt(String.format("%02d", seconds)), Integer.parseInt(String.format("%02d", minutes))));
        return getTimeList;
    }


    public void resetTimeTapped(View view){
        timerTask.cancel();
        AlertDialog.Builder saveTime = new AlertDialog.Builder(this);
        saveTime.setTitle(R.string.alertTitle);
        saveTime.setMessage(R.string.alertMessage);
        saveTime.setIcon(R.drawable.ic_baseline_save_24);


        saveTime.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(MainActivity.this, SaveTimeActivity.class);
                String milliSeconde = String.valueOf(getTimeList.get(0).getMilliseconds());
                String seconde = String.valueOf(getTimeList.get(0).getSeconds());
                String minute = String.valueOf(getTimeList.get(0).getMinutes());

                String formatTimer = String.format("%s : %s : %s", minute,seconde,milliSeconde);
                if (formatTimer != null){
                    intent.putExtra("timerDataIntent", formatTimer);
                    startActivity(intent);
                }

            }
        });

        saveTime.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                timeNum = 0.0;
                resetButton.setEnabled(false);
                timeTextView.setText(getTimerText().get(0).getSeconds()+" : "+
                        getTimerText().get(0).getMinutes() +" : "+
                        getTimerText().get(0).getMilliseconds());


            }
        });
        //separate layout to into AlertDialog
        View view1 = getLayoutInflater().inflate(R.layout.dialog_saving_time, null);
        ImageView dialog_img_in_layout = view1.findViewById(R.id.dialogImageView);
        dialog_img_in_layout.setImageResource(R.drawable.save_time_img);
        saveTime.setView(view1);
        AlertDialog dialogWithLayoutBulider = saveTime.create();
        dialogWithLayoutBulider.show();
    }
}