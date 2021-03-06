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

package me.drall.popularmovie2.ui.screens.detail;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.drall.popularmovie2.PopularMovieComponent;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.ui.base.BaseActivity;

public class DetailActivity extends BaseActivity {

    @BindView(R.id.detail_activity_frame) FrameLayout frameLayout;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_detail);
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(getSupportActionBar()!=null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportFragmentManager().beginTransaction()
            .replace(R.id.detail_activity_frame, DetailFragment.newInstance(),"detail_fragment")
            .commit();
    }

    @Override
    protected void onCreateComponent(PopularMovieComponent component) {
    }

    @Override public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

}
