package com.bhavikateli.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bhavikateli.flixster.models.Movie;
import com.bhavikateli.flixster.models.MovieTrailerActivity;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcels;

import okhttp3.Headers;

public class MovieDetailsActivity extends AppCompatActivity {

    Movie movie;

    //declare views
    TextView tvTitle;
    TextView tvOverview;
    RatingBar rbVoteAverage;
    Button btnTrailer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvOverview = (TextView) findViewById(R.id.tvOverview);
        rbVoteAverage = (RatingBar) findViewById(R.id.rbVoteAverage);
        btnTrailer = findViewById(R.id.btnTrailer);

        //unwrap the movie passed through intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the text for overview and title
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //movie trailer
        String url =  "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=b2fcbccf054a49b37b941d24b54403d2&language=en-US";
        //https://api.themoviedb.org/3/movie/554993/videos?api_key=b2fcbccf054a49b37b941d24b54403d2&language=en-US
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url , new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;

                try {
                     final String youtubeKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");

                    btnTrailer.setOnClickListener((new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);
                            intent.putExtra("youtubeKey", youtubeKey);
                            startActivity(intent);
                        }
                    }));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("MovieDetailsActivity", "onFailure: this isn't working", throwable);
            }
        });

    }

}