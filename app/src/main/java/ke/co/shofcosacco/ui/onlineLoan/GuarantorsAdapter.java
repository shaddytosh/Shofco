package ke.co.shofcosacco.ui.onlineLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.ke.shofcosacco.R;
import ke.co.shofcosacco.app.models.GuarantorNew;


public class GuarantorsAdapter extends RecyclerView.Adapter<GuarantorsAdapter.PhotoViewHolder> {
    private Context context;
    private List<GuarantorNew> nextOfKinNewList;
    private OnRemoveClickListener onRemoveClickListener;

    public GuarantorsAdapter(Context context, List<GuarantorNew> nextOfKinNewList, OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.nextOfKinNewList = nextOfKinNewList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_guarantor, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        GuarantorNew nextOfKinNews = nextOfKinNewList.get(position);

        // Set the values
        holder.title1.setText("Name: "+nextOfKinNews.getMemberName());
        holder.title2.setText("Free Deposits: "+nextOfKinNews.getFreeDeposits());


        holder.removeButton.setOnClickListener(v -> {
            if (onRemoveClickListener != null) {
                onRemoveClickListener.onRemoveClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return nextOfKinNewList.size();
    }

    public interface OnRemoveClickListener {
        void onRemoveClick(int position);
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {
        private TextView title1, title2;

        ImageView removeButton;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the TextViews
            title1 = itemView.findViewById(R.id.title1);
            title2 = itemView.findViewById(R.id.title2);
            removeButton = itemView.findViewById(R.id.remove_button);


        }
    }
}
