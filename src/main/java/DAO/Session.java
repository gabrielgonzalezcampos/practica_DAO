package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.*;


public class Session {
    final static Logger log = Logger.getLogger(Main.class);
    Connection conn = null;

    public Object get(int id,Class clase)throws SQLException,InstantiationException,IllegalAccessException,InvocationTargetException, NoSuchMethodException{
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("SELECT * FROM ");
        String[] table = clase.getName().split("\\.");
        query.append(table[table.length-1]);
        query.append(" WHERE id=");
        query.append(id);
        log.debug("Query Get:" + query);
        Statement stmt = conn.createStatement();
        ResultSet resultset =stmt.executeQuery(query.toString());
        Object res=null;
        if(resultset.next()) {
            ResultSetMetaData metadata = resultset.getMetaData();
            res = clase.newInstance();
            for (int i = 2; i < metadata.getColumnCount() + 1; i++) {
                StringBuffer set = new StringBuffer("set");
                set.append(metadata.getColumnName(i));
                Method metode = tornarMetode(clase, set);
                if (metode != null) {
                    Object valor = resultset.getObject(i);
                    metode.invoke(res, valor);
                }
            }
        }
        return res;
    }

    public void delete(int id,Class clase)throws SQLException{
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("DELETE FROM ");
        query.append(clase.getName());
        query.append(" WHERE id=");
        query.append(id);
        log.debug("Query Delete:" + query);
        Statement stmt = conn.createStatement();
        stmt.execute(query.toString());
        log.debug("Delete realitzat");
    }

    private Method tornarMetode(Class a, StringBuffer funcio){
        Method metodes[]=a.getDeclaredMethods();
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

    public int save(Object a) throws IllegalAccessException, InvocationTargetException,SQLException{
        //Primer obtenim el número de objectes a la clase per fer l'id.
        StringBuffer query = new StringBuffer("SELECT COUNT(*) AS rowcount FROM ");
        String[] table = a.getClass().getName().split("\\.");
        query.append(table[table.length-1]);
        Statement stmt = conn.createStatement();
        ResultSet result = stmt.executeQuery(query.toString());
        result.next();
        int id = result.getInt("rowcount")+1;
        log.debug("Identificador asignat:" + id);
        Field camps[]=a.getClass().getDeclaredFields();
        query = new StringBuffer("INSERT INTO ");
        query.append(table[table.length-1]);
        query.append(" VALUES (");
        for(int i=0; i<camps.length;i++){
            query.append("?,");
        }
        query.append("?);");
        //Executem primera query i obtenem la id
        log.debug("Query Save:" + query);
        PreparedStatement pstmt = conn.prepareStatement(query.toString());
        pstmt.setObject(1,id);
        for (int cont = 0; cont < camps.length; cont++) {
            StringBuffer get = new StringBuffer("get");
            get.append(camps[cont].getName());
            Method metode = tornarMetode(a.getClass(), get);
            if (metode != null) {
                Object res = metode.invoke(a);
                pstmt.setObject(cont+2,res);
            }
        }
        pstmt.execute();
        log.debug("Save realitzat");
        return id;
    }

    public void update(Object a, int id) throws IllegalAccessException, InvocationTargetException,SQLException{
        Field camps[]=a.getClass().getDeclaredFields();
        StringBuffer query = new StringBuffer("UPDATE ");
        String[] table = a.getClass().getName().split("\\.");
        query.append(table[table.length-1]);
        query.append(" SET ");
        int i =0;
        for(i=0; i<camps.length-1;i++){
            query.append(camps[i].getName());
            query.append(" = ");
            query.append("?, ");
        }
        query.append(camps[i].getName());
        query.append(" = ");
        query.append(" ? WHERE id=");
        query.append(id);
        log.debug("Query Update:" + query);
        PreparedStatement pstmt = conn.prepareStatement(query.toString());
        for (int cont = 0; cont < camps.length; cont++) {
            Field camp = camps[cont];
            StringBuffer get = new StringBuffer("get");
            get.append(camp.getName());
            Method metode = tornarMetode(a.getClass(), get);
            if (metode != null) {
                Object res = metode.invoke(a);
                pstmt.setObject(cont+1, res);
            }
        }
        pstmt.execute();
        log.debug("Update realitzat");
    }

    public int initBD() throws java.sql.SQLException{
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
        else {
            Statement stmt = conn.createStatement();
            stmt.execute("USE DAO");
            return 0;
        }
    }
}