package com.bnjrKemal;

public class Gorev {

    private MissionType type;
    private int goal;
    private int progress;

    public Gorev(MissionType type, int goal) {
        this.type = type;
        this.goal = goal;
        this.progress = 0;
    }

    public MissionType getType() {
        return type;
    }

    public int getGoal() {
        return goal;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public void incrementProgress() {
        progress++;
    }
}

