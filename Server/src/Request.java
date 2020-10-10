import java.io.Serializable;

//Object sent from client to Server
public class Request implements Serializable{
    private String reqType;
    private String var1, var2;

    public Request(String s, String a, String b){
        this.reqType = s;
        this.var1 = a;
        this.var2 = b;
    }

    public String getReqType() {
        return reqType;
    }

    public String getVar1() {
        return var1;
    }

    public String getVar2() {
        return var2;
    }
}
