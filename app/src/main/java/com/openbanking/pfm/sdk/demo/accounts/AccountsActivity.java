package com.openbanking.pfm.sdk.demo.accounts;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.openbanking.core.sdk.models.OBAccount;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.responses.OBAccountsResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.accounts.listeners.DeleteAccountListener;
import com.openbanking.pfm.sdk.modules.accounts.listeners.GetAccountListener;
import com.openbanking.pfm.sdk.modules.accounts.listeners.GetAccountsListener;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivityAccountsBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class AccountsActivity extends AppCompatActivity implements AccountsAdapter.OnAccountsClickListener {

    private ActivityAccountsBinding binding;

    private final List<OBAccount> items = new ArrayList<>();

    private AccountsAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAccountsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        addEvents();

        adapter = new AccountsAdapter(items, this);
        binding.rvAccounts.setAdapter(adapter);
    }

    private void addEvents() {
        binding.fabAddAccount.setOnClickListener(view -> {
            startActivity(new Intent(this, CreateUpdateAccountActivity.class));
        });
        binding.btGetAccounts.setOnClickListener(view -> {
            final EditText etUserId = findViewById(R.id.etUserId);
            getAccounts(getSafeId(etUserId.getText().toString()));
        });
        binding.btGetAccount.setOnClickListener(view -> {
            final EditText etAccountId = findViewById(R.id.etAccountId);
            getAccount(getSafeId(etAccountId.getText().toString()));
        });
    }

    private int getSafeId(final String id) {
        try {
            return Integer.parseInt(id);
        } catch (final Exception e) {
            return 0;
        }
    }

    @Override
    public void onCopy(String accountId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Account id", accountId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onUpdate(OBAccount OBAccount) {
        CreateUpdateAccountActivity.obAccount = OBAccount;
        startActivity(new Intent(this, CreateUpdateAccountActivity.class));
    }

    @Override
    public void onDelete(int id) {
        AlertUtils.confirm(this, "Delete", "Do you want to delete this account?", () -> deleteAccount(id));
    }

    private void deleteAccount(int id) {
        new OpenBankingPFMAPI().accountsClient().delete(id, new DeleteAccountListener() {
            @Override
            public void success() {
                Toast.makeText(AccountsActivity.this, "Account deleted", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(AccountsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getAccounts(final int userId) {
        new OpenBankingPFMAPI().accountsClient().getList(userId, true, 0, new GetAccountsListener() {
            @Override
            public void success(@NonNull OBAccountsResponse response) {
                items.clear();
                items.addAll(response.getData());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(AccountsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

    private void getAccount(final int accountId) {
        new OpenBankingPFMAPI().accountsClient().get(accountId, new GetAccountListener() {
            @Override
            public void success(OBAccount account) {
                items.clear();
                items.add(account);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(List<OBError> errors) {
                if (!errors.isEmpty()) {
                    final OBError error = errors.get(0);
                    Toast.makeText(AccountsActivity.this, error.getDetail(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void severError(Throwable serverError) {
                Log.e("ERROR", serverError.getMessage());
            }
        });
    }

}