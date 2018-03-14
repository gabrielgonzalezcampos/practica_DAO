package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;

public class Session {
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

    public void save(Object a) {
        Field camps[]=a.getClass().getDeclaredFields();
        StringBuffer query = new StringBuffer("INSERT INTO");
        query.append(a.getClass().getName());
        query.append(" VALUES (");
        for(int i=0; i<camps.length-1;i++){
            query.append("?,");
        }
        query.append("?)");
        //Executem primera query
        //log.debug("Primera String:" + query);
        for(int i=0; i<camps.length-1;i++){
            query= new StringBuffer("UPDATE ");
        }

    }
}