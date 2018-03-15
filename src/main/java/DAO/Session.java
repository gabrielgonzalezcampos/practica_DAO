package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Session {
    final static Logger log = Logger.getLogger(Main.class);
    public Object get(int id,Class clase){
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("SELECT * FROM ");
        query.append(clase.getName());
        query.append(" WHERE id=");
        query.append(id);
        log.debug("Query:" + query);
        return null;
    }

    public Method tornarMetode(Object b, StringBuffer funcio){
        Method metodes[]=b.getClass().getDeclaredMethods();
        boolean encontrado=false;
        int i=0;
        while(i<metodes.length&&!encontrado) {
            String nom=metodes[i].getName();
            if(metodes[i].getName().equalsIgnoreCase(String.valueOf(funcio)))
                encontrado=true;
            else
                i++;
        }
        if(encontrado)
            return metodes[i];
        else
            return null;
    }

    public void save(Object a) throws IllegalAccessException, InvocationTargetException{
        Field camps[]=a.getClass().getDeclaredFields();

        StringBuffer query = new StringBuffer("INSERT INTO ");
        query.append(a.getClass().getName());
        query.append(" VALUES (");
        for(int i=0; i<camps.length-1;i++){
            query.append("?,");
        }
        query.append("?)");
        //Executem primera query
        log.debug("Primera String:" + query);
        for(int i=0; i<camps.length;i++){
            query= new StringBuffer("UPDATE ");
            query.append(a.getClass().getName());
            query.append(" SET ");
            query.append(camps[i].getName());
            query.append(" = ");
            StringBuffer get = new StringBuffer("get");
            get.append(camps[i].getName());
            log.trace("Métode cridat " + get);
            Method metode= tornarMetode(a,get);
            if(metode!= null) {
                String res = metode.invoke(a).toString();
                query.append(res);
                //Executem segonda query
                log.debug("Segona String:" + query);
            }
        }

    }
}