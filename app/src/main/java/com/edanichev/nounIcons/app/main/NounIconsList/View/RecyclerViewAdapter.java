package com.edanichev.nounIcons.app.main.NounIconsList.View;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.NounIconDetails.Model.IconDetails;
import com.edanichev.nounIcons.app.main.Utils.UI.Animation.NounAnimations;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public List<IconDetails> iconsList = new ArrayList<>();
    private ItemClickListener mClickListener;

    public void setItems(List<IconDetails> icons) {
        if (icons != null) {
            iconsList = icons;
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grid_cell_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return iconsList.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImage;
        Animation animation = NounAnimations.getIconBindAnimation();

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        protected void bind(int position) {
            if (iconImage.getDrawable() != null) {
                Picasso.with(itemView.getContext())
                        .load(iconsList.get(position).getPreview_url_84())
                        .placeholder(R.drawable.grid_placeholder)
                        .into(iconImage);
            }
            else {
                Picasso.with(itemView.getContext())
                        .load(iconsList.get(position).getPreview_url_84())
                        .placeholder(R.drawable.grid_placeholder)
                        .into(iconImage, new Callback() {
                            @Override
                            public void onSuccess() {
                                iconImage.startAnimation(animation);
                            }

                            @Override
                            public void onError() {

                            }
                        });
            }
        }

    }


}