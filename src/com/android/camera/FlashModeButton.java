/*
 * Copyright (C) 2008 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.camera;

import android.hardware.Camera.Parameters;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import org.omnirom.snap.R;
import com.android.camera.app.CameraApp;
import com.android.camera.app.CameraApp;
import com.android.camera.PhotoUI;
import com.android.camera.CaptureUI;
import com.android.camera.SettingsManager;

import com.android.camera.ui.RotateVectorView;

public class FlashModeButton extends RotateVectorView {

    public enum FlashEnum {
        AUTOMATIC, ON, OFF, REDEYE
    }

    public interface FlashListener {
        void onAutomatic();
        void onOn();
        void onOff();
        void onRedEye();
    }

    private FlashEnum mState;
    private FlashListener mFlashListener;
    private PhotoUI mPhotoUI;
    private CaptureUI mCaptureUI;
    private SettingsManager mSettingsManager;

    public FlashModeButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        //Sets initial state
        setState(FlashEnum.AUTOMATIC);
    }

    public void setCameraApi(PhotoUI photoUI, SettingsManager settingsManager) {
        mPhotoUI = photoUI;
	mSettingsManager = settingsManager;
    }

    @Override
    public boolean performClick() {
        int next = ((mState.ordinal() + 1) % FlashEnum.values().length);
        setState(FlashEnum.values()[next]);
        performFlashClick();
        return true;
    }



    private void performFlashClick() {
        if(mFlashListener == null) {
            return;
        }
        switch (mState) {
            case AUTOMATIC:
                mFlashListener.onAutomatic();
                if (mPhotoUI != null) {
                    mPhotoUI.setPreference(CameraSettings.KEY_FLASH_MODE,Parameters.FLASH_MODE_AUTO);
                    return;
                }
                if (mSettingsManager != null) {
                    mSettingsManager.setValue(SettingsManager.KEY_FLASH_MODE, "2");
                    return;
                }
                break;
            case ON:
                mFlashListener.onOn();
                break;
            case OFF:
                mFlashListener.onOff();
                break;
            case REDEYE:
                mFlashListener.onRedEye();
                break;
        }
    }

    private void createDrawableState() {
        switch (mState) {
            case AUTOMATIC:
                setImageResource(R.drawable.ic_flash_auto);
                break;
            case ON:
                setImageResource(R.drawable.ic_flash_on);
                break;
            case OFF:
                setImageResource(R.drawable.ic_flash_off);
                break;
            case REDEYE:
                setImageResource(R.drawable.ic_flash_redeye);
                break;
        }
    }


    public FlashEnum getState() {
        return mState;
    }

    public void setState(FlashEnum state) {
        if(state == null)return;
        this.mState = state;
        createDrawableState();

    }

    public FlashListener getFlashListener() {
        return mFlashListener;
    }

    public void setFlashListener(FlashListener flashListener) {
        this.mFlashListener = flashListener;
    }

}
