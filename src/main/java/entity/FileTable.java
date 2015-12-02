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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Andrew
 */
@Entity
@Table(name = "FILE_TABLE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FileTable.findAll", query = "SELECT f FROM FileTable f"),
    @NamedQuery(name = "FileTable.findByIdFile", query = "SELECT f FROM FileTable f WHERE f.idFile = :idFile"),
    @NamedQuery(name = "FileTable.findByDatefile", query = "SELECT f FROM FileTable f WHERE f.datefile = :datefile"),
    @NamedQuery(name = "FileTable.findByExt", query = "SELECT f FROM FileTable f WHERE f.ext = :ext"),
    @NamedQuery(name = "FileTable.findByFilename", query = "SELECT f FROM FileTable f WHERE f.filename = :filename"),
    @NamedQuery(name = "FileTable.findByHash", query = "SELECT f FROM FileTable f WHERE f.hash = :hash")})
public class FileTable implements Serializable {

    private static final long serialVersionUID = 1L;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID_FILE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQFILE")
    @SequenceGenerator(initialValue = 1, sequenceName = "SEQFILE", allocationSize = 1, name = "SEQFILE")
    private Long idFile;
    @Column(name = "DATEFILE")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datefile;
    @Size(max = 255)
    @Column(name = "EXT")
    private String ext;
    @Size(max = 255)
    @Column(name = "FILENAME")
    private String filename;
    @Size(max = 255)
    @Column(name = "HASH")
    private String hash;
    @JoinColumn(name = "ID_USER", referencedColumnName = "ID_USER")
    @ManyToOne
    private Usertable idUser;
    @OneToMany(mappedBy = "idFile")
    private Collection<UserFile> userFileCollection;

    public FileTable() {
    }

    public FileTable(Long idFile) {
        this.idFile = idFile;
    }

    public Long getIdFile() {
        return idFile;
    }

    public void setIdFile(Long idFile) {
        this.idFile = idFile;
    }

    public Date getDatefile() {
        return datefile;
    }

    public void setDatefile(Date datefile) {
        this.datefile = datefile;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public Usertable getIdUser() {
        return idUser;
    }

    public void setIdUser(Usertable idUser) {
        this.idUser = idUser;
    }

    @XmlTransient
    public Collection<UserFile> getUserFileCollection() {
        return userFileCollection;
    }

    public void setUserFileCollection(Collection<UserFile> userFileCollection) {
        this.userFileCollection = userFileCollection;
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
        if (!(object instanceof FileTable)) {
            return false;
        }
        FileTable other = (FileTable) object;
        if ((this.idFile == null && other.idFile != null) || (this.idFile != null && !this.idFile.equals(other.idFile))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nc.cloudbox2.FileTable[ idFile=" + idFile + " ]";
    }

}
