package ke.co.shofcosacco.ui.nextOfKin;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemNextOfKinBinding;
import ke.co.shofcosacco.app.models.NextOfKin;


public class NextOfKinAdapter extends ListAdapter<NextOfKin, NextOfKinAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public NextOfKinAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<NextOfKin>() {
            @Override
            public boolean areItemsTheSame(@NonNull NextOfKin oldItem, @NonNull NextOfKin newItem) {
                return oldItem.name.equals(newItem.name);
            }

            @Override
            public boolean areContentsTheSame(@NonNull NextOfKin oldItem, @NonNull NextOfKin newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemNextOfKinBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemNextOfKinBinding itemNextOfKinBinding;
        private NextOfKin item;

        public FacilityViewHolder(@NonNull ItemNextOfKinBinding binding) {
            super(binding.getRoot());
            itemNextOfKinBinding = binding;
            binding.baseCardview.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(NextOfKin item) {
            this.item = item;
            itemNextOfKinBinding.name.setText(item.name);
            itemNextOfKinBinding.type.setText("Type: "+item.type);
            itemNextOfKinBinding.allocation.setText("Allocation: "+item.allocation+"%");
            itemNextOfKinBinding.idNo.setText("National ID: "+item.id_no);
            itemNextOfKinBinding.relationship.setText("Relationship: "+item.relationship);
            itemNextOfKinBinding.mobileNo.setText("Mobile No: "+item.mobile_no);


        }
    }

    public interface Listener {
        void onClick(NextOfKin item);
    }


}