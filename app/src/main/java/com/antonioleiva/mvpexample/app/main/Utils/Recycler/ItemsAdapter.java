/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.antonioleiva.mvpexample.app.main.Utils.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.antonioleiva.mvpexample.app.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsAdapter extends RecyclerView.Adapter<ItemsAdapter.NumberViewHolder> {

    public List<String> foundItems = new ArrayList<>();
    private final double IMAGES_IN_ROW = 5.0;

    @Override
    public NumberViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.list_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        NumberViewHolder viewHolder = new NumberViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NumberViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return (int) Math.ceil(foundItems.size()/IMAGES_IN_ROW);
    }


    class NumberViewHolder extends RecyclerView.ViewHolder {
        ImageView[] iconImages;

        NumberViewHolder(View itemView) {
            super(itemView);
            ImageView iconImage1 = (ImageView) itemView.findViewById(R.id.imageView1);
            ImageView iconImage2 = (ImageView) itemView.findViewById(R.id.imageView2);
            ImageView iconImage3 = (ImageView) itemView.findViewById(R.id.imageView3);
            ImageView iconImage4 = (ImageView) itemView.findViewById(R.id.imageView4);
            ImageView iconImage5 = (ImageView) itemView.findViewById(R.id.imageView5);

            iconImages = new ImageView[]{iconImage1, iconImage2, iconImage3, iconImage4, iconImage5};
        }

        void bind(int listIndex) throws IOException {

            for (int positionInRow = 0; positionInRow < iconImages.length; positionInRow++){
                int imagePosition = listIndex*iconImages.length+positionInRow;

                if (imagePosition < foundItems.size()) {
                    Picasso.with(itemView.getContext())
                            .load(foundItems.get(imagePosition))
                            .into(iconImages[positionInRow]);
                }else {
                    Picasso.with(itemView.getContext())
                            .load(R.drawable.transparent)
                            .into(iconImages[positionInRow]);
                }
            }


        }
    }
}
