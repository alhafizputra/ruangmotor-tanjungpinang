/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.math.BigInteger;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 *
 * @author ruangmotor
 */
@Entity
@Table(name = "tr_penjualan_dtl")
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(value = {"createdAt"}, allowGetters = true)
public class TrPenjualanDtl {

    public enum Status {
        INACTIVE,
        ACTIVE
    }

    @Id
    @Basic(optional = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer penjualanDtlId;

    @Column(name = "nama_barang", length = 45)
    private String namaBarang;

    @Column(name = "kode_barang", length = 10)
    private String kodeBarang;

    @Column(name = "harga_satuan")
    private Integer hargaSatuan;

    @Column(name = "jumlah_jual")
    private Integer jumlahJual;

    @Column(name = "sub_total")
    private BigInteger subTotal;

    @JoinColumn(name = "tr_penjualan_id", referencedColumnName = "id")
    @ManyToOne
    private TrPenjualan trPenjualan;

    @JoinColumn(name = "mst_barang_id", referencedColumnName = "id")
    @ManyToOne
    private MstBarang mstBarang;

    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Column(name = "status", length = 10)
    @Enumerated(EnumType.STRING)
    private TrPenjualanDtl.Status status = TrPenjualanDtl.Status.ACTIVE;

}
