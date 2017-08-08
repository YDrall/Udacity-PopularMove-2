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

package me.drall.popularmovie2.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.data.api.model.response.trailor.Trailer;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> trailers;

    public TrailerAdapter(List<Trailer> trailers) {
        this.trailers = trailers;
    }

    @Override public TrailerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_trailer,parent,false));
    }

    @Override public void onBindViewHolder(TrailerAdapter.ViewHolder holder, int position) {
        int trailerCount = position+1;
        holder.trailerButton.setText("Watch Trailer #"+trailerCount+"");
    }

    @Override public int getItemCount() {
        return trailers==null? 0:trailers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.watch_trailer_button) Button trailerButton;
        ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
