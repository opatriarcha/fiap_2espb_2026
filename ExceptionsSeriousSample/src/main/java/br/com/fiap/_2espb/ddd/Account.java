package br.com.fiap._2espb.ddd;

public class Account {
    private Double amount;
    private String owner;

    public Account( Double amount, String owner ){
        this.amount = amount;
        this.owner = owner;
    }

    public Account(){
    }

    public Account(String owner) {
        this.owner = owner;
    }

    public void addMoney(Double amount){
        this.amount = this.amount + amount;
        System.out.println("Actual  amount for " + this.owner + ": "  + this.amount);
    }

    public void sendPix( final Double amount, final String pixKey ){
        this.amount = this.amount - amount;
    }

    public void sendAmount(final Double amount, final Account destinyAccount) throws InsufficientFundsNotCheckedException{

        if(amount > this.amount)
            throw new InsufficientFundsNotCheckedException("Orlandao nao tem essa grana toda!");

        this.amount = this.amount - amount;
        destinyAccount.addMoney(amount);
        System.out.println("Actual sender " + this.owner+ " Amount: " + this.amount);
    }
}
