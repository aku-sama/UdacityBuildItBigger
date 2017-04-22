package com.udacity.gradle.builditbigger;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.example.joke.backend.myApi.MyApi;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;

import java.io.IOException;

import ru.improvegroup.jokeandroidlibrary.BundleConfig;
import ru.improvegroup.jokeandroidlibrary.JokeShowActivity;

import static com.udacity.gradle.builditbigger.BuildConfig.IP_ADDRESS;

/**
 * Created by Diana.Raspopova on 4/22/2017.
 */

class EndpointsAsyncTask extends AsyncTask<Pair<Context, String>, Void, String> {
    private static MyApi myApiService = null;
    private Context context;
    private ProgressDialog progress;

    EndpointsAsyncTask(AppCompatActivity activity) {
        context = activity;
        progress = new ProgressDialog(activity);
    }

    @SafeVarargs
    @Override
    protected final String doInBackground(Pair<Context, String>... params) {
        if (myApiService == null) {  // Only do this once
            MyApi.Builder builder = new MyApi.Builder(AndroidHttp.newCompatibleTransport(),
                    new AndroidJsonFactory(), null)
                    // options for running against local devappserver
                    // - 10.0.2.2 is localhost's IP address in Android emulator
                    // - turn off compression when running against local devappserver
                    .setRootUrl("http://" + IP_ADDRESS + ":8080/_ah/api/")
                    .setGoogleClientRequestInitializer(new GoogleClientRequestInitializer() {
                        @Override
                        public void initialize(AbstractGoogleClientRequest<?> abstractGoogleClientRequest) throws IOException {
                            abstractGoogleClientRequest.setDisableGZipContent(true);
                        }
                    });
            // end options for devappserver

            myApiService = builder.build();
        }

        context = params[0].first;
        String name = params[0].second;

        try {
            return myApiService.sayHi(name).execute().getData();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progress.setMessage(context.getString(R.string.progress_text));
        progress.show();
    }

    @Override
    protected void onPostExecute(String result) {
        if (progress.isShowing()) {
            progress.dismiss();
        }
        Intent intent = new Intent(context, JokeShowActivity.class);
        intent.putExtra(BundleConfig.JOKE, result);
        context.startActivity(intent);
    }
}
