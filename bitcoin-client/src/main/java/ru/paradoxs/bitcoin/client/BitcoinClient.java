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
package ru.paradoxs.bitcoin.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import ru.paradoxs.bitcoin.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.paradoxs.bitcoin.client.exceptions.BitcoinClientException;

/**
 * A Java API for accessing a Bitcoin server
 *
 * @see <a href="http://www.bitcoin.org/wiki/doku.php?id=api">Bitcoin API</a>
 * @author paradoxs
 * @author mats@henricson.se
 */
public class BitcoinClient {
    private HttpSession session = null;

    public BitcoinClient(String host, String login, String password, int port) {
        try {
            Credentials credentials = new UsernamePasswordCredentials(login, password);
            URI uri = new URI("http", null, host, port, null, null, null);
            session = new HttpSession(uri, credentials);
        } catch (URISyntaxException ex) {
            throw new BitcoinClientException("This host probably doesn't have correct syntax: " + host, ex);
        }
    }

    public BitcoinClient(String host, String login, String password) {
        this(host, login, password, 8332);
    }

    /**
     * Returns the list of addresses with the given label
     *
     * @param label Name of label
     * @return list of addresses
     */
    public List<String> getAddressesByLabel(String label) {
        try {
            JSONArray parameters = new JSONArray().put(label);
            JSONObject request = createRequest("getaddressesbylabel", parameters);
            JSONObject response = session.sendAndReceive(request);
            JSONArray result = (JSONArray)response.get("result");
            int size = result.length();

            List<String> list = new ArrayList<String>();

            for (int i = 0; i<size; i++) {
                list.add(result.getString(i));
            }

            return list;
        } catch (JSONException ex) {
            throw new BitcoinClientException("Got incorrect JSON for this label: " + label, ex);
        }
    }

    /**
     * Returns the server's available balance
     *
     * @return the balance
     */
    public double getBalance() {
        try {
            JSONObject request = createRequest("getbalance");
            JSONObject response = session.sendAndReceive(request);

            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting balance", ex);
        }
    }

    /**
     * Returns the number of blocks in the longest block chain
     * 
     * @return the number of blocks
     */
    public int getBlockCount() {
        try {
            JSONObject request = createRequest("getblockcount");
            JSONObject response = session.sendAndReceive(request);

            return  response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting block count", ex);
        }
    }

    /**
     * Returns the block number of the latest block in the longest block chain
     *
     * @return the block number
     */
    public int getBlockNumber() {
        try {
            JSONObject request = createRequest("getblocknumber");
            JSONObject response = session.sendAndReceive(request);

            return response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the block number", ex);
        }
    }

    /**
     * Returns the number of connections to other nodes
     *
     * @return the number of connections
     */
    public int getConnectionCount() {
        try {
            JSONObject request = createRequest("getconnectioncount");
            JSONObject response = session.sendAndReceive(request);

            return response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the number of connections", ex);
        }
    }

    /**
     * Returns the proof-of-work difficulty as a multiple of the minimum difficulty
     *
     * @return the current difficulty
     */
    public double getDifficulty() {
        try {
            JSONObject request = createRequest("getdifficulty");
            JSONObject response = session.sendAndReceive(request);

            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the difficulty", ex);
        }
    }

    /**
     * Returns boolean true if server is trying to generate bitcoins, false otherwise
     *
     * @return true if server is trying to generate bitcoins, false otherwise
     */
    public boolean getGenerate() {
        try {
            JSONObject request = createRequest("getgenerate");
            JSONObject response = session.sendAndReceive(request);

            return response.getBoolean("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting whether the server is generating coins or not", ex);
        }
    }

    /**
     * Turn on/off coins generation
     *
     * @param isGenerate on - true, off - false
     * @param processorsCount proccesorsCount processors, -1 is unlimited
     */
    public void setGenerate(boolean isGenerate, int processorsCount) {
        try {
            JSONArray parameters = new JSONArray().put(isGenerate).put(processorsCount);
            JSONObject request = createRequest("setgenerate", parameters);
            session.sendAndReceive(request);
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when setting whether the server is generating coins or not", ex);
        }
    }

    /**
     * Return server information, about balance, connections, blocks...etc.
     *
     * @return server information
     */
    public ServerInfo getServerInfo() {
        try {
            JSONObject request = createRequest("getinfo");
            JSONObject response = session.sendAndReceive(request);
            JSONObject result = (JSONObject) response.get("result");

            ServerInfo info = new ServerInfo();
            info.setBalance        (result.getDouble ("balance"));
            info.setBlocks         (result.getLong   ("blocks"));
            info.setConnections    (result.getInt    ("connections"));
            info.setDifficulty     (result.getDouble ("difficulty"));
            info.setHashesPerSecond(result.getLong   ("hashespersec"));
            info.setIsGenerateCoins(result.getBoolean("generate"));
            info.setUsedCPUs       (result.getInt    ("genproclimit"));
            info.setVersion        (result.getString ("version"));            

            return info;
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the server info", ex);
        }
    }

    /**
     * Returns the label associated with the given address
     *
     * @return the label associated with a certain address
     */
    public String getLabel(String address) {
        try {
            JSONArray parameters = new JSONArray().put(address);
            JSONObject request = createRequest("getlabel", parameters);
            JSONObject response = session.sendAndReceive(request);

            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the label associated with a given address", ex);
        }
    }

    /**
     * Sets the label associated with the given address
     *
     * @param address address
     * @param label if label is null then label remove
     */
    public void setLabelForAddress(String address, String label) {
        try {
            JSONArray parameters = new JSONArray().put(address).put(label);
            JSONObject request = createRequest("setlabel", parameters);
            session.sendAndReceive(request);
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when setting the label associated with a given address", ex);
        }
    }

    /**
     * Returns a new bitcoin address for receiving payments. If [label] is
     * specified (recommended), it is added to the address book so payments
     * received with the address will be labeled.
     *
     * @param label if not null(recommended), address will be labeled
     * @return the new bitcoin address for receiving payments
     */
    public String getNewAddress(String label) {
        try {
            JSONArray parameters = new JSONArray().put(label);
            JSONObject request = createRequest("getnewaddress", parameters);
            JSONObject response = session.sendAndReceive(request);

            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the new bitcoin address for receiving payments", ex);
        }
    }

    /**
     * Returns the total amount received by bitcoinaddress in transactions
     *
     * @return total amount received
     */
    public double getReceivedByAddress(String address, long minimumConfirmations) {
        try {
            JSONArray parameters = new JSONArray().put(address).put(minimumConfirmations);
            JSONObject request = createRequest("getreceivedbyaddress", parameters);
            JSONObject response = session.sendAndReceive(request);

            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the total amount received by bitcoinaddress", ex);
        }
    }

    /**
     * Returns the total amount received by addresses with label in transactions
     *
     * @return total amount received
     */
    public double getReceivedByLabel(String label, long minimumConfirmations) {
        try {
            JSONArray parameters = new JSONArray().put(label).put(minimumConfirmations);
            JSONObject request = createRequest("getreceivedbylabel", parameters);
            JSONObject response = session.sendAndReceive(request);

            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting the total amount received with label", ex);
        }
    }

    /**
     * Return help for a command
     *
     * @param command the command
     * @return the help text
     */
    public String help(String command) {
        try {
            JSONArray parameters = new JSONArray().put(command);
            JSONObject request = createRequest("help", parameters);
            JSONObject response = session.sendAndReceive(request);

            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting help for a command", ex);
        }
    }

    /**
     * Info about all received transactions by address
     *
     * @param minimumConfirmations is the minimum number of confirmations before payments are included
     * @param includeEmpty whether to include addresses that haven't received any payments
     * @return info about all received transactions by address
     */
    public List<AddressInfo> listReceivedByAddress(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray parameters = new JSONArray().put(minimumConfirmations).put(includeEmpty);
            JSONObject request = createRequest("listreceivedbyaddress", parameters);
            JSONObject response = session.sendAndReceive(request);
            JSONArray result = response.getJSONArray("result");
            int size = result.length();
            List<AddressInfo> list = new ArrayList<AddressInfo>();

            for (int i = 0; i < size; i++) {
                AddressInfo info = new AddressInfo();
                JSONObject jObject = result.getJSONObject(i);
                info.setAddress(jObject.getString("address"));
                info.setLabel(jObject.getString("label"));
                info.setAmount(jObject.getDouble("amount"));
                info.setConfirmations(jObject.getLong("confirmations"));
                list.add(info);
            }

            return list;
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting info about all received transactions by address", ex);
        }
    }

    /**
     * Info about all received transactions by label
     *
     * @param minimumConfirmations is the minimum number of confirmations before payments are included
     * @param includeEmpty whether to include addresses that haven't received any payments
     * @return info about all received transactions by label
     */
    public List<LabelInfo> listReceivedByLabel(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray parameters = new JSONArray().put(minimumConfirmations).put(includeEmpty);
            JSONObject request = createRequest("listreceivedbylabel", parameters);
            JSONObject response = session.sendAndReceive(request);
            JSONArray result = response.getJSONArray("result");
            int size = result.length();

            List<LabelInfo> list = new ArrayList<LabelInfo>();

            for (int i = 0; i < size; i++) {
                LabelInfo info = new LabelInfo();
                JSONObject jObject = result.getJSONObject(i);                
                info.setLabel        (jObject.getString("label"));
                info.setAmount       (jObject.getDouble("amount"));
                info.setConfirmations(jObject.getLong("confirmations"));
                list.add(info);
            }

            return list;
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when getting info about all received transactions by label", ex);
        }
    }

    /**
     * Sends amount from the server's available balance to bitcoinaddress.
     * Amount is a real and is rounded to the nearest 0.01
     *
     * @param address the address to which we want to send bitcoins
     * @param amount the amount we wish to send
     * @param comment a comment for this transfer, can be null
     */
    public void sendToAddress(String address, double amount, String comment) {
        try {
            JSONArray parameters = new JSONArray().put(address).put(amount).put(comment);
            JSONObject request = createRequest("sendtoaddress", parameters);
            session.sendAndReceive(request);
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when sending bitcoins", ex);
        }
    }

    /**
     * Stops the bitcoin server
     */
    public void stop() {
        try {
            JSONObject request = createRequest("stop");
            session.sendAndReceive(request);
        } catch (JSONException ex) {
            throw new BitcoinClientException("Exception when stopping the bitcoin server", ex);
        }
    }

    private JSONObject createRequest(String functionName, JSONArray parameters) throws JSONException {
        JSONObject request = new JSONObject();
        request.put("jsonrpc", "2.0");
        request.put("id",      UUID.randomUUID().toString());
        request.put("method", functionName);
        request.put("params",  parameters);

        return request;
    }

    private JSONObject createRequest(String functionName) throws JSONException {
        return createRequest(functionName, new JSONArray());
    }
}
