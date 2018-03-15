package DAO;

import org.apache.log4j.Logger;

import java.lang.reflect.InvocationTargetException;

public class Main{
    //final static Logger log = Logger.getLogger(Main.class);
    public static void main(String[] args) throws IllegalAccessException,InvocationTargetException{
        Factory factory= new Factory();
        Session session = factory.openSession();
        Employee empleado = new Employee("JJ","Soledispa",23000);
        session.get(12,empleado.getClass());
        session.save(empleado);
    }
}
