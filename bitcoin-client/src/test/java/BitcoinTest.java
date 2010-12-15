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
import ru.paradoxs.bitcoin.client.*;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

/**
 * A number of unit tests against a local Bitcoin server.
 * The tests are written so that they fail if the communication
 * fails, i.e. throws an exception. Can't really assert much, since
 * I don't know how much credit, etc, the server has.
 *
 * @author mats@henricson.se
 */
public class BitcoinTest {
    private static final String EFF_DONATION_ADDRESS = "1MCwBbhNGp5hRm5rC1Aims2YFRe2SXPYKt";
    private static final String RPCUSER          = "matsh";        // TODO: Change to what you have in bitcoin.conf file
    private static final String RPCPASSWORD      = "korpen";    // TODO: Change to what you have in bitcoin.conf file
    private static final String BACKUP_DIRECTORY = "/home/mats";     // TODO: Change, but never ever to your Bitcoin data directory !!

    private BitcoinClient bClient = new BitcoinClient("127.0.0.1", RPCUSER, RPCPASSWORD);

    @Test
    public void testGetBalance() {
        double balance = bClient.getBalance();

        System.out.println("balance = " + balance);

        assertTrue(balance >= 0.0);
    }

    @Test
    public void testGetBalanceWithParameter() {
        String accountName = "anAccountName";
        bClient.getAccountAddress(accountName);        // This creates an account, if it doesn't exist

        double balance = bClient.getBalance(accountName);

        System.out.println("balance = " + balance);

        assertTrue(balance >= 0.0);
    }

    @Test
    public void testGetBlockCount() {
        int blockCount = bClient.getBlockCount();

        System.out.println("blockCount = " + blockCount);

        assertTrue(blockCount > 0);
    }

    @Test
    public void testGetBlockNumber() {
        int blockNumber = bClient.getBlockNumber();

        System.out.println("blockNumber = " + blockNumber);

        assertTrue(blockNumber > 0);
    }

    @Test
    public void testGetConnectionCount() {
        int connectionCount = bClient.getConnectionCount();

        System.out.println("connectionCount = " + connectionCount);

        assertTrue(connectionCount >= 0);
    }

    @Test
    public void testGetDifficulty() {
        double difficulty = bClient.getDifficulty();

        System.out.println("difficulty = " + difficulty);

        assertTrue(difficulty > 0);
    }

    @Test
    public void testHelp() {
        String help = bClient.help("getbalance");

        System.out.println("help = " + help);

        assertTrue(help.length() > 0);
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
    public void testGetHashesPerSecond() {
        bClient.setGenerate(true, 1);
        long hashesPerSecond = bClient.getHashesPerSecond();
        bClient.setGenerate(false, 1);

        System.out.println("hashesPerSecond = " + hashesPerSecond);

        assertTrue(hashesPerSecond >= 0);
    }

    @Test
    public void testGetServerInfo() {
        ServerInfo serverInfo = bClient.getServerInfo();

        System.out.println("serverInfo = " + serverInfo);

        assertTrue(serverInfo != null);
    }

    @Test
    public void testListReceivedByAddress() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        System.out.println("addressInfos = " + addressInfos);

        assertTrue(addressInfos != null);
    }

    @Test
    public void testListReceivedByLabel() {
        List<LabelInfo> labelInfos = bClient.listReceivedByLabel(1, false);

        System.out.println("labelInfos = " + labelInfos);

        assertTrue(labelInfos != null);
    }

    @Test
    public void testGetLabel() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            String label = bClient.getLabel(address);

            System.out.println("label = " + label);

            assertTrue(label.length() > 0);
        }
    }

    @Test
    public void testGetAccount() {
        List<AddressInfo> addressInfos = bClient.listReceivedByAddress(1, false);

        for (AddressInfo ai : addressInfos) {
            String address = ai.getAddress();
            String account = bClient.getAccount(address);

            System.out.println("account = " + account);
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

                assertTrue(address.length() > 0);
            }
        }
    }

    @Test
    public void testGetAddressesByAccount() {
        List<AccountInfo> accountInfos = bClient.listReceivedByAccount(1, false);

        for (AccountInfo ai : accountInfos) {
            String account = ai.getAccount();
            List<String> addresses = bClient.getAddressesByAccount(account);

            for (String address : addresses) {
                System.out.println("address = " + address);

                assertTrue(address.length() > 0);
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

            assertTrue(received >= 0.0);
        }
    }

    @Test
    public void testGetReceivedByLabel() {
        List<LabelInfo> labelInfos = bClient.listReceivedByLabel(1, false);

        for (LabelInfo li : labelInfos) {
            String label = li.getLabel();
            double amount = li.getAmount();
            long confirmations = li.getConfirmations();

            assertTrue(amount > 0);
            assertTrue(confirmations > 0);

            double received = bClient.getReceivedByLabel(label, 1);

            assertEquals(amount, received, 0.001);

            System.out.println("received = " + received);
        }
    }

    @Test
    public void testGetReceivedByAccount() {
        List<AccountInfo> accountInfos = bClient.listReceivedByAccount(1, false);

        for (AccountInfo ai : accountInfos) {
            String account = ai.getAccount();
            double amount = ai.getAmount();
            long confirmations = ai.getConfirmations();

            assertTrue(amount > 0);
            assertTrue(confirmations > 0);

            double received = bClient.getReceivedByAccount(account, 1);

            assertEquals(amount, received, 0.001);

            System.out.println("received = " + received);
        }
    }

    @Test
    public void testGetNewAddress() {
        String address = bClient.getNewAddress("Testing testing");

        System.out.println("address = " + address);

        assertNotNull(address);
        assertEquals(EFF_DONATION_ADDRESS.length(), address.length());
    }

    @Test
    public void testGetAccountAddress() {
        // First we try with null account
        String addressToNullAccount = bClient.getAccountAddress(null);

        System.out.println("addressToNullAccount = " + addressToNullAccount);

        assertNotNull(addressToNullAccount);
        assertTrue(addressToNullAccount.length() > 30);

        // Then we try with empty account
        String addressToEmptyAccount = bClient.getAccountAddress(null);

        System.out.println("addressToEmptyAccount = " + addressToEmptyAccount);

        assertNotNull(addressToEmptyAccount);
        assertTrue(addressToEmptyAccount.length() > 30);

        // Finally with real account name
        String addressToRealAccount = bClient.getAccountAddress(null);

        System.out.println("addressToRealAccount = " + addressToRealAccount);

        assertNotNull(addressToRealAccount);
        assertTrue(addressToRealAccount.length() > 30);
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
    public void testSetAccountForAddress() {
        String newAccount = "Brand New Acount";
        String address = bClient.getAccountAddress("Testing testing client");
        bClient.setAccountForAddress(address, newAccount);
        String account = bClient.getAccount(address);

        assertEquals(newAccount, account);

        System.out.println("account = " + account);
    }

    @Test
    public void testValidateAddress() {
        ValidatedAddressInfo effInfo = bClient.validateAddress(EFF_DONATION_ADDRESS);

        assertTrue(effInfo.getIsValid());
        assertFalse(effInfo.getIsMine());
        assertEquals(EFF_DONATION_ADDRESS, effInfo.getAddress());

        ValidatedAddressInfo bogusInfo = bClient.validateAddress("BogUsAddr3ss");

        assertFalse(bogusInfo.getIsValid());
    }

    @Test
    public void testSendToAddress() {
        String txId = bClient.sendToAddress(EFF_DONATION_ADDRESS, 0.01d, "Use it wisely, EFF");
        assertNotNull(txId);
        assertFalse(txId.equals("sent"));  // Old (pre 0.3.17) behaviour
        assertTrue(txId.length() > 30);    // A 256 bit hash
    }

    @Test
    public void testListTransactions() {
        List<TransactionInfo> txList = bClient.listTransactions(null, 100);     // Default account

        assertNotNull(txList);

        for (TransactionInfo txInfo : txList) {
            System.out.println("txInfo: " + txInfo);

            String category = txInfo.getCategory();

            assertNotNull(category);

            assertTrue(category.equals("generate") ||
                       category.equals("send") ||
                       category.equals("receive") ||
                       category.equals("move"));

            // Can't assert on amount, since it can be positive and negative

            if (category.equals("send")) {
                assertTrue(txInfo.getFee() >= 0.0);
                // Can't assert on "message", since it may be null
                // Can't assert on "to", since it may be null
            }

            if (category.equals("generate") ||
                category.equals("send") ||
                category.equals("receive")) {
                assertTrue(txInfo.getConfirmations() > 0);
                assertNotNull(txInfo.getTxId());
                assertTrue(txInfo.getTxId().length() > 0);
            }

            if (category.equals("move")) {
                // Can't assert on "otheraccount" since it may be null (default account)
            }
        }

        List<TransactionInfo> txList2 = bClient.listTransactions(null);     // Default account

        assertTrue(txList2.size() <= 10);

        List<TransactionInfo> txList3 = bClient.listTransactions();         // Default account

        assertTrue(txList3.size() <= 10);
    }

    @Test
    public void testBackupWallet() {
        bClient.backupWallet(BACKUP_DIRECTORY);

        File file = new File(BACKUP_DIRECTORY + File.separator + "wallet.dat");

        assertTrue(file.exists());

        // file.delete();     // Dangerous, if, by chance, someone sets BACKUP_DIRECTORY to ~/.bitcoin
    }

    @Test
    public void testStop() {
        bClient.stop();
    }

    @Test
    public void testRounding() {
        assertEquals(0.5,  BitcoinClient.roundToTwoDecimals(0.5),    0.00000000001);
        assertEquals(0.06, BitcoinClient.roundToTwoDecimals(0.055),  0.00000000001);
        assertEquals(1.11, BitcoinClient.roundToTwoDecimals(1.114),  0.00000000001);
        assertEquals(1.12, BitcoinClient.roundToTwoDecimals(1.115),  0.00000000001);
        assertEquals(0.01, BitcoinClient.roundToTwoDecimals(0.0149), 0.00000000001);
    }
}
