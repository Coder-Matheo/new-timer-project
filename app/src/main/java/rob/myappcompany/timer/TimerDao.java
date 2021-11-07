package rob.myappcompany.timer;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface TimerDao {
    @Insert
    void insert(Time time);

    @Query("SELECT * FROM time")
    LiveData<List<Time>> getAllTime();

    @Query("SELECT * FROM time")
    LiveData<List<Time>> getAll();

    @Query("SELECT * FROM time WHERE uid LIKE :uid LIMIT 1")
    Time findItemById(int uid);

    @Delete
    void deleteItemById(Time time);

}
