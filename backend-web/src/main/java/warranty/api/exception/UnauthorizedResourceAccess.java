package warranty.api.exception;

public class UnauthorizedResourceAccess extends RuntimeException{
    public UnauthorizedResourceAccess(String message){
        super(message);
    }
}
