package com.openbanking.pfm.sdk.demo.categories;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBCategory;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.responses.OBCategoriesResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.categories.listeners.DeleteCategoryListener;
import com.openbanking.pfm.sdk.modules.categories.listeners.GetCategoriesListener;
import com.openbanking.pfm.sdk.modules.categories.listeners.GetCategoryListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCategoriesBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class CategoriesActivity extends AppCompatActivity implements CategoriesAdapter.OnCategoryClickListener {

    private ActivityCategoriesBinding binding;

    private CategoriesAdapter adapter;

    private final List<OBCategory> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

        adapter = new CategoriesAdapter(items, this);
        binding.rvTransactions.setAdapter(adapter);
    }

    private void addEvents() {
        binding.fabAddCategory.setOnClickListener(view ->
                startActivity(new Intent(this, CreateUpdateCategoryActivity.class)));
        binding.btGetCategories.setOnClickListener(view -> getCategories(binding.etUserId.getText().toString()));
        binding.btGetCategory.setOnClickListener(view -> getCategory(getSafeId(binding.etCategoryId.getText().toString())));
    }

    private int getSafeId(final String id) {
        try {
            return Integer.parseInt(id);
        } catch (final Exception e) {
            return 0;
        }
    }

    @Override
    public void onCopy(String categoryId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Transaction id", categoryId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(OBCategory category) {
        CreateUpdateCategoryActivity.obCategory = category;
        startActivity(new Intent(this, CreateUpdateCategoryActivity.class));
    }

    @Override
    public void onDelete(long id) {
        AlertUtils.confirm(this, "Delete", "Do you want to delete this category?", () -> deleteCategory(id));
    }

    private void deleteCategory(long id) {
        new OpenBankingPFMAPI().categoriesClient().delete(id, new DeleteCategoryListener() {
            @Override
            public void success() {
                Toast.makeText(CategoriesActivity.this, "Category deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CategoriesActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getCategories(final String userId) {
        final Integer id = userId.trim().equals("") ? null : Integer.parseInt(userId.trim());
        new OpenBankingPFMAPI().categoriesClient().getList(id, null, new GetCategoriesListener() {
            @Override
            public void success(OBCategoriesResponse response) {
                items.clear();
                items.addAll(response.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CategoriesActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getCategory(final int categoryId) {
        new OpenBankingPFMAPI().categoriesClient().get(categoryId, new GetCategoryListener() {
            @Override
            public void success(OBCategory category) {
                items.clear();
                items.add(category);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CategoriesActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

}