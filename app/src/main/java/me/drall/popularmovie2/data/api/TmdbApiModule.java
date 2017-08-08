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

import com.google.gson.GsonBuilder;
import dagger.Module;
import dagger.Provides;
import javax.inject.Named;
import me.drall.popularmovie2.AppScope;
import me.drall.popularmovie2.BuildConfig;
import me.drall.popularmovie2.data.api.configuration.ApiKeyInterceptor;
import me.drall.popularmovie2.data.api.configuration.AutoValueGsonFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class TmdbApiModule {

    String baseUrl;

    public TmdbApiModule(String url) {
        this.baseUrl =url;
    }

    @Provides
    @AppScope ApiKeyInterceptor provideApiKeyInterceptor() {
        return new ApiKeyInterceptor(BuildConfig.TMDB_API_TOKEN);
    }

    @Provides
    @AppScope
    HttpLoggingInterceptor providesHttpLoggingInterceptor() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return interceptor;
    }

    @Provides
    @AppScope
    OkHttpClient providesOkHttpClient(ApiKeyInterceptor apiKeyInterceptor, HttpLoggingInterceptor loggingInterceptor) {
        OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
        httpClientBuilder.addInterceptor(loggingInterceptor);
        httpClientBuilder.addInterceptor(apiKeyInterceptor);
        return httpClientBuilder.build();
    }

    @Provides
    @AppScope
    GsonConverterFactory provideGsonConvertorFactory() {
        return GsonConverterFactory.create(new GsonBuilder()
                .registerTypeAdapterFactory(AutoValueGsonFactory.create())
                .create());
    }

    @Provides
    @AppScope
    @Named("callAdapterFactory")
    CallAdapter.Factory providesRxJava2CallAdapterFactory() {
        return RxJava2CallAdapterFactory.create();
    }

    @Provides
    @AppScope
    Retrofit provideRetrofit(OkHttpClient httpClient, GsonConverterFactory gsonConverterFactory, @Named("callAdapterFactory")CallAdapter.Factory factory) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addCallAdapterFactory(factory)
                .addConverterFactory(gsonConverterFactory)
                .client(httpClient)
                .build();
    }

    @Provides
    @AppScope
    TmdbService provideTmdbService(Retrofit retrofit) {
        return retrofit.create(TmdbService.class);
    }
}
