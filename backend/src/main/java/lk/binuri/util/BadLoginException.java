package lk.binuri.util;

public class BadLoginException extends RuntimeException{
    public BadLoginException(String msg){
        super(msg);
    }
}
