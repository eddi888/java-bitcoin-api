package ru.paradoxs.bitcoin.client;

/**
 * @see http://www.bitcoin.org/wiki/doku.php?id=api
 * @author paradoxs
 */
public class ServerInfo {

    private String version = "";    
    public String getVersion() {
        return version;
    }    
    public void setVersion(String version) {
        this.version = version;
    }


    private double balance = 0;    
    public double getBalance() {
        return balance;
    }    
    public void setBalance(double balance) {
        this.balance = balance;
    }


    private long blocks = 0;
    public long getBlocks() {
        return blocks;
    }
    public void setBlocks(long blocks) {
        this.blocks = blocks;
    }


    private int connections = 0;
    public int getConnections() {
        return connections;
    }
    public void setConnections(int connections) {
        this.connections = connections;
    }


    private boolean isGenerateCoins = false;
    public boolean isIsGenerateCoins() {
        return isGenerateCoins;
    }
    public void setIsGenerateCoins(boolean isGenerateCoins) {
        this.isGenerateCoins = isGenerateCoins;
    }


    private int usedCPUs = -1;
    public int getUsedCPUs() {
        return usedCPUs;
    }
    public void setUsedCPUs(int usedCPUs) {
        this.usedCPUs = usedCPUs;
    }

    
    private double difficulty = 0;
    public double getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(double difficulty) {
        this.difficulty = difficulty;
    }


    private long HashesPerSecond = 0;
    public long getHashesPerSecond() {
        return HashesPerSecond;
    }
    public void setHashesPerSecond(long HashesPerSecond) {
        this.HashesPerSecond = HashesPerSecond;
    }

}
