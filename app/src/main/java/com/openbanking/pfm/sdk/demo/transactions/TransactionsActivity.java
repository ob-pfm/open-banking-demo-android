package com.openbanking.pfm.sdk.demo.transactions;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.OBTransaction;
import com.openbanking.core.sdk.models.responses.OBTransactionsResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivityTransactionsBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;
import com.openbanking.pfm.sdk.modules.transactions.listeners.DeleteTransactionListener;
import com.openbanking.pfm.sdk.modules.transactions.listeners.GetTransactionListener;
import com.openbanking.pfm.sdk.modules.transactions.listeners.GetTransactionsListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TransactionsActivity extends AppCompatActivity implements TransactionAdapter.OnTransactionClickListener {

    private ActivityTransactionsBinding binding;

    private final List<OBTransaction> items = new ArrayList<>();

    private TransactionAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTransactionsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

        adapter = new TransactionAdapter(items, this);
        binding.rvTransactions.setAdapter(adapter);
    }

    private void addEvents() {
        binding.fabAddTransaction.setOnClickListener(view ->
                startActivity(new Intent(this, CreateUpdateTransactionActivity.class)));
        binding.btGetTransactions.setOnClickListener(view -> {
            final EditText etAccountId = findViewById(R.id.etAccountId);
            getTransactions(getSafeId(etAccountId.getText().toString()));
        });
        binding.btGetTransaction.setOnClickListener(view -> {
            final EditText etTransactionId = findViewById(R.id.etTransactionId);
            getTransaction(getSafeId(etTransactionId.getText().toString()));
        });
    }

    private int getSafeId(final String id){
        try {
            return Integer.parseInt(id);
        } catch (final Exception e){
            return 0;
        }
    }

    @Override
    public void onCopy(String transactionId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Transaction id", transactionId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(OBTransaction transaction) {
        CreateUpdateTransactionActivity.obTransaction = transaction;
        startActivity(new Intent(this, CreateUpdateTransactionActivity.class));
    }

    @Override
    public void onDelete(int id) {
        AlertUtils.confirm(this, "Delete", "Do you want to delete this transaction?", () -> deleteTransaction(id));
    }

    private void deleteTransaction(int id){
        new OpenBankingPFMAPI().transactionsClient().delete(id, new DeleteTransactionListener() {
            @Override
            public void success() {
                Toast.makeText(TransactionsActivity.this, "Transaction deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(TransactionsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getTransactions(final int accountId){
        new OpenBankingPFMAPI().transactionsClient().getList(accountId, new HashMap<>(), new GetTransactionsListener() {
            @Override
            public void success(@NonNull OBTransactionsResponse response) {
                items.clear();
                items.addAll(response.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(TransactionsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getTransaction(final int transactionId){
        new OpenBankingPFMAPI().transactionsClient().get(transactionId, new GetTransactionListener() {
            @Override
            public void success(OBTransaction transaction) {
                items.clear();
                items.add(transaction);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if(!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(TransactionsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

}
