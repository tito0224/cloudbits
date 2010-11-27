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

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class PermissionActivity extends Activity {
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle result = getIntent().getExtras();
        
        Intent intent = (Intent) result.get(AccountManager.KEY_INTENT);
        int flags = intent.getFlags();
        flags &= ~Intent.FLAG_ACTIVITY_NEW_TASK;
        intent.setFlags(flags);
        
        startActivityForResult(intent, 0);
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {        
        if (data.getBooleanExtra("retry", false)) {
            SyncService.requestSync(this, SyncService.TYPE_READER, -1);
        }
        
        finish();
    }
}
