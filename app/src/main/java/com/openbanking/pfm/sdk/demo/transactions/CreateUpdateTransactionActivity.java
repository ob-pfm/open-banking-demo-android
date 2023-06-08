package com.openbanking.pfm.sdk.demo.transactions;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.OBTransaction;
import com.openbanking.core.sdk.models.requests.OBCreateTransactionRequest;
import com.openbanking.core.sdk.models.requests.OBUpdateTransactionRequest;
import com.openbanking.core.sdk.shared.Constants;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.demo.databinding.ActivityCreateUpdateTransactionBinding;
import com.openbanking.pfm.sdk.demo.utils.DatePickerFragment;
import com.openbanking.pfm.sdk.modules.transactions.listeners.CreateTransactionListener;
import com.openbanking.pfm.sdk.modules.transactions.listeners.UpdateTransactionListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

public class CreateUpdateTransactionActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static OBTransaction obTransaction;

    private ActivityCreateUpdateTransactionBinding binding;

    private long dateSelected = new Date().getTime();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreateUpdateTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initViews();
        if(obTransaction == null){
            binding.etDate.setText(formatDate(dateSelected));
        } else {
            binding.btSave.setText("Update");
            fillForm();
        }
    }

    private void initViews() {
        binding.etDate.setOnClickListener(view -> new DatePickerFragment(this).show(getSupportFragmentManager(), "DatePicker"));
        binding.btSave.setOnClickListener(view -> {
            if(obTransaction == null) saveTransaction(); else updateTransaction();
        });
    }

    private void fillForm() {
        binding.tilAccountId.setVisibility(View.GONE);
        binding.etDate.setText(formatDate(obTransaction.getDate()));
        binding.cbIsChargeable.setChecked(obTransaction.getCharge());
        binding.tilDescription.getEditText().setText(obTransaction.getDescription());
        binding.tilAmount.getEditText().setText(String.valueOf(obTransaction.getAmount()));
        binding.tilCategoryId.getEditText().setText(String.valueOf(obTransaction.getCategoryId()));
        dateSelected = obTransaction.getDate();
    }

    private String formatDate(final Long time){
        final Date date = new Date();
        date.setTime(time);
        return new SimpleDateFormat("dd-MM-yyyy", new Locale(Constants.DateFormat.LOCALE_LANGUAGE)).format(date);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        final GregorianCalendar calendar = new GregorianCalendar(year, month, dayOfMonth);
        binding.etDate.setText(formatDate(calendar.getTimeInMillis()));
        dateSelected = calendar.getTimeInMillis();
    }

    private void saveTransaction(){
        if(!validateSaveTransaction()){
            return;
        }
        final Integer categoryId = binding.tilCategoryId.getEditText().getText().toString().equals("") ? null :
                Integer.parseInt(binding.tilCategoryId.getEditText().getText().toString());

        final OBCreateTransactionRequest request = new OBCreateTransactionRequest(
                Integer.parseInt(binding.tilAccountId.getEditText().getText().toString()),
                dateSelected,
                binding.cbIsChargeable.isChecked(),
                binding.tilDescription.getEditText().getText().toString(),
                Double.parseDouble(binding.tilAmount.getEditText().getText().toString()),
                categoryId
        );
        new OpenBankingPFMAPI().transactionsClient().create(request, new CreateTransactionListener() {
            @Override
            public void success(OBTransaction transaction) {
                Toast.makeText(CreateUpdateTransactionActivity.this, "Transaction created!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateTransactionActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void updateTransaction(){
        if(!validateUpdateTransaction()){
            return;
        }
        final int categoryId = binding.tilCategoryId.getEditText().getText().toString().isEmpty() ||
                binding.tilCategoryId.getEditText().getText().toString().equals("0") ? null :
                Integer.parseInt(binding.tilCategoryId.getEditText().getText().toString());

        final OBUpdateTransactionRequest request = new OBUpdateTransactionRequest(
                dateSelected,
                binding.cbIsChargeable.isChecked(),
                binding.tilDescription.getEditText().getText().toString(),
                Double.parseDouble(binding.tilAmount.getEditText().getText().toString()),
                categoryId
        );
        new OpenBankingPFMAPI().transactionsClient().edit(obTransaction.getId(), request, new UpdateTransactionListener() {
            @Override
            public void success(OBTransaction transaction) {
                Toast.makeText(CreateUpdateTransactionActivity.this, "Transaction updated!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(CreateUpdateTransactionActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private boolean validateSaveTransaction() {
        return !binding.tilAccountId.getEditText().getText().toString().isEmpty() &&
                !binding.tilDescription.getEditText().getText().toString().isEmpty() &&
                !binding.tilAmount.getEditText().getText().toString().isEmpty();
    }

    private boolean validateUpdateTransaction() {
        return !binding.tilDescription.getEditText().getText().toString().isEmpty() &&
                !binding.tilAmount.getEditText().getText().toString().isEmpty();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        obTransaction = null;
    }

}