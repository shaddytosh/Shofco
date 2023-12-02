package ke.co.shofcosacco.ui.deposits;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemDepositsBinding;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;

public class DepositAccountBalanceFosaAdapter extends ListAdapter<AccountBalanceFosa, DepositAccountBalanceFosaAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public DepositAccountBalanceFosaAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<AccountBalanceFosa>() {
            @Override
            public boolean areItemsTheSame(@NonNull AccountBalanceFosa oldItem, @NonNull AccountBalanceFosa newItem) {
                return oldItem.getAccountName().equals(newItem.getAccountName());
            }

            @Override
            public boolean areContentsTheSame(@NonNull AccountBalanceFosa oldItem, @NonNull AccountBalanceFosa newItem) {
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
        private AccountBalanceFosa item;

        public FacilityViewHolder(@NonNull ItemDepositsBinding binding) {
            super(binding.getRoot());
            itemAccountBalanceBinding = binding;
            binding.tvDeposit.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(AccountBalanceFosa item) {
            this.item = item;

            itemAccountBalanceBinding.tvBalanceCode.setText(item.getBalCode());
            if (item.balCode.equals("ORD")){
                itemAccountBalanceBinding.tvAccountName.setText("ORDINARY");

            }else {
                itemAccountBalanceBinding.tvAccountName.setText(item.balCode);
            }
            itemAccountBalanceBinding.tvBalanceCode.setText(item.getBalCode());


        }
    }

    public interface Listener {
        void onClick(AccountBalanceFosa item);
    }


}