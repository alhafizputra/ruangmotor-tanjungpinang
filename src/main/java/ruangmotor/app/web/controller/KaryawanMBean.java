/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import ruangmotor.app.model.Kota;
import ruangmotor.app.model.MstKaryawan;
import ruangmotor.app.repo.KaryawanRepo;
import ruangmotor.app.repo.KotaRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;

/**
 *
 * @author PROSIA
 */
@Controller
@Scope("view")
@Data
public class KaryawanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private KaryawanRepo karyawanRepo;
    private LazyDataModelFilterJPA<MstKaryawan> listKaryawan;
    private List<MstKaryawan> listKaryawan2;
    private MstKaryawan mstKaryawan;
    private MstKaryawan mstKaryawanCek;
    private String kodeKaryawan;
    private String dalogHeader;

//    @Autowired
//    private KotaRepo kotaRepo;
//    private Kota kota;
//    private List<Kota> listKota;

    public void init() {
        mstKaryawan = new MstKaryawan();
//        kota = new Kota();
//        listKota = kotaRepo.findAllByStatusOrderByNamaAsc(Kota.Status.ACTIVE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listKaryawan2 = new ArrayList<>();
        listKaryawan2 = karyawanRepo.findAllByStatusOrderByNamaKaryawanAsc(MstKaryawan.Status.ACTIVE);
        listKaryawan = new LazyDataModelFilterJPA(karyawanRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstKaryawan.setNamaKaryawan((String) filters.get("namaKaryawan"));
                mstKaryawan.setKodeKaryawan((String) filters.get("kodeKaryawan"));
                mstKaryawan.setAlamat((String) filters.get("alamat"));
                mstKaryawan.setJenisKelamin((String) filters.get("jenisKelamin"));
                mstKaryawan.setEmail((String) filters.get("email"));
                mstKaryawan.setTelepon((String) filters.get("telepon"));
//                kota = (Kota) filters.get("kota");
                return karyawanRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstKaryawan.setNamaKaryawan((String) filters.get("namaKaryawan"));
                mstKaryawan.setKodeKaryawan((String) filters.get("kodeKaryawan"));
                mstKaryawan.setAlamat((String) filters.get("alamat"));
                mstKaryawan.setJenisKelamin((String) filters.get("jenisKelamin"));
                mstKaryawan.setEmail((String) filters.get("email"));
                mstKaryawan.setTelepon((String) filters.get("telepon"));
//                kota = (Kota) filters.get("kota");
                return karyawanRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstKaryawan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstKaryawan>() {
            @Override
            public Predicate toPredicate(Root<MstKaryawan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstKaryawan.getNamaKaryawan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaKaryawan")),
                            getLikePattern(mstKaryawan.getNamaKaryawan())));
                }
                if (StringUtils.isNotBlank(mstKaryawan.getKodeKaryawan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodeKaryawan")),
                            getLikePattern(mstKaryawan.getKodeKaryawan())));
                }
//                if (kota != null && kota.getKotaId() != null) {
//                    predicates.add(cb.equal(root.<Kota>get("kota"), kota));
//                }
                if (StringUtils.isNotBlank(mstKaryawan.getJenisKelamin())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("jeniskelamin")),
                            getLikePattern(mstKaryawan.getJenisKelamin())));
                }
                if (StringUtils.isNotBlank(mstKaryawan.getEmail())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("email")),
                            getLikePattern(mstKaryawan.getEmail())));
                }
                if (StringUtils.isNotBlank(mstKaryawan.getTelepon())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("telepon")),
                            getLikePattern(mstKaryawan.getTelepon())));
                }
                if (StringUtils.isNotBlank(mstKaryawan.getAlamat())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("alamat")),
                            getLikePattern(mstKaryawan.getAlamat())));
                }
                predicates.add(cb.equal(root.<BigInteger>get("status"), MstKaryawan.Status.ACTIVE));
                query.orderBy(cb.asc(root.<Integer>get("karyawanId")));
                return andTogether(predicates, cb);
            }

        };
    }

    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
        return cb.and(predicates.toArray(new Predicate[0]));
    }

    private String getLikePattern(String searchTerm) {
        return new StringBuilder("%")
                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
                .append("%")
                .toString();
    }

    public void showDialogAction() {
        mstKaryawanCek = (MstKaryawan) getRequestParam("karyawan");
        mstKaryawan = new MstKaryawan();
        if (mstKaryawanCek == null) {
            System.out.println("tambah");
            dalogHeader = "Tambah Karyawan";
        } else {
            System.out.println("update");
            dalogHeader = "Ubah Karyawan";
            mstKaryawan = mstKaryawanCek;
            kodeKaryawan = mstKaryawanCek.getKodeKaryawan();
            System.out.println("kode karyawan : " + kodeKaryawan);
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void saveRecord() throws InterruptedException {
        try {
            MstKaryawan br = karyawanRepo.findTop1ByKodeKaryawan(mstKaryawan.getKodeKaryawan().toUpperCase());
            System.out.println("br : " + br);
            if (br != null) {
                if (br.getStatus().equals(MstKaryawan.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_ERROR,
                            "Karyawan dengan kode " + mstKaryawan.getKodeKaryawan().toUpperCase()
                            + " sudah ada", "");
                    return;
                } else {
                    mstKaryawan.setStatus(MstKaryawan.Status.INACTIVE);
                    mstKaryawan.setKodeKaryawan(kodeKaryawan);
                    karyawanRepo.save(mstKaryawan);
                    String namaKaryawan = mstKaryawan.getNamaKaryawan();
                    String jeniskelamin = mstKaryawan.getJenisKelamin();
                    String email = mstKaryawan.getEmail();
                    String telepon = mstKaryawan.getTelepon();
                    String alamat = mstKaryawan.getAlamat();
//                    Kota kt = mstKaryawan.getKota();
                    mstKaryawan = br;
                    mstKaryawan.setNamaKaryawan(namaKaryawan);
                    mstKaryawan.setJenisKelamin(jeniskelamin);
                    mstKaryawan.setEmail(email);
                    mstKaryawan.setTelepon(telepon);
                    mstKaryawan.setAlamat(alamat);
//                    mstKaryawan.setKota(kt);
                }
            }
            mstKaryawan.setStatus(MstKaryawan.Status.ACTIVE);
            karyawanRepo.save(mstKaryawan);
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
        } catch (Exception e) {
            log.error("error : {}", e);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan simpan data");
            RequestContext.getCurrentInstance().update("growl");
        } finally {
            init();
        }
    }

    public void deleteRecord() throws InterruptedException {
        mstKaryawanCek = (MstKaryawan) getRequestParam("karyawan");
        mstKaryawanCek.setStatus(MstKaryawan.Status.INACTIVE);
        karyawanRepo.save(mstKaryawanCek);
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
    }

    public void showDialogCetak() {
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }
}
