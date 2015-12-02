/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejbEntity;

import entity.FileTable;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author Andrew
 */
@Local
public interface FileTableFacadeLocal {

    void create(FileTable fileTable);

    void edit(FileTable fileTable);

    void remove(FileTable fileTable);

    FileTable find(Object id);

    List<FileTable> findAll();

    List<FileTable> findRange(int[] range);

    int count();
    
}
