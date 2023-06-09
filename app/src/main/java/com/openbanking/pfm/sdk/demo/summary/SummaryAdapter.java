package com.openbanking.pfm.sdk.demo.summary;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBResumeByMonth;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class SummaryAdapter extends RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder> {
    private final List<OBResumeByMonth> items;

    public SummaryAdapter(final List<OBResumeByMonth> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public SummaryAdapter.SummaryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new SummaryAdapter.SummaryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryAdapter.SummaryViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class SummaryViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public SummaryViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBResumeByMonth summary) {
            binding.tvId.setText(String.valueOf(summary.getDate()));
        }
    }
}