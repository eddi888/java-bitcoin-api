/**
 * Copyright 2010 Mats Henricson (mats@henricson.se)
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

import org.junit.Test;
import ru.paradoxs.bitcoin.client.AddressInfo;
import ru.paradoxs.bitcoin.client.BitcoinClient;
import ru.paradoxs.bitcoin.client.LabelInfo;
import ru.paradoxs.bitcoin.client.ServerInfo;

import java.util.List;

import static junit.framework.Assert.assertEquals;

/**
 * A number of unit tests against a local Bitcoin server.
 * The tests are written so that they fail if the communication
 * fails, i.e. throws an exception. Can't really assert much, since
 * I don't know how much credit, etc, the server has.
 */
public class BitcoinTest {
    private static final String RPCUSER     = "RPCUSER";        // TODO: Change
    private static final String RPCPASSWORD = "RPCPASSWORD";    // TODO: Change

    private BitcoinClient bClient = new BitcoinClient("127.0.0.1", RPCUSER, RPCPASSWORD);

    @Test
    public void testGetBalance() {
        double balance = bClient.getBalance();

        System.out.println("balance = " + balance);
    }

    @Test
    public void testGetBlockCount() {
        int blockCount = bClient.getBlockCount();

        System.out.println("blockCount = " + blockCount);
    }

    @Test
    public void testGetBlockNumber() {
        int blockNumber = bClient.getBlockNumber();

        System.out.println("blockNumber = " + blockNumber);
    }

    @Test
    public void testGetConnectionCount() {
        int connectionCount = bClient.getConnectionCount();

        System.out.println("connectionCount = " + connectionCount);
    }

    @Test
    public void testGetDifficulty() {
        double difficulty = bClient.getDifficulty();

        System.out.println("difficulty = " + difficulty);
    }

    @Test
    public void testHelp() {
        String help = bClient.help("getbalance");

        System.out.println("help = " + help);
    }

    @Test
    public void testGetGenerate() {
        boolean generate = bClient.getGenerate();

        System.out.println("generate = " + generate);
    }

    @Test
    public void testSetGenerate() {
        boolean generate = bClient.getGenerate();

        System.out.println("generate = " + generate);

        bClient.setGenerate(!generate, 1);
        boolean generateNew = bClient.getGenerate();

        System.out.println("generateNew = " + generateNew);

        bClient.setGenerate(generate, 1);
        boolean generateNewNew = bClient.getGenerate();

        System.out.println("generateNewNew = " + generateNewNew);
    }

    @Test
    public void testGetServerInfo() {
        ServerInfo serverInfo = bClient.getServerInfo();

        System.out.println("serverInfo = " + serverInfo);
    }

    @Test
    public void testListReceivedByAddress() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        System.out.println("addressInfos = " + addressInfos);
    }

    @Test
    public void testListReceivedByLabel() {
        List<LabelInfo> labelInfos = bClient.listReceivedByLabel(1, false);

        System.out.println("labelInfos = " + labelInfos);
    }

    @Test
    public void testGetLabel() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            String label = bClient.getLabel(address);

            System.out.println("label = " + label);
        }
    }

    @Test
    public void testGetAddressesByLabel() {
        List<LabelInfo> labelInfos = bClient.listReceivedByLabel(1, false);

        for (LabelInfo li : labelInfos) {
            String label = li.getLabel();
            List<String> addresses = bClient.getAddressesByLabel(label);

            for (String address : addresses) {
                System.out.println("address = " + address);
            }
        }
    }

    @Test
    public void testGetReceivedByAddress() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            double received = bClient.getReceivedByAddress(address, 1);

            System.out.println("received = " + received);
        }
    }

    @Test
    public void testGetReceivedByLabel() {
        List<LabelInfo> labelInfos = bClient.listReceivedByLabel(1, false);

        for (LabelInfo li : labelInfos) {
            String label = li.getLabel();
            double received = bClient.getReceivedByLabel(label, 1);

            System.out.println("received = " + received);
        }
    }

    @Test
    public void testGetNewAddress() {
        String address = bClient.getNewAddress("Testing testing");

        System.out.println("address = " + address);
    }

    @Test
    public void testSetLabelForAddress() {
        String newLabel = "New Label";
        String address = bClient.getNewAddress("Testing testing");
        bClient.setLabelForAddress(address, newLabel);
        String label = bClient.getLabel(address);

        assertEquals(newLabel, label);

        System.out.println("label = " + label);
    }

    @Test
    public void testSendToAddress() {
        bClient.sendToAddress("1MCwBbhNGp5hRm5rC1Aims2YFRe2SXPYKt", 0.01d, "Use it wisely, EFF");
    }

    @Test
    public void testStop() {
        bClient.stop();
    }
}
