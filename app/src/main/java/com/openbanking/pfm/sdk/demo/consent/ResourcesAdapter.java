package com.openbanking.pfm.sdk.demo.consent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemBankBinding;

import java.util.List;

public class ResourcesAdapter extends RecyclerView.Adapter<ResourcesAdapter.ResourcesViewHolder> {
    private final List<String> items;

    public ResourcesAdapter(final List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ResourcesAdapter.ResourcesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bank, parent, false);
        return new ResourcesAdapter.ResourcesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResourcesAdapter.ResourcesViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ResourcesViewHolder extends RecyclerView.ViewHolder {

        private final ItemBankBinding binding;

        public ResourcesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemBankBinding.bind(itemView);
        }

        public void bind(final String resourceName) {
            binding.tvBankName.setText(resourceName);
        }
    }
}
