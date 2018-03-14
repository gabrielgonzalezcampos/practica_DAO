package DAO;

public class Factory {

    public Session openSession() {
        Session session = new Session();
        return session;
    }

}
