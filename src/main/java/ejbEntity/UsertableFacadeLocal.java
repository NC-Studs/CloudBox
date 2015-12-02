/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbEntity;

import entity.Usertable;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrew
 */
@Local
public interface UsertableFacadeLocal {

    void create(Usertable usertable);

    void edit(Usertable usertable);

    void remove(Usertable usertable);

    Usertable find(Object id);

    List<Usertable> findAll();

    List<Usertable> findRange(int[] range);

    int count();
    
}
