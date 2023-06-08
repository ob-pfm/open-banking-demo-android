package com.openbanking.pfm.sdk.demo.accounts;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.openbanking.core.sdk.models.OBAccount;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.requests.OBCreateAccountRequest;
import com.openbanking.core.sdk.models.requests.OBUpdateAccountRequest;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.accounts.listeners.CreateAccountListener;
import com.openbanking.pfm.sdk.modules.accounts.listeners.UpdateAccountListener;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCreateUpdateAccountBinding;

import java.util.List;

public class CreateUpdateAccountActivity extends AppCompatActivity {

    public static OBAccount obAccount;

    private ActivityCreateUpdateAccountBinding binding;

    private ArrayAdapter<String> natureArrayAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUpdateAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        if(obAccount != null){
            binding.btSave.setText("Update");
            fillForm();
        }
    }

    private void initViews(){
        this.natureArrayAdapter = new ArrayAdapter<>(
                this,android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.nature_list));
        binding.spNature.setAdapter(this.natureArrayAdapter);

        binding.btSave.setOnClickListener(view -> {
            if(obAccount == null){
                saveAccount();
            } else {
                updateAccount();
            }
        });
    }

    private void fillForm(){
        binding.tilUserId.setVisibility(View.GONE);
        binding.tilFinancialEntityId.setVisibility(View.GONE);

        int position = this.natureArrayAdapter.getPosition(obAccount.getNature());
        if (position >= 0) {
            binding.spNature.setSelection(position);
        }
        binding.tilName.getEditText().setText(obAccount.getName());
        binding.tilNumber.getEditText().setText(obAccount.getNumber());
        binding.tilBalance.getEditText().setText(String.valueOf(obAccount.getBalance()));
        binding.cbIsChargeable.setChecked(obAccount.getChargeable());
    }

    private void saveAccount(){
        if(!isValidSaveForm()){
            return;
        }
        final OBCreateAccountRequest request = new OBCreateAccountRequest(
                Integer.parseInt(binding.tilUserId.getEditText().getText().toString()),
                Integer.parseInt(binding.tilFinancialEntityId.getEditText().getText().toString()),
                binding.spNature.getSelectedItem().toString(),
                binding.tilName.getEditText().getText().toString(),
                binding.tilNumber.getEditText().getText().toString(),
                Double.parseDouble(binding.tilBalance.getEditText().getText().toString()),
                binding.cbIsChargeable.isChecked()
        );

        new OpenBankingPFMAPI().accountsClient().create(request, new CreateAccountListener() {
            @Override
            public void success(OBAccount account) {
                Toast.makeText(CreateUpdateAccountActivity.this, "Account Created", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateAccountActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void updateAccount(){
        if(!isValidUpdateForm()){
            return;
        }
        final OBUpdateAccountRequest request = new OBUpdateAccountRequest(
                binding.spNature.getSelectedItem().toString(),
                binding.tilName.getEditText().getText().toString(),
                binding.tilNumber.getEditText().getText().toString(),
                Double.parseDouble(binding.tilBalance.getEditText().getText().toString()),
                binding.cbIsChargeable.isChecked()
        );

        new OpenBankingPFMAPI().accountsClient().update(obAccount.getId(), request, new UpdateAccountListener() {
            @Override
            public void success(OBAccount obAccount) {
                Toast.makeText(CreateUpdateAccountActivity.this, "Account Updated", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateAccountActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private boolean isValidSaveForm(){
        return !binding.tilUserId.getEditText().getText().toString().isEmpty() &&
                !binding.tilFinancialEntityId.getEditText().getText().toString().isEmpty() &&
                !binding.tilName.getEditText().getText().toString().isEmpty() &&
                !binding.tilNumber.getEditText().getText().toString().isEmpty() &&
                !binding.tilBalance.getEditText().getText().toString().isEmpty();
    }

    private boolean isValidUpdateForm(){
        return !binding.tilName.getEditText().getText().toString().isEmpty() &&
                !binding.tilNumber.getEditText().getText().toString().isEmpty() &&
                !binding.tilBalance.getEditText().getText().toString().isEmpty();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        obAccount = null;
    }

}
