import java.io.Serializable;


//Object sent back to clients
public class Response implements Serializable {
    private String respType;
    private String field1;

    public Response(String a, String b){
        this.respType = a;
        this.field1 = b;
    }

    public String getRespType() {
        return respType;
    }

    public String getField1() {
        return field1;
    }
}
