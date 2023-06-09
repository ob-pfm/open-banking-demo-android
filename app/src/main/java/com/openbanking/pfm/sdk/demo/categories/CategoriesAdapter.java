package com.openbanking.pfm.sdk.demo.categories;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBCategory;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCategoryBinding;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder> {

    private final List<OBCategory> items;

    private final OnCategoryClickListener listener;

    public CategoriesAdapter(final List<OBCategory> items, final OnCategoryClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class CategoryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCategoryBinding binding;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCategoryBinding.bind(itemView);
        }

        public void bind(final OBCategory category) {
            binding.tvCategoryId.setText(String.valueOf(category.getId()));
            binding.tvCategoryName.setText(category.getName());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(category.getId())));
            binding.ibUpdate.setOnClickListener(view -> listener.onUpdate(category));
            binding.ibDelete.setOnClickListener(view -> listener.onDelete(category.getId()));
        }

    }

    interface OnCategoryClickListener {

        void onCopy(final String categoryId);

        void onUpdate(final OBCategory category);

        void onDelete(final long id);

    }

}
