/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
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
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@Entity
@Table(name = "rawdata")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Rawdata.findAll", query = "SELECT r FROM Rawdata r"),
    @NamedQuery(name = "Rawdata.findByIdRawData", query = "SELECT r FROM Rawdata r WHERE r.idRawData = :idRawData"),
    @NamedQuery(name = "Rawdata.findByPageAddress", query = "SELECT r FROM Rawdata r WHERE r.pageAddress = :pageAddress"),
    @NamedQuery(name = "Rawdata.findByLevel", query = "SELECT r FROM Rawdata r WHERE r.level = :level"),
    @NamedQuery(name = "Rawdata.findByPageDate", query = "SELECT r FROM Rawdata r WHERE r.pageDate = :pageDate"),
    @NamedQuery(name = "Rawdata.findByPageContent", query = "SELECT r FROM Rawdata r WHERE r.pageContent = :pageContent"),
    @NamedQuery(name = "Rawdata.findByProcessed", query = "SELECT r FROM Rawdata r WHERE r.processed = :processed"),
    @NamedQuery(name = "Rawdata.findByCrc", query = "SELECT r FROM Rawdata r WHERE r.crc = :crc"),
    @NamedQuery(name = "Rawdata.findByTitle", query = "SELECT r FROM Rawdata r WHERE r.title = :title"),
    @NamedQuery(name = "Rawdata.findByDescription", query = "SELECT r FROM Rawdata r WHERE r.description = :description")})
public class Rawdata implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_RawData")
    private Integer idRawData;
    @Column(name = "PageAddress")
    private String pageAddress;
    @Column(name = "Level")
    private Integer level;
    @Column(name = "PageDate")
    @Temporal(TemporalType.DATE)
    private Date pageDate;
    @Column(name = "PageContent")
    private String pageContent;
    @Column(name = "Processed")
    private Integer processed;
    @Column(name = "CRC")
    private Integer crc;
    @Column(name = "Title")
    private String title;
    @Column(name = "Description")
    private String description;
    @OneToMany(mappedBy = "idRawData")
    private Collection<Headers> headersCollection;
    @JoinColumn(name = "Id_Domain", referencedColumnName = "Id_Domain")
    @ManyToOne
    private Domain idDomain;

    public Rawdata() {
    }

    public Rawdata(Integer idRawData) {
        this.idRawData = idRawData;
    }

    public Integer getIdRawData() {
        return idRawData;
    }

    public void setIdRawData(Integer idRawData) {
        this.idRawData = idRawData;
    }

    public String getPageAddress() {
        return pageAddress;
    }

    public void setPageAddress(String pageAddress) {
        this.pageAddress = pageAddress;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Date getPageDate() {
        return pageDate;
    }

    public void setPageDate(Date pageDate) {
        this.pageDate = pageDate;
    }

    public String getPageContent() {
        return pageContent;
    }

    public void setPageContent(String pageContent) {
        this.pageContent = pageContent;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public Integer getCrc() {
        return crc;
    }

    public void setCrc(Integer crc) {
        this.crc = crc;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<Headers> getHeadersCollection() {
        return headersCollection;
    }

    public void setHeadersCollection(Collection<Headers> headersCollection) {
        this.headersCollection = headersCollection;
    }

    public Domain getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(Domain idDomain) {
        this.idDomain = idDomain;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idRawData != null ? idRawData.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Rawdata)) {
            return false;
        }
        Rawdata other = (Rawdata) object;
        if ((this.idRawData == null && other.idRawData != null) || (this.idRawData != null && !this.idRawData.equals(other.idRawData))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Rawdata[ idRawData=" + idRawData + " ]";
    }
    
}
