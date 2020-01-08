/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import ruangmotor.app.model.Kategori;
import ruangmotor.app.repo.KategoriRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
public class KategoriMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private KategoriRepo kategoriRepo;
    private LazyDataModelFilterJPA<Kategori> listKategori;
    private List<Kategori> listKategori2;
    private Kategori kategori;
    private Kategori kategoriCek;
    private String namaKategori;

    public void init() {
        kategori = new Kategori();
        namaKategori = null;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listKategori2 = new ArrayList<>();
        listKategori2 = kategoriRepo.findAllByStatusOrderByNamaKategoriAsc(Kategori.Status.ACTIVE);
        listKategori = new LazyDataModelFilterJPA(kategoriRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                kategori.setNamaKategori((String) filters.get("namaKategori"));
                return kategoriRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                kategori.setNamaKategori((String) filters.get("namaKategori"));
                return kategoriRepo.count(whereQuery());
            }
        };
    }

    public Specification<Kategori> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<Kategori>() {
            @Override
            public Predicate toPredicate(Root<Kategori> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(kategori.getNamaKategori())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaKategori")),
                            getLikePattern(kategori.getNamaKategori())));
                }
                predicates.add(cb.equal(root.<Integer>get("status"), Kategori.Status.ACTIVE));
                query.orderBy(cb.asc(root.<BigDecimal>get("kategoriId")));
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
            System.out.println("namaKategori : " + namaKategori);
            System.out.println("kategori : " + kategori);
            if (namaKategori.isEmpty()) {
                showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kategori tidak boleh kosong");
                RequestContext.getCurrentInstance().update("growl");
                return;
            }
            //kategori cek ada atau tidak null saat ubah 
            if (kategoriCek == null) {
                System.out.println("tambah");
                //tambah
                Kategori kategoriCek2 = kategoriRepo.findTop1ByNamaKategori(namaKategori);
                //cek apakah kategori tsb sudah ada atau tidak
                if (kategoriCek2 != null) {
                    //cek apakah status kategori tsb aktif atau tidak
                    if (kategoriCek2.getStatus().equals(Kategori.Status.ACTIVE)) {
                        showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kategori " + namaKategori + " sudah tersedia");
                        RequestContext.getCurrentInstance().update("growl");
                        return;
                    } else {
                        kategori = kategoriCek2;
                        System.out.println("kategori : " + kategori);
                        kategori.setNamaKategori(namaKategori);
                    }
                } else {
                    kategori = new Kategori();
                    kategori.setNamaKategori(namaKategori);
                }
            } else {
                System.out.println("update");
                //update
                if (!namaKategori.equals(kategoriCek.getNamaKategori())) {
                    Kategori kategoriCek2 = kategoriRepo.findTop1ByNamaKategori(namaKategori);
                    System.out.println("kategoriCek : " + kategoriCek);
                    if (kategoriCek2 != null) {
                        System.out.println("if");
                        if (kategoriCek2.getStatus().equals(Kategori.Status.ACTIVE)) {
                            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Kategori " + namaKategori + " sudah tersedia");
                            RequestContext.getCurrentInstance().update("growl");
                            return;
                        } else {
                            kategori = kategoriCek2;
                            System.out.println("kategori : " + kategori);
                            kategori.setNamaKategori(namaKategori);
                        }
                    } else {
                        System.out.println("else");
                        kategori = kategoriCek;
                        System.out.println("kategori : " + kategori);
                        System.out.println("namaKategori : " + namaKategori);
                        kategori.setNamaKategori(namaKategori);
                    }
                } else {
                    showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Harap ubah kategori");
                    RequestContext.getCurrentInstance().update("growl");
                    return;
                }
            }
            kategori.setCreatedAt(new Date());
            kategori.setStatus(Kategori.Status.ACTIVE);
            kategoriRepo.save(kategori);
            init();
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
        } catch (Exception ex) {
            log.error("error : {}", ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        } finally {
            kategoriCek = null;
        }
    }

    public void ubah() throws InterruptedException {
        try {
            RequestContext.getCurrentInstance().scrollTo("idForm");
            kategoriCek = (Kategori) getRequestParam("kategori");
            System.out.println("kategoriCek : " + kategoriCek);
            namaKategori = kategoriCek.getNamaKategori();
        } catch (Exception ex) {
            log.error("error : {}", ex);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan ubah data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void hapus() throws InterruptedException {
        try {
            kategori = (Kategori) getRequestParam("kategori");
            System.out.println("kategori : " + kategori);
            kategori.setStatus(Kategori.Status.INACTIVE);
            kategoriRepo.save(kategori);
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

    public void tutup() throws IOException {
        try {
            FacesContext.getCurrentInstance().getExternalContext().redirect("/index.xhtml");
        } catch (IOException ex) {
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
