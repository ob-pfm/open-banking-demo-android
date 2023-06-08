package com.openbanking.pfm.sdk.demo.summary;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.OBResumeByMonth;
import com.openbanking.core.sdk.models.responses.OBSummaryResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivitySummaryBinding;
import com.openbanking.pfm.sdk.modules.insights.listeners.GetSummaryListener;

import java.util.ArrayList;
import java.util.List;

public class SummaryActivity extends AppCompatActivity {
    private ActivitySummaryBinding mBinding;

    private final List<OBResumeByMonth> items = new ArrayList<>();
    private SummaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivitySummaryBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        adapter = new SummaryAdapter(items);
        mBinding.rvSummary.setAdapter(adapter);

        mBinding.btGetSummary.setOnClickListener(view -> {
            if (!mBinding.etUserId.getText().toString().isEmpty()) {
                findViewById(R.id.progressBar).setVisibility(View.VISIBLE);
                getSummary();
            }
        });
    }

    private void getSummary() {
        new OpenBankingPFMAPI().insightsClient().getResume(
                Integer.parseInt(mBinding.etUserId.getText().toString()),
                null,
                null,
                null,
                new GetSummaryListener() {

                    @Override
                    public void success(OBSummaryResponse response) {
                        findViewById(R.id.progressBar).setVisibility(View.GONE);

                        items.clear();
                        items.addAll(response.getExpenses());
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Resumen no obtenido!\n-Titulo: " +
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
        Log.d("SERVICE", message);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Ok", (dialog, view) -> dialog.dismiss())
                .setCancelable(false)
                .show();
    }
}