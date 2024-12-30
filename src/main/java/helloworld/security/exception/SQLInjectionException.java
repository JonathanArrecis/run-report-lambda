package helloworld.security.exception;

public final class SQLInjectionException extends  RuntimeException{

    public SQLInjectionException(String message){
        super(message);
    }

    public SQLInjectionException(String message, Throwable cause){
        super(message, cause);
    }

    public SQLInjectionException(Throwable cause){
        super(cause);
    }

    public SQLInjectionException(){
        super();
    }
}
