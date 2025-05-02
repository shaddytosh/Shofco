package ke.co.shofcosacco.ui.onboarding;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.ke.shofcosacco.R;


public class IdFrontAdapter extends RecyclerView.Adapter<IdFrontAdapter.PhotoViewHolder> {
    private Context context;
    private List<Bitmap> photoList;
    private OnRemoveClickListener onRemoveClickListener;

    public IdFrontAdapter(Context context, List<Bitmap> photoList, OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.photoList = photoList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_photo, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        Bitmap photo = photoList.get(position);
        holder.photoImage.setImageBitmap(photo);

        holder.removeButton.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photoList.size();
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        ImageView photoImage;
        ImageView removeButton;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            photoImage = itemView.findViewById(R.id.imageViewPhoto);
            removeButton = itemView.findViewById(R.id.remove_button);

        }
    }
}
