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

package me.drall.popularmovie2.ui.listener;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
import me.drall.popularmovie2.util.ViewUtils;

public class RecyclerItemClickListener implements RecyclerView.OnItemTouchListener {
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        public void onItemClick(final View view, int position);
    }

    GestureDetector mGestureDetector;

    public RecyclerItemClickListener(Context context, OnItemClickListener listener) {
        mListener = listener;
        mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView view, MotionEvent e) {
        View parent = view.findChildViewUnder(e.getX(), e.getY());
        View childView = parent;
        if(parent instanceof ViewGroup && mGestureDetector.onTouchEvent(e)) {

            childView = findClosestViewTo((ViewGroup)parent,e.getX(),e.getY());
        }
        if (parent != null && mListener != null && mGestureDetector.onTouchEvent(e)) {
            mListener.onItemClick(childView, view.getChildAdapterPosition(parent));
            return true;
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView view, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     *  finds view under and closest to a given point inside a ViewGroup
     *
     * @param parent parent {@link ViewGroup} for which to find the view
     * @param x Horizontal position in pixels to search
     * @param y Vertical position in pixels to search
     * @return The child view under and closest to (x, y)
     */
    private View findClosestViewTo(ViewGroup parent, float x, float y) {
        List<View> children = ViewUtils.getAllChildrenBFS(parent);

        View bottomMostChild = parent;
        float minDistance=Float.MAX_VALUE;
        for (View child:children) {
            final float translationX = ViewCompat.getTranslationX(child);
            final float translationY = ViewCompat.getTranslationY(child);

            if (x >= parent.getLeft() + child.getLeft() + translationX &&
                    x <= parent.getRight() + child.getRight() + translationX &&
                    y >= parent.getTop() +child.getTop() + translationY &&
                    y <= parent.getBottom() + child.getBottom() + translationY) {
                final float sum = Math.abs(x-(parent.getLeft() +child.getLeft() + translationX)) +
                        Math.abs(x-(parent.getRight()+child.getRight() + translationX)) +
                        Math.abs(y-(parent.getTop()+child.getTop()+translationY)) +
                        Math.abs(y-(parent.getBottom()+child.getBottom()+translationY));


                if(sum<minDistance) {
                    bottomMostChild = child;
                    minDistance = sum;
                }
            }

        }
        return bottomMostChild;
    }
}