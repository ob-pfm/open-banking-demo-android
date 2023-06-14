package com.openbanking.pfm.sdk.demo.financial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.OBFinancialEntity;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.demo.databinding.ActivityFinancialEntitiesBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;
import com.openbanking.pfm.sdk.modules.users.listeners.GetFinancialEntitiesListener;

import java.util.ArrayList;
import java.util.List;

public class FinancialEntitiesActivity extends AppCompatActivity implements FinancialEntitiesAdapter.OnFinancialEntityClickListener {
    private ActivityFinancialEntitiesBinding mBinding;

    private final List<OBFinancialEntity> items = new ArrayList<>();
    private FinancialEntitiesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityFinancialEntitiesBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        mBinding.btGetFinancialEntities.setOnClickListener(view -> {
            mBinding.progressBar.setVisibility(View.VISIBLE);
            getFinancialEntities();
        });

        adapter = new FinancialEntitiesAdapter(items, this);
        mBinding.rvFinancialEntities.setAdapter(adapter);
    }

    private void getFinancialEntities() {
        new OpenBankingPFMAPI().usersClient().getFinancialEntities(
                null,
                new GetFinancialEntitiesListener() {

                    @Override
                    public void success(List<OBFinancialEntity> financialEntities) {
                        mBinding.progressBar.setVisibility(View.GONE);

                        items.clear();
                        items.addAll(financialEntities);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "financial entities no obtenidos!\n-Titulo: " +
                                    error.getTitle() + "\n-Detalle: " + error.getDetail() + "\n-Código: " +
                                    error.getCode());
                        }
                    }

                    @Override
                    public void severError(Throwable serverError) {
                        showMessage("Fatal Error!", "Error de servidor!: " + serverError.getMessage());
                    }
                });
    }

    @Override
    public void onCopy(String financialEntityId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Financial entity id", financialEntityId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String title, String message) {
        mBinding.progressBar.setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }
}