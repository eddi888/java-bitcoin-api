package ru.paradoxs.bitcoin.client;

public class LabelInfo {

    private String label = "";
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }


    private double amount = 0;
    public double getAmount() {
        return amount;
    }
    public void setAmount(double amount) {
        this.amount = amount;
    }


    private long confirmations = 0;
    public long getConfirmations() {
        return confirmations;
    }
    public void setConfirmations(long confirmations) {
        this.confirmations = confirmations;
    }


}
