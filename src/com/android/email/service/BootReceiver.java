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

package com.android.email.service;

import com.android.email.AccountBackupRestore;
import com.android.email.Email;
import com.android.email.SecurityPolicy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Restore accounts, if it has not happened already
        AccountBackupRestore.restoreAccountsIfNeeded(context);

        String intentAction = intent.getAction();

        if (Intent.ACTION_BOOT_COMPLETED.equals(intentAction)) {
            // Returns true if there are any accounts
            if (Email.setServicesEnabled(context)) {
                MailService.actionReschedule(context);
            }
        }
        else if (Intent.ACTION_DEVICE_STORAGE_LOW.equals(intentAction)) {
            MailService.actionCancel(context);
        }
        else if (Intent.ACTION_DEVICE_STORAGE_OK.equals(intentAction)) {
            MailService.actionReschedule(context);
        }
        else if (Intent.ACTION_PACKAGE_ADDED.equals(intentAction) ||
                Intent.ACTION_PACKAGE_REPLACED.equals(intentAction) ||
                Intent.ACTION_PACKAGE_REMOVED.equals(intentAction)) {
            SecurityPolicy.getInstance(context.getApplicationContext()).invalidateKeyguardCache();
        }
    }
}
