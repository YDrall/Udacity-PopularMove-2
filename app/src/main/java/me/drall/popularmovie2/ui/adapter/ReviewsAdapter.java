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

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import java.util.List;
import me.drall.popularmovie2.R;
import me.drall.popularmovie2.data.api.model.response.reviews.Review;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder> {

    private List<Review> reviewList;
    private Context context;

    public ReviewsAdapter(@Nullable List<Review> reviewList,Context context) {
        if(reviewList==null)
            this.reviewList = null;
        else
            this.reviewList = reviewList;
        this.context = context;
    }

    @Override public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_review,parent,false));
    }

    @Override public void onBindViewHolder(ViewHolder holder, int position) {
        holder.author.setText(reviewList.get(position).author());
        holder.review.setText(reviewList.get(position).content());
    }

    @Override public int getItemCount() {
        return reviewList==null? 0:reviewList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.author) TextView author;
        @BindView(R.id.review) TextView review;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }
}
