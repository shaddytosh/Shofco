package ke.co.shofcosacco.ui.statements;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import co.ke.shofcosacco.databinding.ItemStatementBinding;
import ke.co.shofcosacco.app.models.MiniStatement;
import ke.co.shofcosacco.app.utils.Constants;


public class StatementAdapter extends ListAdapter<MiniStatement, StatementAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public StatementAdapter(@NonNull Context context, Listener listener) {
        super(new DiffUtil.ItemCallback<MiniStatement>() {
            @Override
            public boolean areItemsTheSame(@NonNull MiniStatement oldItem, @NonNull MiniStatement newItem) {
                return oldItem.getAmount().equals(newItem.getAmount());
            }

            @Override
            public boolean areContentsTheSame(@NonNull MiniStatement oldItem, @NonNull MiniStatement newItem) {
                return oldItem.equals(newItem);
            }
        });
        mContext = context;
        mListener = listener;
    }

    @NonNull
    @Override
    public FacilityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FacilityViewHolder(ItemStatementBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemStatementBinding statementBinding;
        private MiniStatement item;

        public FacilityViewHolder(@NonNull ItemStatementBinding binding) {
            super(binding.getRoot());
            statementBinding = binding;
            binding.baseCardview.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }
            });
        }

        public void bind(MiniStatement item) {
            this.item = item;
            statementBinding.tvDescription.setText(item.getDescription());
            statementBinding.tvAmount.setText(item.transType+" "+String.format("%s %s", Constants.CURRENCY, item.getAmount()));
            statementBinding.tvDate.setText(item.transType+" "+String.format("Transaction Date: %s", item.getTransactionDate()));
            statementBinding.tvDocumentNo.setText(String.format("Document No: %s", item.getDocumentNo()));
            if (item.getTransType().equals("CR")){
                statementBinding.tvAmount.setTextColor(Color.parseColor("#43A047"));
            }else {
                statementBinding.tvAmount.setTextColor(Color.parseColor("#F44336"));

            }


        }
    }

    public interface Listener {
        void onClick(MiniStatement item);
    }


}