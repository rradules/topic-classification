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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Roxana Radulescu <roxana.radulescu07@gmail.com>
 */
@Entity
@Table(name = "stopwords")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stopwords.findAll", query = "SELECT s FROM Stopwords s"),
    @NamedQuery(name = "Stopwords.findByIdStopWord", query = "SELECT s FROM Stopwords s WHERE s.idStopWord = :idStopWord"),
    @NamedQuery(name = "Stopwords.findByStopWord", query = "SELECT s FROM Stopwords s WHERE s.stopWord = :stopWord")})
public class Stopwords implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_StopWord")
    private Integer idStopWord;
    @Column(name = "StopWord")
    private String stopWord;

    public Stopwords() {
    }

    public Stopwords(Integer idStopWord) {
        this.idStopWord = idStopWord;
    }

    public Integer getIdStopWord() {
        return idStopWord;
    }

    public void setIdStopWord(Integer idStopWord) {
        this.idStopWord = idStopWord;
    }

    public String getStopWord() {
        return stopWord;
    }

    public void setStopWord(String stopWord) {
        this.stopWord = stopWord;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idStopWord != null ? idStopWord.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Stopwords)) {
            return false;
        }
        Stopwords other = (Stopwords) object;
        if ((this.idStopWord == null && other.idStopWord != null) || (this.idStopWord != null && !this.idStopWord.equals(other.idStopWord))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Stopwords[ idStopWord=" + idStopWord + " ]";
    }
    
}
