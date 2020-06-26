package com.bhavikateli.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bhavikateli.flixster.databinding.ActivityMovieDetailsBinding;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //ViewBinding
        final ActivityMovieDetailsBinding binding = ActivityMovieDetailsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //unwrap the movie passed through intent, using its simple name as a key
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra(Movie.class.getSimpleName()));
        Log.d("MovieDetailsActivity", String.format("Showing details for '%s'", movie.getTitle()));

        // set the text for overview and title
        binding.tvTitle.setText(movie.getTitle());
        binding.tvOverview.setText(movie.getOverview());

        float voteAverage = movie.getVoteAverage().floatValue();
        binding.rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        //movie trailer
        String url = "https://api.themoviedb.org/3/movie/" + movie.getId() + "/videos?api_key=b2fcbccf054a49b37b941d24b54403d2&language=en-US";
        //https://api.themoviedb.org/3/movie/554993/videos?api_key=b2fcbccf054a49b37b941d24b54403d2&language=en-US
        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                JSONObject jsonObject = json.jsonObject;

                try {
                    final String youtubeKey = jsonObject.getJSONArray("results").getJSONObject(0).getString("key");

                    binding.btnTrailer.setOnClickListener((new View.OnClickListener() {
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