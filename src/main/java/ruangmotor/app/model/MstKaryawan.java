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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "mst_karyawan")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class MstKaryawan {
    public enum Status {
        INACTIVE,
        ACTIVE
    }
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer karyawanId;
    
    @Column(name = "kode", length = 10, unique = true)
    private String kodeKaryawan;

    @Column(name = "nama", length = 50)
    private String namaKaryawan;
    
    @Column(name = "alamat", length = 50)
    private String alamat;

    @Column(name = "jenis_kelamin", length = 10)
    private String jenisKelamin;

    @Column(name = "telepon", length = 15)
    private String telepon;

//    @JoinColumn(name = "kota_id", referencedColumnName = "id")
//    @ManyToOne
//    private Kota kota;
    
    @Column(name = "email", length = 45)
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;
    
    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private MstKaryawan.Status status = MstKaryawan.Status.ACTIVE;

    public MstKaryawan() {
    }

    
    
    public MstKaryawan(String kodeKaryawan, String namaKaryawan, String alamat, String jenisKelamin, String telepon, String email, Date createdAt) {
        this.kodeKaryawan = kodeKaryawan;
        this.namaKaryawan = namaKaryawan;
        this.alamat = alamat;
        this.jenisKelamin = jenisKelamin;
        this.telepon = telepon;
//        this.kota = kota;
        this.email = email;
        this.createdAt = createdAt;
    }
    
    
}
