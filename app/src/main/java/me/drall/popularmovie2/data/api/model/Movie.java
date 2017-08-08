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

package me.drall.popularmovie2.data.api.model;

import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;

@AutoValue
public abstract class Movie implements Parcelable{

    public abstract String overview();

    public abstract String originalTitle();

    public abstract String title();

    @Nullable
    public abstract String posterPath();

    @Nullable
    public abstract String backdropPath();

    public abstract String releaseDate();

    public abstract double popularity();

    public abstract int id();

    public abstract boolean adult();

    public abstract double voteAverage();

    public static Movie create(String overview,String originalTitle, String posterPath, String backdropPath,
        String releaseDate, double popularity, int id, boolean adult, String title, double voteAverage) {
        return new AutoValue_Movie(overview,originalTitle,title,posterPath,backdropPath,releaseDate,popularity,id,adult,voteAverage);
    }

}
