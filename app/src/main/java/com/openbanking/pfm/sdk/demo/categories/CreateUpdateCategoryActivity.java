package com.openbanking.pfm.sdk.demo.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBCategory;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.requests.OBCreateCategoryRequest;
import com.openbanking.core.sdk.models.requests.OBUpdateCategoryRequest;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.categories.listeners.CreateCategoryListener;
import com.openbanking.pfm.sdk.modules.categories.listeners.UpdateCategoryListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCreateUpdateCategoryBinding;

import java.util.List;

public class CreateUpdateCategoryActivity extends AppCompatActivity {

    public static OBCategory obCategory;

    private ActivityCreateUpdateCategoryBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUpdateCategoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        if (obCategory == null) {
            binding.tilUserId.setVisibility(View.VISIBLE);
        } else {
            binding.btSave.setText("Update");
            fillForm();
        }
    }

    private void initViews() {
        binding.btSave.setOnClickListener(view -> {
            if (obCategory == null) saveCategory();
            else updateCategory();
        });
    }

    private void fillForm() {
        binding.tilUserId.setVisibility(View.GONE);
        binding.tilCategoryName.getEditText().setText(obCategory.getName());
        binding.tilCategoryColor.getEditText().setText(obCategory.getColor() == null ? "" : obCategory.getColor());
        binding.tilParentCategoryId.getEditText().setText(obCategory.getParentCategoryId() == null ? "" : String.valueOf(obCategory.getParentCategoryId()));
    }

    private void saveCategory() {
        if (!validateSaveCategory()) {
            return;
        }
        final String userId = binding.tilUserId.getEditText().getText().toString().trim();
        final String color = binding.tilCategoryColor.getEditText().getText().toString().trim();
        final String parentId = binding.tilParentCategoryId.getEditText().getText().toString().trim();

        final OBCreateCategoryRequest request = new OBCreateCategoryRequest(
                userId.equals("") ? null : Integer.parseInt(userId),
                binding.tilCategoryName.getEditText().getText().toString(),
                color.equals("") ? null : color,
                parentId.equals("") ? null : Long.parseLong(parentId)
        );

        new OpenBankingPFMAPI().categoriesClient().create(request, new CreateCategoryListener() {
            @Override
            public void success(OBCategory category) {
                Toast.makeText(CreateUpdateCategoryActivity.this, "Category created!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateCategoryActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void updateCategory() {
        if (!validateUpdateCategory()) {
            return;
        }
        final String color = binding.tilCategoryColor.getEditText().getText().toString().trim();
        final String parentId = binding.tilParentCategoryId.getEditText().getText().toString().trim();

        final OBUpdateCategoryRequest request = new OBUpdateCategoryRequest(
                binding.tilCategoryName.getEditText().getText().toString(),
                color.equals("") ? null : color,
                parentId.equals("") ? null : Integer.parseInt(parentId)
        );

        new OpenBankingPFMAPI().categoriesClient().edit(obCategory.getId(), request, new UpdateCategoryListener() {
            @Override
            public void success(OBCategory category) {
                Toast.makeText(CreateUpdateCategoryActivity.this, "Category updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateCategoryActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private boolean validateSaveCategory() {
        return !binding.tilUserId.getEditText().getText().toString().trim().equals("") ||
                !binding.tilCategoryName.getEditText().getText().toString().trim().equals("");
    }

    private boolean validateUpdateCategory() {
        return !binding.tilCategoryName.getEditText().getText().toString().trim().equals("");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        obCategory = null;
    }

}