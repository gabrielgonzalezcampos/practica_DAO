package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.sql.SQLException;

public class Main{
    final static Logger log = Logger.getLogger(Main.class);

    public static void main(String[] args) throws IllegalAccessException,InvocationTargetException,SQLException,InstantiationException,NoSuchMethodException{
        Factory factory= new Factory();
        Session session = factory.openSession();
        int res =session.initBD();
        if(res==0) {
            Employee empleado = new Employee("JJ", "Soledispa", 23000);
            int id =session.save(empleado);
            Employee proba = (Employee) session.get(id,Employee.class);
            log.debug("Resultat del Get -> Nom: "+proba.getNom()+" Cognom: "+proba.getCognom()+" Salari: "+proba.getSalari());
            empleado = new Employee("Marc", "Llena", 3242);
            session.update(empleado, id);
            proba = (Employee) session.get(id,Employee.class);
            log.debug("Resultat del Get -> Nom: "+proba.getNom()+" Cognom: "+proba.getCognom()+" Salari: "+proba.getSalari());
            session.delete(id,Employee.class);
        }
    }
}
