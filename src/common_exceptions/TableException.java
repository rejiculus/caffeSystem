package common_exceptions;

import params.CaffeParams;

public class TableException extends RuntimeException{
    public TableException(){
        super(String.format("Incorrect table! Must be between 1 and %d",CaffeParams.TABLE_COUNT));
    }
    
}
