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

package me.drall.popularmovie2.data.api.model.response.top_rated;

import android.support.annotation.Nullable;

import com.google.auto.value.AutoValue;
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@AutoValue
public abstract class ResultsItem{

	@SerializedName("overview")
	public abstract String overview();

	@SerializedName("original_language")
	public abstract String originalLanguage();

	@SerializedName("original_title")
	public abstract String originalTitle();

	@SerializedName("video")
	public abstract boolean video();

	@SerializedName("title")
	public abstract String title();

	@SerializedName("genre_ids")
	public abstract List<Integer> genreIds();

	@Nullable
	@SerializedName("poster_path")
	public abstract String posterPath();

	@Nullable
	@SerializedName("backdrop_path")
	public abstract String backdropPath();

	@SerializedName("release_date")
	public abstract String releaseDate();

	@SerializedName("popularity")
	public abstract double popularity();

	@SerializedName("vote_average")
	public abstract double voteAverage();

	@SerializedName("id")
	public abstract int id();

	@SerializedName("adult")
	public abstract boolean adult();

	@SerializedName("vote_count")
	public abstract int voteCount();

	public static TypeAdapter<ResultsItem> typeAdapter(Gson gson) {
		return new AutoValue_ResultsItem.GsonTypeAdapter(gson);
	}
}