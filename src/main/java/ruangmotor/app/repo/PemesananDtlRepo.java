/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.repo;

import ruangmotor.app.model.TrPemesanan;
import ruangmotor.app.model.TrPemesananDtl;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

/**
 *
 * @author teg
 */
@Repository
public interface PemesananDtlRepo extends JpaRepository<TrPemesananDtl, Integer>, JpaSpecificationExecutor<TrPemesananDtl>{
    List<TrPemesananDtl> findAllByTrPemesanan(TrPemesanan pemesanan);
}
