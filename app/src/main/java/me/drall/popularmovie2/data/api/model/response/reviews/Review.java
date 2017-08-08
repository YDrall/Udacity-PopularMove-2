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

package me.drall.popularmovie2.data.api.model.response.reviews;

import com.google.auto.value.AutoValue;
import com.google.gson.TypeAdapter;

import com.google.gson.annotations.SerializedName;
import com.google.gson.Gson;


@AutoValue
public abstract class Review {

	@SerializedName("author")
	public abstract String author();

	@SerializedName("id")
	public abstract String id();

	@SerializedName("content")
	public abstract String content();

	@SerializedName("url")
	public abstract String url();

	public static TypeAdapter<Review> typeAdapter(Gson gson) {
		return new AutoValue_Review.GsonTypeAdapter(gson);
	}
}