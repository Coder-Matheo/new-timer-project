package rob.myappcompany.timer;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Blob;

@Entity
public class Time {


    //public String TIMER_TABLE = "TIMER_TABLE";

    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "timer_data")
    private String timerData;

    @ColumnInfo(name = "timer_description")
    private String timerDescription;

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    private byte[] timerImg;

    public Time(String timerData, String timerDescription, byte[] timerImg) {
        this.timerData = timerData;
        this.timerDescription = timerDescription;
        this.timerImg = timerImg;
    }

    public Time() {
    }


    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTimerData() {
        return timerData;
    }

    public void setTimerData(String timerData) {
        this.timerData = timerData;
    }

    public String getTimerDescription() {
        return timerDescription;
    }

    public void setTimerDescription(String timerDescription) {
        this.timerDescription = timerDescription;
    }

    public byte[] getTimerImg()
    {
        return timerImg;
    }

    public void setTimerImg(byte[] timerImg) {
        this.timerImg = timerImg;
    }

    @Override
    public String toString() {
        return "Time{" +
                "uid='" + uid + '\'' +
                ", timerData='" + timerData + '\'' +
                ", timerDescription='" + timerDescription + '\'' +
                ", timerImg=" + timerImg +
                '}';
    }
}
