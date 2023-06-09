package com.openbanking.pfm.sdk.demo.analysis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBAnalysisByCategory;
import com.openbanking.core.sdk.models.OBAnalysisByMonth;
import com.openbanking.core.sdk.shared.Constants;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemAnalysisBinding;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class AnalysisAdapter extends RecyclerView.Adapter<AnalysisAdapter.AnalysisViewHolder> {

    private final List<OBAnalysisByMonth> items;

    public AnalysisAdapter(final List<OBAnalysisByMonth> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public AnalysisViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_analysis, parent, false);
        return new AnalysisViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnalysisViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class AnalysisViewHolder extends RecyclerView.ViewHolder {

        private final ItemAnalysisBinding binding;

        public AnalysisViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemAnalysisBinding.bind(itemView);
        }

        public void bind(final OBAnalysisByMonth item) {
            binding.tvSummaryDate.setText(new SimpleDateFormat(Constants.DateFormat.SHORT_MONTH_FORMAT, new Locale(Constants.DateFormat.LOCALE_LANGUAGE)).format(item.getDate()));
            if (!item.getCategories().isEmpty()) {
                Double totalAmount = 0.0;
                for (final OBAnalysisByCategory analysisByCategory : item.getCategories()) {
                    totalAmount += analysisByCategory.getAmount();
                }
                binding.tvSummaryTotalAmount.setText("$" + totalAmount);
            }
        }

    }

}
