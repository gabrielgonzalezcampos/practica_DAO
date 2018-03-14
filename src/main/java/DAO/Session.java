package DAO;

import java.lang.reflect.Field;

public class Session {

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
