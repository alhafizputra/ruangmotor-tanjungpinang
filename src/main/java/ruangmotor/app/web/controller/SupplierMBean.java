/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import java.io.IOException;
import ruangmotor.app.model.Kota;
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
import ruangmotor.app.model.MstSupplier;
import ruangmotor.app.repo.KotaRepo;
import ruangmotor.app.repo.SupplierRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.getRequestParam;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.util.Date;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author ruangmotor
 */
@Controller
@Scope("view")
@Data
public class SupplierMBean extends AbstractManagedBean implements InitializingBean {
    
    @Autowired
    private SupplierRepo supplierRepo;
//    private LazyDataModelFilterJPA<MstSupplier> listSupplier;
    private List<MstSupplier> listSupplier;
    private MstSupplier mstSupplier;
    private MstSupplier mstSupplierCek;
    private String kodeSupplier;
    private String dalogHeader;

//    @Autowired
//    private KotaRepo kotaRepo;
//    private Kota kota;
//    private List<Kota> listKota;
    public void init() {
//        mstSupplier = new MstSupplier();
//        kota = new Kota();
//        listKota = kotaRepo.findAllByStatusOrderByNamaAsc(Kota.Status.ACTIVE);
    }
    
    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listSupplier = new ArrayList<>();
        listSupplier = supplierRepo.findAllByStatusOrderByNamaSupplierAsc(MstSupplier.Status.ACTIVE);
//        listSupplier = new LazyDataModelFilterJPA(supplierRepo) {
//            @Override
//            protected Page getDatas(PageRequest request, Map filters) {
//                mstSupplier.setNamaSupplier((String) filters.get("namaSupplier"));
//                mstSupplier.setKodeSupplier((String) filters.get("kodeSupplier"));
//                mstSupplier.setAlamat((String) filters.get("alamat"));
////                mstSupplier.setJenisKelamin((String) filters.get("jenisKelamin"));
////                mstSupplier.setTglLahir((Date) filters.get("tglLahir"));
//                mstSupplier.setTelepon((String) filters.get("telepon"));
//                mstSupplier.setTelepon((String) filters.get("email"));
////                kota = (Kota) filters.get("kota");
//                return supplierRepo.findAll(whereQuery(), request);
//            }
//
//            @Override
//            protected long getDataSize(Map filters) {
//                mstSupplier.setNamaSupplier((String) filters.get("namaSupplier"));
//                mstSupplier.setKodeSupplier((String) filters.get("kodeSupplier"));
//                mstSupplier.setAlamat((String) filters.get("alamat"));
////                mstSupplier.setJenisKelamin((String) filters.get("jenisKelamin"));
////                 mstSupplier.setTglLahir((Date) filters.get("tglLahir"));
//                mstSupplier.setTelepon((String) filters.get("telepon"));
//                mstSupplier.setEmail((String) filters.get("email"));
////                kota = (Kota) filters.get("kota");
//                return supplierRepo.count(whereQuery());
//            }
//        };
    }

//    public Specification<MstSupplier> whereQuery() {
//        List<Predicate> predicates = new ArrayList<>();
//        return new Specification<MstSupplier>() {
//            @Override
//            public Predicate toPredicate(Root<MstSupplier> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                if (StringUtils.isNotBlank(mstSupplier.getNamaSupplier())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("namaSupplier")),
//                            getLikePattern(mstSupplier.getNamaSupplier())));
//                }
//                if (StringUtils.isNotBlank(mstSupplier.getKodeSupplier())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("kodeSupplier")),
//                            getLikePattern(mstSupplier.getKodeSupplier())));
//                }
////                if (kota != null && kota.getKotaId() != null) {
////                    predicates.add(cb.equal(root.<Kota>get("kota"), kota));
////                }
////                if (StringUtils.isNotBlank(mstSupplier.getJenisKelamin())) {
////                    predicates.add(cb.like(cb.lower(root.<String>get("jeniskelamin")),
////                            getLikePattern(mstSupplier.getJenisKelamin())));
////                }
//                if (StringUtils.isNotBlank(mstSupplier.getEmail())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("email")),
//                            getLikePattern(mstSupplier.getEmail())));
//                }
//                if (StringUtils.isNotBlank(mstSupplier.getTelepon())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("telepon")),
//                            getLikePattern(mstSupplier.getTelepon())));
//                }
//                if (StringUtils.isNotBlank(mstSupplier.getAlamat())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("alamat")),
//                            getLikePattern(mstSupplier.getAlamat())));
//                }
//                predicates.add(cb.equal(root.<BigInteger>get("status"), MstSupplier.Status.ACTIVE));
//                query.orderBy(cb.asc(root.<Integer>get("supplierId")));
//                return andTogether(predicates, cb);
//            }
//
//        };
//    }
//
//    private Predicate andTogether(List<Predicate> predicates, CriteriaBuilder cb) {
//        return cb.and(predicates.toArray(new Predicate[0]));
//    }
//
//    private String getLikePattern(String searchTerm) {
//        return new StringBuilder("%")
//                .append(searchTerm.toLowerCase().replaceAll("\\*", "%"))
//                .append("%")
//                .toString();
//    }
    public void showDialogAction() {
        mstSupplierCek = (MstSupplier) getRequestParam("supplier");
        System.out.println("mstSupplierCek : " + mstSupplierCek);
        mstSupplier = new MstSupplier();
        if (mstSupplierCek == null) {
            System.out.println("tambah");
            dalogHeader = "Tambah Supplier";
        } else {
            System.out.println("update");
            dalogHeader = "Ubah Supplier";
            mstSupplier = mstSupplierCek;
            kodeSupplier = mstSupplierCek.getKodeSupplier();
            System.out.println("kode supplier : " + kodeSupplier);
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }
    
    public void saveRecord() throws InterruptedException {
        try {
            System.out.println("mstSupplier : " + mstSupplier);
            if (dalogHeader.equals("Tambah Supplier")) {
                System.out.println("tambah");
                MstSupplier sp = supplierRepo.findTop1ByKodeSupplier(mstSupplier.getKodeSupplier().toUpperCase());
                System.out.println("sp : " + sp);
                if (sp != null) {
                    if (sp.getStatus().equals(MstSupplier.Status.ACTIVE)) {
                        showGrowl(FacesMessage.SEVERITY_ERROR,
                                "Supplier dengan kode " + mstSupplier.getKodeSupplier().toUpperCase()
                                + " sudah ada", "");
                        return;
                    } else {
                        mstSupplier.setStatus(MstSupplier.Status.INACTIVE);
                        mstSupplier.setKodeSupplier(kodeSupplier);
                        supplierRepo.save(mstSupplier);
                        String namaSupplier = mstSupplier.getNamaSupplier();
//                    String jeniskelamin = mstSupplier.getJenisKelamin();
//                    Date tglLahir = mstSupplier.getTglLahir();
                        String telepon = mstSupplier.getTelepon();
                        String alamat = mstSupplier.getAlamat();
                        String email = mstSupplier.getEmail();
//                    Kota kt = mstSupplier.getKota();
                        mstSupplier = sp;
                        mstSupplier.setNamaSupplier(namaSupplier);
//                    mstSupplier.setJenisKelamin(jeniskelamin);
//                    mstSupplier.setTglLahir(tglLahir);
                        mstSupplier.setTelepon(telepon);
                        mstSupplier.setAlamat(alamat);
                        mstSupplier.setEmail(email);
//                    mstSupplier.setKota(kt);
                    }
                }
                mstSupplier.setStatus(MstSupplier.Status.ACTIVE);
                supplierRepo.save(mstSupplier);
            } else {
                System.out.println("update");
                MstSupplier sp = supplierRepo.findTop1ByKodeSupplier(mstSupplier.getKodeSupplier().toUpperCase());
                System.out.println("sp : " + sp);
                if (sp != null) {
                    if (sp.getStatus().equals(MstSupplier.Status.ACTIVE) && !sp.getKodeSupplier().equals(kodeSupplier)) {
                        showGrowl(FacesMessage.SEVERITY_ERROR,
                                "Supplier dengan kode " + mstSupplier.getKodeSupplier().toUpperCase()
                                + " sudah ada", "");
//                        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
//                        RequestContext.getCurrentInstance().update("idList");
//                        RequestContext.getCurrentInstance().update("growl");
//                        RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
//                        mstSupplier = new MstSupplier();
                        mstSupplier.setKodeSupplier(kodeSupplier);
                        return;
                    } else {
                        mstSupplier.setStatus(MstSupplier.Status.INACTIVE);
                        mstSupplier.setKodeSupplier(kodeSupplier);
                        supplierRepo.save(mstSupplier);
                        String namaSupplier = mstSupplier.getNamaSupplier();
                        String telepon = mstSupplier.getTelepon();
                        String alamat = mstSupplier.getAlamat();
                        String email = mstSupplier.getEmail();
                        mstSupplier = sp;
                        mstSupplier.setNamaSupplier(namaSupplier);
                        mstSupplier.setTelepon(telepon);
                        mstSupplier.setAlamat(alamat);
                        mstSupplier.setEmail(email);
                    }
                }
                mstSupplier.setStatus(MstSupplier.Status.ACTIVE);
                supplierRepo.save(mstSupplier);
            }
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
        mstSupplierCek = (MstSupplier) getRequestParam("supplier");
        mstSupplierCek.setStatus(MstSupplier.Status.INACTIVE);
        supplierRepo.save(mstSupplierCek);
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
    }
    
    public void showDialogCetak() {
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }
    
    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
