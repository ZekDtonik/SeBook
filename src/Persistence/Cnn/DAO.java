package Persistence.Cnn;

import Persistence.Persister;
import org.hibernate.DuplicateMappingException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.*;

public class DAO {
    private static EntityManager db = Connector.getCnn();
    private static Throwable error;

    public static Throwable getError() {
        return error;
    }
    public static EntityManager getEntityManager(){
        return db;
    }
    public static <T> Persister<T> getById(Class<T> tClass, final Integer id){
        return (Persister<T>) db.find(tClass, id);
    }

    public static List findAll(String hql){
        return findAll(hql,null);
    }
    public static List findAll(String hql,  Map<String,String> params){
        List resultData = new ArrayList();
        try{

            Query query = db.createQuery(hql);
            if(params != null){
                Set<Map.Entry<String,String>> entry = params.entrySet();
                Iterator iterator = entry.iterator();
                for (Map.Entry<String, String> eachOne : entry) {
                    query.setParameter(eachOne.getKey(),eachOne.getValue());
                }
            }
            error = null;
            resultData = query.getResultList();
        }
        catch (PersistenceException e) {
            error = e.getCause().getCause();
            System.out.println("Error on retrieve data from Database. Original message: " + e.getMessage());
        }
        return resultData;
    }
    public static Persister findOne(String hql){
        return findOne(hql,null);
    }
    public static Persister findOne(String hql,  Map<String,String> params ){
        try{

            Query query = db.createQuery(hql);
            if(params != null ){
                Set<Map.Entry<String,String> > entry = params.entrySet();
                Iterator iterator = entry.iterator();
                for (Map.Entry<String, String> eachOne : entry) {
                    query.setParameter(eachOne.getKey(),eachOne.getValue());
                }
            }
            error = null;
            return (Persister) query.getSingleResult();
        }
        catch (PersistenceException e) {
            error = e.getCause().getCause();
            System.out.println("Error on retrieve data from Database. Original message: " + e.getMessage());
            return null;
        }
    }
    public static <T> Persister findTypedOne(String hql,Class<T> aClass){
        return findTypedOne(hql,null,aClass);
    }
    public static <T> Persister<T> findTypedOne(String hql,  Map<String,String> params, Class<T> aClass ){
        try{

            TypedQuery<T> query = db.createQuery(hql,aClass);
            if(params != null ){
                Set<Map.Entry<String,String> > entry = params.entrySet();
                Iterator iterator = entry.iterator();
                for (Map.Entry<String, String> eachOne : entry) {
                    query.setParameter(eachOne.getKey(),eachOne.getValue());
                }
            }
            error = null;
            return (Persister<T>) query.getSingleResult();
        }
        catch (PersistenceException e) {
            error = e.getCause().getCause();
            System.out.println("Error on retrieve data from Database. Original message: " + e.getMessage());
            return null;
        }
    }
    public static boolean insert(Persister pojo){
        return insert(pojo,false);
    }
    public static boolean insert(Persister pojo,boolean noCommit){
        try {
            db.getTransaction().begin();
            db.persist(pojo);
            if(!noCommit) {
                db.getTransaction().commit();
            }
            error = null;
            return true;
        }
        catch (PersistenceException e){
            error = e.getCause().getCause();
            db.getTransaction().rollback();
            return false;
        }

    }

    public static  boolean update( Persister pojoNewData){
        try{
            db.getTransaction().begin();
            db.merge(pojoNewData);
            db.getTransaction().commit();
            error = null;
            return true;
        }
        catch (PersistenceException e){
            error = e.getCause().getCause();
            db.getTransaction().rollback();
            return false;
        }
    }
    public static boolean delete(Persister pojo){
        try{
            db.remove(pojo);
            error = null;
            return true;
        }
        catch (PersistenceException e){
            error = e.getCause().getCause();
            db.getTransaction().rollback();
            return false;
        }
    }
}
