package DAO;
import org.apache.log4j.Logger;

public class Session4 {
    final static Logger log = Logger.getLogger(Main.class);
    public Object get(int id,Class clase){
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("SELECT * FROM");
        query.append(clase.getName());
        query.append("WHERE id=");
        query.append(id);
        log.debug("Query:" + query);
        return null;
    }
}
