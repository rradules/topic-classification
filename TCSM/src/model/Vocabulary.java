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
@Table(name = "vocabulary")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Vocabulary.findAll", query = "SELECT v FROM Vocabulary v"),
    @NamedQuery(name = "Vocabulary.findByIdVocabulary", query = "SELECT v FROM Vocabulary v WHERE v.idVocabulary = :idVocabulary"),
    @NamedQuery(name = "Vocabulary.findByWord", query = "SELECT v FROM Vocabulary v WHERE v.word = :word")})
public class Vocabulary implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Vocabulary")
    private Integer idVocabulary;
    @Column(name = "Word")
    private String word;
    @JoinColumn(name = "Id_Stem", referencedColumnName = "Id_Stem")
    @ManyToOne
    private Stems idStem;

    public Vocabulary() {
    }

    public Vocabulary(Integer idVocabulary) {
        this.idVocabulary = idVocabulary;
    }

    public Integer getIdVocabulary() {
        return idVocabulary;
    }

    public void setIdVocabulary(Integer idVocabulary) {
        this.idVocabulary = idVocabulary;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Stems getIdStem() {
        return idStem;
    }

    public void setIdStem(Stems idStem) {
        this.idStem = idStem;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idVocabulary != null ? idVocabulary.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Vocabulary)) {
            return false;
        }
        Vocabulary other = (Vocabulary) object;
        if ((this.idVocabulary == null && other.idVocabulary != null) || (this.idVocabulary != null && !this.idVocabulary.equals(other.idVocabulary))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Vocabulary[ idVocabulary=" + idVocabulary + " ]";
    }
    
}
