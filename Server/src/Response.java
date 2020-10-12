import java.io.Serializable;


//Object sent back to clients
public abstract class Response implements Serializable {
    protected String respType;
    public abstract String getRespType() ;
}
