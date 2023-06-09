package com.openbanking.pfm.sdk.demo.budgets;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBBudget;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.requests.OBCreateBudgetRequest;
import com.openbanking.core.sdk.models.requests.OBUpdateBudgetRequest;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.budgets.listeners.CreateBudgetListener;
import com.openbanking.pfm.sdk.modules.budgets.listeners.UpdateBudgetListener;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCreateUpdateBudgetsBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.List;

public class CreateUpdateBudgetsActivity extends AppCompatActivity {
    public static OBBudget budgetModel;

    private ActivityCreateUpdateBudgetsBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCreateUpdateBudgetsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
        if (budgetModel != null) {
            mBinding.btnCreateUpdateBudget.setText("Update");
            fillBudget();
        }
    }

    private void setup() {
        mBinding.btnCreateUpdateBudget.setOnClickListener(view -> {
            findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
            if (budgetModel == null) {
                if (!mBinding.tiUBudgetId.getEditText().getText().toString().equals("")) {
                    createBudget();
                }
            } else {
                updateBudget();
            }
        });
    }

    private void fillBudget() {
        mBinding.tiCategoryId.getEditText().setText(String.valueOf(budgetModel.getCategoryId()));
        mBinding.tiName.getEditText().setText(budgetModel.getName());
        mBinding.tiAmount.getEditText().setText(String.valueOf((int) budgetModel.getAmount()));
    }

    private void createBudget() {
        OBCreateBudgetRequest budgetRequest = new OBCreateBudgetRequest(
                Integer.parseInt(mBinding.tiUBudgetId.getEditText().getText().toString()),
                Integer.parseInt(mBinding.tiCategoryId.getEditText().getText().toString()),
                mBinding.tiName.getEditText().getText().toString(),
                Double.parseDouble(mBinding.tiAmount.getEditText().getText().toString()),
                1.0
        );

        new OpenBankingPFMAPI().budgetsClient().create(
                budgetRequest,
                new CreateBudgetListener() {

                    @Override
                    public void success(OBBudget budget) {
                        showMessage("Exito!", "Budget creado!");
                        String budgetText = budget.getName() + ": " + budget.getId();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget no creado!\n-Titulo: " +
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

    private void updateBudget() {
        OBUpdateBudgetRequest budgetRequest = new OBUpdateBudgetRequest(
                mBinding.tiName.getEditText().getText().toString(),
                Double.parseDouble(mBinding.tiAmount.getEditText().getText().toString()),
                1.0,
                Long.parseLong(mBinding.tiCategoryId.getEditText().toString())
        );

        new OpenBankingPFMAPI().budgetsClient().edit(
                Integer.parseInt(mBinding.tiUBudgetId.getEditText().getText().toString()),
                budgetRequest,
                new UpdateBudgetListener() {

                    @Override
                    public void success(OBBudget budget) {
                        showMessage("Exito!", "Budget actualizado!");
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget no actualizado!\n-Titulo: " +
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

    private void showMessage(String title, String message) {
        findViewById(R.id.progressBar).setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        budgetModel = null;
    }
}