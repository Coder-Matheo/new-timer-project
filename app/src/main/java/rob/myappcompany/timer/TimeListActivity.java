package rob.myappcompany.timer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

import it.xabaras.android.recyclerview.swipedecorator.RecyclerViewSwipeDecorator;

public class TimeListActivity extends AppCompatActivity implements RecyclerViewClickInterface{

    private static final String TAG = TimeListActivity.class.getSimpleName();

    RecyclerView recyclerview;
    RecyclerAdapter recyclerAdapter;
    ArrayList<String> getTimeList;
    ArrayList<String> getDescriptionTimeList;
    ArrayList<byte[]> getImageTimeList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_list);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getTimerDataFun();
    }

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
            ItemTouchHelper.RIGHT){

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            final int position = viewHolder.getAdapterPosition();
            switch (direction){
                case ItemTouchHelper.LEFT:
                    getTimeList.remove(position);
                    getDescriptionTimeList.remove(position);
                    deleteItemFromDB(position);
                    recyclerAdapter.notifyItemRemoved(position);


                    Log.i(TAG, "onSwiped: LEFT");
                    break;
                case ItemTouchHelper.RIGHT:
                    Log.i(TAG, "onSwiped: RIGHT");
                    break;
            }

        }
        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
            new RecyclerViewSwipeDecorator.Builder(TimeListActivity.this, c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
                    .addSwipeLeftBackgroundColor(ContextCompat.getColor(TimeListActivity.this, R.color.purple_200))
                    .addSwipeLeftActionIcon(R.drawable.ic_delete)
                    .create()
                    .decorate();
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        }
    };



    private void getTimerDataFun() {
        getTimeList = new ArrayList<>();
        getDescriptionTimeList = new ArrayList<>();
        getImageTimeList = new ArrayList<>();

        LiveData<List<Time>> getAllTime1 = MyRoomDatabase.getInstance(getApplicationContext())
                .timerDao()
                .getAll();
        getAllTime1.observe(TimeListActivity.this, new Observer<List<Time>>() {
            @Override
            public void onChanged(List<Time> times) {
                //get All Time from Database and put in timeList
                for (int i = 0; i < times.size(); i++) {
                    getTimeList.add(times.get(i).getTimerData().trim());
                    getDescriptionTimeList.add(times.get(i).getTimerDescription().trim());
                    getImageTimeList.add(times.get(i).getTimerImg());
                    Log.i(TAG, "onSwiped:" + times.size());
                    setRecyclerView_Adapter_Divider(getTimeList, getDescriptionTimeList, getImageTimeList);

                }
            }
        });
    }

    public void deleteItemFromDB(int position){
        Log.i(TAG, "deleteItemFromDB: " + position);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Time timeGetItemId = MyRoomDatabase.getInstance(getApplicationContext())
                        .timerDao()
                        .findItemById(position);

                if(timeGetItemId != null){
                    MyRoomDatabase.getInstance(getApplicationContext())
                            .timerDao()
                            .deleteItemById(timeGetItemId);
                }

            }
        }).start();
    }
    public void setRecyclerView_Adapter_Divider(List<String> timeListParam, List<String> descriptionTimeListParam, ArrayList<byte[]> imageTimeListParam) {
        recyclerview = findViewById(R.id.recyclerView);
        //this <- after implements RecyclerViewInterface possible to use
        recyclerAdapter = new RecyclerAdapter(timeListParam, descriptionTimeListParam,imageTimeListParam,   this);
        recyclerview.setAdapter(recyclerAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerview.addItemDecoration(dividerItemDecoration);

        //Help you to create Swipe Left and Right
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerview);

    }

    @Override
    public void onItemClickInterface(int position) {
        Toast.makeText(this, getTimeList.get(position),Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLongItemClickInterface(int position) {

    }




}