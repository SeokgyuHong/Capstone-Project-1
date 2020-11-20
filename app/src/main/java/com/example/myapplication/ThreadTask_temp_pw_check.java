package com.example.myapplication;

import org.json.JSONException;

import java.io.IOException;

public abstract class ThreadTask_temp_pw_check<Object> implements Runnable {
    // Argument
    Object mArgument;

    // Result
    Object mResult;
    //T1, T2 자료형

    // Execute
    final public void execute(final Object arg) {
        // Store the argument
        mArgument = arg;

        // Begin thread work
        Thread thread = new Thread(this);
        thread.start();

        // Wait for the thread work
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            return;
        }
    }

    @Override
    public void run() {
        try {
            doInBackground(mArgument.toString());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // doInBackground
    protected abstract void doInBackground(String... urls) throws IOException, JSONException;

    public abstract int getResult();
    public abstract String getErrorCode();
}