package com.android.settings.security;

import android.content.Context;

import android.os.UserHandle;
import android.os.UserManager;
import android.os.SystemProperties;

import android.provider.Settings;

import androidx.preference.SwitchPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceScreen;

import com.android.internal.widget.LockPatternUtils;
import com.android.settings.core.PreferenceControllerMixin;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.events.OnResume;

public class PinScramblePreferenceController extends AbstractPreferenceController
        implements PreferenceControllerMixin, OnResume, Preference.OnPreferenceChangeListener {

    private static final String KEY_KEYGUARD_CAMERA_SWITCH = "keyguard_camera_toggle";
    private static final String KEY_KEYGUARD_CAMERA = "persist.keyguard.camera";
    private static final String PREF_KEY_SECURITY_CATEGORY = "security_category";

    private PreferenceCategory mSecurityCategory;
    private SwitchPreference mKeyguardCamera;

    public KeyguardCameraPreferenceController(Context context) {
        super(context);
    }

    @Override
    public void displayPreference(PreferenceScreen screen) {
        super.displayPreference(screen);
        mSecurityCategory = screen.findPreference(PREF_KEY_SECURITY_CATEGORY);
        updatePreferenceState();
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public String getPreferenceKey() {
        return KEY_KEYGUARD_CAMERA_SWITCH;
    }

    private void updatePreferenceState() {
        if (mSecurityCategory == null) {
            return;
        }
        mKeyguardCamera = (SwitchPreference) mSecurityCategory.findPreference(KEY_KEYGUARD_CAMERA_SWITCH);
        mKeyguardCamera.setChecked(SystemProperties.getBoolean(KEY_KEYGUARD_CAMERA, false));
    }

    @Override
    public void onResume() {
        updatePreferenceState();
        if (mKeyguardCamera != null) {
            if (mKeyguardCamera.isChecked()) {
                setOn();
            } else {
                setOff();
            }
        }
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object value) {
        final String key = preference.getKey();
        if (KEY_KEYGUARD_CAMERA_SWITCH.equals(key)) {
            boolean mode = (boolean) value;
            if (mode) {
                setOn();
            } else {
                setOff();
            }
        }
        return true;
    }

    private void setOn() {
        SystemProperties.putBoolean(KEY_KEYGUARD_CAMERA, true);
        mKeyguardCamera.setSwitchTextOn();
    }

    private void setOff() {
        SystemProperties.putBoolean(KEY_KEYGUARD_CAMERA, false);
        mKeyguardCamera.setSwitchTextOff();
    }
}
