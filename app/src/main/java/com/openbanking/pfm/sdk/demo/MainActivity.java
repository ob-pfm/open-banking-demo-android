package com.openbanking.pfm.sdk.demo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.openbanking.core.sdk.core.OpenBankingCore;
import com.openbanking.core.sdk.shared.enums.Environment;
import com.openbanking.core.sdk.shared.enums.LogLevel;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.demo.accounts.AccountsActivity;
import com.openbanking.pfm.sdk.demo.analysis.AnalysisActivity;
import com.openbanking.pfm.sdk.demo.budgets.BudgetsActivity;
import com.openbanking.pfm.sdk.demo.categories.CategoriesActivity;
import com.openbanking.pfm.sdk.demo.consent.ConsentActivity;
import com.openbanking.pfm.sdk.demo.credits.CreditsActivity;
import com.openbanking.pfm.sdk.demo.databinding.ActivityMainBinding;
import com.openbanking.pfm.sdk.demo.financial.FinancialEntitiesActivity;
import com.openbanking.pfm.sdk.demo.summary.SummaryActivity;
import com.openbanking.pfm.sdk.demo.transactions.TransactionsActivity;
import com.openbanking.pfm.sdk.demo.users.UsersActivity;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        configSDK();
        setListeners();
    }

    private void configSDK() {
        final OpenBankingCore openBankingCore = OpenBankingCore.Companion.getShared();
        openBankingCore.setApiKey("your_api_key");
        openBankingCore.configure();

        OpenBankingPFMAPI.Companion.getShared().init();
    }

    private void setListeners() {
        mBinding.btGoUsersSDK.setOnClickListener(view -> goNextClass(UsersActivity.class));
        mBinding.btGoAvailableBanksSDK.setOnClickListener(view -> goNextClass(ConsentActivity.class));
        mBinding.btGoCategoriesSDK.setOnClickListener(view -> goNextClass(CategoriesActivity.class));
        mBinding.btGoCreditsSDK.setOnClickListener(view -> goNextClass(CreditsActivity.class));
        mBinding.btGoSummarySDK.setOnClickListener(view -> goNextClass(SummaryActivity.class));
        mBinding.btGoTransactionSDK.setOnClickListener(view -> goNextClass(TransactionsActivity.class));
        mBinding.btGoAnalysisSDK.setOnClickListener(view -> goNextClass(AnalysisActivity.class));
        mBinding.btGoAccountSDK.setOnClickListener(view -> goNextClass(AccountsActivity.class));
        mBinding.btGoBudgetsSDK.setOnClickListener(view -> goNextClass(BudgetsActivity.class));
        mBinding.btGoFinancialEntitiesSDK.setOnClickListener(view -> goNextClass(FinancialEntitiesActivity.class));
    }

    @SuppressWarnings("rawtypes")
    private void goNextClass(final Class _class) {
        final Intent intent = new Intent(this, _class);
        startActivity(intent);
    }
}