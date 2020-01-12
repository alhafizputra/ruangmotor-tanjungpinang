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
import ruangmotor.app.model.MstSupplier;

/**
 *
 * @author ruangmotor
 */
public interface SupplierRepo extends JpaRepository<MstSupplier, BigDecimal>, JpaSpecificationExecutor<MstSupplier> {

    public List<MstSupplier> findAllByStatusOrderByNamaSupplierAsc(MstSupplier.Status s);

    public List<MstSupplier> findAllByOrderByNamaSupplierAsc();

    public MstSupplier findTop1ByNamaSupplierAndStatus(String namaSupplier, MstSupplier.Status s);

    public MstSupplier findTop1ByKodeSupplierAndStatus(String kodeSupplier, MstSupplier.Status s);

    public MstSupplier findTop1ByKodeSupplier(String kodeSupplier);

    public Integer countByStatus(MstSupplier.Status status);

    public MstSupplier findTop1ByStatusOrderByNamaSupplierAsc(MstSupplier.Status status);

    public MstSupplier findTop1ByStatusOrderByKodeSupplierAsc(MstSupplier.Status status);
}
