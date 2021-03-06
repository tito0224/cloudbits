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

public abstract class BaseRequest {
    public static final String API_BASE = "http://www.google.com/reader/api/0/";
    public static final String ACTION_LIST = "list";
    public static final String ACTION_EDIT = "edit";
	public static final String CLIENT = "cloudbits";
	public static final String OUTPUT = "json";
    
    public abstract void parseResponse(String response);
    public abstract void list(String sid);
	public abstract void edit(String sid);
}