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
package org.cloudbits.ui;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import org.cloudbits.R;

public class DashboardActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        
        setDashboardTypeface();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        Log.d("Cloudbits", "In on resume");
    }
    
    private void setDashboardTypeface() {
        Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/euro-bold.ttf");
        
        Button btnUnread = (Button) findViewById(R.id.dashboard_btn_unread);
        btnUnread.setTypeface(tf);
        
        Button btnStarred = (Button) findViewById(R.id.dashboard_btn_starred);
        btnStarred.setTypeface(tf);
        
        Button btnPopular = (Button) findViewById(R.id.dashboard_btn_popular);
        btnPopular.setTypeface(tf);
        
        Button btnSubscriptions = (Button) findViewById(R.id.dashboard_btn_subscriptions);
        btnSubscriptions.setTypeface(tf);
    }
}

