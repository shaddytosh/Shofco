package ke.co.shofcosacco.ui.onboarding;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import co.ke.shofcosacco.R;
import ke.co.shofcosacco.app.models.NextOfKinNew;


public class NextOfKinsAdapter extends RecyclerView.Adapter<NextOfKinsAdapter.PhotoViewHolder> {
    private Context context;
    private List<NextOfKinNew> nextOfKinNewList;
    private OnRemoveClickListener onRemoveClickListener;

    public NextOfKinsAdapter(Context context, List<NextOfKinNew> nextOfKinNewList, OnRemoveClickListener onRemoveClickListener) {
        this.context = context;
        this.nextOfKinNewList = nextOfKinNewList;
        this.onRemoveClickListener = onRemoveClickListener;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_next_of_kin, parent, false);
        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        NextOfKinNew nextOfKinNews = nextOfKinNewList.get(position);

        // Set the values
        holder.textName.setText("Name: "+nextOfKinNews.getName());
        holder.textDateOfBirth.setText("Date of Birth: "+nextOfKinNews.getDateOfBirth());
        holder.textIdNo.setText("ID No: "+nextOfKinNews.getIdNo());
        holder.textTelephone.setText("Telephone: "+nextOfKinNews.getTelephone());
        holder.textEmail.setText("Email: "+nextOfKinNews.getEmail());
        holder.textTown.setText("Town: "+nextOfKinNews.getTown());
        holder.textAddress.setText("Address: "+nextOfKinNews.getAddress());
        holder.textAllocation.setText("Allocation: "+nextOfKinNews.getAllocation() +
                "\nKin Type: " + (nextOfKinNews.getKinType().equals("1") ? "Next Of Kin": "Beneficiary\n"));


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
        private TextView textName, textDateOfBirth, textIdNo, textTelephone, textEmail, textTown, textAddress, textAllocation;

        ImageView removeButton;


        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);

            // Initialize the TextViews
            textName = itemView.findViewById(R.id.textName);
            textDateOfBirth = itemView.findViewById(R.id.textDateOfBirth);
            textIdNo = itemView.findViewById(R.id.textIdNo);
            textTelephone = itemView.findViewById(R.id.textTelephone);
            textEmail = itemView.findViewById(R.id.textEmail);
            textTown = itemView.findViewById(R.id.textTown);
            textAddress = itemView.findViewById(R.id.textAddress);
            textAllocation = itemView.findViewById(R.id.textAllocation);
            removeButton = itemView.findViewById(R.id.remove_button);


        }
    }
}
