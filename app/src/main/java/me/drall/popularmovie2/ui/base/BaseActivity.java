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

package me.drall.popularmovie2.ui.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import me.drall.popularmovie2.PopularMovieApp;
import me.drall.popularmovie2.PopularMovieComponent;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        onSetContentView(savedInstanceState);
        onBindView(savedInstanceState);
        onCreateComponent(PopularMovieApp.getComponent());

    }

    protected abstract void onCreateComponent(PopularMovieComponent component);
    protected  abstract void onBindView(Bundle savedInstanceState);
    protected abstract void onSetContentView(Bundle savedInstanceState);
}
