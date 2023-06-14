package com.openbanking.pfm.sdk.demo.financial;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBFinancialEntity;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class FinancialEntitiesAdapter extends RecyclerView.Adapter<FinancialEntitiesAdapter.FinancialEntitiesViewHolder> {
    private final List<OBFinancialEntity> items;
    private final FinancialEntitiesAdapter.OnFinancialEntityClickListener listener;

    public FinancialEntitiesAdapter(final List<OBFinancialEntity> items, final FinancialEntitiesAdapter.OnFinancialEntityClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FinancialEntitiesAdapter.FinancialEntitiesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new FinancialEntitiesAdapter.FinancialEntitiesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FinancialEntitiesAdapter.FinancialEntitiesViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class FinancialEntitiesViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public FinancialEntitiesViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBFinancialEntity financialEntityResponse) {
            binding.tvId.setText(String.valueOf(financialEntityResponse.getId()));
            binding.tvTitle.setText(financialEntityResponse.getName());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(financialEntityResponse.getId())));

            binding.ibDelete.setVisibility(View.INVISIBLE);
            binding.ibUpdate.setVisibility(View.INVISIBLE);
        }
    }

    interface OnFinancialEntityClickListener {
        void onCopy(final String financialEntityId);
    }
}
