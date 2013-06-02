/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Student
 */
@Entity
@Table(name = "blogpost")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Blogpost.findAll", query = "SELECT b FROM Blogpost b"),
    @NamedQuery(name = "Blogpost.findByIdBlogPost", query = "SELECT b FROM Blogpost b WHERE b.idBlogPost = :idBlogPost"),
    @NamedQuery(name = "Blogpost.findByPageAddress", query = "SELECT b FROM Blogpost b WHERE b.pageAddress = :pageAddress"),
    @NamedQuery(name = "Blogpost.findByBlogDate", query = "SELECT b FROM Blogpost b WHERE b.blogDate = :blogDate"),
    @NamedQuery(name = "Blogpost.findByBlogContent", query = "SELECT b FROM Blogpost b WHERE b.blogContent = :blogContent"),
    @NamedQuery(name = "Blogpost.findByProcessed", query = "SELECT b FROM Blogpost b WHERE b.processed = :processed"),
    @NamedQuery(name = "Blogpost.findByCrc", query = "SELECT b FROM Blogpost b WHERE b.crc = :crc"),
    @NamedQuery(name = "Blogpost.findByTitle", query = "SELECT b FROM Blogpost b WHERE b.title = :title"),
    @NamedQuery(name = "Blogpost.findByDescription", query = "SELECT b FROM Blogpost b WHERE b.description = :description")})
public class Blogpost implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_BlogPost")
    private Integer idBlogPost;
    @Column(name = "PageAddress")
    private String pageAddress;
    @Column(name = "BlogDate")
    @Temporal(TemporalType.DATE)
    private Date blogDate;
    @Column(name = "BlogContent")
    private String blogContent;
    @Column(name = "Processed")
    private Integer processed;
    @Column(name = "CRC")
    private Long crc;
    @Column(name = "Title")
    private String title;
    @Column(name = "Description")
    private String description;
    @JoinColumn(name = "Id_Domain", referencedColumnName = "Id_Domain")
    @ManyToOne
    private Domain idDomain;

    public Blogpost() {
    }

    public Blogpost(Integer idBlogPost) {
        this.idBlogPost = idBlogPost;
    }

    public Integer getIdBlogPost() {
        return idBlogPost;
    }

    public void setIdBlogPost(Integer idBlogPost) {
        this.idBlogPost = idBlogPost;
    }

    public String getPageAddress() {
        return pageAddress;
    }

    public void setPageAddress(String pageAddress) {
        this.pageAddress = pageAddress;
    }

    public Date getBlogDate() {
        return blogDate;
    }

    public void setBlogDate(Date blogDate) {
        this.blogDate = blogDate;
    }

    public String getBlogContent() {
        return blogContent;
    }

    public void setBlogContent(String blogContent) {
        this.blogContent = blogContent;
    }

    public Integer getProcessed() {
        return processed;
    }

    public void setProcessed(Integer processed) {
        this.processed = processed;
    }

    public long getCrc() {
        return crc;
    }

    public void setCrc(long crc) {
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

    public Domain getIdDomain() {
        return idDomain;
    }

    public void setIdDomain(Domain idDomain) {
        this.idDomain = idDomain;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idBlogPost != null ? idBlogPost.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Blogpost)) {
            return false;
        }
        Blogpost other = (Blogpost) object;
        if ((this.idBlogPost == null && other.idBlogPost != null) || (this.idBlogPost != null && !this.idBlogPost.equals(other.idBlogPost))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Blogpost[ idBlogPost=" + idBlogPost + " ]";
    }
    
}
