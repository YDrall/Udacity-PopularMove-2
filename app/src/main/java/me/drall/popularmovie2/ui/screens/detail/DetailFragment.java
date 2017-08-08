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

package me.drall.popularmovie2.ui.screens.detail;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.List;
import java.util.Locale;
import javax.inject.Inject;
import me.drall.popularmovie2.GlideApp;
import me.drall.popularmovie2.PopularMovieApp;
import me.drall.popularmovie2.PopularMovieComponent;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.data.api.TmdbService;
import me.drall.popularmovie2.data.api.model.Movie;
import me.drall.popularmovie2.data.api.model.response.reviews.Review;
import me.drall.popularmovie2.data.api.model.response.reviews.ReviewsResponse;
import me.drall.popularmovie2.data.api.model.response.trailor.Trailer;
import me.drall.popularmovie2.data.api.model.response.trailor.TrailerResponse;
import me.drall.popularmovie2.data.storage.Database;
import me.drall.popularmovie2.ui.adapter.ReviewsAdapter;
import me.drall.popularmovie2.ui.adapter.TrailerAdapter;
import me.drall.popularmovie2.ui.base.BaseFragment;
import me.drall.popularmovie2.ui.listener.RecyclerItemClickListener;
import me.drall.popularmovie2.ui.screens.home.HomeFragment;
import timber.log.Timber;

public class DetailFragment extends BaseFragment {

    @Inject Database database;

    @Inject TmdbService tmdbService;

    @BindView(R.id.fragment_detail_textView_title) TextView titleView;

    @BindView(R.id.fragment_detail_textView_description) TextView descriptionView;

    @BindView(R.id.fragment_detail_imageView_posterImage) ImageView posterView;

    @BindView(R.id.trailerList) RecyclerView trailerListView;

    @BindView(R.id.fragment_detail_textView_releaseDate) TextView releaseDateView;

    @BindView(R.id.fragment_detail_textView_voteAverage) TextView voteAverageView;

    @BindView(R.id.favourite) ImageView favoriteIcon;

    @BindView(R.id.reviewsList) RecyclerView reviewList;

    private Movie movie;

    private List<Trailer> trailers;

    private String trailerPath ="";

    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    @Override
    protected void onCreateComponent(PopularMovieComponent component) {
        PopularMovieApp.getComponent().inject(this);
    }

    @Override
    protected void onBindView(View source) {
        ButterKnife.bind(this,source);
    }

    @Override
    protected View onSetContentView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detail,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        movie = getActivity().getIntent().getParcelableExtra(HomeFragment.MOVIE_EXTRA);

        showMovieTitle(movie.title());
        showMovieDescription(movie.overview());
        showMoviePoster("http://image.tmdb.org/t/p/w300"+movie.posterPath());
        showMovieReleaseDate(movie.releaseDate());
        showMovieVotingAverage(movie.voteAverage());
        showFavorite();

        tmdbService.getReviews(movie.id())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<ReviewsResponse>() {
                @Override
                public void accept(@NonNull ReviewsResponse response) throws Exception {
                    showComments(response.results());
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    Timber.d(throwable);
                }
            });

        tmdbService.getTrailer(movie.id())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<TrailerResponse>() {
                @Override public void accept(@NonNull TrailerResponse trailerResponse)
                    throws Exception {
                    showMoviePreviewVideo(trailerResponse.results());
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(@NonNull Throwable throwable) throws Exception {
                    Timber.d(throwable);
                }
            });

        favoriteIcon.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onFavoriteIconCliked();
            }
        });



    }

    private void showTrailer(String trailerPath) {
        String youtubePath = "https://www.youtube.com/watch?v="+trailerPath;
        Intent intent = new Intent(Intent.ACTION_VIEW,
            Uri.parse(youtubePath));
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }


    public void showMovieTitle(String title) {
        titleView.setText(title);
    }


    public void showMovieDescription(String description) {
        descriptionView.setText(description);
    }


    public void showMoviePoster(String path) {
        GlideApp.with(this)
                .load(path)
                .centerCrop()
                .into(posterView);
    }


    public void showMoviePreviewVideo(final List<Trailer> trailers) {
        trailerListView.setLayoutManager(new LinearLayoutManager(getContext()));
        trailerListView.setAdapter(new TrailerAdapter(trailers));
        trailerListView.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override public void onItemClick(View view, int position) {
                switch (view.getId()) {
                    case R.id.watch_trailer_button:
                        if(trailers!=null) {
                            showTrailer(trailers.get(position).key());
                        }
                }
            }
        }));
    }

    public void onFavoriteIconCliked() {
        if(!database.isFavorite(movie.id())) {
            database.addToFavorite(movie, new Database.DbOperationCallback() {
                @Override public void onSuccess() {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_red);
                }

                @Override public void onError(String error) {

                }
            });
        }
        else {
            database.removeFromFavorite(movie.id(), new Database.DbOperationCallback() {
                @Override public void onSuccess() {
                    favoriteIcon.setImageResource(R.drawable.ic_favorite_border_red);
                }

                @Override public void onError(String error) {

                }
            });
        }
    }

    public void showFavorite() {
        if(database.isFavorite(movie.id())) {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_red);
        }
        else {
            favoriteIcon.setImageResource(R.drawable.ic_favorite_border_red);
        }
    }

    public void showComments(List<Review> reviews) {
        reviewList.setLayoutManager(new LinearLayoutManager(getContext()));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(),DividerItemDecoration.VERTICAL);
        dividerItemDecoration.setDrawable(ContextCompat.getDrawable(getContext(),R.drawable.divider_line));
        reviewList.addItemDecoration(dividerItemDecoration);
        reviewList.setAdapter(new ReviewsAdapter(reviews,getContext()));
    }
    public void showMovieReleaseDate(String date) {
        releaseDateView.setText(
            String.format(
                Locale.getDefault(),
                "%s: %s",getContext().getString(R.string.release_date),date));
    }

    public void showMovieVotingAverage(double average) {
        voteAverageView.setText(
            String.format(
                Locale.getDefault(),
                "%s: %s",getContext().getString(R.string.voting_Average),String.valueOf(average)));
    }

}
