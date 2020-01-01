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
import ruangmotor.app.model.TrPenjualan;

/**
 *
 * @author Owner
 */
@Repository
public interface PenjualanRepo extends JpaRepository<TrPenjualan, Integer>, JpaSpecificationExecutor<TrPenjualan> {

    public TrPenjualan findTop1ByNotaJual(String notaJual);

    public List<TrPenjualan> findAllByNotaJual(String notaJual);

    public TrPenjualan findTop1ByStatusOrderByPenjualanIdDesc(TrPenjualan.Status status);

    @Query(value = "SELECT * FROM penjualan p WHERE MONTH(p.tgl_jual) = ?1", nativeQuery = true)
    public List<TrPenjualan> listPenjualanBulan(String bulan);

    @Query(value = "SELECT * FROM penjualan p where p.tgl_jual between ?1 and ?2", nativeQuery = true)
    public List<TrPenjualan> listPenjualan(Date tglAwal, Date tglAkhir);

    public List<TrPenjualan> findAllByStatus(TrPenjualan.Status status);

    public Integer countByStatus(TrPenjualan.Status status);
}
