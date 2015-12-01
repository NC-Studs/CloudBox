/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.UserFiles;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author victori
 */
@Stateless
public class UserFilesFacade extends AbstractFacade<UserFiles> implements UserFilesFacadeLocal {
    @PersistenceContext(unitName = "oraclePU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserFilesFacade() {
        super(UserFiles.class);
    }
    
}
