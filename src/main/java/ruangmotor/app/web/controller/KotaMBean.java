/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import ruangmotor.app.model.Kota;
import ruangmotor.app.repo.KotaRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
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
public class KotaMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private KotaRepo kotaRepo;
    private LazyDataModelFilterJPA<Kota> listKota;
    private List<Kota> listKota2;
    private Kota kota;
    private Kota kotaCek;
    private String kodeKota;

    public void init() {
        kota = new Kota();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listKota2 = new ArrayList<>();
        listKota2 = kotaRepo.findAllByStatusOrderByNamaAsc(Kota.Status.ACTIVE);
        listKota = new LazyDataModelFilterJPA(kotaRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                kota.setKode((String) filters.get("kode"));
                kota.setNama((String) filters.get("nama"));
                return kotaRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                kota.setKode((String) filters.get("kode"));
                kota.setNama((String) filters.get("nama"));
                return kotaRepo.count(whereQuery());
            }
        };
    }

    public Specification<Kota> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Kota>() {
            @Override
            public Predicate toPredicate(Root<Kota> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(kota.getKode())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kode")),
                            getLikePattern(kota.getKode())));
                }
                if (StringUtils.isNotBlank(kota.getNama())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("nama")),
                            getLikePattern(kota.getNama())));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), Kota.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("kotaId")));
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

    public void tambah() throws InterruptedException {
        try {
            System.out.println("namaKota : " + kota.getNama());
            System.out.println("kota : " + kota);
            if (kota.getNama().isEmpty() || kota.getKode().isEmpty()) {
                if (kota.getNama().isEmpty()) {
                    showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kota tidak boleh kosong");
                    RequestContext.getCurrentInstance().update("growl");
                }
                if (kota.getKode().isEmpty()) {
                    showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kode Kota tidak boleh kosong");
                    RequestContext.getCurrentInstance().update("growl");
                }
                return;
            }
            if (kotaCek == null) {
                System.out.println("tambah");
                //tambah
                kotaCek = kotaRepo.findTop1ByKode(kota.getKode());
                if (kotaCek != null) {
                    if (kotaCek.getStatus().equals(Kota.Status.ACTIVE)) {
                        showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kota dengan kode " + kotaCek.getKode() + " sudah tersedia");
                        RequestContext.getCurrentInstance().update("growl");
                        return;
                    }
                }
            } else {
                System.out.println("update");
                //update
                if (!kodeKota.equals(kota.getKode())) {
                    kotaCek = kotaRepo.findTop1ByKodeAndStatus(kota.getKode(), Kota.Status.ACTIVE);
                    if (kotaCek != null) {
                        if (kotaCek.getStatus().equals(Kota.Status.ACTIVE)) {
                            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kota dengan kode " + kotaCek.getKode() + " sudah tersedia");
                            RequestContext.getCurrentInstance().update("growl");
                            return;
                        }
                    }
                } else {
                    showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Harap ubah kota");
                    RequestContext.getCurrentInstance().update("growl");
                    return;
                }
            }
            kota.setCreatedAt(new Date());
            kota.setStatus(Kota.Status.ACTIVE);
            kotaRepo.save(kota);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            log.error("error : {}", ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        } finally {
            kotaCek = null;
        }
    }

    public void ubah() throws InterruptedException {
        try {
            RequestContext.getCurrentInstance().scrollTo("idForm");
            kota = (Kota) getRequestParam("kota");
            System.out.println("kota : " + kota);
            kodeKota = kota.getKode();
            kotaCek = kota;
        } catch (Exception ex) {
            log.error("error : {}", ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void hapus() throws InterruptedException {
        try {
            kota = (Kota) getRequestParam("kota");
            System.out.println("kota : " + kota);
            kota.setStatus(Kota.Status.INACTIVE);
            kotaRepo.save(kota);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            log.error("error : {}", ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan hapus data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void showDialogCetak() {
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }
}
