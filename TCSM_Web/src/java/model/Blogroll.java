/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@Entity
@Table(name = "blogroll")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Blogroll.findAll", query = "SELECT b FROM Blogroll b"),
    @NamedQuery(name = "Blogroll.findByIdBlogRoll", query = "SELECT b FROM Blogroll b WHERE b.idBlogRoll = :idBlogRoll"),
    @NamedQuery(name = "Blogroll.findByBlog", query = "SELECT b FROM Blogroll b WHERE b.blog = :blog"),
    @NamedQuery(name = "Blogroll.findByTypeBlogRoll", query = "SELECT b FROM Blogroll b WHERE b.typeBlogRoll = :typeBlogRoll"),
    @NamedQuery(name = "Blogroll.findByIddestination", query = "SELECT b FROM Blogroll b WHERE b.iddestination = :iddestination"),
    @NamedQuery(name = "Blogroll.findByIddomain", query = "SELECT b FROM Blogroll b WHERE b.idDomain = :idDomain"),
    @NamedQuery(name = "Blogroll.findByDomainBlog", query = "SELECT b FROM Blogroll b WHERE b.idDomain = :idDomain AND b.blog = :blog")
})
public class Blogroll implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_BlogRoll")
    private Integer idBlogRoll;
    @Column(name = "Blog")
    private String blog;
    @Column(name = "TypeBlogRoll")
    private Integer typeBlogRoll;
    @Column(name = "Id_destination")
    private Integer iddestination;
    @JoinColumn(name = "Id_Domain", referencedColumnName = "Id_Domain")
    @ManyToOne
    private Domain idDomain;

    public Blogroll() {
    }

    public Blogroll(Integer idBlogRoll) {
        this.idBlogRoll = idBlogRoll;
    }

    public Integer getIdBlogRoll() {
        return idBlogRoll;
    }

    public void setIdBlogRoll(Integer idBlogRoll) {
        this.idBlogRoll = idBlogRoll;
    }

    public String getBlog() {
        return blog;
    }

    public void setBlog(String blog) {
        this.blog = blog;
    }

    public Integer getTypeBlogRoll() {
        return typeBlogRoll;
    }

    public void setTypeBlogRoll(Integer typeBlogRoll) {
        this.typeBlogRoll = typeBlogRoll;
    }

    public Integer getIddestination() {
        return iddestination;
    }

    public void setIddestination(Integer iddestination) {
        this.iddestination = iddestination;
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
        hash += (idBlogRoll != null ? idBlogRoll.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Blogroll)) {
            return false;
        }
        Blogroll other = (Blogroll) object;
        if ((this.idBlogRoll == null && other.idBlogRoll != null) || (this.idBlogRoll != null && !this.idBlogRoll.equals(other.idBlogRoll))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Blogroll[ idBlogRoll=" + idBlogRoll + " ]";
    }
    
}
