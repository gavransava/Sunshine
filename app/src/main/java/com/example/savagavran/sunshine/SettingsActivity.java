package com.example.savagavran.sunshine;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;

import com.example.savagavran.sunshine.component.DaggerSettingsActivityComponent;
import com.example.savagavran.sunshine.module.SettingsActivityModule;
import com.example.savagavran.sunshine.presenter.Presenter;

import javax.inject.Inject;

public class SettingsActivity extends AppCompatActivity
        implements RequiredView.SettingsViewOps {

    private boolean mUnit;
    public static final String UNIT_RESULT = "unit_result";

    private EditText mLocationEdit;
    ListView mTemperatureUnits;
    CheckBox mNotifications;

    @Inject
    public Presenter.SettingsPresenter mSettingsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mLocationEdit = (EditText) findViewById(R.id.location_edit);
        mTemperatureUnits = (ListView) findViewById(R.id.temperature_units);
        mNotifications = (CheckBox) findViewById(R.id.notification_enable);

        DaggerSettingsActivityComponent
                .builder()
                .activityComponent(SunshineApp.getApp(this).getComponent())
                .settingsActivityModule(new SettingsActivityModule(this))
                .build()
                .inject(this);

        wireEditText();
        wireListView();
        wireCheckBox();

        mSettingsPresenter.getLocationValue(mSettingsPresenter);
        mSettingsPresenter.getUnitValue(mSettingsPresenter);
        mSettingsPresenter.getNotificationsValue(mSettingsPresenter);
    }

    private void wireEditText() {
        mLocationEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() >= 6) {
                    mSettingsPresenter.onLocationChanged(SettingsActivity.this, s.toString());
                }
            }
        });
    }

    private void wireListView() {
        mTemperatureUnits.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mSettingsPresenter.onUnitChanged(SettingsActivity.this, position);
                mUnit = true; // temporary solution
            }
        });
    }

    private void wireCheckBox() {
        mNotifications.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mSettingsPresenter.onNotificationsChange(SettingsActivity.this, isChecked);

            }
        });
    }

    @Override
    public void updateNotifitacionSettingValue(boolean value) {
        mNotifications.setChecked(value);
    }

    @Override
    public void updateUnitSettingValue(int value) {

    }

    @Override
    public void updateLocationSettingValue(String value) {
        mLocationEdit.setText(value);
    }

    private void returnResult() {
        Intent intent = this.getIntent();
        intent.putExtra(UNIT_RESULT, mUnit);
        this.setResult(RESULT_OK, intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void finish() {
        returnResult();
        super.finish();
    }
}