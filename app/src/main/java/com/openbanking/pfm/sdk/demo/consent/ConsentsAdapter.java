package com.openbanking.pfm.sdk.demo.consent;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBConsent;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class ConsentsAdapter extends RecyclerView.Adapter<ConsentsAdapter.ConsentsViewHolder> {
    private final List<OBConsent> items;
    private final ConsentsAdapter.OnConsentClickListener listener;

    public ConsentsAdapter(final List<OBConsent> items, final ConsentsAdapter.OnConsentClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ConsentsAdapter.ConsentsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new ConsentsAdapter.ConsentsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ConsentsAdapter.ConsentsViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class ConsentsViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public ConsentsViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBConsent consentResponse) {
            binding.tvId.setText(consentResponse.getConsentId());
            binding.tvTitle.setText(consentResponse.getBank().getName());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(consentResponse.getConsentId()));
            binding.ibUpdate.setOnClickListener(view -> listener.onEdit(consentResponse));
            binding.ibDelete.setOnClickListener(view -> listener.onDelete(consentResponse.getConsentId()));
        }
    }

    interface OnConsentClickListener {
        void onCopy(final String consentId);

        void onEdit(final OBConsent consentResponse);

        void onDelete(final String id);
    }
}
