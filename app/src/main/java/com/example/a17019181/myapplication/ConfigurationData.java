package com.example.a17019181.myapplication;

public class ConfigurationData {

    public int onConfig;
    public int difficulty;

    public ConfigurationData(int onConfig, int difficulty) {
        this.onConfig = onConfig;
        this.difficulty = difficulty;
    }

    public int getOnConfig() {
        return onConfig;
    }

    public void setOnConfig(int onConfig) {
        this.onConfig = onConfig;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }
}
