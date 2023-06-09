package com.openbanking.pfm.sdk.demo.credits;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBCredit;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCreditBinding;

import java.util.List;

public class CreditsAdapter extends RecyclerView.Adapter<CreditsAdapter.CreditsViewHolder> {
    private final List<OBCredit> items;

    public CreditsAdapter(final List<OBCredit> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public CreditsAdapter.CreditsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_credit, parent, false);
        return new CreditsAdapter.CreditsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CreditsAdapter.CreditsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CreditsViewHolder extends RecyclerView.ViewHolder {

        private final ItemCreditBinding binding;

        public CreditsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCreditBinding.bind(itemView);
        }

        public void bind(final OBCredit creditResponse) {
            binding.tvCreditName.setText(creditResponse.getName());

            Double availableAmount = creditResponse.getAvailableAmount();
            binding.tvAvailableAmount.setText(availableAmount.toString());

            Double limitAmount = creditResponse.getLimitAmount();
            binding.tvLimitAmount.setText(limitAmount.toString());
        }
    }
}
