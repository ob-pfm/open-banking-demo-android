package com.openbanking.pfm.sdk.demo.users;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.OBUser;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.users.listeners.GetUserListener;
import com.openbanking.pfm.sdk.demo.databinding.ActivityUsersBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UsersAdapter.OnUserClickListener {

    private ActivityUsersBinding mBinding;

    private final List<OBUser> items = new ArrayList<>();
    private UsersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mBinding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    private void setup() {
        mBinding.btGetUser.setOnClickListener(view -> {
            if (mBinding.etUserId.getText().toString().equals("")) {
                return;
            }
            mBinding.progressBar.setVisibility(View.VISIBLE);
            getUser();
        });

        adapter = new UsersAdapter(items, this);
        mBinding.rvUsers.setAdapter(adapter);
    }

    private void getUser() {
        new OpenBankingPFMAPI().usersClient().get(
                Integer.parseInt(mBinding.etUserId.getText().toString()),
                new GetUserListener() {
                    @Override
                    public void success(OBUser user) {
                        mBinding.progressBar.setVisibility(View.GONE);

                        items.clear();
                        items.add(user);
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "User no obtenidos!\n-Titulo: " +
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

    @Override
    public void onCopy(String user) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("User id", user);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    private void showMessage(String title, String message) {
        mBinding.progressBar.setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }
}