package com.openbanking.pfm.sdk.demo.consent;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.openbanking.core.sdk.models.OBBank;
import com.openbanking.core.sdk.models.OBConsent;
import com.openbanking.core.sdk.models.OBError;
import com.openbanking.core.sdk.models.responses.OBAggregationStatusResponse;
import com.openbanking.core.sdk.models.responses.OBBanksAvailableResponse;
import com.openbanking.core.sdk.models.responses.OBConsentsResponse;
import com.openbanking.core.sdk.models.responses.OBCreateConsentResponse;
import com.openbanking.core.sdk.models.responses.OBResourcesResponse;
import com.openbanking.pfm.sdk.core.OpenBankingPFMAPI;
import com.openbanking.pfm.sdk.modules.aggregation.listeners.CreateConsentListener;
import com.openbanking.pfm.sdk.modules.aggregation.listeners.GetAggregationStatusListener;
import com.openbanking.pfm.sdk.modules.aggregation.listeners.GetAvailableBanksListener;
import com.openbanking.pfm.sdk.modules.aggregation.listeners.GetResourcesListener;
import com.openbanking.pfm.sdk.modules.aggregation.listeners.SynchronizeListener;
import com.openbanking.pfm.sdk.modules.consents.listeners.DeleteConsentListener;
import com.openbanking.pfm.sdk.modules.consents.listeners.GetConsentListener;
import com.openbanking.pfm.sdk.modules.consents.listeners.GetConsentsListener;
import com.openbanking.pfm.sdk.demo.R;
import com.openbanking.pfm.sdk.demo.databinding.ActivityConsentBinding;
import com.openbanking.pfm.sdk.demo.databinding.BottomSheetBanksBinding;
import com.openbanking.pfm.sdk.demo.databinding.BottomSheetResourcesBinding;
import com.openbanking.pfm.sdk.demo.databinding.PopupDialogBinding;
import com.openbanking.pfm.sdk.demo.utils.AlertUtils;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ConsentActivity extends AppCompatActivity implements BanksAvailableAdapter.OnBankClickListener,
        ConsentsAdapter.OnConsentClickListener {
    private ActivityConsentBinding mBinding;
    private BottomSheetBanksBinding mBottomSheetBanksBinding;
    private BottomSheetResourcesBinding mBottomSheetResourcesBinding;
    private PopupDialogBinding mPopupDialogBinding;

    private BottomSheetDialog bottomBanksSheetDialog;
    private BottomSheetDialog bottomResourcesSheetDialog;

    private AlertDialog popupDialog;

    private Timer mTimer;
    private TimerTask mTimerTask;

    private AggregationStatusAsyncTask mAggregationStatusAsyncTask;
    private ConsentsAsyncTask mConsentsAsyncTask;

    private String timeSelected;

    private final List<OBConsent> mConsents = new ArrayList<>();

    private final List<OBBank> banks = new ArrayList<>();
    private BanksAvailableAdapter adapterBanks;

    private ResourcesAdapter resourcesAdapter;
    private ConsentsAdapter consentsAdapter;

    private OBBank bankReponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityConsentBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());

        setup();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cancelAsyncTask();
    }

    private void setup() {
        setTitle("Consent");
        configMenu();
        setListeners();
        initBanksBottomSheet();
        initResourcesBottomSheet();
        initPopupView();

        setupConsentsRecyclerView();
        adapterBanks = new BanksAvailableAdapter(banks, this);
    }

    private void configMenu() {
        mBinding.fabCreateConsent.setOnClickListener(view -> {
            PopupMenu popup = new PopupMenu(ConsentActivity.this, mBinding.fabCreateConsent);
            popup.inflate(R.menu.custom_menu);

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.itemConsent:
                        getAvailableBanks();
                        return true;
                    case R.id.itemResources:
                        getBankResources();
                        return true;
                    default:
                        return false;
                }
            });

            popup.show();
        });
    }

    private void setListeners() {
        mBinding.btGetConsents.setOnClickListener(view -> {
            if (mBinding.etUserId.getText().toString().equals("")) {
                return;
            }
            getConsents();
        });

        mBinding.btGetConsent.setOnClickListener(view -> {
            if (mBinding.etConsenttId.getText().toString().equals("")) {
                return;
            }
            getConsent();
        });
    }

    private int getUserId() {
        return Integer.parseInt(mBinding.etUserId.getText().toString());
    }

    private String getConsentId() {
        return mBinding.etConsenttId.getText().toString();
    }

    private void initBanksBottomSheet() {
        bottomBanksSheetDialog = new BottomSheetDialog(
                ConsentActivity.this, R.style.AppBottomSheetDialogTheme);

        mBottomSheetBanksBinding = BottomSheetBanksBinding.inflate(getLayoutInflater(), null, false);
        bottomBanksSheetDialog.setContentView(mBottomSheetBanksBinding.getRoot());
    }

    private void initResourcesBottomSheet() {
        bottomResourcesSheetDialog = new BottomSheetDialog(
                ConsentActivity.this, R.style.AppBottomSheetDialogTheme);

        mBottomSheetResourcesBinding = BottomSheetResourcesBinding.inflate(getLayoutInflater(), null, false);
        bottomResourcesSheetDialog.setContentView(mBottomSheetResourcesBinding.getRoot());
    }

    private void initPopupView() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        mPopupDialogBinding = PopupDialogBinding.inflate(getLayoutInflater());
        builder.setView(mPopupDialogBinding.getRoot());
        popupDialog = builder.create();

        String[] spinnerData = {"3", "6", "12"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerData);
        mPopupDialogBinding.spinner.setAdapter(adapter);

        mPopupDialogBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                timeSelected = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        mPopupDialogBinding.btConfirm.setOnClickListener(view -> {
            popupDialog.dismiss();
            bottomBanksSheetDialog.dismiss();

            Integer userId = getUserId();
            Integer time = Integer.parseInt(timeSelected);
            createConsent(userId, time);
        });
    }

    private void showBanksAvailable() {
        bottomBanksSheetDialog.show();
    }

    private void showPopupDialog() {
        mPopupDialogBinding.tvBankName.setText(bankReponse.getName());
        popupDialog.show();
    }

    private void setupBanksRecyclerView(final OBBanksAvailableResponse response) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mBottomSheetBanksBinding.rvBanks.setLayoutManager(layoutManager);
        mBottomSheetBanksBinding.rvBanks.setItemAnimator(new DefaultItemAnimator());
        adapterBanks = new BanksAvailableAdapter(response.getBanks(), this);
        mBottomSheetBanksBinding.rvBanks.setAdapter(adapterBanks);
    }

    private void getConsents() {
        if (mConsentsAsyncTask != null) {
            mConsentsAsyncTask.cancel(true);
            mConsentsAsyncTask = null;
        }

        mConsentsAsyncTask = new ConsentsAsyncTask();
        mConsentsAsyncTask.execute();
    }

    private void getConsent() {
        new OpenBankingPFMAPI().consentsClient().get(getConsentId(),
                new GetConsentListener() {
                    @Override
                    public void success(@NonNull OBConsent consent) {
                        showMessage("Exito!", "Consentimiento obtenido!");

                        mConsents.clear();
                        mConsents.add(consent);
                        consentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Consentimiento no obtenido!\n-Titulo: " +
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

    private void getAvailableBanks() {
        mBinding.progressBar.setVisibility(View.VISIBLE);

        new OpenBankingPFMAPI().banksClient().getAvailables(
                getUserId(),
                new GetAvailableBanksListener() {

                    @Override
                    public void success(@NonNull OBBanksAvailableResponse response) {
                        mBinding.progressBar.setVisibility(View.GONE);

                        setupBanksRecyclerView(response);
                        showBanksAvailable();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Bancos no obtenidos!\n-Titulo: " +
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

    @Override
    public void onTap(OBBank bankReponse) {
        this.bankReponse = bankReponse;
        showPopupDialog();
    }

    @Override
    public void onCopy(String consentId) {
        final ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        final ClipData clip = ClipData.newPlainText("Consent id: ", consentId);
        clipboard.setPrimaryClip(clip);
        Toast.makeText(this, "Id copied", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onEdit(OBConsent consentResponse) {
        ConsentDetailActivity.consentModel = consentResponse;
        Intent intent = new Intent(this, ConsentDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onDelete(String id) {
        mBinding.progressBar.setVisibility(View.VISIBLE);

        new OpenBankingPFMAPI().consentsClient().delete(getConsentId(),
                new DeleteConsentListener() {
                    @Override
                    public void success() {
                        mBinding.progressBar.setVisibility(View.GONE);
                        showMessage("Exito!", "Consentimiento eliminado!");

                        mConsents.clear();
                        consentsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Consentimiento no eliminado!\n-Titulo: " +
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

    private class ConsentsAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            new OpenBankingPFMAPI().consentsClient().getList(
                    getUserId(),
                    null,
                    null,
                    new GetConsentsListener() {

                        @Override
                        public void success(@NonNull OBConsentsResponse response) {
                            mConsents.clear();
                            mConsents.addAll(response.getConsents());
                            consentsAdapter.notifyDataSetChanged();

                            showMessage("Exito!", "Consentimientos obtenidos!");
                        }

                        @Override
                        public void error(List<OBError> errors) {
                            if (!errors.isEmpty()) {
                                final OBError error = errors.get(0);
                                showMessage("Error!", "Consentimientos no obtenidos!\n-Titulo: " +
                                        error.getTitle() + "\n-Detalle: " + error.getDetail() + "\n-Código: " +
                                        error.getCode());
                            }
                        }

                        @Override
                        public void severError(Throwable serverError) {
                            showMessage("Fatal Error!", "Error de servidor!: " + serverError.getMessage());
                        }
                    });

            return null;
        }
    }

    private void setupConsentsRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mBinding.rvConsents.setLayoutManager(layoutManager);
        mBinding.rvConsents.setItemAnimator(new DefaultItemAnimator());
        consentsAdapter = new ConsentsAdapter(mConsents, this);
        mBinding.rvConsents.setAdapter(consentsAdapter);
    }

    private void createConsent(final Integer userId, final Integer time) {
        mBinding.progressBar.setVisibility(View.VISIBLE);

        new OpenBankingPFMAPI().banksClient().createConsent(
                bankReponse.getBankId(),
                userId,
                time,
                new CreateConsentListener() {

                    @Override
                    public void success(@NonNull OBCreateConsentResponse consent) {
                        mBinding.progressBar.setVisibility(View.GONE);
                        bottomBanksSheetDialog.dismiss();

                        showMessage("Exito!", "Consent creado!");
                        Log.i("Consentimiento: ", consent.toString());

                        getAggregationStatus();
                        goToWebviewConsumeConsent(consent);
                    }

                    @Override
                    public void error(List<OBError> errors) {
                        bottomBanksSheetDialog.dismiss();

                        if (!errors.isEmpty()) {
                            final OBError error = errors.get(0);
                            showMessage("Error!", "Consentimiento no creado!\n-Titulo: " +
                                    error.getTitle() + "\n-Detalle: " + error.getDetail() + "\n-Código: " +
                                    error.getCode());
                        }
                    }

                    @Override
                    public void severError(Throwable serverError) {
                        bottomBanksSheetDialog.dismiss();
                        showMessage("Fatal Error!", "Error de servidor!: " + serverError.getMessage());
                    }
                });
    }

    private void getAggregationStatus() {
        new TimerTaskHandler().execute();
    }

    private class TimerTaskHandler extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    mAggregationStatusAsyncTask = new AggregationStatusAsyncTask();
                    mAggregationStatusAsyncTask.execute();
                }
            };

            mTimer = new Timer();
            mTimer.schedule(mTimerTask, 0, 10000);

            return null;
        }
    }

    private class AggregationStatusAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            new OpenBankingPFMAPI().banksClient().getAggregationStatus(
                    bankReponse.getBankId(),
                    getUserId(),
                    new GetAggregationStatusListener() {
                        @Override
                        public void success(@NonNull OBAggregationStatusResponse response) {
                            Toast.makeText(ConsentActivity.this,
                                    "Estatus: " + response.getStatus(), Toast.LENGTH_SHORT).show();
                            checkStatus(response);
                        }

                        @Override
                        public void error(List<OBError> errors) {
                            bottomBanksSheetDialog.dismiss();

                            if (!errors.isEmpty()) {
                                final OBError error = errors.get(0);
                                showMessage("Error!", "Estatus no obtenido!\n-Titulo: " +
                                        error.getTitle() + "\n-Detalle: " + error.getDetail() + "\n-Código: " +
                                        error.getCode());
                            }
                        }

                        @Override
                        public void severError(Throwable serverError) {
                            bottomBanksSheetDialog.dismiss();
                            showMessage("Fatal Error!", "Error de servidor!: " + serverError.getMessage());
                        }
                    }
            );

            return null;
        }
    }

    private void goToWebviewConsumeConsent(final OBCreateConsentResponse consent) {
        if (consent != null && !consent.getUrl().isEmpty()) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(ConsentActivity.this, Uri.parse(consent.getUrl()));
        }
    }

    private void checkStatus(final OBAggregationStatusResponse aggregationStatusResponse) {
        if (aggregationStatusResponse.getStatus().equals("CONSENT_AUTHORISED")) {
            cancelAsyncTask();
        }
    }

    private void getBankResources() {
        if (bankReponse != null && !bankReponse.getBankId().isEmpty()) {
            mBinding.progressBar.setVisibility(View.VISIBLE);

            new OpenBankingPFMAPI().banksClient().getResources(
                    bankReponse.getBankId(),
                    getUserId(),
                    new GetResourcesListener() {
                        @Override
                        public void success(@NonNull OBResourcesResponse response) {
                            synchronize();
                            setupResourcesRecyclerView(response);
                            Toast.makeText(ConsentActivity.this, "Recursos obtenidos!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error(List<OBError> errors) {
                            if (!errors.isEmpty()) {
                                final OBError error = errors.get(0);
                                showMessage("Error!", "Recursos no obtenidos!\n-Titulo: " +
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
    }

    private void setupResourcesRecyclerView(final OBResourcesResponse response) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mBottomSheetResourcesBinding.rvResources.setLayoutManager(layoutManager);
        mBottomSheetResourcesBinding.rvResources.setItemAnimator(new DefaultItemAnimator());
        resourcesAdapter = new ResourcesAdapter(response.getResources());
        mBottomSheetResourcesBinding.rvResources.setAdapter(resourcesAdapter);

        bottomResourcesSheetDialog.show();
    }

    private void synchronize() {
        if (bankReponse != null && !bankReponse.getBankId().isEmpty()) {
            new OpenBankingPFMAPI().banksClient().synchronize(
                    bankReponse.getBankId(),
                    getUserId(),
                    new SynchronizeListener() {
                        @Override
                        public void success() {
                            mBinding.progressBar.setVisibility(View.GONE);
                            Toast.makeText(ConsentActivity.this, "Sincronizacion Satisfactoria!", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void error(List<OBError> errors) {
                            if (!errors.isEmpty()) {
                                final OBError error = errors.get(0);
                                showMessage("Error!", "Sincronizacion fallida!\n-Titulo: " +
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
    }

    private void showMessage(String title, String message) {
        mBinding.progressBar.setVisibility(View.GONE);
        AlertUtils.show(this, title, message);
    }

    private void cancelAsyncTask() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer.purge();
        }

        if (mAggregationStatusAsyncTask != null) {
            mAggregationStatusAsyncTask.cancel(true);
        }

        if (mTimerTask != null) {
            mTimerTask.cancel();
        }

        if (mConsentsAsyncTask != null) {
            mConsentsAsyncTask.cancel(true);
        }
    }
}