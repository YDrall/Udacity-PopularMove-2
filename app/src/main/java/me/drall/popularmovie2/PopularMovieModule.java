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
import android.content.Context;
import dagger.Module;
import dagger.Provides;

@Module
public class PopularMovieModule {
    PopularMovieApp mApplication;

    public PopularMovieModule(PopularMovieApp application) {
        this.mApplication = application;
    }

    @Provides
    @AppScope
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @AppScope
    Context providesContext() {
        return mApplication.getApplicationContext();
    }

}
