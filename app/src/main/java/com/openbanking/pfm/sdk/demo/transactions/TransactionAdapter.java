package com.openbanking.pfm.sdk.demo.transactions;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBTransaction;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<OBTransaction> items;
    private final OnTransactionClickListener listener;

    public TransactionAdapter(final List<OBTransaction> items, final OnTransactionClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class TransactionViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBTransaction transaction) {
            binding.tvId.setText(String.valueOf(transaction.getId()));
            binding.tvTitle.setText(transaction.getDescription());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(transaction.getId())));
            binding.ibUpdate.setOnClickListener(view -> listener.onUpdate(transaction));
            binding.ibDelete.setOnClickListener(view -> listener.onDelete(transaction.getId()));
        }

    }

    interface OnTransactionClickListener {

        void onCopy(final String transactionId);

        void onUpdate(final OBTransaction transaction);

        void onDelete(final int id);

    }

}
