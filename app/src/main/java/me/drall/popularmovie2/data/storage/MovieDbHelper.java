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

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movie.db";
    private static int version =1;

    public MovieDbHelper(Context context) {
        super(context,DB_NAME,null,version);
    }

    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
        int version) {
        super(context, name, factory, version);
    }

    public MovieDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
        int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+ MovieContract.FavoriteEntry.TABLE_NAME + " ( " +
            MovieContract.FavoriteEntry.MOVIE_ID + " integer primary key, " +
            MovieContract.FavoriteEntry.TITLE + " text, " +
            MovieContract.FavoriteEntry.ORIGINAL_TITLE + " text, " +
            MovieContract.FavoriteEntry.BACKDROP_PATH + " text, " +
            MovieContract.FavoriteEntry.POSTER_PATH + " text, " +
            MovieContract.FavoriteEntry.RELEASE_DATE + " text, " +
            MovieContract.FavoriteEntry.OVERVIEW + " text, " +
            MovieContract.FavoriteEntry.POPULARITY + " text, " +
            MovieContract.FavoriteEntry.ADULT + " integer, " +
            MovieContract.FavoriteEntry.VOTE_AVERAGE + " real );"
        );
    }

    @Override public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


}
