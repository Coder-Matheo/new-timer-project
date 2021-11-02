package rob.myappcompany.timer;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Time.class}, version = 1)
public abstract class MyRoomDatabase extends RoomDatabase {
    //Database Name
    private static String TIMER_DB = "TIMER_DB";
    //call TimerDao interface
    public abstract TimerDao timerDao();
    //singleton database
    private static volatile MyRoomDatabase INSTANCE;

    //getInstance from Database class
    static MyRoomDatabase getInstance(Context context){
        if (INSTANCE == null){
            synchronized (MyRoomDatabase.class){
                if (INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            MyRoomDatabase.class, TIMER_DB)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
