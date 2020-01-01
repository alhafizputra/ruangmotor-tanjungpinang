/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

/**
 *
 * @author ruangmotor
 */
@Entity
@Table(name = "kategori")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Kategori {

    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer kategoriId;

    @Column(name = "kategori", length = 45)
    private String namaKategori;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private Kategori.Status status = Kategori.Status.ACTIVE;

    public Kategori() {
    }

    
    public Kategori(String namaKategori, Date createdAt) {
        this.namaKategori = namaKategori;
        this.createdAt = createdAt;
    }
    
    
}
