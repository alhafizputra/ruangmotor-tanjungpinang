/**
 *
 */
package ruangmotor.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Data;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "mst_supplier")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class MstSupplier {

    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer supplierId;

    @Column(name = "kode", length = 15, unique = true)
    private String kodeSupplier;

    @Column(name = "nama", length = 50)
    private String namaSupplier;

//    @Column(name = "jenis_kelamin", length = 10)
//    private String jenisKelamin;
//    
//    @Column(name = "tgl_lahir")
//    private Date tglLahir;

    @Column(name = "alamat", length = 50)
    private String alamat;

    @Column(name = "telepon", length = 15)
    private String telepon;

    @Column(name = "email")
    private String email;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private MstSupplier.Status status = MstSupplier.Status.ACTIVE;

    public MstSupplier() {
    }
    
    

    public MstSupplier(String kodeSupplier, String namaSupplier, String alamat, String telepon, String email, Date createdAt) {
        this.kodeSupplier = kodeSupplier;
        this.namaSupplier = namaSupplier;
//        this.jenisKelamin = jenisKelamin;
//        this.tglLahir = tglLahir;
        this.alamat = alamat;
        this.telepon = telepon;
        this.createdAt = createdAt;
        this.email = email;
    }
    
    
}
