package Responses;

import constants.Constant;

import java.io.Serializable;

//Object sent back to clients
public abstract class Response implements Serializable {
    protected Constant respType;
    public abstract Constant getRespType() ;
}
