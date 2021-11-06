package rob.myappcompany.timer;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TimeListActivity extends AppCompatActivity {

    private static final String TAG = TimeListActivity.class.getSimpleName();

    private TextView timeTextView;
    private TextView descriptionTextView;
    private ImageView imageView;

    private RecyclerView recyclerview;

    List<String> timeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);

        //imageView.setImageResource(getImageIntent);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //Intent intent = getIntent();
        //String getIntentTime = intent.getStringExtra("timeDataToRecycler");
        //String getIntentDescription = intent.getStringExtra("descriptionDataToRecycler");
        //byte getImageIntent = intent.getByteExtra("imageDataToRecycler", (byte) -1);

        //timeTextView.setText(getIntentTime);
        //descriptionTextView.setText(getIntentDescription);

        timeList = new ArrayList<>();
        recyclerview = findViewById(R.id.recyclerView);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);


        timeList.add("Iron man");
        timeList.add("The Incredible Hulk");
        timeList.add("Iron Man 2");
        timeList.add("The Average");
        timeList.add("Iron Man 3");
        timeList.add("Ant-Man");
        timeList.add("Iron man 4");
        timeList.add("Doctor strong");
        timeList.add("Iron man");
        timeList.add("The Incredible Hulk");
        timeList.add("Iron Man 2");
        timeList.add("The Average");
        timeList.add("Iron Man 3");
        timeList.add("Ant-Man");
        timeList.add("Iron man 4");
        timeList.add("Doctor strong");





    }

}