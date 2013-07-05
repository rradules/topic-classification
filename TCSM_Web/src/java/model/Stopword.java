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
@Table(name = "stopword")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Stopword.findAll", query = "SELECT s FROM Stopword s"),
    @NamedQuery(name = "Stopword.findByIdStopWord", query = "SELECT s FROM Stopword s WHERE s.idStopWord = :idStopWord"),
    @NamedQuery(name = "Stopword.findByStopWord", query = "SELECT s FROM Stopword s WHERE s.stopWord = :stopWord")})
public class Stopword implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_StopWord")
    private Integer idStopWord;
    @Column(name = "StopWord")
    private String stopWord;

    public Stopword() {
    }

    public Stopword(Integer idStopWord) {
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
        if (!(object instanceof Stopword)) {
            return false;
        }
        Stopword other = (Stopword) object;
        if ((this.idStopWord == null && other.idStopWord != null) || (this.idStopWord != null && !this.idStopWord.equals(other.idStopWord))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Stopword[ idStopWord=" + idStopWord + " ]";
    }
    
}
