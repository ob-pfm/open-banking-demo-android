package com.openbanking.pfm.sdk.demo.budgets;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBBudget;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class BudgetsAdapter extends RecyclerView.Adapter<BudgetsAdapter.BudgetsViewHolder> {
    private final List<OBBudget> items;
    private final BudgetsAdapter.OnBudgetClickListener listener;

    public BudgetsAdapter(final List<OBBudget> items, final BudgetsAdapter.OnBudgetClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BudgetsAdapter.BudgetsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new BudgetsAdapter.BudgetsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BudgetsAdapter.BudgetsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class BudgetsViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public BudgetsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBBudget budgetResponse) {
            binding.tvId.setText(String.valueOf(budgetResponse.getId()));
            binding.tvTitle.setText(budgetResponse.getName());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(budgetResponse.getId())));
            binding.ibUpdate.setOnClickListener(view -> listener.onUpdate(budgetResponse));
            binding.ibDelete.setOnClickListener(view -> listener.onDelete(budgetResponse.getId()));
        }
    }

    interface OnBudgetClickListener {
        void onCopy(final String budgetId);

        void onUpdate(final OBBudget budgetResponse);

        void onDelete(final int id);
    }
}
