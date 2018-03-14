package DAO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class Session {

    public Method tornarMetode(Object b, String funcio){
        Method metodes[]=b.getClass().getDeclaredMethods();
        boolean encontrado;
        
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
            query= new StringBuffer("UPDATE");
            query.append(a.getClass().getName());
            query.append("SET");
            query.append(camps[i].getName());
            query.append("=");
            query.append(metodes[i].getName());

        }

    }
}