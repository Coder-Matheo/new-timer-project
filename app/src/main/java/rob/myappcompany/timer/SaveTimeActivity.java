package rob.myappcompany.timer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class SaveTimeActivity extends AppCompatActivity {

    private static final String TAG = SaveTimeActivity.class.getSimpleName();
    private static final int REQUEST_CODE_GALLERY = 999;

    private Button doneButton;
    private ImageButton chooseButton;
    private EditText descriptionEditText;
    private ImageView imageView;
    private TextView timeShowTextView;
    private Intent intent;
    private String timeData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_time);

        View view = findViewById(android.R.id.content).getRootView();
        init();
        choose_add_image();
        done_image_to_DB();

        intent = getIntent();
        timeData = intent.getStringExtra("timerDataIntent");
        timeShowTextView.setText(timeData);

        hideAndShowKeyboard(2, view);
    }

    public void init(){
        doneButton = findViewById(R.id.doneButton);
        chooseButton = findViewById(R.id.chooseButton);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        imageView = findViewById(R.id.imageView);
        timeShowTextView = findViewById(R.id.timeShowTextView);


    }

    private void choose_add_image() {
        chooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(SaveTimeActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY);

                hideAndShowKeyboard(1, view);
            }
        });
    }
    
    public void hideAndShowKeyboard(int choose, View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (choose == 1){
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);    
        }else if (choose == 2){
            inputMethodManager.showSoftInput(descriptionEditText, InputMethodManager.SHOW_FORCED);
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_GALLERY){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent jumpToStorage = new Intent(Intent.ACTION_PICK);
                jumpToStorage.setType("image/*");
                startActivityForResult(jumpToStorage, REQUEST_CODE_GALLERY);
            }else {
                Toast.makeText(getApplicationContext(), "You don't have Permission", Toast.LENGTH_LONG).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void done_image_to_DB() {

        if (!descriptionEditText.getText().equals(" ")){
            doneButton.setEnabled(true);
        }else {
            doneButton.setEnabled(false);
        }
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Time time = new Time(timeData.trim(),
                            descriptionEditText.getText().toString().trim(),
                            imageViewToByte(imageView));
                    insertAsyncTask insertAsyncTask = new insertAsyncTask();
                    insertAsyncTask.execute(time);

                    //send Value through Intent to RecyclerView
                    Intent intentToRecycler = new Intent(SaveTimeActivity.this, TimeListActivity.class);
                    startActivity(intentToRecycler);
                }catch (Exception e){
                    e.printStackTrace();
                }
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


    class insertAsyncTask extends AsyncTask<Time, Void, Void>{
        @Override
        protected Void doInBackground(Time... times) {
            MyRoomDatabase.getInstance(getApplicationContext())
                    .timerDao()
                    .insert(times[0]);

            return null;
        }
    }
}