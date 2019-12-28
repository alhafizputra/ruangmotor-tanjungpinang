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
@Table(name = "mst_barang")
@Data
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class MstBarang {

    public enum Status {
        INACTIVE,
        ACTIVE
    }
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer barangId;
    
    @Column(name = "kode", length = 10, unique = true)
    private String kodeBarang;

    @Column(name = "nama", length = 45)
    private String namaBarang;
    
    @Column(name = "harga_beli")
    private Integer hargaBeli;

    @Column(name = "harga_jual")
    private Integer hargaJual;

    @Column(name = "stok")
    private Integer stok;
    
    @Column(name = "stok_minimal")
    private Integer stok_minimal;

    @JoinColumn(name = "kategori_id", referencedColumnName = "id")
    @ManyToOne
    private Kategori kategori;
    
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;
    
    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private MstBarang.Status status = MstBarang.Status.ACTIVE;

}
