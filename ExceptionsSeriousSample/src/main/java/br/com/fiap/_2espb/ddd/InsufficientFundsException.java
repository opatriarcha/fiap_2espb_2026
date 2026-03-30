package br.com.fiap._2espb.ddd;

public class InsufficientFundsException extends Exception{

    public InsufficientFundsException( final String message){
        super(message);
    }

    public InsufficientFundsException( Throwable throwable) {
        super(throwable);
    }
}
