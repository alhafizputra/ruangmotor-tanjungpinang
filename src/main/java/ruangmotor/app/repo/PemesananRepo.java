/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.repo;

import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ruangmotor.app.model.MstSupplier;
import ruangmotor.app.model.TrPemesanan;

/**
 *
 * @author Owner
 */
@Repository
public interface PemesananRepo extends JpaRepository<TrPemesanan, Integer>, JpaSpecificationExecutor<TrPemesanan> {

    public TrPemesanan findTop1ByNotaPesan(String notaPesan);

    public List<TrPemesanan> findAllByNotaPesan(String notaPesan);

    public List<TrPemesanan> findAllByMstSupplier(MstSupplier mstSupplier);

    public TrPemesanan findTop1ByStatusOrderByPemesananIdDesc(TrPemesanan.Status status);

    @Query(value = "SELECT * FROM pemesanan p WHERE MONTH(p.tgl_pesan) = ?1", nativeQuery = true)
    public List<TrPemesanan> listPemesananBulan(String bulan);

    @Query(value = "SELECT * FROM pemesanan p where p.tgl_pesan between ?1 and ?2", nativeQuery = true)
    public List<TrPemesanan> listPemesanan(Date tglAwal, Date tglAkhir);

    public List<TrPemesanan> findAllByStatus(TrPemesanan.Status status);

    public Integer countByStatus(TrPemesanan.Status status);
}
