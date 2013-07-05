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
@Table(name = "keyword")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Keyword.findAll", query = "SELECT k FROM Keyword k"),
    @NamedQuery(name = "Keyword.findByIdKeyword", query = "SELECT k FROM Keyword k WHERE k.idKeyword = :idKeyword"),
    @NamedQuery(name = "Keyword.findByKeyword", query = "SELECT k FROM Keyword k WHERE k.keyword = :keyword"),
    @NamedQuery(name = "Keyword.findByWeight", query = "SELECT k FROM Keyword k WHERE k.weight = :weight"),
    @NamedQuery(name = "Keyword.findByCategory", query = "SELECT k FROM Keyword k WHERE k.idCategory = :idCategory"),
    @NamedQuery(name = "Keyword.findByFrequency", query = "SELECT k FROM Keyword k WHERE k.frequency = :frequency")})
public class Keyword implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id_Keyword")
    private Integer idKeyword;
    @Column(name = "Keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "Weight")
    private double weight;
    @Basic(optional = false)
    @Column(name = "Frequency")
    private int frequency;
    @JoinColumn(name = "Id_Category", referencedColumnName = "Id_Category")
    @ManyToOne
    private Category idCategory;

    public Keyword() {
    }

    public Keyword(Integer idKeyword) {
        this.idKeyword = idKeyword;
    }

    public Keyword(Integer idKeyword, double weight, int frequency) {
        this.idKeyword = idKeyword;
        this.weight = weight;
        this.frequency = frequency;
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

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
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
