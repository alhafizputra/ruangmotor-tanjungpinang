/**
 *
 */
package ruangmotor.app.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import javax.persistence.Basic;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import lombok.Data;

/**
 * @author Owner
 *
 */
@Entity
@Table(name = "tr_penjualan")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class TrPenjualan {

    public enum Status {
        INACTIVE,
        ACTIVE,
        CANCEL,
        REJECT,
        HOLD,
        DONE
    }
    
    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer penjualanId;

    @Column(name = "nota_jual", length = 45)
    private String notaJual;

    @Column(name = "tgl_jual")
    @Temporal(TemporalType.DATE)
    private Date tglJual;

    @JoinColumn(name = "mst_pelanggan_id", referencedColumnName = "id")
    @ManyToOne
    private MstPelanggan mstPelanggan;

    @JoinColumn(name = "mst_karyawan_id", referencedColumnName = "id")
    @ManyToOne
    private MstKaryawan mstKaryawan;
    
    @Column(name = "total", length = 255)
    private BigInteger totalHarga;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;
    
    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private TrPenjualan.Status status = TrPenjualan.Status.ACTIVE;

}
