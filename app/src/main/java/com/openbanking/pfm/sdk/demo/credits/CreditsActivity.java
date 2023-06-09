package com.openbanking.pfm.sdk.demo.credits;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openbanking.core.sdk.models.OBCredit;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.responses.OBCreditsResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.credits.listeners.GetCreditsListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCreditsBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class CreditsActivity extends AppCompatActivity {
    private ActivityCreditsBinding mBinding;

    private CreditsAdapter creditsAdapter;

    private final List<OBCredit> mCredits = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityCreditsBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        setTitle("Credits");
        setupCreditsRecyclerView();
        setListeners();
    }

    private void setupCreditsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.rvCredits.setLayoutManager(layoutManager);
        mBinding.rvCredits.setItemAnimator(new DefaultItemAnimator());
        creditsAdapter = new CreditsAdapter(mCredits);
        mBinding.rvCredits.setAdapter(creditsAdapter);
    }

    private void setListeners() {
        mBinding.btGetCredits.setOnClickListener(view -> {
            if (mBinding.etUserId.getText().toString().equals("")) {
                return;
            }

            mBinding.progressBar.setVisibility(View.VISIBLE);
            getCredits();
        });
    }

    private void getCredits() {
        new OpenBankingPFMAPI().creditsClient().getList(getUserId(), null,
                new GetCreditsListener() {
                    @Override
                    public void success(@NonNull OBCreditsResponse response) {
                        mBinding.progressBar.setVisibility(View.GONE);
                        Toast.makeText(CreditsActivity.this, "Creditos obtenidos!", Toast.LENGTH_SHORT).show();

                        updateBalance(response);
                        mCredits.clear();
                        mCredits.addAll(response.getCredits());
                        creditsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Creditos fallidos!\n-Titulo: " +
                                    error.getTitle() + "\n-Detalle: " + error.getDetail() + "\n-Código: " +
                                    error.getCode());
                        }
                    }

                    @Override
                    public void severError(Throwable serverError) {
                        showMessage("Fatal Error!", "Error de servidor!: " + serverError.getMessage());
                    }
                }
        );
    }

    private void updateBalance(OBCreditsResponse response) {
        mBinding.tvTotalBalance.setText("Disponible: " + response.getTotalBalance().getAvailableAmount().toString());
    }

    private Integer getUserId() {
        return Integer.parseInt(mBinding.etUserId.getText().toString());
    }

    private void showMessage(String title, String message) {
        mBinding.progressBar.setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }
}