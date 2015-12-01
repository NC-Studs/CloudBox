/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import org.eclipse.persistence.annotations.Cache;

/**
 *
 * @author victori
 */
@Entity
@Table(name = "USER_FILES")
@Cache(
        alwaysRefresh = true,
        refreshOnlyIfNewer = true
)
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "UserFiles.findAll", query = "SELECT u FROM UserFiles u"),
    @NamedQuery(name = "UserFiles.findByDeleted", query = "SELECT u FROM UserFiles u WHERE u.deleted = :deleted"),
    @NamedQuery(name = "UserFiles.findById", query = "SELECT u FROM UserFiles u WHERE u.id = :id")})
public class UserFiles implements Serializable {
    private static final long serialVersionUID = 1L;
    @Column(name = "DELETED")
    @Temporal(TemporalType.TIMESTAMP)
    private Date deleted;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private BigDecimal id;
    @JoinColumn(name = "ID_FILE", referencedColumnName = "ID_FILE")
    @ManyToOne(optional = false)
    private File idFile;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    @ManyToOne(optional = false)
    private User idUser;

    public UserFiles() {
    }

    public UserFiles(BigDecimal id) {
        this.id = id;
    }

    public Date getDeleted() {
        return deleted;
    }

    public void setDeleted(Date deleted) {
        this.deleted = deleted;
    }

    public BigDecimal getId() {
        return id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public File getIdFile() {
        return idFile;
    }

    public void setIdFile(File idFile) {
        this.idFile = idFile;
    }

    public User getIdUser() {
        return idUser;
    }

    public void setIdUser(User idUser) {
        this.idUser = idUser;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof UserFiles)) {
            return false;
        }
        UserFiles other = (UserFiles) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "filters.UserFiles[ id=" + id + " ]";
    }
    
}
