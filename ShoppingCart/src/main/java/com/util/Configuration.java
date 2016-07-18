package com.util;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.hibernate.Session;
import org.hibernate.cfg.AnnotationConfiguration;


public class Configuration implements ServletContextListener{
	static org.hibernate.cfg.Configuration cfg;
	static Set<Class> objSet; 
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		cfg=new AnnotationConfiguration().configure();
		objSet=new HashSet<Class>();
	}

	public static <T> Session getSession( Class<T>  className){
		if(!objSet.contains(className)){
			cfg.addAnnotatedClass(className);
			objSet.add(className);
			
		}		
		return cfg.buildSessionFactory().openSession();		
	}
}
