package com.example.wavrecorder;

import android.widget.Button;

public class RecordViews {
    Button start,stop,play;

    public RecordViews(Button start, Button stop, Button play) {
        this.start = start;
        this.stop = stop;
        this.play = play;
    }

    public Button getStart() {
        return start;
    }

    public void setStart(Button start) {
        this.start = start;
    }

    public Button getStop() {
        return stop;
    }

    public void setStop(Button stop) {
        this.stop = stop;
    }

    public Button getPlay() {
        return play;
    }

    public void setPlay(Button play) {
        this.play = play;
    }
}
