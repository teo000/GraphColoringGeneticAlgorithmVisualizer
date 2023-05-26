package org.example;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class DefaultEntityManagerFactory implements AutoCloseable{
    private static DefaultEntityManagerFactory instance;
    public EntityManagerFactory emf;
    private DefaultEntityManagerFactory(){
        emf = Persistence.createEntityManagerFactory("default");
    }
    public static synchronized DefaultEntityManagerFactory getInstance(){
        if(instance == null)
            instance = new DefaultEntityManagerFactory();
        return instance;
    }

    @Override
    public void close() throws Exception {
        emf.close();
    }
}
