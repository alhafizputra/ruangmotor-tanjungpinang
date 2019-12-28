/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.repo;

import ruangmotor.app.model.Kota;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author PROSIA
 */
public interface KotaRepo extends JpaRepository<Kota, Integer>, JpaSpecificationExecutor<Kota> {

    public Kota findTop1ByKodeAndStatus(String kode, Kota.Status s);
    
    public Kota findTop1ByKode(String kode);
    
    public List<Kota> findAllByStatusOrderByNamaAsc(Kota.Status s);
}
