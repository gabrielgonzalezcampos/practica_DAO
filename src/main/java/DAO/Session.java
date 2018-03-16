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

    public void delete(int id,Class clase){
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("DELETE * FROM ");
        query.append(clase.getName());
        query.append(" WHERE id=");
        query.append(id);
        log.debug("Query:" + query);
    }

    private Method tornarMetode(Object b, StringBuffer funcio){
        Method metodes[]=b.getClass().getDeclaredMethods();
        boolean encontrado=false;
        int i=0;
        while(i<metodes.length&&!encontrado) {
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
        //Executem primera query i obtenem la id
        int id=0;
        log.debug("Primera Query:" + query);
        for (Field camp : camps) {
            query = new StringBuffer("UPDATE ");
            query.append(a.getClass().getName());
            query.append(" SET ");
            query.append(camp.getName());
            query.append(" = ");
            StringBuffer get = new StringBuffer("get");
            get.append(camp.getName());
            log.trace("Métode cridat " + get);
            Method metode = tornarMetode(a, get);
            if (metode != null) {
                String res = metode.invoke(a).toString();
                ferQuery(query, metode, res);

                query.append(" WHERE id=");
                query.append(id);
                //Executem segonda query
                log.debug("Segona Query:" + query);
            }
        }
    }

    public void update(Object a, int id) throws IllegalAccessException, InvocationTargetException{
        Field camps[]=a.getClass().getDeclaredFields();
        StringBuffer query = new StringBuffer("UPDATE ");
        query.append(a.getClass().getName());
        query.append(" SET ");
        int i;
        for(i=0;i<camps.length-1;i++) {
            query.append(camps[i].getName());
            query.append(" = ");
            StringBuffer get = new StringBuffer("get");
            get.append(camps[i].getName());
            log.trace("Métode cridat " + get);
            Method metode = tornarMetode(a, get);
            if (metode != null) {
                String res = metode.invoke(a).toString();
                ferQuery(query, metode, res);
                query.append(", ");
            }
        }
        query.append(camps[i].getName());
        query.append(" = ");
        StringBuffer get = new StringBuffer("get");
        get.append(camps[i].getName());
        log.trace("Métode cridat " + get);
        Method metode = tornarMetode(a, get);
        if (metode != null) {
            String res = metode.invoke(a).toString();
            ferQuery(query, metode, res);
        }
        query.append(" WHERE id=");
        query.append(id);
        //Executem segonda query
        log.debug("Query:" + query);
    }

    private void ferQuery(StringBuffer query, Method metode, String res) {
        if(metode.getReturnType()==java.lang.String.class){
            query.append("'");
            query.append(res);
            query.append("'");
        }
        else{ query.append(res); }
    }
}