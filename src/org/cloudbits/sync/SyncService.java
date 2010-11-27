/*
 * Copyright 2010 Mike Novak <michael.novakjr@gmail.com>
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
package org.cloudbits.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;

import java.util.concurrent.atomic.AtomicBoolean;

import org.cloudbits.provider.CloudbitsProvider;

public class SyncService extends Service {
    public static final String ACCOUNT_TYPE = "com.google";
    public static final String KEY_TYPE = "org.cloudbits.SYNC_TYPE";
    
    public static final int TYPE_READER = 0x1;
    
    private static final Object sSyncAdapterLock = new Object();
    private static SyncAdapter sSyncAdapter = null;
    
    private final HandlerThread mSyncThread = new HandlerThread("CloudbitsSyncThread");
    private final Handler mSyncHandler;
    
    private static final AtomicBoolean sSyncPending = new AtomicBoolean(false);
    
    public static void requestSync(Context context, int type, long id) {
        Account[] accounts = AccountManager.get(context).getAccountsByType("com.google");
        Bundle extras = new Bundle();
        extras.putInt(KEY_TYPE, type);
        
        for (Account acct : accounts) {
            /* check if the account is set to sync automatically. */
            if (ContentResolver.getSyncAutomatically(acct, CloudbitsProvider.AUTHORITY)) {
                ContentResolver.requestSync(acct, CloudbitsProvider.AUTHORITY, extras);
            }
        }
    }
    
    public SyncService() {
        super();
        mSyncThread.start();
        mSyncHandler = new Handler(mSyncThread.getLooper());
        mSyncHandler.post(new Runnable() {
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            }
        });
    }
    
    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new SyncAdapter(getApplicationContext(), true);
            }
        }
    }
    
    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        mSyncHandler.post(new Runnable() {
            public void run() {
                performSync(SyncService.this, null, intent.getExtras(), new SyncResult());
                stopSelf(startId);
            }
        });
        
        return START_NOT_STICKY;
    }
    
    @Override
    public void onDestroy() {
        mSyncThread.quit();
    }
    
    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }
    
    public static boolean performSync(Context context, Account account, Bundle extras, SyncResult syncResult) {
        if (!sSyncPending.compareAndSet(false, true)) {
            return false;
        }
        
        performSyncImpl(context, account, extras, syncResult);
        
        sSyncPending.set(false);
        
        synchronized (sSyncPending) {
            sSyncPending.notifyAll();
        }
        
        return true;
    }
    
    private static void performSyncImpl(Context context, Account account, Bundle extras, SyncResult syncResult) {
        CloudbitsProvider provider = getContentProvider(context);
        
        int type = extras.getInt(KEY_TYPE, TYPE_READER);
        
        switch (type) {
        case TYPE_READER:
            break;
        }
    }
    
    private static CloudbitsProvider getContentProvider(Context context) {
        ContentResolver resolver = context.getContentResolver();
        ContentProviderClient client = resolver.acquireContentProviderClient(CloudbitsProvider.AUTHORITY);
        return (CloudbitsProvider) client.getLocalContentProvider();
    }
}
