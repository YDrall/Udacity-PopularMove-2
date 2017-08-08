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

package me.drall.popularmovie2.data.api;


import io.reactivex.Observable;
import me.drall.popularmovie2.data.api.model.response.reviews.ReviewsResponse;
import me.drall.popularmovie2.data.api.model.response.trailor.TrailerResponse;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface TmdbService {

    @GET("movie/popular")
    Observable<me.drall.popularmovie2.data.api.model.response.popular.Response> getPopularMovies();

    @GET("movie/top_rated")
    Observable<me.drall.popularmovie2.data.api.model.response.top_rated.Response> getTopRatedMovies();

    @GET("movie/{id}/reviews")
    Observable<ReviewsResponse> getReviews(@Path("id") int id);

    @GET("movie/{id}/videos")
    Observable<TrailerResponse> getTrailer(@Path("id") int id);
}
