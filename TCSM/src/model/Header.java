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
@Table(name = "header")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Header.findAll", query = "SELECT h FROM Header h"),
    @NamedQuery(name = "Header.findByIdHeader", query = "SELECT h FROM Header h WHERE h.idHeader = :idHeader"),
    @NamedQuery(name = "Header.findByIdOrder", query = "SELECT h FROM Header h WHERE h.idOrder = :idOrder"),
    @NamedQuery(name = "Header.findByHeader", query = "SELECT h FROM Header h WHERE h.header = :header")})
public class Header implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "Id_Header")
    private Integer idHeader;
    @Column(name = "Id_Order")
    private Integer idOrder;
    @Column(name = "Header")
    private String header;
    @JoinColumn(name = "Id_RawData", referencedColumnName = "Id_RawData")
    @ManyToOne
    private Rawdata idRawData;

    public Header() {
    }

    public Header(Integer idHeader) {
        this.idHeader = idHeader;
    }

    public Integer getIdHeader() {
        return idHeader;
    }

    public void setIdHeader(Integer idHeader) {
        this.idHeader = idHeader;
    }

    public Integer getIdOrder() {
        return idOrder;
    }

    public void setIdOrder(Integer idOrder) {
        this.idOrder = idOrder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public Rawdata getIdRawData() {
        return idRawData;
    }

    public void setIdRawData(Rawdata idRawData) {
        this.idRawData = idRawData;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idHeader != null ? idHeader.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Header)) {
            return false;
        }
        Header other = (Header) object;
        if ((this.idHeader == null && other.idHeader != null) || (this.idHeader != null && !this.idHeader.equals(other.idHeader))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Header[ idHeader=" + idHeader + " ]";
    }
    
}
