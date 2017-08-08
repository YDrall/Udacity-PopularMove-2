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

package me.drall.popularmovie2.data.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import me.drall.popularmovie2.data.storage.MovieContract;
import me.drall.popularmovie2.data.storage.MovieDbHelper;

public class FavoriteContentProvider extends ContentProvider {

    public static final int FAVORITE = 100;
    public static final int FAVORITE_WITH_ID = 101;
    public static final UriMatcher mUriMatcher = buildUriMatcher();

    public static UriMatcher buildUriMatcher() {
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASK,FAVORITE);
        uriMatcher.addURI(MovieContract.AUTHORITY,MovieContract.PATH_TASK + "/#",FAVORITE_WITH_ID);
        return uriMatcher;
    }

    private MovieDbHelper dbHelper;

    @Override public boolean onCreate() {
        dbHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Nullable @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor resultCursor;
        String[] columns = { MovieContract.FavoriteEntry.MOVIE_ID};
        switch(mUriMatcher.match(uri)) {
            case FAVORITE:
                resultCursor = dbHelper.getReadableDatabase().query(
                    MovieContract.FavoriteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            case FAVORITE_WITH_ID:
                resultCursor = dbHelper.getReadableDatabase().query(
                    MovieContract.FavoriteEntry.TABLE_NAME,
                    projection,
                    selection,
                    selectionArgs,
                    null,
                    null,
                    sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unsupported Action! uri: "+ uri);
        }
        resultCursor.setNotificationUri(getContext().getContentResolver(),uri);

        return resultCursor;
    }

    @Nullable @Override public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }

    @Nullable @Override public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {

        Uri resultUri;

        switch (mUriMatcher.match(uri)) {
            case FAVORITE:
                long id = dbHelper.getWritableDatabase().insert(MovieContract.FavoriteEntry.TABLE_NAME,null,values);
                if(id>0) {
                    resultUri = ContentUris.withAppendedId(MovieContract.BASE_CONTENT_URI,id);
                }
                else {
                    throw new SQLException("Failed to insert a row into "+uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Action!");
        }

        getContext().getContentResolver().notifyChange(uri,null);

        return resultUri;
    }

    @Override public int delete(@NonNull Uri uri, @Nullable String selection,
        @Nullable String[] selectionArgs) {

        int deleteId;
        switch (mUriMatcher.match(uri)) {
            case FAVORITE_WITH_ID:
                String id = uri.getPathSegments().get(1);
                deleteId = dbHelper.getWritableDatabase().delete(MovieContract.FavoriteEntry.TABLE_NAME,
                    ""+MovieContract.FavoriteEntry.MOVIE_ID+"=?",new String[]{id});
                break;
            default:
                throw new UnsupportedOperationException("Unknown Action");
        }

        if(deleteId>0) {
            getContext().getContentResolver().notifyChange(uri,null);
        }

        return deleteId;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection,
        @Nullable String[] selectionArgs) {
        throw new UnsupportedOperationException("Not yet implemented!");
    }
}
