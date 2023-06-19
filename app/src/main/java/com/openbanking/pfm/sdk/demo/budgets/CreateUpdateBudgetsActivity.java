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
    public static Integer budgetId;

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
                if (!mBinding.tiUserId.getEditText().getText().toString().equals("")) {
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
        mBinding.tiPercentage.getEditText().setText(String.valueOf((Double) budgetModel.getWarningPercentage()));
    }

    private void createBudget() {
        OBCreateBudgetRequest budgetRequest = new OBCreateBudgetRequest(
                Integer.parseInt(mBinding.tiUserId.getEditText().getText().toString()),
                Integer.parseInt(mBinding.tiCategoryId.getEditText().getText().toString()),
                mBinding.tiName.getEditText().getText().toString(),
                Double.parseDouble(mBinding.tiAmount.getEditText().getText().toString()),
                Double.parseDouble(mBinding.tiPercentage.getEditText().getText().toString())
        );

        mBinding.progressBar.setVisibility(View.VISIBLE);
        new OpenBankingPFMAPI().budgetsClient().create(
                budgetRequest,
                new CreateBudgetListener() {

                    @Override
                    public void success(OBBudget budget) {
                        showMessage("Success!", "Budget created!");
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget not created!\n-Title: " +
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

    private void updateBudget() {
        String amountString = mBinding.tiAmount.getEditText().getText().toString();
        long amount = Long.parseLong(amountString);

        OBUpdateBudgetRequest budgetRequest = new OBUpdateBudgetRequest(
                mBinding.tiName.getEditText().getText().toString(),
                amount,
                Double.parseDouble(mBinding.tiPercentage.getEditText().getText().toString()),
                null
        );

        mBinding.progressBar.setVisibility(View.VISIBLE);

        new OpenBankingPFMAPI().budgetsClient().edit(
                budgetId,
                budgetRequest,
                new UpdateBudgetListener() {

                    @Override
                    public void success(OBBudget budget) {
                        showMessage("Success!", "Budget updated!");
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Budget not updated!\n-Title: " +
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        budgetModel = null;
    }
}