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

package me.drall.popularmovie2.data.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import me.drall.popularmovie2.data.api.model.Movie;

public class Database {

    Context mContext;

    public Database(Context context) {
        this.mContext = context;
    }

    public boolean isFavorite(int movieId) {
        String[] columns = { MovieContract.FavoriteEntry.MOVIE_ID};
        String selections = MovieContract.FavoriteEntry.MOVIE_ID + " = ?";
        String[] selectArgs = {String.valueOf(movieId)};
        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();

        try {
            Cursor cursor =
                mContext.getContentResolver().query(uri, columns, selections, selectArgs, null);
            boolean result = cursor != null && cursor.moveToFirst();
            if (cursor != null) {
                cursor.close();
            }
            return result;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void addToFavorite(Movie movie, @Nullable DbOperationCallback callback) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(MovieContract.FavoriteEntry.MOVIE_ID,movie.id());
        contentValues.put(MovieContract.FavoriteEntry.TITLE,movie.title());
        contentValues.put(MovieContract.FavoriteEntry.ORIGINAL_TITLE,movie.originalTitle());
        contentValues.put(MovieContract.FavoriteEntry.BACKDROP_PATH,movie.backdropPath());
        contentValues.put(MovieContract.FavoriteEntry.POSTER_PATH,movie.posterPath());
        contentValues.put(MovieContract.FavoriteEntry.POPULARITY,movie.popularity());
        contentValues.put(MovieContract.FavoriteEntry.ADULT,movie.adult());
        contentValues.put(MovieContract.FavoriteEntry.RELEASE_DATE, movie.releaseDate());
        contentValues.put(MovieContract.FavoriteEntry.VOTE_AVERAGE, movie.voteAverage());
        contentValues.put(MovieContract.FavoriteEntry.OVERVIEW,movie.overview());

        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI;
        Uri resultUri = mContext.getContentResolver().insert(uri,contentValues);

        if(callback!=null) {
            if (resultUri == null) {
                callback.onError("Operation Failed: Uri is null");
            } else {
                callback.onSuccess();
            }
        }
    }

    public void removeFromFavorite(int movieId, @Nullable DbOperationCallback callback) {
        String selections = MovieContract.FavoriteEntry.MOVIE_ID + " = ?";
        String[] selectArgs = {String.valueOf(movieId)};
        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(Integer.toString(movieId)).build();

        int deleteId=0;
        try {
            deleteId = mContext.getContentResolver().delete(uri, selections, selectArgs);
        }
        catch (Exception e) {
            e.printStackTrace();
            if(callback!=null) {
                callback.onError("An exception occurred");
            }
        }
        if(callback!=null) {
            if(deleteId>0) {
                callback.onSuccess();
            }
            else {
                callback.onError("delete id is less than or equal to 0.");
            }
        }
    }

    public List<Movie> getFavoriteMovies() {
        //String[] columns = { MovieContract.FavoriteEntry.MOVIE_ID,
        //    MovieContract.FavoriteEntry.TITLE,
        //    MovieContract.FavoriteEntry.ORIGINAL_TITLE,
        //    MovieContract.FavoriteEntry.POSTER_PATH,
        //    MovieContract.FavoriteEntry.BACKDROP_PATH,
        //    MovieContract.FavoriteEntry.OVERVIEW,
        //    MovieContract.FavoriteEntry.ADULT,
        //    MovieContract.FavoriteEntry.VOTE_AVERAGE,
        //    MovieContract.FavoriteEntry.RELEASE_DATE,
        //    MovieContract.FavoriteEntry.POSTER_PATH,
        //};
        Uri uri = MovieContract.FavoriteEntry.CONTENT_URI;

        List<Movie> movieList = new ArrayList<>();
        Cursor cursor=null;
        try {
            cursor = mContext.getContentResolver().query(uri, null, null,null,null);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        if(cursor!=null && cursor.moveToFirst()) {
            do {
                int title = cursor.getColumnIndex(MovieContract.FavoriteEntry.TITLE);
                int originalTitle = cursor.getColumnIndex(MovieContract.FavoriteEntry.ORIGINAL_TITLE);
                int posterPath = cursor.getColumnIndex(MovieContract.FavoriteEntry.POSTER_PATH);
                int backdropPath = cursor.getColumnIndex(MovieContract.FavoriteEntry.BACKDROP_PATH);
                int overview = cursor.getColumnIndex(MovieContract.FavoriteEntry.OVERVIEW);
                int adult = cursor.getColumnIndex(MovieContract.FavoriteEntry.ADULT);
                int voteAverage = cursor.getColumnIndex(MovieContract.FavoriteEntry.VOTE_AVERAGE);
                int releaseDate = cursor.getColumnIndex(MovieContract.FavoriteEntry.RELEASE_DATE);
                int popularity = cursor.getColumnIndex(MovieContract.FavoriteEntry.POPULARITY);
                int movieId= cursor.getColumnIndex(MovieContract.FavoriteEntry.MOVIE_ID);

                movieList.add(Movie.create(
                    cursor.getString(overview),
                    cursor.getString(originalTitle),
                    cursor.getString(posterPath),
                    cursor.getString(backdropPath),
                    cursor.getString(releaseDate),
                    cursor.getFloat(popularity),
                    cursor.getInt(movieId),
                    cursor.getInt(adult) == 0,
                    cursor.getString(title),
                    cursor.getDouble(voteAverage)
                ));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return movieList;
    }

    public interface DbOperationCallback {
        void onSuccess();
        void onError(String error);
    }

}
