package com.openbanking.pfm.sdk.demo.consent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBBank;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemBankBinding;

import java.util.List;

public class BanksAvailableAdapter extends RecyclerView.Adapter<BanksAvailableAdapter.BankssViewHolder> {
    private final List<OBBank> items;
    private final BanksAvailableAdapter.OnBankClickListener listener;

    public BanksAvailableAdapter(final List<OBBank> items, final BanksAvailableAdapter.OnBankClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BanksAvailableAdapter.BankssViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new BanksAvailableAdapter.BankssViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BanksAvailableAdapter.BankssViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BankssViewHolder extends RecyclerView.ViewHolder {

        private final ItemBankBinding binding;

        public BankssViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemBankBinding.bind(itemView);
        }

        public void bind(final OBBank bankReponse) {
            binding.tvBankName.setText(bankReponse.getName());
            binding.llRootContainer.setOnClickListener(view -> listener.onTap(bankReponse));
        }
    }

    interface OnBankClickListener {
        void onTap(final OBBank bankReponse);
    }
}