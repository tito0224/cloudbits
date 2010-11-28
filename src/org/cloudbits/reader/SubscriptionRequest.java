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
package org.cloudbits.reader;

import android.util.Log;

public class SubscriptionRequest extends BaseRequest {
    public static final String API_SUBSCRIPTION = "subscription";
    public static final String ACTION_LIST = "list";

    public void list(String sid) {
		String baseUrl = String.format("%s/%s/%s", API_BASE, API_SUBSCRIPTION, ACTION_LIST);
		String ts = String.valueOf(System.currentTimeMillis());

		RESTClient client = new RESTClient(baseUrl);

		client.addHeader("sid", sid);
		client.addParam("output", OUTPUT);
		client.addParam("ck", ts);
		client.addParam("client", CLIENT);
		client.get();

		String response = client.getResponseContent();

		parseResponse(response);
	}
	
	public void edit(String sid) {
		
	}

    public void parseResponse(String response) {
        if(response != null) {
			Log.d("Cloudbits", response);
		}
    }

	
}