package com.example.myapplication;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private List<Bitmap> images;
    private List<Uri> imageUris;
    private Context context;

    public interface OnImageClickListener {
        void onImageClick(Uri imageUri);
    }

    private OnImageClickListener listener;

    public void setOnImageClickListener(OnImageClickListener listener) {
        this.listener = listener;
    }

    public ImageAdapter(Context context) {
        this.context = context;
        this.images = new ArrayList<>();
        this.imageUris = new ArrayList<>();
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, int position) {
        holder.imageView.setImageBitmap(images.get(position));
        holder.deleteButton.setOnClickListener(v -> deleteImage(position));

        holder.imageView.setOnClickListener(v -> {
            if (listener != null && position < imageUris.size()) {
                listener.onImageClick(imageUris.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void addImage(Bitmap bitmap, Uri uri) {
        images.add(bitmap);
        imageUris.add(uri);
        notifyItemInserted(images.size() - 1);
    }

    private void deleteImage(int position) {
            Uri imageUri = imageUris.get(position);
            try {
                context.getContentResolver().delete(imageUri, null, null);
                images.remove(position);
                imageUris.remove(position);
                notifyItemRemoved(position);
                Toast.makeText(context, "Image deleted successfully", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(context, "Error deleting image: " + e.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }

    }

    static class ImageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        ImageButton deleteButton;

        ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}