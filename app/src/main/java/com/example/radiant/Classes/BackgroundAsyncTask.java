package com.example.radiant.Classes;

import android.content.Context;
import android.os.AsyncTask;

import com.example.radiant.repo.AsyncTaskCallback;

public class BackgroundAsyncTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private AsyncTaskCallback callback;
    private Exception exception;

    public BackgroundAsyncTask (Context context, AsyncTaskCallback callback)
    {
        this.context = context;
        this.callback = callback;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(Void... voids) {

        //create collections and documents in firestore
        //do here
        try
        {

            Thread.sleep(1000);

        }
        catch (InterruptedException e)
        {
            exception = e;
        }

        return "Finished";
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(callback != null)
        {
            if (exception == null)
            {
                callback.handleResponse(s);
            }
            else
            {
                callback.handleFault(exception);
            }
        }
    }
}
