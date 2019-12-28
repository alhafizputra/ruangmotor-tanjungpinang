/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import ruangmotor.app.model.MstBarang;
import ruangmotor.app.model.TrPenjualan;
import ruangmotor.app.repo.BarangRepo;
import ruangmotor.app.repo.PenjualanRepo;
import ruangmotor.app.web.util.AbstractManagedBean;

/**
 *
 * @author ruangmotor
 */
@Controller
@Scope("view")
@Data
public class LaporanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PenjualanRepo penjualanRepo;
    private TrPenjualan penjualan;
    private List<TrPenjualan> listPenjualan;

    @Autowired
    private BarangRepo barangRepo;
    private List<MstBarang> listBarang;
    private MstBarang mstBarang;

    private List<Tahun> listTahun;
    private Tahun objTahun;
    private Integer tahun;
    private List<Bulan> listBulan;
    private Bulan objBulan;
    private Integer bulan;

    private String periode;
    private Date tglAwal;
    private Date tglAkhir;

    public void init() {
        penjualan = new TrPenjualan();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
        listTahun = new ArrayList<>();
        for (Integer year = 2000; year <= Calendar.getInstance().get(Calendar.YEAR); year++) {
//            System.out.println("year = " + year);

            objTahun = new Tahun();
            objTahun.setTahun(year);
            listTahun.add(objTahun);
        }
    }

//    public void onChangePeriode() {
//        System.out.println("periode = " + periode);
//        tanggal = periode.equals("hari") ? tanggal : null;
//        bulan = periode.equals("bulan") ? bulan : null;
//    }

    public void onChangeTahun() {
        Date today = new Date();
        Calendar now = Calendar.getInstance();
        now.setTime(today);
        Integer yearNow = now.get(Calendar.YEAR);

        if (tahun.equals(yearNow)) {
            System.out.println("bulan calendar get instance month = " + now.get(Calendar.MONTH));
            System.out.println("bulan calendar get instance day = " + now.get(Calendar.DAY_OF_MONTH));
            listBulan = new ArrayList<>();
            for (Integer month = 0; month <= now.get(Calendar.MONTH); month++) {
                objBulan = new Bulan();
                objBulan.setBulan(month);
                listBulan.add(objBulan);
            }
        } else if (tahun < yearNow) {
            listBulan = new ArrayList<>();
            for (Integer month = 0; month <= 11; month++) {
                objBulan = new Bulan();
                objBulan.setBulan(month);
                listBulan.add(objBulan);
            }
        }
    }

    public void tampilkan() {
        String laporan = (String) getRequestParam("laporan");
        if (laporan.equals("penjualan")) {
            listPenjualan = penjualanRepo.listPenjualan(tglAwal, tglAkhir);
        } else if (laporan.equals("inventory")) {
            listBarang = barangRepo.findAllByOrderByNamaBarangAsc();
        }
//        System.out.println("listPembelian = " + listPembelian);
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }

    @Data
    public static class Tahun implements Serializable {

        private Integer tahun;
    }

    @Data
    public static class Bulan implements Serializable {

        private Integer bulan;
    } 

}
