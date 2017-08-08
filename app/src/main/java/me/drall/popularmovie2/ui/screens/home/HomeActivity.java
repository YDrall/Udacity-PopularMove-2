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

package me.drall.popularmovie2.ui.screens.home;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.drall.popularmovie2.PopularMovieComponent;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.ui.base.BaseActivity;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.home_frag)
    FrameLayout frameLayout;

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private static final String HOME_FRAGMENT_TAG= "HOME_FRAGMENT_TAG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onSetContentView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_home);
    }

    @Override
    protected void onBindView(Bundle savedInstanceState) {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if(savedInstanceState==null) {
            getSupportFragmentManager().beginTransaction()
                .add(R.id.home_frag, HomeFragment.newInstance(), HOME_FRAGMENT_TAG)
                .commit();
        }
        /*else {
            getSupportFragmentManager().findFragmentByTag(HOME_FRAGMENT_TAG);
        }*/
    }

    @Override
    protected void onCreateComponent(PopularMovieComponent component) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}
