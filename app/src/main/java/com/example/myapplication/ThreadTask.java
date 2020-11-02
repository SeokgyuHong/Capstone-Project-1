package com.example.myapplication;

import org.json.JSONException;

import java.io.IOException;

public abstract class ThreadTask<Object> implements Runnable {
    // Argument
    Object mArgument;

    // Result
    Object mResult;
    //T1, T2 자료형

    // Execute
    final public void execute(final Object arg) {
        // Store the argument
        mArgument = arg;

        // Call onPreExecute
        onPreExecute();

        // Begin thread work
        Thread thread = new Thread(this);
        thread.start();

        // Wait for the thread work
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
            onPostExecute();
            return;
        }

        // Call onPostExecute
        onPostExecute();
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

    // onPreExecute
    protected abstract void onPreExecute();

    // doInBackground
    protected abstract void doInBackground(String... urls) throws IOException, JSONException;

    // onPostExecute
    protected abstract void onPostExecute();
}