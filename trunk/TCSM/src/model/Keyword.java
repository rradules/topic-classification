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
@Table(name = "keyword")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Keyword.findAll", query = "SELECT k FROM Keyword k"),
    @NamedQuery(name = "Keyword.findByIdKeyword", query = "SELECT k FROM Keyword k WHERE k.idKeyword = :idKeyword"),
    @NamedQuery(name = "Keyword.findByKeyword", query = "SELECT k FROM Keyword k WHERE k.keyword = :keyword")})
public class Keyword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Keyword")
    private Integer idKeyword;
    @Column(name = "Keyword")
    private String keyword;
    @JoinColumn(name = "Id_Category", referencedColumnName = "Id_Category")
    @ManyToOne
    private Category idCategory;

    public Keyword() {
    }

    public Keyword(Integer idKeyword) {
        this.idKeyword = idKeyword;
    }

    public Integer getIdKeyword() {
        return idKeyword;
    }

    public void setIdKeyword(Integer idKeyword) {
        this.idKeyword = idKeyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Category getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Category idCategory) {
        this.idCategory = idCategory;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idKeyword != null ? idKeyword.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Keyword)) {
            return false;
        }
        Keyword other = (Keyword) object;
        if ((this.idKeyword == null && other.idKeyword != null) || (this.idKeyword != null && !this.idKeyword.equals(other.idKeyword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Keyword[ idKeyword=" + idKeyword + " ]";
    }
    
}
