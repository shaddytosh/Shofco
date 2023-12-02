package ke.co.shofcosacco.ui.home;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemRecentTransactionsBinding;
import ke.co.shofcosacco.app.models.Dashboard;
import ke.co.shofcosacco.app.models.MiniStatement;
import ke.co.shofcosacco.app.utils.Constants;


public class RecentTransactionsAdapter extends ListAdapter<Dashboard, RecentTransactionsAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public RecentTransactionsAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<Dashboard>() {
            @Override
            public boolean areItemsTheSame(@NonNull Dashboard oldItem, @NonNull Dashboard newItem) {
                return oldItem.document_no.equals(newItem.document_no);
            }

            @Override
            public boolean areContentsTheSame(@NonNull Dashboard oldItem, @NonNull Dashboard newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemRecentTransactionsBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemRecentTransactionsBinding transactionsBinding;
        private Dashboard item;

        public FacilityViewHolder(@NonNull ItemRecentTransactionsBinding binding) {
            super(binding.getRoot());
            transactionsBinding = binding;
        }

        public void bind(Dashboard item) {
            this.item = item;
            transactionsBinding.tvDescription.setText(!item.description.isEmpty() ? item.description: item.document_no);
            transactionsBinding.tvAmount.setText(String.format("%s %s", Constants.CURRENCY, item.amount));
            transactionsBinding.tvDate.setText(item.transaction_date);
            if (item.trans_type.equals("CR")){
                transactionsBinding.tvAmount.setTextColor(Color.parseColor("#43A047"));
            }else {
                transactionsBinding.tvAmount.setTextColor(Color.parseColor("#F44336"));

            }


        }
    }

    public interface Listener {
        void onClick(Dashboard item);
    }


    @Override
    public int getItemCount() {
        return 5;
    }
}