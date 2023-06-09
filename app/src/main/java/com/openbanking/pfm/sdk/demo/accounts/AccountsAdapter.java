package com.openbanking.pfm.sdk.demo.accounts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBAccount;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class AccountsAdapter extends RecyclerView.Adapter<AccountsAdapter.AccountsViewHolder> {

    private final List<OBAccount> items;

    private final OnAccountsClickListener listener;

    public AccountsAdapter(final List<OBAccount> items, final OnAccountsClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public AccountsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new AccountsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class AccountsViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public AccountsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBAccount OBAccount){
            binding.tvId.setText(String.valueOf(OBAccount.getId()));
            binding.tvTitle.setText(OBAccount.getName());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(OBAccount.getId())));
            binding.ibUpdate.setOnClickListener(view -> listener.onUpdate(OBAccount));
            binding.ibDelete.setOnClickListener(view -> listener.onDelete(OBAccount.getId()));
        }

    }

    interface OnAccountsClickListener {

        void onCopy(final String accountId);

        void onUpdate(final OBAccount OBAccount);

        void onDelete(final int id);

    }

}