/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sv.udb.controlador;

import com.sv.udb.modelo.Alumnos;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.ManagedBean;
import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;
import org.primefaces.context.RequestContext;

/**
 *
 * @author REGISTRO
 */
@Named(value = "alumnosBean")
@ViewScoped
@ManagedBean

public class AlumnosBean implements Serializable{
    private Alumnos objeAlum;
    private boolean guardar;
    private List<Alumnos> alumnosList=null;

    public Alumnos getObjeAlum() {
        return objeAlum;
    }

    public void setObjeAlum(Alumnos objeAlum) {
        this.objeAlum = objeAlum;
    }

    public boolean isGuardar() {
        return guardar;
    }
    
    public List<Alumnos> getAlumnosList(){
        return alumnosList;
    }
    
    public void setAlumnosList(List<Alumnos> alumnosList){
        this.alumnosList=alumnosList;
    }
    
    /**
     * Creates a new instance of AlumnosBean
     */
    
    public AlumnosBean() {
    }
    
    @PostConstruct
    public void init()
    {
        this.objeAlum = new Alumnos();
        this.guardar = true;
        this.alumnosList=this.ConsTodo();
    }
    
    public List<Alumnos> ConsTodo() {
        List<Alumnos> resp = new ArrayList<>();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POOPU");
        EntityManager em = emf.createEntityManager();
        try 
        {
            TypedQuery<Alumnos> query = em.createNamedQuery("Alumnos.findAll", Alumnos.class);
            resp = query.getResultList();
        } 
        catch (Exception ex) 
        {

        }
        return resp;
    }
    
    public void guar()
    {
        RequestContext ctx= RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POOPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            em.persist(this.objeAlum);
            tx.commit();
            this.guardar = true;
            this.objeAlum= new Alumnos();
            this.alumnosList= this.ConsTodo();
            ctx.execute("setMessage('MESS_SUCC','Alerta','Datos guardados')");
        }
        catch(Exception ex)
        {
            ctx.execute("setMessage('MESS_ERRO','Alerta','Error algo ha pasado')");
            tx.rollback();
            ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
    
    public void modi()
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POOPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            em.merge(objeAlum);
            tx.commit();
            this.alumnosList = this.ConsTodo();
            this.objeAlum = new Alumnos();
            ctx.execute("setMessage('MESS_SUCC', 'Alerta', 'Datos modificado');");
        }
        catch(Exception ex)
        {
            tx.rollback();
        }
        em.close();
        emf.close();
    }
    
    public void elim(int codi)
    {
        RequestContext ctx = RequestContext.getCurrentInstance();
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POOPU");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try
        {
            Alumnos obj = em.find(Alumnos.class, codi);
            em.remove(obj);
            tx.commit();
            this.alumnosList = this.ConsTodo();
            this.objeAlum = new Alumnos();
            ctx.execute("setMessage('MESS_SUCC', 'Alerta', 'Datos eliminados');");
        }
        catch(Exception ex)
        {
            tx.rollback();
        }
        em.close();
        emf.close();
    }
    
    public void cons(int codi)
    {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("POOPU");
        EntityManager em = emf.createEntityManager();
        try
        {
            this.objeAlum = em.find(Alumnos.class, codi);
        }
        catch(Exception ex)
        {
            ex.printStackTrace();
        }
        finally
        {
            em.close();
            emf.close();            
        }
    }
}
