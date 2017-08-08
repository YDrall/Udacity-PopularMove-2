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

package me.drall.popularmovie2;

import android.app.Application;
import dagger.Component;
import me.drall.popularmovie2.data.api.TmdbApiModule;
import me.drall.popularmovie2.data.api.TmdbService;
import me.drall.popularmovie2.data.storage.Database;
import me.drall.popularmovie2.data.storage.DatabaseModule;
import me.drall.popularmovie2.ui.screens.detail.DetailFragment;
import me.drall.popularmovie2.ui.screens.home.HomeFragment;

@AppScope
@Component(modules = {PopularMovieModule.class, TmdbApiModule.class, DatabaseModule.class} )
public interface PopularMovieComponent {
    TmdbService tmdbService();
    Application application();
    Database database();
    void inject(HomeFragment fragment);
    void inject(DetailFragment fragment);
}
