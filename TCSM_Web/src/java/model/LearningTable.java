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
@Table(name = "learning_table")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "LearningTable.findAll", query = "SELECT l FROM LearningTable l"),
    @NamedQuery(name = "LearningTable.findByIdLearnTable", query = "SELECT l FROM LearningTable l WHERE l.idLearnTable = :idLearnTable"),
    @NamedQuery(name = "LearningTable.findByKeyword", query = "SELECT l FROM LearningTable l WHERE l.keyword = :keyword"),
    @NamedQuery(name = "LearningTable.findByCategory", query = "SELECT l FROM LearningTable l WHERE l.idCategory = :idCategory"),
    @NamedQuery(name = "LearningTable.findByWeight", query = "SELECT l FROM LearningTable l WHERE l.weight = :weight")})
public class LearningTable implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_LearnTable")
    private Integer idLearnTable;
    @Basic(optional = false)
    @Column(name = "Keyword")
    private String keyword;
    @Basic(optional = false)
    @Column(name = "Weight")
    private double weight;
    @JoinColumn(name = "Id_Category", referencedColumnName = "Id_Category")
    @ManyToOne(optional = false)
    private Category idCategory;

    public LearningTable() {
    }

    public LearningTable(Integer idLearnTable) {
        this.idLearnTable = idLearnTable;
    }

    public LearningTable(Integer idLearnTable, String keyword, double weight) {
        this.idLearnTable = idLearnTable;
        this.keyword = keyword;
        this.weight = weight;
    }

    public Integer getIdLearnTable() {
        return idLearnTable;
    }

    public void setIdLearnTable(Integer idLearnTable) {
        this.idLearnTable = idLearnTable;
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
        hash += (idLearnTable != null ? idLearnTable.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof LearningTable)) {
            return false;
        }
        LearningTable other = (LearningTable) object;
        if ((this.idLearnTable == null && other.idLearnTable != null) || (this.idLearnTable != null && !this.idLearnTable.equals(other.idLearnTable))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.LearningTable[ idLearnTable=" + idLearnTable + " ]";
    }
}
