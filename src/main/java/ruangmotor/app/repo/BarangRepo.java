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
import ruangmotor.app.model.MstBarang;

/**
 *
 * @author ruangmotor
 */
public interface BarangRepo extends JpaRepository<MstBarang, BigDecimal>, JpaSpecificationExecutor<MstBarang> {
    
    public List<MstBarang> findAllByStatusOrderByNamaBarangAsc(MstBarang.Status s);
    
    public List<MstBarang> findAllByOrderByNamaBarangAsc();

    public MstBarang findTop1ByNamaBarangAndStatus(String namaBarang, MstBarang.Status s);
    
    public MstBarang findTop1ByKodeBarangAndStatus(String kodeBarang, MstBarang.Status s);
    
    public MstBarang findTop1ByKodeBarang(String kodeBarang);
}
