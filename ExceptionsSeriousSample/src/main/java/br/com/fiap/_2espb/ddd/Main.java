package br.com.fiap._2espb.ddd;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    static void main(){
            Account orlandaoAccount = new Account(1000d, "Orlando");
            Account cilasAccount = new Account(1000d, "Cilas");

        orlandaoAccount.sendPix(200d, "cilas2026@gmail.com");
            try {
                orlandaoAccount.sendAmount(10000d, cilasAccount);
            } catch (InsufficientFundsNotCheckedException e) {
                e.printStackTrace();
            }
//            orlandaoAccount.sendAmount(10000d, cilasAccount);
            System.out.println("Program terminated successfully");
        }
}
