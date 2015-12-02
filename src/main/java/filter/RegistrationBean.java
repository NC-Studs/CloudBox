/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package filter;



import ejbEntity.UsertableFacadeLocal;
import ejbTools.GenerateHash;
import entity.Usertable;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.transaction.UserTransaction;

import org.eclipse.persistence.exceptions.DatabaseException;

/**
 *
 * @author victori
 */
@ManagedBean(name = "registrationBean")
@SessionScoped

public class RegistrationBean implements Serializable {

    @EJB
    UsertableFacadeLocal ufl;
    
    @EJB
    GenerateHash gh;
    
    private Usertable user;
    private String userName;
    private String pass;
    private String eMail;

    /**
     * Creates a new instance of RegistrationBean
     */
    public RegistrationBean() {
    }

    public Usertable getUser() {
        return user;
    }

    public void setUser(Usertable user) {
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    @Resource
    UserTransaction utx;

    public String regNewUser() throws DatabaseException, Exception {
       
        Usertable user = new Usertable();
        user.setName(userName);
        user.setPasshash(gh.generateHash(pass));
        user.setEmail(eMail);
        ufl.create(user);

        return "index.xhtml?faces-redirect=true";
    }
}
