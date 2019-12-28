/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

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
import ruangmotor.app.model.MstPelanggan;
import ruangmotor.app.repo.KotaRepo;
import ruangmotor.app.repo.PelangganRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.getRequestParam;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.util.Date;

/**
 *
 * @author ruangmotor
 */
@Controller
@Scope("view")
@Data
public class PelangganMBean extends AbstractManagedBean implements InitializingBean {

     @Autowired
    private PelangganRepo pelangganRepo;
    private LazyDataModelFilterJPA<MstPelanggan> listPelanggan;
    private List<MstPelanggan> listPelanggan2;
    private MstPelanggan mstPelanggan;
    private MstPelanggan mstPelangganCek;
    private String kodePelanggan;
    private String dalogHeader;

    @Autowired
    private KotaRepo kotaRepo;
    private Kota kota;
    private List<Kota> listKota;

    public void init() {
        mstPelanggan = new MstPelanggan();
        kota = new Kota();
        listKota = kotaRepo.findAllByStatusOrderByNamaAsc(Kota.Status.ACTIVE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listPelanggan2 = new ArrayList<>();
        listPelanggan2 = pelangganRepo.findAllByStatusOrderByNamaPelangganAsc(MstPelanggan.Status.ACTIVE);
        listPelanggan = new LazyDataModelFilterJPA(pelangganRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstPelanggan.setNamaPelanggan((String) filters.get("namaPelanggan"));
                mstPelanggan.setKodePelanggan((String) filters.get("kodePelanggan"));
                mstPelanggan.setAlamat((String) filters.get("alamat"));
                mstPelanggan.setJenisKelamin((String) filters.get("jenisKelamin"));
                mstPelanggan.setTglLahir((Date) filters.get("tglLahir"));
                mstPelanggan.setTelepon((String) filters.get("telepon"));
                kota = (Kota) filters.get("kota");
                return pelangganRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstPelanggan.setNamaPelanggan((String) filters.get("namaPelanggan"));
                mstPelanggan.setKodePelanggan((String) filters.get("kodePelanggan"));
                mstPelanggan.setAlamat((String) filters.get("alamat"));
                mstPelanggan.setJenisKelamin((String) filters.get("jenisKelamin"));
                 mstPelanggan.setTglLahir((Date) filters.get("tglLahir"));
                mstPelanggan.setTelepon((String) filters.get("telepon"));
                kota = (Kota) filters.get("kota");
                return pelangganRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstPelanggan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstPelanggan>() {
            @Override
            public Predicate toPredicate(Root<MstPelanggan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstPelanggan.getNamaPelanggan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaPelanggan")),
                            getLikePattern(mstPelanggan.getNamaPelanggan())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getKodePelanggan())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodePelanggan")),
                            getLikePattern(mstPelanggan.getKodePelanggan())));
                }
                if (kota != null && kota.getKotaId() != null) {
                    predicates.add(cb.equal(root.<Kota>get("kota"), kota));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getJenisKelamin())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("jeniskelamin")),
                            getLikePattern(mstPelanggan.getJenisKelamin())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getTelepon())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("telepon")),
                            getLikePattern(mstPelanggan.getTelepon())));
                }
                if (StringUtils.isNotBlank(mstPelanggan.getAlamat())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("alamat")),
                            getLikePattern(mstPelanggan.getAlamat())));
                }
                predicates.add(cb.equal(root.<BigInteger>get("status"), MstPelanggan.Status.ACTIVE));
                query.orderBy(cb.asc(root.<Integer>get("pelangganId")));
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
        mstPelangganCek = (MstPelanggan) getRequestParam("pelanggan");
        mstPelanggan = new MstPelanggan();
        if (mstPelangganCek == null) {
            System.out.println("tambah");
            dalogHeader = "Tambah Pelanggan";
        } else {
            System.out.println("update");
            dalogHeader = "Ubah Pelanggan";
            mstPelanggan = mstPelangganCek;
            kodePelanggan = mstPelangganCek.getKodePelanggan();
            System.out.println("kode pelanggan : " + kodePelanggan);
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void saveRecord() throws InterruptedException {
        try {
            MstPelanggan br = pelangganRepo.findTop1ByKodePelanggan(mstPelanggan.getKodePelanggan().toUpperCase());
            System.out.println("br : " + br);
            if (br != null) {
                if (br.getStatus().equals(MstPelanggan.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_ERROR,
                            "Pelanggan dengan kode " + mstPelanggan.getKodePelanggan().toUpperCase()
                            + " sudah ada", "");
                    return;
                } else {
                    mstPelanggan.setStatus(MstPelanggan.Status.INACTIVE);
                    mstPelanggan.setKodePelanggan(kodePelanggan);
                    pelangganRepo.save(mstPelanggan);
                    String namaPelanggan = mstPelanggan.getNamaPelanggan();
                    String jeniskelamin = mstPelanggan.getJenisKelamin();
                    Date tglLahir = mstPelanggan.getTglLahir();
                    String telepon = mstPelanggan.getTelepon();
                    String alamat = mstPelanggan.getAlamat();
                    Kota kt = mstPelanggan.getKota();
                    mstPelanggan = br;
                    mstPelanggan.setNamaPelanggan(namaPelanggan);
                    mstPelanggan.setJenisKelamin(jeniskelamin);
                    mstPelanggan.setTglLahir(tglLahir);
                    mstPelanggan.setTelepon(telepon);
                    mstPelanggan.setAlamat(alamat);
                    mstPelanggan.setKota(kt);
                }
            }
            mstPelanggan.setStatus(MstPelanggan.Status.ACTIVE);
            pelangganRepo.save(mstPelanggan);
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
        mstPelangganCek = (MstPelanggan) getRequestParam("pelanggan");
        mstPelangganCek.setStatus(MstPelanggan.Status.INACTIVE);
        pelangganRepo.save(mstPelangganCek);
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
