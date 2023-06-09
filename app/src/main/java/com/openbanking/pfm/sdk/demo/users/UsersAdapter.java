package com.openbanking.pfm.sdk.demo.users;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.openbanking.core.sdk.models.OBUser;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ItemCommonBinding;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.UsersViewHolder> {
    private final List<OBUser> items;
    private final UsersAdapter.OnUserClickListener listener;

    public UsersAdapter(final List<OBUser> items, final UsersAdapter.OnUserClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UsersAdapter.UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_common, parent, false);
        return new UsersAdapter.UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.UsersViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class UsersViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommonBinding binding;

        public UsersViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ItemCommonBinding.bind(itemView);
        }

        public void bind(final OBUser userResponse) {
            binding.tvId.setText(String.valueOf(userResponse.getId()));
            binding.tvTitle.setText(userResponse.getCpf());
            binding.llContainer.setOnClickListener(view -> listener.onCopy(String.valueOf(userResponse.getId())));
        }
    }

    interface OnUserClickListener {
        void onCopy(final String userId);
    }
}
