package ke.co.shofcosacco.ui.loans;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemLoanBalanceBinding;
import ke.co.shofcosacco.app.models.LoanBalance;
import ke.co.shofcosacco.app.utils.Constants;


public class LoanRepaymentAdapter extends ListAdapter<LoanBalance, LoanRepaymentAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public LoanRepaymentAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<LoanBalance>() {
            @Override
            public boolean areItemsTheSame(@NonNull LoanBalance oldItem, @NonNull LoanBalance newItem) {
                return oldItem.getLoanNo().equals(newItem.getLoanNo());
            }

            @Override
            public boolean areContentsTheSame(@NonNull LoanBalance oldItem, @NonNull LoanBalance newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemLoanBalanceBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemLoanBalanceBinding loanBalanceBinding;
        private LoanBalance item;

        public FacilityViewHolder(@NonNull ItemLoanBalanceBinding binding) {
            super(binding.getRoot());
            loanBalanceBinding = binding;
            binding.tvRepay.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(LoanBalance item) {
            this.item = item;
            loanBalanceBinding.tvLoanName.setText(item.getLoanName());
            loanBalanceBinding.tvLoanBalance.setText(String.format("%s %s %s", "Balance: ",Constants.CURRENCY, item.getLoanBalance()));
            loanBalanceBinding.tvLoanInstallments.setText(String.format("%s %s %s", "Installments: ",Constants.CURRENCY, item.getLoanInstallment()));
            loanBalanceBinding.tvLoanApplicationDate.setText(String.format("Application Date: %s", item.getLoanApplicationDate()));
            loanBalanceBinding.tvLoanIssueDate.setText(String.format("Issue Date: %s", item.getLoanApplicationDate()));
            loanBalanceBinding.tvRepay.setText("Repay Loan");

        }
    }

    public interface Listener {
        void onClick(LoanBalance item);
    }


}