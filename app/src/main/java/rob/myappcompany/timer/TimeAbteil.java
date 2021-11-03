package rob.myappcompany.timer;

public class TimeAbteil {
    private int milliseconds;
    private int minutes;
    private int seconds;

    public TimeAbteil(int milliseconds, int minutes, int seconds) {
        this.milliseconds = milliseconds;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public void setMilliseconds(int milliseconds) {
        this.milliseconds = milliseconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public void setSeconds(int seconds) {
        this.seconds = seconds;
    }
}
