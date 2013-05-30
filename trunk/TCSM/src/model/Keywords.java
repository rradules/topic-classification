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
@Table(name = "keywords")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Keywords.findAll", query = "SELECT k FROM Keywords k"),
    @NamedQuery(name = "Keywords.findByIdKeyword", query = "SELECT k FROM Keywords k WHERE k.idKeyword = :idKeyword"),
    @NamedQuery(name = "Keywords.findByKeyword", query = "SELECT k FROM Keywords k WHERE k.keyword = :keyword")})
public class Keywords implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Keyword")
    private Integer idKeyword;
    @Column(name = "Keyword")
    private String keyword;
    @JoinColumn(name = "Id_Category", referencedColumnName = "Id_Category")
    @ManyToOne
    private Categories idCategory;

    public Keywords() {
    }

    public Keywords(Integer idKeyword) {
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

    public Categories getIdCategory() {
        return idCategory;
    }

    public void setIdCategory(Categories idCategory) {
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
        if (!(object instanceof Keywords)) {
            return false;
        }
        Keywords other = (Keywords) object;
        if ((this.idKeyword == null && other.idKeyword != null) || (this.idKeyword != null && !this.idKeyword.equals(other.idKeyword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Keywords[ idKeyword=" + idKeyword + " ]";
    }
    
}
