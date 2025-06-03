package ke.co.shofcosacco.ui.onlineLoan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemGuarantorBinding;
import co.ke.shofcosacco.databinding.ItemLoanProductBinding;
import ke.co.shofcosacco.app.api.responses.GuarantorResponse;
import ke.co.shofcosacco.app.models.Guarantor;


public class GuarantorAdapter extends ListAdapter<GuarantorResponse.GuarantorRequest, GuarantorAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;

    public GuarantorAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<GuarantorResponse.GuarantorRequest>() {
            @Override
            public boolean areItemsTheSame(@NonNull GuarantorResponse.GuarantorRequest oldItem, @NonNull GuarantorResponse.GuarantorRequest newItem) {
                return oldItem.getApplicationNo().equals(newItem.getApplicationNo());
            }

            @Override
            public boolean areContentsTheSame(@NonNull GuarantorResponse.GuarantorRequest oldItem, @NonNull GuarantorResponse.GuarantorRequest newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemGuarantorBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemGuarantorBinding itemGuarantorBinding;
        private GuarantorResponse.GuarantorRequest item;

        public FacilityViewHolder(@NonNull ItemGuarantorBinding binding) {
            super(binding.getRoot());
            itemGuarantorBinding = binding;
            binding.action.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(GuarantorResponse.GuarantorRequest item) {
            this.item = item;
            itemGuarantorBinding.clientName.setText(item.getName());
            itemGuarantorBinding.loanName.setText(String.format("Loan Name: %s", item.getLoanType()));
            itemGuarantorBinding.period.setText(String.format("Loan Type: %s", item.getLoanType()));
            itemGuarantorBinding.loanNo.setText(String.format("ID No: %s", item.getIdNo()));
            itemGuarantorBinding.memberNo.setText(String.format("Status: %s", item.getApprovalStatus()));
            itemGuarantorBinding.amount.setText(String.format("Requested Amount: KES %s", item.getRequestedAmount()));



        }
    }

    public interface Listener {
        void onClick(GuarantorResponse.GuarantorRequest item);
    }


}
