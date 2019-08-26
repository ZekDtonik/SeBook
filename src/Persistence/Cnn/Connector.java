package Persistence.Cnn;

import javax.persistence.*;

public class Connector {
    private static String PERSISTANCE_UNITNAME = "SeboOnlineUnit";
    private static EntityManager instance;
    private Connector(){}
    private EntityManager getPersistenceInstance(){
        EntityManagerFactory factory = Persistence.createEntityManagerFactory(PERSISTANCE_UNITNAME);
        return factory.createEntityManager();
    }
    public static EntityManager getCnn(){
        if(instance == null){
            Connector connector = new Connector();
            instance = connector.getPersistenceInstance();
        }
        return instance;
    }
}
