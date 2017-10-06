package com.firechatbot.adapters;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.firechatbot.R;

public class BottomGalleryAdapter extends RecyclerView.Adapter<BottomGalleryAdapter.GalleryHolder>{

    public BottomGalleryAdapter()
    {

    }
    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gallery,parent,false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class GalleryHolder extends RecyclerView.ViewHolder{
        private ImageView galleryIconIv;
        GalleryHolder(View itemView) {
            super(itemView);
            galleryIconIv = itemView.findViewById(R.id.iv_gallery_icon);
        }
    }
}
