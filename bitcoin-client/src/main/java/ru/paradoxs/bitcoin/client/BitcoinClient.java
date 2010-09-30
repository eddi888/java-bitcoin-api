package ru.paradoxs.bitcoin.client;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.httpclient.Credentials;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import ru.paradoxs.bitcoin.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.paradoxs.bitcoin.client.exceptions.BitcoinClientException;

/** * 
 * @see http://www.bitcoin.org/wiki/doku.php?id=api
 * @author paradoxs
 */
public class BitcoinClient {
    HttpSession     _session = null;
    Credentials _credentials = null;
    URI                 _uri = null;

    /**
     * @param host 
     * @param login
     * @param password
     * @param port
     */
    public BitcoinClient(String host, String login, String password, int port) {
        try {
            _credentials = new UsernamePasswordCredentials(login, password);
            _uri = new URI("http", null, host, port, null, null, null);
            _session = new HttpSession(_uri, _credentials);
        } catch (URISyntaxException ex) {
            throw new BitcoinClientException(ex);
        }
    }

    
    /**
     * @param host 
     * @param login
     * @param password
     * @param port
     */
    public BitcoinClient(String host, String login, String password) {
        try {
            _credentials = new UsernamePasswordCredentials(login, password);
            _uri = new URI("http", null, host, 8332, null, null, null);
            _session = new HttpSession(_uri, _credentials);
        } catch (URISyntaxException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the list of addresses with the given label
     * @param label Name of label
     * @return list of addresses
     */
    public List<String> getAddressesByLabel(String label) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(label);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getaddressesbylabel");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            JSONArray    result = (JSONArray)response.get("result");
            int size = result.length();

            List<String> list = new ArrayList<String>();            
            for (int i = 0; i<size; i++) {                
                list.add(result.getString(i));
            }
            return list;
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }

    
    /**
     * Returns the server's available balance
     * @return balance
     */
    public double getBalance() {
        try {            
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getbalance");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the number of blocks in the longest block chain
     * @return 
     */
    public int getBlockCount() {
        try {
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getblockcount");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return  response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the block number of the latest block in the longest block chain
     * @return
     */
    public int getBlockNumber() {
        try {
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getblocknumber");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the number of connections to other nodes
     * @return
     */
    public int getConnectionCount() {
        try {
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getconnectioncount");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return response.getInt("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the proof-of-work difficulty as a multiple of the minimum difficulty
     */
    public double getDifficulty() {
        try {
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getdifficulty");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns boolean true if server is trying to generate bitcoins, false otherwise
     */
    public boolean getGenerate() {
        try {
            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getgenerate");
            object.put("params",  new JSONArray());

            JSONObject response = _session.sendAndReceive(object);
            return response.getBoolean("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Turn on/off coins generation
     * @param isGenerate on - true, off - false
     * @param processorsCount proccesorsCount processors, -1 is unlimited
     */
    public void setGenerate(boolean isGenerate, int processorsCount) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(isGenerate);
            parameters.put(processorsCount);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "setgenerate");
            object.put("params",  parameters);

            _session.sendAndReceive(object);            
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Return server information, about balance, connections, blocks...etc.
     * @return server information
     */
    public ServerInfo getServerInfo() {
        try {
            JSONArray parameters = new JSONArray();            

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getinfo");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
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
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the label associated with the given address
     * @param label
     * @return 
     */
    public String getLabel(String address) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(address);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getlabel");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Sets the label associated with the given address
     * @param address address
     * @param label if label is null then label remove
     */
    public void setLabelForAddress(String address, String label) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(address);
            parameters.put(label);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "setlabel");
            object.put("params",  parameters);

            _session.sendAndReceive(object);            
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns a new bitcoin address for receiving payments. If [label] is
     * specified (recommended), it is added to the address book so payments
     * received with the address will be labeled.
     * @param label if not null(recommended), address will be labeled
     * @return
     */
    public String getNewAddress(String label) {
        try {
            JSONArray parameters = new JSONArray();            
            parameters.put(label);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getnewaddress");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the total amount received by bitcoinaddress in transactions
     * @param address
     * @param minimumConfirmations
     * @return total amount received
     */
    public double getReceivedByAddress(String address, long minimumConfirmations) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(address);
            parameters.put(minimumConfirmations);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getreceivedbyaddress");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Returns the total amount received by addresses with label in transactions
     * @param label
     * @param minimumConfirmations
     * @return total amount received
     */
    public double getReceivedByLabel(String label, long minimumConfirmations) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(label);
            parameters.put(minimumConfirmations);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "getreceivedbylabel");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            return response.getDouble("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Return help for a command
     * @param command
     * @return
     */
    public String help(String command) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(command);            

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "help");
            object.put("params",  parameters);

            JSONObject response = _session.sendAndReceive(object);
            return response.getString("result");
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Info about all received transactions by address
     * @param minimumConfirmations is the minimum number of confirmations before payments are included
     * @param includeEmpty whether to include addresses that haven't received any payments
     * @return
     */
    public List<AddressInfo> listReceivedByAddress(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(minimumConfirmations);
            parameters.put(includeEmpty);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "listreceivedbyaddress");
            object.put("params",  parameters);
            JSONObject response = _session.sendAndReceive(object);

            JSONArray result = response.getJSONArray("result");
            int size = result.length();

            List<AddressInfo> list = new ArrayList<AddressInfo>();
            for(int i = 0; i<size; i++) {
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
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Info about all received transactions by label
     * @param minimumConfirmations is the minimum number of confirmations before payments are included
     * @param includeEmpty whether to include addresses that haven't received any payments
     * @return
     */
    public List<LabelInfo> listReceivedByLabel(long minimumConfirmations, boolean includeEmpty) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(minimumConfirmations);
            parameters.put(includeEmpty);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "listreceivedbylabel");
            object.put("params",  parameters);
            JSONObject response = _session.sendAndReceive(object);

            JSONArray result = response.getJSONArray("result");
            int size = result.length();

            List<LabelInfo> list = new ArrayList<LabelInfo>();
            for(int i = 0; i<size; i++) {
                LabelInfo info = new LabelInfo();
                JSONObject jObject = result.getJSONObject(i);                
                info.setLabel        (jObject.getString("label"));
                info.setAmount       (jObject.getDouble("amount"));
                info.setConfirmations(jObject.getLong("confirmations"));
                list.add(info);
            }
            return list;
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }


    /**
     * Sends amount from the server's available balance to bitcoinaddress. amount is a real and is rounded to the nearest 0.01
     * @param address
     * @param amount
     * @param comment can be null
     */
    public void sendToAddress(String address, double amount, String comment) {
        try {
            JSONArray parameters = new JSONArray();
            parameters.put(address);
            parameters.put(amount);

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "sendtoaddress");
            object.put("params",  parameters);
            _session.sendAndReceive(object);            
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }
    

    /**
     * stops the bitcoin server
     */
    public void stop() {
        try {
            JSONArray parameters = new JSONArray();            

            JSONObject object = new JSONObject();
            object.put("jsonrpc", "2.0");
            object.put("id",      UUID.randomUUID().toString());
            object.put("method",  "stop");
            object.put("params",  parameters);

            _session.sendAndReceive(object);            
        } catch (JSONException ex) {
            throw new BitcoinClientException(ex);
        }
    }
}
