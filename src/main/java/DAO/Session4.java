package DAO;

public class Session4 {
    public Object get(int id,Class clase){
        //String query="SELECT * FROM";
        StringBuffer query=new StringBuffer("SELECT * FROM");
        query.append(clase.getName());
        query.append("WHERE id=");
        query.append(id);

    }
}
