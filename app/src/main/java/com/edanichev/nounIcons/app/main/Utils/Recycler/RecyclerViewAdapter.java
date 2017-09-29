package com.edanichev.nounIcons.app.main.Utils.Recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.edanichev.nounIcons.app.R;
import com.edanichev.nounIcons.app.main.Utils.Network.Noun.IconsList.IconDetails;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public List<IconDetails> mData = new ArrayList<>();
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    public RecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    public void setItems(List<IconDetails> data){
        mData = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.bind(position);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    protected class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView iconImage ;

        ViewHolder(View itemView) {
            super(itemView);
            iconImage = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }

        protected void bind (int position) throws IOException {

            Picasso.with(itemView.getContext())
                    .load(mData.get(position).getPreview_url_84())
                    .placeholder(R.drawable.grid_placeholder)
                    .noFade()
                    .into(iconImage, new Callback() {
                        @Override
                        public void onSuccess() {
                            Animation myAnim = AnimationUtils.loadAnimation(mInflater.getContext(),R.anim.grid_cell_animation);
                            iconImage.startAnimation(myAnim);
                        }

                        @Override
                        public void onError() {

                        }
                    });


        }

    }


}