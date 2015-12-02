/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbEntity;

import entity.UserFile;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrew
 */
@Local
public interface UserFileFacadeLocal {

    void create(UserFile userFile);

    void edit(UserFile userFile);

    void remove(UserFile userFile);

    UserFile find(Object id);

    List<UserFile> findAll();

    List<UserFile> findRange(int[] range);

    int count();
    
}
