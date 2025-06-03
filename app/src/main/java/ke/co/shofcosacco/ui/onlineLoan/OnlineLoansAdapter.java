package ke.co.shofcosacco.ui.onlineLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemGuarantorBinding;
import co.ke.shofcosacco.databinding.ItemOnlineLoanBinding;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;


public class OnlineLoansAdapter extends ListAdapter<GuarantorResponse.Loans, OnlineLoansAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;

    public OnlineLoansAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<GuarantorResponse.Loans>() {
            @Override
            public boolean areItemsTheSame(@NonNull GuarantorResponse.Loans oldItem, @NonNull GuarantorResponse.Loans newItem) {
                return oldItem.applicationNo.equals(newItem.applicationNo);
            }

            @Override
            public boolean areContentsTheSame(@NonNull GuarantorResponse.Loans oldItem, @NonNull GuarantorResponse.Loans newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemOnlineLoanBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemOnlineLoanBinding itemGuarantorBinding;
        private GuarantorResponse.Loans item;

        public FacilityViewHolder(@NonNull ItemOnlineLoanBinding binding) {
            super(binding.getRoot());
            itemGuarantorBinding = binding;
            binding.btnView.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(GuarantorResponse.Loans item) {
            this.item = item;
            itemGuarantorBinding.tvLoanPurpose.setText(item.loanPurpose);
            itemGuarantorBinding.tvLoanType.setText("Loan Type: "+item.loanType);
            itemGuarantorBinding.tvInstallment.setText("Inatsallment: "+item.installement);
            itemGuarantorBinding.tvRequestedAmount.setText("Requested Amount: "+item.requestedAmount);

        }
    }

    public interface Listener {
        void onClick(GuarantorResponse.Loans item);
    }


}
