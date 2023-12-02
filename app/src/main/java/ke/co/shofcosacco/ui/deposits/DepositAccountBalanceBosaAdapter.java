package ke.co.shofcosacco.ui.deposits;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemDepositsBinding;
import ke.co.shofcosacco.app.models.AccountBalanceBosa;

public class DepositAccountBalanceBosaAdapter extends ListAdapter<AccountBalanceBosa, DepositAccountBalanceBosaAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public DepositAccountBalanceBosaAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<AccountBalanceBosa>() {
            @Override
            public boolean areItemsTheSame(@NonNull AccountBalanceBosa oldItem, @NonNull AccountBalanceBosa newItem) {
                return oldItem.getBalances().equals(newItem.getBalances());
            }

            @Override
            public boolean areContentsTheSame(@NonNull AccountBalanceBosa oldItem, @NonNull AccountBalanceBosa newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemDepositsBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemDepositsBinding itemAccountBalanceBinding;
        private AccountBalanceBosa item;

        public FacilityViewHolder(@NonNull ItemDepositsBinding binding) {
            super(binding.getRoot());
            itemAccountBalanceBinding = binding;
            binding.tvDeposit.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(AccountBalanceBosa item) {
            this.item = item;
            itemAccountBalanceBinding.tvAccountName.setText(item.getAccountName());
            itemAccountBalanceBinding.tvBalanceCode.setText(item.getBalCode());


        }
    }

    public interface Listener {
        void onClick(AccountBalanceBosa item);
    }


}