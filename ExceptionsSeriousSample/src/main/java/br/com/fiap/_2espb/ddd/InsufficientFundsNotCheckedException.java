package br.com.fiap._2espb.ddd;

public class InsufficientFundsNotCheckedException extends RuntimeException{

    public InsufficientFundsNotCheckedException(final String message){
        super(message);
    }

    public InsufficientFundsNotCheckedException(Throwable throwable) {
        super(throwable);
    }
}
