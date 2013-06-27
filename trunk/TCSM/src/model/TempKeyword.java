/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@Entity
@Table(name = "temp_keyword")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "TempKeyword.findAll", query = "SELECT t FROM TempKeyword t"),
    @NamedQuery(name = "TempKeyword.findByIdTempKeyword", query = "SELECT t FROM TempKeyword t WHERE t.idTempKeyword = :idTempKeyword"),
    @NamedQuery(name = "TempKeyword.findByKeyword", query = "SELECT t FROM TempKeyword t WHERE t.keyword = :keyword"),
    @NamedQuery(name = "TempKeyword.findByWeight", query = "SELECT t FROM TempKeyword t WHERE t.weight = :weight")})
public class TempKeyword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_TempKeyword")
    private Integer idTempKeyword;
    @Basic(optional = false)
    @Column(name = "Keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "Weight")
    private double weight;
    @JoinColumn(name = "Id_Category", referencedColumnName = "Id_Category")
    @ManyToOne(optional = false)
    private Category idCategory;

    public TempKeyword() {
    }

    public TempKeyword(Integer idTempKeyword) {
        this.idTempKeyword = idTempKeyword;
    }

    public TempKeyword(Integer idTempKeyword, String keyword, double weight) {
        this.idTempKeyword = idTempKeyword;
        this.keyword = keyword;
        this.weight = weight;
    }

    public Integer getIdTempKeyword() {
        return idTempKeyword;
    }

    public void setIdTempKeyword(Integer idTempKeyword) {
        this.idTempKeyword = idTempKeyword;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
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
        hash += (idTempKeyword != null ? idTempKeyword.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TempKeyword)) {
            return false;
        }
        TempKeyword other = (TempKeyword) object;
        if ((this.idTempKeyword == null && other.idTempKeyword != null) || (this.idTempKeyword != null && !this.idTempKeyword.equals(other.idTempKeyword))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.TempKeyword[ idTempKeyword=" + idTempKeyword + " ]";
    }
    
}
