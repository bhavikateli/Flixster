package com.bhavikateli.flixster.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bhavikateli.flixster.MovieDetailsActivity;
import com.bhavikateli.flixster.R;
import com.bhavikateli.flixster.models.Movie;
import com.bumptech.glide.Glide;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    Context context;
    List<Movie> movies;

    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    @NonNull
    @Override

    //inflates a layout from XML and returning the holder
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //populating the data into the item through the holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        //get the movie at the passed in position
        Movie movie = movies.get(position);
        //bind movie data into the viewholder
        holder.bind(movie);
    }

    //returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            itemView.setOnClickListener(this);
        }

        public void bind(Movie movie) {
            tvTitle.setText((movie.getTitle()));
            tvOverview.setText(movie.getOverview());

            //if phone in landscape
            String imageURL;
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                imageURL = movie.getBackdropPath();
                //Glide.with(context).load(imageURL).placeholder(R.drawable.landscape).into(ivPoster);
                Glide.with(context).load(imageURL).placeholder(R.drawable.landscape).transform(new RoundedCornersTransformation(30, 10)).into(ivPoster);
            } else {
                imageURL = movie.getPosterPath();
                Glide.with(context).load(imageURL).placeholder(R.drawable.portrait).transform(new RoundedCornersTransformation(30, 10)).into(ivPoster);
            }

        }

        @Override
        public void onClick(View view) {
            //gets the position of the item
            int position = getAdapterPosition();
            //check if its a valid position
            if (position != RecyclerView.NO_POSITION) {
                //grab the movie
                Movie movie = movies.get(position);
                //Create an Intent to display MovieDetailsActivity
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                //Pass the movie as an extra serialized via Parcels.wrap()
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                //show the activity
                context.startActivity(intent);
            }


        }
    }
}
