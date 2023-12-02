package ke.co.shofcosacco.ui.profile;


import android.content.Context;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import co.ke.shofcosacco.R;
import co.ke.shofcosacco.databinding.ItemAccountBalanceBinding;
import ke.co.shofcosacco.app.models.AccountBalanceFosa;
import ke.co.shofcosacco.app.utils.Constants;

public class AccountBalanceFosaAdapter extends ListAdapter<AccountBalanceFosa, AccountBalanceFosaAdapter.FacilityViewHolder> {

    @NonNull
    private final Context mContext;
    private final Listener mListener;
    public AccountBalanceFosaAdapter(@NonNull Context context, Listener listener) {
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
        return new FacilityViewHolder(ItemAccountBalanceBinding.inflate(LayoutInflater.from(mContext), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FacilityViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    class FacilityViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final ItemAccountBalanceBinding itemAccountBalanceBinding;
        private AccountBalanceFosa item;

        public FacilityViewHolder(@NonNull ItemAccountBalanceBinding binding) {
            super(binding.getRoot());
            itemAccountBalanceBinding = binding;
            binding.ivShowStatement.setOnClickListener(v -> {
                if (mListener != null) {
                    mListener.onClick(this.item);
                }

            });

            TransitionManager.beginDelayedTransition(binding.baseCardview, new AutoTransition());
            binding.cardGroup.setVisibility(View.VISIBLE);
            binding.ivShowBlance.setImageResource(android.R.drawable.arrow_up_float);

            binding.baseCardview.setOnClickListener(view -> {
                if(binding.cardGroup.getVisibility() == View.VISIBLE){
                    TransitionManager.beginDelayedTransition(binding.baseCardview, new AutoTransition());
                    binding.cardGroup.setVisibility(View.GONE);
                    binding.ivShowBlance.setImageResource(R.drawable.baseline_remove_red_eye_24);
                }
                else {
                    TransitionManager.beginDelayedTransition(binding.baseCardview, new AutoTransition());
                    binding.cardGroup.setVisibility(View.VISIBLE);
                    binding.ivShowBlance.setImageResource(android.R.drawable.arrow_up_float);
                }
            });
        }

        public void bind(AccountBalanceFosa item) {
            this.item = item;

            itemAccountBalanceBinding.tvAccountName.setText(item.getAccountName());
            itemAccountBalanceBinding.tvBalanceCode.setText(item.getBalCode());
            itemAccountBalanceBinding.tvAmount.setText(String.format("%s %s", Constants.CURRENCY, item.getBalance()));

            Calendar calendar = Calendar.getInstance();

            SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy", Locale.getDefault());
            String formattedDate = dateFormat.format(calendar.getTime());

            // Format the time as "h:mm a"
            SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a", Locale.getDefault());
            String formattedTime = timeFormat.format(calendar.getTime());

            // Combine the formatted date and time
            String dateTimeString = formattedDate + " at " + formattedTime;

            itemAccountBalanceBinding.tvDate.setText(String.format("Account balances as at  %s", dateTimeString));


        }
    }

    public interface Listener {
        void onClick(AccountBalanceFosa item);
    }


}