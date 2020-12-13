package Requests;

import constants.Constant;

import java.io.Serializable;

//Object sent from client to Server
public abstract class Request implements Serializable{
    protected Constant reqType;
    public abstract Constant getReqType();
}
