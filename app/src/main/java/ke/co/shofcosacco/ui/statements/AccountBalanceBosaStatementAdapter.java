package ke.co.shofcosacco.ui.statements;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemAccountBalanceAltBinding;
import ke.co.shofcosacco.app.models.AccountBalanceBosa;

public class AccountBalanceBosaStatementAdapter extends ListAdapter<AccountBalanceBosa, AccountBalanceBosaStatementAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public AccountBalanceBosaStatementAdapter(@NonNull Context context, Listener listener) {
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
        return new FacilityViewHolder(ItemAccountBalanceAltBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemAccountBalanceAltBinding itemAccountBalanceBinding;
        private AccountBalanceBosa item;

        public FacilityViewHolder(@NonNull ItemAccountBalanceAltBinding binding) {
            super(binding.getRoot());
            itemAccountBalanceBinding = binding;
            binding.tvView.setOnClickListener(v -> {
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