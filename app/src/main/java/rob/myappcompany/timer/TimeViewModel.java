package rob.myappcompany.timer;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class TimeViewModel extends AndroidViewModel {

    MyRoomDatabase myRoomDatabase;

    public TimeViewModel(@NonNull Application application) {
        super(application);

        myRoomDatabase = MyRoomDatabase.getInstance(application.getApplicationContext());
    }

    //get query from TimerDao then use in MainActivity class
    public LiveData<List<Time>> getAllTime(){
        return myRoomDatabase.timerDao().getAllTime();
    }


}
