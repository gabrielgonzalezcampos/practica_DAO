package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class Session {
    final static Logger log = Logger.getLogger(Main.class);
    Connection conn = null;

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

    public void save(Object a) throws IllegalAccessException, InvocationTargetException,java.sql.SQLException{
        Field camps[]=a.getClass().getDeclaredFields();
        StringBuffer query = new StringBuffer("INSERT INTO ");
        query.append(a.getClass().getName());
        query.append(" VALUES (");
        for(int i=0; i<camps.length-1;i++){
            query.append("?,");
        }
        query.append("?);");
        //Executem primera query i obtenem la id
        log.debug("Query Save:" + query);
        String consulta = query.toString();
        PreparedStatement pstmt = conn.prepareStatement(consulta);
        int cont =1;
        for (Field camp : camps) {
            StringBuffer get = new StringBuffer("get");
            get.append(camp.getName());
            Method metode = tornarMetode(a, get);
            if (metode != null) {
                Object res = metode.invoke(a);
                pstmt.setObject(cont,res);
            }
            cont++;
        }
        boolean res = pstmt.execute();
        log.debug("Save realitzat, resultat " + res);
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

    public int initBD(){
        try {
            //conn = DriverManager.getConnection("http://147.83.7.157/" + "?user=ea0&password=Mazinger82");
            conn = DriverManager.getConnection("jdbc:mysql://147.83.7.157:3306/DAO"+ "?user=root&password=Mazinger82");
        } catch (SQLException ex) {
            // handle any errors
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }
        if(conn==null) {
            log.debug("Error al accedir a la base de dades");
            return -1;
        }
        else {return 0;}
    }
}