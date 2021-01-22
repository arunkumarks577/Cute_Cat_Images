package com.example.catimages;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

public class CatViewAdapter extends RecyclerView.Adapter<CatViewAdapter.ViewHolder> implements View.OnClickListener {
    List cats;
    Context context;
    OnBottomReachedListener onBottomReachedListener;

    public CatViewAdapter(Context context, List catsList)
    {
        this.context = context;
        cats= catsList;
    }

    public void setOnBottomReachedListener(OnBottomReachedListener onBottomReachedListener){

        this.onBottomReachedListener = onBottomReachedListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.cat_images, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        if (position == cats.size() - 1){
            if(onBottomReachedListener != null)
                onBottomReachedListener.onBottomReached(position);
        }
        final Object object = cats.get(position);
        if(object instanceof Cats)
        {
            final Cats newcat = (Cats)object;
            //Glide.with(context).load(cat.getUrl()).into(holder.imageView);
            callingGlid(newcat, holder);
        }
        else
        {
            Glide.with(context).asBitmap().load(((LocalCats)object).getBitmapUrl()).into(holder.imageView);
        }
    }

    private void callingGlid(final Cats newcat, final ViewHolder holder)
    {
        Glide.with(context).asBitmap().load(newcat.getUrl()).override(250,250).into(new CustomTarget<Bitmap>() {
            @Override
            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                holder.imageView.setImageBitmap(resource);
                holder.imageView.setClickable(false);
                Utilities.bitmapToStringandAddtoDB(resource, newcat.getId());
            }

            @Override
            public void onLoadCleared(@Nullable Drawable placeholder) {

            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.refresh_icon));
                holder.imageView.setClickable(true);
                holder.imageView.setOnClickListener(CatViewAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cats.size();
    }

    @Override
    public void onClick(View view) {
        ViewHolder holder = (ViewHolder) view.getTag();
        int position = holder.getAdapterPosition();
        holder.imageView.setImageDrawable(context.getResources().getDrawable(R.drawable.loading));
        callingGlid((Cats) cats.get(position), holder);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageview);
            this.imageView.setTag(this);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public void clear() {
        cats.clear();
    }

}
