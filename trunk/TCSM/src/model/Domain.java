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
@Table(name = "domain")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Domain.findAll", query = "SELECT d FROM Domain d"),
    @NamedQuery(name = "Domain.findByIdDomain", query = "SELECT d FROM Domain d WHERE d.idDomain = :idDomain"),
    @NamedQuery(name = "Domain.findByDomainName", query = "SELECT d FROM Domain d WHERE d.domainName = :domainName"),
    @NamedQuery(name = "Domain.findByTypeDomain", query = "SELECT d FROM Domain d WHERE d.typeDomain = :typeDomain"),
    @NamedQuery(name = "Domain.findByActivation", query = "SELECT d FROM Domain d WHERE d.activation = :activation"),
    @NamedQuery(name = "Domain.findByRobots", query = "SELECT d FROM Domain d WHERE d.robots = :robots"),
    @NamedQuery(name = "Domain.findByDepth", query = "SELECT d FROM Domain d WHERE d.depth = :depth")})
public class Domain implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Domain")
    private Integer idDomain;
    @Column(name = "DomainName")
    private String domainName;
    @Column(name = "TypeDomain")
    private Integer typeDomain;
    @Column(name = "Activation")
    @Temporal(TemporalType.DATE)
    private Date activation;
    @Column(name = "Robots")
    private String robots;
    @Column(name = "Depth")
    private Integer depth;
    @OneToMany(mappedBy = "idDomain")
    private Collection<Blogroll> blogrollCollection;
    @JoinColumn(name = "Id_Location", referencedColumnName = "Id_Location")
    @ManyToOne
    private Location idLocation;
    @OneToMany(mappedBy = "idDomain")
    private Collection<Blogpost> blogpostCollection;
    @OneToMany(mappedBy = "idDomain")
    private Collection<Rawdata> rawdataCollection;

    public Domain() {
    }

    public Domain(Integer idDomain) {
        this.idDomain = idDomain;
    }

    public Integer getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(Integer idDomain) {
        this.idDomain = idDomain;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public Integer getTypeDomain() {
        return typeDomain;
    }

    public void setTypeDomain(Integer typeDomain) {
        this.typeDomain = typeDomain;
    }

    public Date getActivation() {
        return activation;
    }

    public void setActivation(Date activation) {
        this.activation = activation;
    }

    public String getRobots() {
        return robots;
    }

    public void setRobots(String robots) {
        this.robots = robots;
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    @XmlTransient
    public Collection<Blogroll> getBlogrollCollection() {
        return blogrollCollection;
    }

    public void setBlogrollCollection(Collection<Blogroll> blogrollCollection) {
        this.blogrollCollection = blogrollCollection;
    }

    public Location getIdLocation() {
        return idLocation;
    }

    public void setIdLocation(Location idLocation) {
        this.idLocation = idLocation;
    }

    @XmlTransient
    public Collection<Blogpost> getBlogpostCollection() {
        return blogpostCollection;
    }

    public void setBlogpostCollection(Collection<Blogpost> blogpostCollection) {
        this.blogpostCollection = blogpostCollection;
    }

    @XmlTransient
    public Collection<Rawdata> getRawdataCollection() {
        return rawdataCollection;
    }

    public void setRawdataCollection(Collection<Rawdata> rawdataCollection) {
        this.rawdataCollection = rawdataCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idDomain != null ? idDomain.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Domain)) {
            return false;
        }
        Domain other = (Domain) object;
        if ((this.idDomain == null && other.idDomain != null) || (this.idDomain != null && !this.idDomain.equals(other.idDomain))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Domain[ idDomain=" + idDomain + " ]";
    }
    
}
