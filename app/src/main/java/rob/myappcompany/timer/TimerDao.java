package rob.myappcompany.timer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimerDao {
    @Insert
    void insert(Time time);

    @Query("SELECT * FROM time")
    LiveData<List<Time>> getAllTime();


}
