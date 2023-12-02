package ke.co.shofcosacco.ui.loans;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemLoanProductBinding;
import ke.co.shofcosacco.app.models.LoanProduct;


public class LoanProductAdapter extends ListAdapter<LoanProduct, LoanProductAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public LoanProductAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<LoanProduct>() {
            @Override
            public boolean areItemsTheSame(@NonNull LoanProduct oldItem, @NonNull LoanProduct newItem) {
                return oldItem.getProductCode().equals(newItem.getProductCode());
            }

            @Override
            public boolean areContentsTheSame(@NonNull LoanProduct oldItem, @NonNull LoanProduct newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemLoanProductBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemLoanProductBinding itemLoanProductBinding;
        private LoanProduct item;

        public FacilityViewHolder(@NonNull ItemLoanProductBinding binding) {
            super(binding.getRoot());
            itemLoanProductBinding = binding;
            binding.tvApply.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(LoanProduct item) {
            this.item = item;
            itemLoanProductBinding.installmentPeriod.setText(String.format("Installment Period: %s Months", item.getInstallmentPeriod()));
            itemLoanProductBinding.maximumLoanAmt.setText(String.format("Maximum Amount: KES %s", item.getMaximumLoanAmt()));
            itemLoanProductBinding.repaymentMethod.setText(String.format("Repayment Method: %s", item.getRepaymentMethod()));
            itemLoanProductBinding.productDescription.setText(item.getProductDescription());
            itemLoanProductBinding.interest.setText(String.format("Interest: %s%%", item.getInterest()));

        }
    }

    public interface Listener {
        void onClick(LoanProduct item);
    }


}