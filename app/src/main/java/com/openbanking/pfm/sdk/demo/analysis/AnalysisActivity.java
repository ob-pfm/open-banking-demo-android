package com.openbanking.pfm.sdk.demo.analysis;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBAnalysisByMonth;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.insights.listeners.GetAnalysisListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityAnalysisBinding;

import java.util.ArrayList;
import java.util.List;

public class AnalysisActivity extends AppCompatActivity {

    private ActivityAnalysisBinding binding;

    private AnalysisAdapter adapter;

    private final List<OBAnalysisByMonth> items = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAnalysisBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adapter = new AnalysisAdapter(items);
        binding.rvAnalysis.setAdapter(adapter);
        binding.ibSearch.setOnClickListener(view -> getAnalysis());
    }

    private void getAnalysis() {
        final String userIdString = binding.tilUserId.getEditText().getText().toString();
        if (userIdString.equals("")) return;

        new OpenBankingPFMAPI().insightsClient().getAnalysis(Integer.parseInt(userIdString), null, null, null, new GetAnalysisListener() {
            @Override
            public void success(List<OBAnalysisByMonth> response) {
                items.clear();
                items.addAll(response);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(AnalysisActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

}