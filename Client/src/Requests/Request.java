package Requests;

import java.io.Serializable;

//Object sent from client to Server
public abstract class Request implements Serializable{
    protected String reqType;
    public abstract String getReqType() ;
}
