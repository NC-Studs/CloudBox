/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb;

import entity.UserFiles;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author victori
 */
@Local
public interface UserFilesFacadeLocal {

    void create(UserFiles userFiles);

    void edit(UserFiles userFiles);

    void remove(UserFiles userFiles);

    UserFiles find(Object id);

    List<UserFiles> findAll();

    List<UserFiles> findRange(int[] range);

    int count();
    
}
