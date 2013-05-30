/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@Entity
@Table(name = "stems")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stems.findAll", query = "SELECT s FROM Stems s"),
    @NamedQuery(name = "Stems.findByIdStem", query = "SELECT s FROM Stems s WHERE s.idStem = :idStem"),
    @NamedQuery(name = "Stems.findByStemWord", query = "SELECT s FROM Stems s WHERE s.stemWord = :stemWord")})
public class Stems implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Stem")
    private Integer idStem;
    @Column(name = "StemWord")
    private String stemWord;
    @OneToMany(mappedBy = "idStem")
    private Collection<Vocabulary> vocabularyCollection;

    public Stems() {
    }

    public Stems(Integer idStem) {
        this.idStem = idStem;
    }

    public Integer getIdStem() {
        return idStem;
    }

    public void setIdStem(Integer idStem) {
        this.idStem = idStem;
    }

    public String getStemWord() {
        return stemWord;
    }

    public void setStemWord(String stemWord) {
        this.stemWord = stemWord;
    }

    @XmlTransient
    public Collection<Vocabulary> getVocabularyCollection() {
        return vocabularyCollection;
    }

    public void setVocabularyCollection(Collection<Vocabulary> vocabularyCollection) {
        this.vocabularyCollection = vocabularyCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStem != null ? idStem.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stems)) {
            return false;
        }
        Stems other = (Stems) object;
        if ((this.idStem == null && other.idStem != null) || (this.idStem != null && !this.idStem.equals(other.idStem))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Stems[ idStem=" + idStem + " ]";
    }
    
}
