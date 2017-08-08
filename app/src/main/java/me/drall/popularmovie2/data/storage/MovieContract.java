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

import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String AUTHORITY = "me.drall.popularmovie2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    public static final String PATH_TASK = "favorite";

    public static final class FavoriteEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
            BASE_CONTENT_URI.buildUpon().appendPath(PATH_TASK).build();

        public static String TABLE_NAME = "FAVORITES";
        public static String MOVIE_ID = "MOVIE_ID";
        public static String OVERVIEW = "OVERVIEW";
        public static String ORIGINAL_TITLE = "ORIGINAL_TITLE";
        public static String TITLE = "TITLE";
        public static String BACKDROP_PATH = "BACKDROP_PATH";
        public static String POSTER_PATH = "POSTER_PATH";
        public static String RELEASE_DATE = "RELEASE_DATE";
        public static String POPULARITY = "POPULARITY";
        public static String ADULT = "ADULT";
        public static String VOTE_AVERAGE = "VOTE_AVERAGE";
    }

}
