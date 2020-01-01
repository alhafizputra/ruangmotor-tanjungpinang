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
import ruangmotor.app.model.MstKaryawan;
import ruangmotor.app.model.MstPelanggan;
import ruangmotor.app.model.TrPenjualan;
import ruangmotor.app.model.User;
import ruangmotor.app.repo.KaryawanRepo;
import ruangmotor.app.repo.PelangganRepo;
import ruangmotor.app.repo.PenjualanRepo;
import ruangmotor.app.repo.UserRepository;

/**
 *
 * @author ruangmotor
 */
@Controller
@Scope("view")
@Data
public class DashboardMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private BarangRepo barangRepo;
    @Autowired
    private PelangganRepo pelangganRepo;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private KaryawanRepo karyawanRepo;
    @Autowired
    private PenjualanRepo penjualanRepo;

    private Integer jumlahBarang;
    private Integer jumlahTransaksi;
    private Integer jumlahPelanggan;
    private Integer jumlahKaryawan;

    public void init() {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
         jumlahBarang = barangRepo.countByStatus(MstBarang.Status.ACTIVE);
         jumlahTransaksi = penjualanRepo.countByStatus(TrPenjualan.Status.ACTIVE);
         jumlahPelanggan = pelangganRepo.countByStatus(MstPelanggan.Status.ACTIVE);
         jumlahKaryawan = karyawanRepo.countByStatus(MstKaryawan.Status.ACTIVE);
         System.out.println("jumlahKaryawan : " + jumlahKaryawan);
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
