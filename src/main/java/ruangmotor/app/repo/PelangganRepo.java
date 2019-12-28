/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.repo;

import java.math.BigDecimal;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import ruangmotor.app.model.MstPelanggan;

/**
 *
 * @author ruangmotor
 */
public interface PelangganRepo extends JpaRepository<MstPelanggan, BigDecimal>, JpaSpecificationExecutor<MstPelanggan> {
    
    public List<MstPelanggan> findAllByStatusOrderByNamaPelangganAsc(MstPelanggan.Status s);
    
    public List<MstPelanggan> findAllByOrderByNamaPelangganAsc();

    public MstPelanggan findTop1ByNamaPelangganAndStatus(String namaPelanggan, MstPelanggan.Status s);
    
    public MstPelanggan findTop1ByKodePelangganAndStatus(String kodePelanggan, MstPelanggan.Status s);
    
    public MstPelanggan findTop1ByKodePelanggan(String kodePelanggan);
}
