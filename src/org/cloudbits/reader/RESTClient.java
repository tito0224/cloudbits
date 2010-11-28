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

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.ArrayList;

import java.net.URLEncoder;

public class RESTClient {
	
	private String mUrl;

	private ArrayList <NameValuePair> mParams;
    private ArrayList <NameValuePair> mHeaders;

	private HttpClient mClient;
	private HttpRequestBase mRequest;
	private HttpResponse mResponse;
    private int mResponseCode;
    private String mResponseMessage;
	private String mContent;
	
	public RESTClient(String url) {
		mClient = new DefaultHttpClient();
		mUrl = url;
		mParams = new ArrayList<NameValuePair>();
        mHeaders = new ArrayList<NameValuePair>();
	}

    public void addParam(String name, String value)
    {
        mParams.add(new BasicNameValuePair(name, value));
    }
 
    public void addHeader(String name, String value)
    {
        mHeaders.add(new BasicNameValuePair(name, value));
    }
	
	public void get() {
		buildGetRequest();
		execute();
	}

	public String getResponseContent() {
		return mContent;
	}
	
	public int getResponseCode() {
		return mResponseCode;
	}
	
	public String getResponseMessage() {
		return mResponseMessage;
	}
	
	private void buildGetRequest() {
		
		String combinedParamString = "";
        if(!mParams.isEmpty()) {
            combinedParamString += "?";
            for(NameValuePair p : mParams)
            {
                String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
                if(combinedParamString.length() > 1)
                    combinedParamString  +=  "&" + paramString;
                else
                    combinedParamString += paramString;
            }
        }
		
		mRequest = new HttpGet(mUrl + combinedParamString);
		
		for(NameValuePair h : mHeaders)
		{
		    mRequest.addHeader(h.getName(), h.getValue());
		}
	}
	
	private boolean execute() {
		try {
			mResponse = mClient.execute(mRequest);
		} catch (ClientProtocolException e) {
            mClient.getConnectionManager().shutdown();
            return false;
        } catch (IOException e) {
            mClient.getConnectionManager().shutdown();
            return false;
        }
		
		mResponseCode = mResponse.getStatusLine().getStatusCode();
		mResponseMessage = mResponse.getStatusLine().getReasonPhrase();
		
		StringBuffer buffer = new StringBuffer();
        String line;
        try {
            InputStream in = mResponse.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in), 8096);
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
                buffer.append("\n");
            }
            reader.close();
			mContent = buffer.toString();
        } catch (IOException e) {
            mClient.getConnectionManager().shutdown();
            return false;
        }
		
		mClient.getConnectionManager().shutdown();
		return true;
	}
}            