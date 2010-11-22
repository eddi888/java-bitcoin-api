/**
 * Copyright 2010 Aleksey Krivosheev (paradoxs.mail@gmail.com)
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package ru.paradoxs.bitcoin.http;

import ru.paradoxs.bitcoin.http.exceptions.HttpSessionException;
import java.io.IOException;
import java.net.URI;
import org.apache.commons.httpclient.Credentials;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

public class HttpSession {

    static final String JSON_CONTENT_TYPE = "application/json";
    static final String POST_CONTENT_TYPE = "text/plain";

    private HttpClient       _client = null;
    private URI                 _uri = null;
    private Credentials _credentials = null;
    

    public HttpSession(URI uri, Credentials credentials) {
        this._uri = uri;
        this._credentials = credentials;
    }

    public JSONObject sendAndReceive(JSONObject message) {
        PostMethod method = new PostMethod(_uri.toString());
        try {
            method.setRequestHeader("Content-Type", POST_CONTENT_TYPE);

            RequestEntity requestEntity = new StringRequestEntity(message.toString(), JSON_CONTENT_TYPE, null);
            method.setRequestEntity(requestEntity);

            getHttpClient().executeMethod(method);
            int statusCode = method.getStatusCode();
            if (statusCode != HttpStatus.SC_OK) {
                throw new HttpSessionException("HTTP Status - " + HttpStatus.getStatusText(statusCode) + " (" + statusCode + ")");
            }
            JSONTokener       tokener = new JSONTokener(method.getResponseBodyAsString());
            Object rawResponseMessage = tokener.nextValue();
            JSONObject response = (JSONObject) rawResponseMessage;
            if (response == null) {
                throw new HttpSessionException("Invalid response type");
            }
            return response;
        } catch (HttpException e) {
            throw new HttpSessionException(e);
        } catch (IOException e) {
            throw new HttpSessionException(e);
        } catch (JSONException e) {
            throw new HttpSessionException(e);
        } finally {
            method.releaseConnection();
        }
    }    

    private HttpClient getHttpClient() {
        if (_client == null) {
            _client = new HttpClient();
            _client.getState().setCredentials(AuthScope.ANY, _credentials);
        }
        return _client;
    }
}
