package com.firechatbot.adapters;


import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.firechatbot.R;
import com.firechatbot.activities.ChatActivity;
import com.firechatbot.beans.ImageBean;
import com.firechatbot.utils.AppUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class BottomGalleryAdapter extends RecyclerView.Adapter<BottomGalleryAdapter.GalleryHolder> {

    private Context mContext;
    private List<ImageBean> mImageList;
    public static int mSelectedImage = 0;
    private List<Uri> mImagesUriList;

    public BottomGalleryAdapter(Context mContext, List<ImageBean> mImageList) {
        this.mContext = mContext;
        this.mImageList = mImageList;
        mImagesUriList = new ArrayList<>();
    }

    @Override
    public GalleryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_gallery, parent, false);
        return new GalleryHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryHolder holder, int position) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.fromFile(mImageList.get(position).getImageFile()))
                .setResizeOptions(new ResizeOptions(150, 150))
                .build();
        holder.galleryIconSdv.setController(Fresco.newDraweeControllerBuilder()
                .setOldController(holder.galleryIconSdv.getController())
                .setImageRequest(request)
                .build());
        holder.checkboxesCb.setChecked(mImageList.get(position).isSelected());
    }

    @Override
    public int getItemCount() {
        return mImageList.size();
    }

    class GalleryHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private SimpleDraweeView galleryIconSdv;
        private CheckBox checkboxesCb;

        GalleryHolder(View itemView) {
            super(itemView);
            galleryIconSdv = itemView.findViewById(R.id.sdv_gallery_icon);
            checkboxesCb = itemView.findViewById(R.id.cb_checkbox);
            galleryIconSdv.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (!mImageList.get(getAdapterPosition()).isSelected()) {
                if (mSelectedImage < 5) {
                    mSelectedImage = mSelectedImage + 1;
                    mImageList.get(getAdapterPosition()).setSelected(true);
                    mImagesUriList.add(Uri.fromFile(mImageList.get(getAdapterPosition()).getImageFile()));
                } else
                    AppUtils.displayToast(mContext, mContext.getString(R.string.max_image_selected));
            } else if (mSelectedImage > 0) {
                mSelectedImage = mSelectedImage - 1;
                mImageList.get(getAdapterPosition()).setSelected(false);
                mImagesUriList.remove(Uri.fromFile(mImageList.get(getAdapterPosition()).getImageFile()));
            }
            ((ChatActivity) mContext).getSelectedImages(mImagesUriList);
            notifyDataSetChanged();
        }
    }
}
