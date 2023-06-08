package com.openbanking.pfm.sdk.demo.consent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.openbanking.core.sdk.models.OBConsent;
import com.openbanking.pfm.sdk.demo.databinding.ActivityConsentDetailBinding;

public class ConsentDetailActivity extends AppCompatActivity {
    private ActivityConsentDetailBinding mBinding;

    public static OBConsent consentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityConsentDetailBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        fillConsent();
    }

    private void fillConsent() {
        mBinding.tvConsentId.setText("Consent ID: " + consentModel.getConsentId());
        mBinding.tvConsentBankName.setText("Bank name: " + consentModel.getOriginBankName());
        mBinding.tvConsentCPF.setText("CPF: " + consentModel.getCpf());
        mBinding.tvConsentCustomer.setText("Customer: " + consentModel.getCustomerIdentification());
        mBinding.tvConsentDeadline.setText("Dead line: " + consentModel.getDeadline());
        mBinding.tvConsentExpirationDay.setText("Expiration Day: " + consentModel.getExpirationDay());
        mBinding.tvConsentSync.setText("Synchronized: " + consentModel.isSynchronized());
        mBinding.tvConsentStatus.setText("Status: " + consentModel.getStatus());
        mBinding.tvConsentPurpose.setText("Purpose: " + consentModel.getPurpose());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        consentModel = null;
    }
}