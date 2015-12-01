/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author victori
 */
@Entity
@Table(name = "FILE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "File.findAll", query = "SELECT f FROM File f"),
    @NamedQuery(name = "File.findByIdFile", query = "SELECT f FROM File f WHERE f.idFile = :idFile"),
    @NamedQuery(name = "File.findByFilename", query = "SELECT f FROM File f WHERE f.filename = :filename"),
    @NamedQuery(name = "File.findByExt", query = "SELECT f FROM File f WHERE f.ext = :ext"),
    @NamedQuery(name = "File.findByDate", query = "SELECT f FROM File f WHERE f.date = :date"),
    @NamedQuery(name = "File.findByHash", query = "SELECT f FROM File f WHERE f.hash = :hash")})
public class File implements Serializable {
    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FILE")
    private BigDecimal idFile;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "FILENAME")
    private String filename;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 15)
    @Column(name = "EXT")
    private String ext;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1000)
    @Column(name = "HASH")
    private String hash;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "idFile")
    private Collection<UserFiles> userFilesCollection;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    @ManyToOne(optional = false)
    private User idUser;

    public File() {
    }

    public File(BigDecimal idFile) {
        this.idFile = idFile;
    }

    public File(BigDecimal idFile, String filename, String ext, Date date, String hash) {
        this.idFile = idFile;
        this.filename = filename;
        this.ext = ext;
        this.date = date;
        this.hash = hash;
    }

    public BigDecimal getIdFile() {
        return idFile;
    }

    public void setIdFile(BigDecimal idFile) {
        this.idFile = idFile;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    @XmlTransient
    public Collection<UserFiles> getUserFilesCollection() {
        return userFilesCollection;
    }

    public void setUserFilesCollection(Collection<UserFiles> userFilesCollection) {
        this.userFilesCollection = userFilesCollection;
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
        hash += (idFile != null ? idFile.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof File)) {
            return false;
        }
        File other = (File) object;
        if ((this.idFile == null && other.idFile != null) || (this.idFile != null && !this.idFile.equals(other.idFile))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "filters.File[ idFile=" + idFile + " ]";
    }
    
}
