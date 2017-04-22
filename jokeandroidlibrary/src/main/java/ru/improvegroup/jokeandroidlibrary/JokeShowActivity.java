package ru.improvegroup.jokeandroidlibrary;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Diana.Raspopova on 4/22/2017.
 */

public class JokeShowActivity extends AppCompatActivity {

    String joke;
    TextView jokeText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke);

        jokeText = (TextView) findViewById(R.id.jokeText);
        joke = getIntent().getExtras().getString(BundleConfig.JOKE, getString(R.string.default_joke));

        jokeText.setText(joke);
    }
}
