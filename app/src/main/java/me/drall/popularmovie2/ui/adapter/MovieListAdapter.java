/*
 *  Copyright (C) 2017 Yogesh Drall
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and limitations under the License.
 *
 */

package me.drall.popularmovie2.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import me.drall.popularmovie2.GlideApp;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.data.api.model.Movie;

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<Movie> moviesList;
    private Context context;
    private Set<Integer> favoriteSet;

    public MovieListAdapter(@Nullable List<Movie> list, Context context) {
        if(list == null)
            this.moviesList = new ArrayList<>();
        else
            this.moviesList = list;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_movies_list,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GlideApp.with(context)
                .load("http://image.tmdb.org/t/p/w300"+moviesList.get(position).posterPath())
                .centerCrop()
                .into(holder.imageView);
        holder.textView.setText(moviesList.get(position).title());
        if(favoriteSet!=null) {
            if(favoriteSet.contains(moviesList.get(position).id())) {
                holder.favIcon.setImageResource(R.drawable.ic_favorite_red);
            }
            else {
                holder.favIcon.setImageResource(R.drawable.ic_favorite_border_red);
            }
        }
    }

    @Override
    public int getItemCount() {
        return moviesList==null ? 0:moviesList.size();
    }

    public void setMoviesList(@NonNull List<Movie> list) {
        this.moviesList = list;
        notifyDataSetChanged();
    }

    public void setFavorites(@Nullable Set<Integer> favouriteSet) {
        this.favoriteSet = favouriteSet;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.poster) ImageView imageView;

        @BindView(R.id.title) TextView textView;

        @BindView(R.id.favourite) ImageView favIcon;

        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

}
