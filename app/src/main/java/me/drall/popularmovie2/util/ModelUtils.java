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

package me.drall.popularmovie2.util;

import java.util.ArrayList;
import java.util.List;
import me.drall.popularmovie2.data.api.model.Movie;
import me.drall.popularmovie2.data.api.model.response.popular.ResultsItem;

public class ModelUtils {

    public static List<Movie> convertPopularMovies(List<ResultsItem> popularList) {
        List<Movie> list = new ArrayList<>();
        for(ResultsItem item:popularList) {
            list.add(Movie.create(
                item.overview(),item.originalTitle(),item.posterPath(),item.backdropPath(),
                item.releaseDate(),item.popularity(),item.id(),item.adult(),item.title(),item.voteAverage()
            ));
        }

        return list;
    }

    public static List<Movie> convertTopMovies(List<me.drall.popularmovie2.data.api.model.response.top_rated.ResultsItem> popularList) {
        List<Movie> list = new ArrayList<>();
        for(me.drall.popularmovie2.data.api.model.response.top_rated.ResultsItem item:popularList) {
            list.add(Movie.create(
                item.overview(),item.originalTitle(),item.posterPath(),item.backdropPath(),
                item.releaseDate(),item.popularity(),item.id(),item.adult(),item.title(), item.voteAverage()
            ));
        }

        return list;
    }
}
