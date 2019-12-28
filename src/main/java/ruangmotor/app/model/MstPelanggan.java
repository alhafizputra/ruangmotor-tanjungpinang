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
@Table(name = "mst_pelanggan")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class MstPelanggan {

    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer pelangganId;

    @Column(name = "kode", length = 15, unique = true)
    private String kodePelanggan;

    @Column(name = "nama", length = 50)
    private String namaPelanggan;

    @Column(name = "jenis_kelamin", length = 10)
    private String jenisKelamin;
    
    @Column(name = "tgl_lahir")
    private Date tglLahir;

    @Column(name = "alamat", length = 50)
    private String alamat;

    @JoinColumn(name = "kota_id", referencedColumnName = "id")
    @ManyToOne
    private Kota kota;

    @Column(name = "telepon", length = 15)
    private String telepon;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private MstPelanggan.Status status = MstPelanggan.Status.ACTIVE;
}
