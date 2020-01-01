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
@Table(name = "kota")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class Kota {
    
    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer kotaId;

    @Column(name = "nama", length = 45)
    private String nama;
    @Column(name = "kode", length = 10)
    private String kode;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private Kota.Status status = Kota.Status.ACTIVE;

    public Kota() {
    }

    
    
    public Kota(String nama, String kode, Date createdAt) {
        this.nama = nama;
        this.kode = kode;
        this.createdAt = createdAt;
    }
    
    
}
