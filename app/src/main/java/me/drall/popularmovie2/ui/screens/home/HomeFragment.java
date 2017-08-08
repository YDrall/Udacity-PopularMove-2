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

package me.drall.popularmovie2.ui.screens.home;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.inject.Inject;
import me.drall.popularmovie2.PopularMovieApp;
import me.drall.popularmovie2.PopularMovieComponent;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.data.api.TmdbService;
import me.drall.popularmovie2.data.api.model.Movie;
import me.drall.popularmovie2.data.api.model.response.popular.Response;
import me.drall.popularmovie2.data.storage.Database;
import me.drall.popularmovie2.ui.adapter.MovieListAdapter;
import me.drall.popularmovie2.ui.base.BaseFragment;
import me.drall.popularmovie2.ui.listener.RecyclerItemClickListener;
import me.drall.popularmovie2.ui.screens.detail.DetailActivity;
import me.drall.popularmovie2.util.ModelUtils;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import timber.log.Timber;

public class HomeFragment extends BaseFragment implements LoaderManager.LoaderCallbacks<List<Movie>> {

    public static final String MOVIE_EXTRA = "MOVIE_EXTRA";
    public static final String CHOICE_STATE_EXTRA = "CHOICE_STATE_EXTRA";
    public static final String MOVIE_LIST_STATE_EXTRA = "MOVIE_LIST_STATE_EXTRA";
    public static final String MOVIE_LIST_ITEMS_EXTRA = "MOVIE_LIST_ITEMS_EXTRA";

    public final int MOVIE_LOADER_ID = 100;

    @Inject
    TmdbService api;

    @Inject Database database;

    private List<Movie> movies;
    private String[] choices = {"Popularity","Top Rated","Favorite"};
    private final int CHOICE_POPULARITY = 0;
    private final int CHOICE_TOP_RATED = 1;
    private final int CHOICE_FAVORITE = 2;
    private int selectedChoice;
    private Parcelable movieListState;


    @BindView(R.id.fragment_home_recyclerView_movies)
    RecyclerView moviesList;

    @BindView(R.id.fragment_home_swipeRefresh_movies)
    SwipeRefreshLayout swipeRefreshLayout;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    public HomeFragment() {
    }



    public void showPopularMovies(List<Movie> movies) {
        this.movies = movies;
        ((MovieListAdapter)moviesList.getAdapter()).setMoviesList(movies);
    }


    public void showTopRatedMovies(List<Movie> movies) {
        this.movies = movies;
        ((MovieListAdapter)moviesList.getAdapter()).setMoviesList(movies);
    }

    public void showFavoriteMovie(List<Movie> movies) {
        this.movies = movies;
        ((MovieListAdapter)moviesList.getAdapter()).setMoviesList(movies);
    }

    public void showSortView(String[] choices, int currentSelection) {
        final AlertDialog dialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        dialogBuilder.setTitle(getContext().getString(R.string.filter_by));
        dialogBuilder.setSingleChoiceItems(choices, currentSelection, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                onSortItemSelected(which);
                dialog.dismiss();
            }
        });

        dialog = dialogBuilder.create();
        dialog.show();
    }


    public void showProgressView() {
        swipeRefreshLayout.setRefreshing(true);
    }


    public void hideProgressView() {
        swipeRefreshLayout.setRefreshing(false);
    }


    public void showErrorView(String error) {
        //Snackbar.make(moviesList,error,Snackbar.LENGTH_SHORT);
        Toast.makeText(getActivity().getApplicationContext(),error,Toast.LENGTH_SHORT).show();
    }


    public void showMovieDetail(Movie movie) {
        Intent intent = new Intent(getContext(), DetailActivity.class);
        intent.putExtra(MOVIE_EXTRA,movie);
        startActivity(intent);
    }

    @Override public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState!=null) {
            selectedChoice = savedInstanceState.getInt(CHOICE_STATE_EXTRA);
            movieListState = savedInstanceState.getParcelable(MOVIE_LIST_STATE_EXTRA);
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_ITEMS_EXTRA);
        }
    }

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        return inflater.inflate(R.layout.fragment_home,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,HomeFragment.this);
                onListRefresh();
            }
        });
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getContext(),3, LinearLayoutManager.VERTICAL,false);
        moviesList.setLayoutManager(layoutManager);
        moviesList.setAdapter(new MovieListAdapter(null,getContext()));
        if(savedInstanceState==null) {
            selectedChoice = CHOICE_POPULARITY;
            onListRefresh();
        }
        else {
            selectedChoice = savedInstanceState.getInt(CHOICE_STATE_EXTRA,CHOICE_POPULARITY);
            movieListState = savedInstanceState.getParcelable(MOVIE_LIST_STATE_EXTRA);
            movies = savedInstanceState.getParcelableArrayList(MOVIE_LIST_ITEMS_EXTRA);
            if(movies!=null)
                ((MovieListAdapter)moviesList.getAdapter()).setMoviesList(movies);
            else {
                // refresh dirty list.
                onListRefresh();
            }
            if(movieListState!=null)
                 moviesList.getLayoutManager().onRestoreInstanceState(movieListState);
        }

        moviesList.addOnItemTouchListener(new RecyclerItemClickListener(getContext(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
                switch (view.getId()) {
                    case R.id.favourite:
                        if(!database.isFavorite(movies.get(position).id())) {
                            database.addToFavorite(movies.get(position), new Database.DbOperationCallback() {
                                @Override public void onSuccess() {
                                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_red);
                                }

                                @Override public void onError(String error) {

                                }
                            });
                        }
                        else {
                            database.removeFromFavorite(movies.get(position).id(), new Database.DbOperationCallback() {
                                @Override public void onSuccess() {
                                    ((ImageView) view).setImageResource(R.drawable.ic_favorite_border_red);
                                }
                                @Override public void onError(String error) {

                                }
                            });
                        }
                        onListRefresh();
                        break;
                    case R.id.poster:
                    case R.id.title:
                    case R.id.cover:
                    case R.id.item_movieList_parent:
                        onListItemClicked(position);
                        break;
                    default:
                        break;
                }
            }
        }));

        setHasOptionsMenu(true);

        getActivity().getSupportLoaderManager().initLoader(MOVIE_LOADER_ID,null,this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_movie_list,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_movieList_sortBy:
                onSortMenuItemClicked();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    @Override public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(CHOICE_STATE_EXTRA,selectedChoice);
        outState.putParcelableArrayList(MOVIE_LIST_ITEMS_EXTRA,
            (ArrayList<? extends Parcelable>) movies);
        outState.putParcelable(MOVIE_LIST_STATE_EXTRA,moviesList.getLayoutManager().onSaveInstanceState());
    }

    @Override public void onResume() {
        super.onResume();
        getActivity().getSupportLoaderManager().restartLoader(MOVIE_LOADER_ID,null,this);
    }

    public void loadPopularMovies() {
        showProgressView();
        api.getPopularMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<Response>() {
                @Override
                public void accept(@NonNull Response response) throws Exception {
                    movies = ModelUtils.convertPopularMovies(response.results());
                    showPopularMovies(movies);
                    hideProgressView();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    Timber.d(throwable);
                    showErrorView(throwable.getMessage());
                    hideProgressView();
                }
            });

    }

    public void loadTopRatedMovies() {
        showProgressView();
        api.getTopRatedMovies()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<me.drall.popularmovie2.data.api.model.response.top_rated.Response>() {
                @Override public void accept(@NonNull
                    me.drall.popularmovie2.data.api.model.response.top_rated.Response response)
                    throws Exception {
                    Timber.d(response.toString());
                    movies = ModelUtils.convertTopMovies(response.results());
                    showTopRatedMovies(movies);
                    hideProgressView();
                }
            }, new Consumer<Throwable>() {
                @Override
                public void accept(@NonNull Throwable throwable) throws Exception {
                    Timber.d(throwable);
                    showErrorView(throwable.getMessage());
                    hideProgressView();
                }
            });

    }

    private void loadFavoriteMovies() {
        showProgressView();
        Observable.fromPublisher(new Publisher<List<Movie>>() {
            @Override public void subscribe(Subscriber<? super List<Movie>> s) {
                s.onNext(database.getFavoriteMovies());
                s.onComplete();
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Movie>>() {
                @Override public void accept(@NonNull List<Movie> list) throws Exception {
                    showFavoriteMovie(list);
                    hideProgressView();
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(@NonNull Throwable throwable) throws Exception {
                    showErrorView(throwable.getMessage());
                    hideProgressView();
                }
            });

    }

    private void updateFavoriteSet() {
        Observable.fromPublisher(new Publisher<List<Movie>>() {
            @Override public void subscribe(Subscriber<? super List<Movie>> s) {
                s.onNext(database.getFavoriteMovies());
            }
        })
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<List<Movie>>() {
                @Override public void accept(@NonNull List<Movie> movies) throws Exception {
                    Set<Integer> set = new HashSet<>();
                    for(Movie movie: movies) {
                        set.add(movie.id());
                    }
                    ((MovieListAdapter)moviesList.getAdapter()).setFavorites(set);
                }
            }, new Consumer<Throwable>() {
                @Override public void accept(@NonNull Throwable throwable) throws Exception {
                    showErrorView(throwable.getMessage());
                }
            });
    }


    public void onSortItemSelected(int which) {
        if(selectedChoice==which)
            return;

        selectedChoice = which;
        onListRefresh();
    }


    public void onListRefresh() {
        if(selectedChoice==CHOICE_POPULARITY) {
            loadPopularMovies();
        }
        else if(selectedChoice==CHOICE_TOP_RATED) {
            loadTopRatedMovies();
        }
        else if(selectedChoice==CHOICE_FAVORITE) {
            loadFavoriteMovies();
        }
        updateFavoriteSet();
    }


    public void onListItemClicked(int position) {
        showMovieDetail(movies.get(position));
    }


    public void onSortMenuItemClicked() {
        showSortView(choices,selectedChoice);
    }

    @Override public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Timber.d("loader's onCreateLoader called");

        return new AsyncTaskLoader<List<Movie>>(getContext()) {

            List<Movie> favList=null;

            @Override protected void onStartLoading() {
                if(favList!=null) {
                    deliverResult(favList);
                }
                else {
                    forceLoad();
                }
            }

            public void deliverResult(List<Movie> data) {
                favList = data;
                super.deliverResult(data);
            }

            @Override public List<Movie> loadInBackground() {

                try {
                    return database.getFavoriteMovies();
                }
                catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        };
    }

    @Override public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Timber.d("loader's onLoadFinished called");
        Set<Integer> set = new HashSet<>();
        for(Movie movie: data) {
            set.add(movie.id());
        }
        ((MovieListAdapter)moviesList.getAdapter()).setFavorites(set);
        onListRefresh();
    }

    @Override public void onLoaderReset(Loader<List<Movie>> loader) {
        Timber.d("loader's onLoaderReset called");
        ((MovieListAdapter)moviesList.getAdapter()).setFavorites(null);
    }
}
