/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.repo;

import ruangmotor.app.model.MstKaryawan;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 *
 * @author PROSIA
 */
public interface KaryawanRepo extends JpaRepository<MstKaryawan, Integer>, JpaSpecificationExecutor<MstKaryawan> {

    public MstKaryawan findTop1ByNamaKaryawanAndStatus(String nama, MstKaryawan.Status s);

    public MstKaryawan findTop1ByKodeKaryawanAndStatus(String kode, MstKaryawan.Status s);
    
    public MstKaryawan findTop1ByKodeKaryawan(String kode);

    public List<MstKaryawan> findAllByStatusOrderByNamaKaryawanAsc(MstKaryawan.Status s);
    
    public Integer countByStatus(MstKaryawan.Status status);
}
