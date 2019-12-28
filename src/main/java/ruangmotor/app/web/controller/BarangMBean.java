/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import ruangmotor.app.model.Kategori;
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
import ruangmotor.app.model.MstBarang;
import ruangmotor.app.repo.BarangRepo;
import ruangmotor.app.repo.KategoriRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.getRequestParam;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;
import java.math.BigInteger;
import java.util.Date;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author ruangmotor
 */
@Controller
@Scope("view")
@Data
public class BarangMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private BarangRepo barangRepo;
    private LazyDataModelFilterJPA<MstBarang> listBarang;
    private List<MstBarang> listBarang2;
    private MstBarang mstBarang;
    private MstBarang mstBarangCek;
    private String kodeBarang;
    private Integer tambahStok;
    private String dalogHeader;

    @Autowired
    private KategoriRepo kategoriRepo;
    private Kategori kategori;
    private List<Kategori> listKategori;

    public void init() {
        mstBarang = new MstBarang();
        kategori = new Kategori();
        listKategori = kategoriRepo.findAllByStatusOrderByNamaKategoriAsc(Kategori.Status.ACTIVE);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listBarang2 = new ArrayList<>();
        listBarang2 = barangRepo.findAllByStatusOrderByNamaBarangAsc(MstBarang.Status.ACTIVE);
        listBarang = new LazyDataModelFilterJPA(barangRepo) {
            @Override
            protected Page getDatas(PageRequest request, Map filters) {
                mstBarang.setNamaBarang((String) filters.get("namaBarang"));
                mstBarang.setKodeBarang((String) filters.get("kodeBarang"));
                kategori = (Kategori) filters.get("kategori");
                mstBarang.setHargaBeli((Integer) filters.get("hargaBeli"));
                mstBarang.setHargaJual((Integer) filters.get("hargaJual"));
                mstBarang.setStok((Integer) filters.get("stok"));
                return barangRepo.findAll(whereQuery(), request);
            }

            @Override
            protected long getDataSize(Map filters) {
                mstBarang.setNamaBarang((String) filters.get("namaBarang"));
                mstBarang.setKodeBarang((String) filters.get("kodeBarang"));
                kategori = (Kategori) filters.get("kategori");
                mstBarang.setHargaBeli((Integer) filters.get("hargaBeli"));
                mstBarang.setHargaJual((Integer) filters.get("hargaJual"));
                mstBarang.setStok((Integer) filters.get("stok"));
                return barangRepo.count(whereQuery());
            }
        };
    }

    public Specification<MstBarang> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<MstBarang>() {
            @Override
            public Predicate toPredicate(Root<MstBarang> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                if (StringUtils.isNotBlank(mstBarang.getNamaBarang())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("namaBarang")),
                            getLikePattern(mstBarang.getNamaBarang())));
                }
                if (StringUtils.isNotBlank(mstBarang.getKodeBarang())) {
                    predicates.add(cb.like(cb.lower(root.<String>get("kodeBarang")),
                            getLikePattern(mstBarang.getKodeBarang())));
                }
                if (kategori != null && kategori.getKategoriId() != null) {
                    predicates.add(cb.equal(root.<Kategori>get("kategori"), kategori));
                }
                if (mstBarang.getHargaBeli() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("hargaBeli")),
                            mstBarang.getHargaBeli()));
                }
                if (mstBarang.getHargaJual() != null) {
                    predicates.add(cb.equal(cb.lower(root.<String>get("hargaJual")),
                            mstBarang.getHargaJual()));
                }
//                if (mstBarang.getStok() != 0) {
//                    predicates.add(cb.equal(cont.<Integer>get("stok"), mstBarang.getHargaJual()));
//                }

                predicates.add(cb.equal(root.<BigInteger>get("status"), MstBarang.Status.ACTIVE));
                query.orderBy(cb.asc(root.<Integer>get("barangId")));
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

    public void showDialogTambahStok() {
        tambahStok = null;
        mstBarang = (MstBarang) getRequestParam("barang");
        RequestContext.getCurrentInstance().reset("idDialogTambahStok");
        RequestContext.getCurrentInstance().update("idDialogTambahStok");
        RequestContext.getCurrentInstance().execute("PF('showDialogTambahStok').show()");
    }

    public void showDialogAction() {
        mstBarangCek = (MstBarang) getRequestParam("barang");
        mstBarang = new MstBarang();
        if (mstBarangCek == null) {
            System.out.println("tambah");
            dalogHeader = "Tambah Barang";
        } else {
            System.out.println("update");
            dalogHeader = "Ubah Barang";
            mstBarang = mstBarangCek;
            kodeBarang = mstBarangCek.getKodeBarang();
            System.out.println("kode barang : " + kodeBarang);
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void saveRecord() throws InterruptedException {
        try {
            MstBarang br = barangRepo.findTop1ByKodeBarang(mstBarang.getKodeBarang().toUpperCase());
            String param = (String) getRequestParam("save");
            if (param != null) {
                //tambah stok
                br.setStok(br.getStok() + tambahStok);
                barangRepo.save(br);
                showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Stok berhasil ditambah");
                RequestContext.getCurrentInstance().update("idList");
                RequestContext.getCurrentInstance().update("growl");
                RequestContext.getCurrentInstance().execute("PF('showDialogTambahStok').hide()");
                return;
            }

            System.out.println("br : " + br);
            if (br != null) {
                //update
                if (br.getStatus().equals(MstBarang.Status.ACTIVE)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Barang dengan kode " + br.getKodeBarang() + " sudah tersedia");
                    return;
                }
                mstBarang.setStatus(MstBarang.Status.INACTIVE);
                mstBarang.setKodeBarang(kodeBarang);
                barangRepo.save(mstBarang);
                String namaBarang = mstBarang.getNamaBarang();
                Integer hargaBeli = mstBarang.getHargaBeli();
                Integer hargaJual = mstBarang.getHargaJual();
                Integer stok = mstBarang.getStok();
                Kategori kt = mstBarang.getKategori();
                mstBarang = br;
                mstBarang.setNamaBarang(namaBarang);
                mstBarang.setHargaBeli(hargaBeli);
                mstBarang.setHargaJual(hargaJual);
                mstBarang.setStok(stok);
                mstBarang.setKategori(kt);
            } else {
                if (mstBarang.getStok() < mstBarang.getStok_minimal()) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Stok harus lebih besar dari stok minimal");
                    RequestContext.getCurrentInstance().update("idGrowl");
                    return;
                }
                mstBarang.setCreatedAt(new Date());
            }
            mstBarang.setStatus(MstBarang.Status.ACTIVE);
            barangRepo.save(mstBarang);
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
        mstBarangCek = (MstBarang) getRequestParam("barang");
        mstBarangCek.setStatus(MstBarang.Status.INACTIVE);
        barangRepo.save(mstBarangCek);
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
    }

    public void showDialogCetak() {
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }

//    private void createBarModels() {
//        createBarModel();
//    }
//    private void createBarModel() {
//        barModel = initBarModel();
//
//        barModel.setTitle("Bar Chart");
//        barModel.setLegendPosition("ne");
//
//        Axis xAxis = barModel.getAxis(AxisType.X);
//        xAxis.setLabel("Gender");
//
//        Axis yAxis = barModel.getAxis(AxisType.Y);
//        yAxis.setLabel("Births");
//        yAxis.setMin(0);
//        yAxis.setMax(200);
//    }
//    
//    private BarChartModel initBarModel() {
//        BarChartModel model = new BarChartModel();
//
//        ChartSeries boys = new ChartSeries();
//        boys.setLabel("Boys");
//        boys.set("2004", 120);
//        boys.set("2005", 100);
//        boys.set("2006", 44);
//        boys.set("2007", 150);
//        boys.set("2008", 25);
//
//        ChartSeries girls = new ChartSeries();
//        girls.setLabel("Girls");
//        girls.set("2004", 52);
//        girls.set("2005", 60);
//        girls.set("2006", 110);
//        girls.set("2007", 135);
//        girls.set("2008", 120);
//
//        model.addSeries(boys);
//        model.addSeries(girls);
//
//        return model;
//    }
}
