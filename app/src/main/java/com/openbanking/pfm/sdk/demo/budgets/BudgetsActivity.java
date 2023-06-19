package com.openbanking.pfm.sdk.demo.budgets;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBBudget;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.responses.OBBudgetsResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.budgets.listeners.DeleteBudgetListener;
import com.openbanking.pfm.sdk.modules.budgets.listeners.GetBudgetListener;
import com.openbanking.pfm.sdk.modules.budgets.listeners.GetBudgetsListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityBudgetsBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class BudgetsActivity extends AppCompatActivity implements BudgetsAdapter.OnBudgetClickListener {
    private ActivityBudgetsBinding mBinding;

    private final List<OBBudget> items = new ArrayList<>();
    private BudgetsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityBudgetsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        mBinding.btGetBudgets.setOnClickListener(view -> {
            if (mBinding.etUserId.getText().toString().equals("")) {
                return;
            }
            mBinding.progressBar.setVisibility(View.VISIBLE);
            getBudgets();
        });

        mBinding.btGetBudget.setOnClickListener(view -> {
            if (mBinding.etBudgetId.getText().toString().equals("")) {
                return;
            }
            mBinding.progressBar.setVisibility(View.VISIBLE);
            getBudget();
        });

        mBinding.fabAddBudget.setOnClickListener(view -> {
            startActivity(new Intent(this, CreateUpdateBudgetsActivity.class));
        });

        adapter = new BudgetsAdapter(items, this);
        mBinding.rvBudgets.setAdapter(adapter);
    }

    private void getBudgets() {
        new OpenBankingPFMAPI().budgetsClient().getList(
                Integer.parseInt(mBinding.etUserId.getText().toString()),
                null,
                new GetBudgetsListener() {

                    @Override
                    public void success(OBBudgetsResponse response) {
                        mBinding.progressBar.setVisibility(View.GONE);

                        items.clear();
                        items.addAll(response.getBudgets());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budgets no obtenidos!\n-Titulo: " +
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

    private void getBudget() {
        new OpenBankingPFMAPI().budgetsClient().get(
                Integer.parseInt(mBinding.etBudgetId.getText().toString()),
                new GetBudgetListener() {

                    @Override
                    public void success(OBBudget budget) {
                        mBinding.progressBar.setVisibility(View.GONE);

                        items.clear();
                        items.add(budget);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget not obtained!\n-Title: " +
                                    error.getTitle() + "\n-Detail: " + error.getDetail() + "\n-Code: " +
                                    error.getCode());
                        }
                    }

                    @Override
                    public void severError(Throwable serverError) {
                        showMessage("Fatal Error!", "Server Error!: " + serverError.getMessage());
                    }
                });
    }

    @Override
    public void onCopy(String budgetId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Budget id", budgetId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(OBBudget budgetResponse) {
        CreateUpdateBudgetsActivity.budgetModel = budgetResponse;
        CreateUpdateBudgetsActivity.budgetId = budgetResponse.getId();

        Intent intent = new Intent(this, CreateUpdateBudgetsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDelete(int id) {
        new OpenBankingPFMAPI().budgetsClient().delete(
                id,
                new DeleteBudgetListener() {

                    @Override
                    public void success() {
                        mBinding.progressBar.setVisibility(View.GONE);
                        showMessage("Success!", "Budget deleted!");
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget not deleted!\n-Title: " +
                                    error.getTitle() + "\n-Detail: " + error.getDetail() + "\n-Code: " +
                                    error.getCode());
                        }
                    }

                    @Override
                    public void severError(Throwable serverError) {
                        showMessage("Fatal Error!", "Server Error!: " + serverError.getMessage());
                    }
                });
    }

    private void showMessage(String title, String message) {
        mBinding.progressBar.setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }
}