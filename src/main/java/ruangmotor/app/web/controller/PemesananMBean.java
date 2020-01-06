/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import ruangmotor.app.model.MstBarang;
import ruangmotor.app.model.MstSupplier;
import ruangmotor.app.model.TrPemesanan;
import ruangmotor.app.model.TrPemesananDtl;
import ruangmotor.app.repo.BarangRepo;
import ruangmotor.app.repo.SupplierRepo;
import ruangmotor.app.repo.PemesananDtlRepo;
import ruangmotor.app.repo.PemesananRepo;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import lombok.Cleanup;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class PemesananMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PemesananRepo pemesananRepo;
    private TrPemesanan pemesanan;
    private List<TrPemesanan> listPemesanan;

    @Autowired
    private PemesananDtlRepo pemesananDtlRepo;
    private TrPemesananDtl pemesananDtl;
    private List<TrPemesananDtl> listPemesananDtl;

    @Autowired
    private BarangRepo barangRepo;
    private MstBarang barang;
    private List<MstBarang> listBarang;

    @Autowired
    private SupplierRepo supplierRepo;
    private MstSupplier supplier;
    private List<MstSupplier> listSupplier;

    private String notaPesan;
    private String cariNotaPesan;
    private Date tglDari;
    private Date tglSampai;

    @Autowired
    private DataSource dataSource;

    private Integer total = 0;
    private Integer bayar = 0;
    private Integer kembali = 0;

    private Boolean disabledSimpan = true;

    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");

    public void init() {
        pemesanan = new TrPemesanan();
        pemesananDtl = new TrPemesananDtl();
        barang = new MstBarang();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listPemesanan = pemesananRepo.findAllByStatus(TrPemesanan.Status.ACTIVE);
        TrPemesanan pPemesanan = pemesananRepo.findTop1ByStatusOrderByPemesananIdDesc(TrPemesanan.Status.ACTIVE);
//        System.out.println("pPemesanan : " + pPemesanan);
        Integer nextPemesananId = pPemesanan != null ? pPemesanan.getPemesananId() + 1 : 1;
//        System.out.println("nextPemesananId : " + nextPemesananId);
//        System.out.println("nextPemesananId size : " + nextPemesananId.toString().length());
        notaPesan = nextPemesananId.toString().length() == 1 ? "PSN00".concat(nextPemesananId.toString())
                : nextPemesananId.toString().length() == 2 ? "PSN0".concat(nextPemesananId.toString())
                : "PSN".concat(nextPemesananId.toString());
//        System.out.println("notaPesan : " + notaPesan);
        init();
        pemesanan.setNotaPesan(notaPesan);
        pemesanan.setTglPesan(new Date());

        listBarang = barangRepo.findAllByStatusOrderByNamaBarangAsc(MstBarang.Status.ACTIVE);
        listSupplier = supplierRepo.findAllByStatusOrderByNamaSupplierAsc(MstSupplier.Status.ACTIVE);

        listPemesananDtl = new ArrayList<>();
//        System.out.println("pemesananDtl : " + pemesananDtl);
        listPemesananDtl.add(pemesananDtl);
    }

    public Specification<TrPemesanan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<TrPemesanan>() {
            @Override
            public Predicate toPredicate(Root<TrPemesanan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.asc(root.<BigDecimal>get("pemesananId")));
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

    public void hapusBaris() {
        TrPemesananDtl pjd = (TrPemesananDtl) getRequestParam("pemesanandtl");
        System.out.println("pjd : " + pjd);
        if (pjd != null) {
            listPemesananDtl.remove(pjd);
        }
    }

    public void simpan() throws IOException {
//        pemesanan.set();
        pemesanan.setStatus(TrPemesanan.Status.ACTIVE);
        pemesanan = pemesananRepo.save(pemesanan);
        System.out.println("pemesanan : " + pemesanan);
//        for (TrPemesananDtl pd : listPemesananDtl) {
//            barang = pd.getMstBarang();
//            barang.setStok(barang.getStok() - pd.getJumlahPesan());
//            barangRepo.save(barang);
//        }
        pemesananDtlRepo.save(listPemesananDtl);
        System.out.println("selesai..");

//        for (TrPemesananDtl pjd : listPemesananDtl) {
//            pemesananDtlRepo.save(listPemesananDtl);
//        }
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Pemesanan Berhasil");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
//        click();
//        init();
//        listPemesananDtl = new ArrayList<>();
//        reload();
    }

    public void preCetak() {
        System.out.println("listPemesananDtl : " + listPemesananDtl);
//        for (TrPemesananDtl pd : listPemesananDtl) {
//            barang = pd.getMstBarang();
//            barang.setStok(barang.getStok() - pd.getJumlahPesan());
//            barangRepo.save(barang);
//            barang = pd.getMstBarang();
//        }
        System.out.println("totalHarga : " + total);
        System.out.println("bayar : " + bayar);
        click();
    }

    public void cetak() {
        TrPemesanan pemesananTmp = pemesananRepo.findTop1ByNotaPesan(pemesanan.getNotaPesan());
        if (pemesananTmp == null) {
            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Cari data terlebih dahulu");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
            return;
        }
        RequestContext.getCurrentInstance().reset("idDialogCetak");
        RequestContext.getCurrentInstance().update("idDialogCetak");
        RequestContext.getCurrentInstance().execute("PF('showDialogCetak').show()");
    }

    public void totalHarga() {
        //cek stok minimal
        if (pemesananDtl.getJumlahPesan() != null) {
//            System.out.println("stok minimal : " + pemesananDtl.getMstBarang().getStok_minimal());
//            System.out.println("stok setelah dikurang : " + (pemesananDtl.getMstBarang().getStok() - pemesananDtl.getJumlahPesan()));
            if ((pemesananDtl.getMstBarang().getStok() - pemesananDtl.getJumlahPesan()) < pemesananDtl.getMstBarang().getStok_minimal()) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Stok tersisa tidak boleh kurang dari jumlah stok minimal");
                RequestContext.getCurrentInstance().update("idList");
                RequestContext.getCurrentInstance().update("growl");
                pemesananDtl.setJumlahPesan(null);
                return;
            }
        }

        Integer subTotal = 0;
        total = 0;
        if (pemesananDtl != null && pemesananDtl.getMstBarang() != null) {
//            pemesananDtl.setHargaSatuan(pemesananDtl.getMstBarang().getHargaPesan());
            pemesananDtl.setKodeBarang(pemesananDtl.getMstBarang().getKodeBarang());
            pemesananDtl.setNamaBarang(pemesananDtl.getMstBarang().getNamaBarang());
            pemesananDtl.setTrPemesanan(pemesanan);
            if (pemesananDtl.getJumlahPesan() != null) {
                subTotal = pemesananDtl.getJumlahPesan() * pemesananDtl.getHargaSatuan();
                pemesananDtl.setSubTotal(BigInteger.valueOf(subTotal));
                for (TrPemesananDtl pd : listPemesananDtl) {
                    total += pd.getSubTotal().intValue();
                }
                pemesanan.setTotalHarga(BigInteger.valueOf(total));
            }
        }
//        System.out.println("subtotal : " + subTotal);
    }

    public void jumlahKembali() {
        if (bayar < total) {
            showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Jumlah bayar kurang dari total harga");
            RequestContext.getCurrentInstance().update("idList");
            RequestContext.getCurrentInstance().update("growl");
            return;
        }
        kembali = bayar - total;
        disabledSimpan = false;
    }

    public void tambahBaris() {
        pemesananDtl = new TrPemesananDtl();
        System.out.println("pemesananDtl : " + pemesananDtl);
        listPemesananDtl.add(pemesananDtl);
    }

//    public void onChangeBayar() {
//        kembali = bayar - pemesanan.getTotalHarga().intValue();
//    }
    public void cariPemesananByNota() {
        System.out.println("cariNotaPesan : " + cariNotaPesan);
        listPemesanan = new ArrayList<>();
        if (cariNotaPesan == null || cariNotaPesan.equals("")) {
            listPemesanan = pemesananRepo.findAllByStatus(TrPemesanan.Status.ACTIVE);
        } else {
            listPemesanan = pemesananRepo.findAllByNotaPesan(cariNotaPesan);
        }

    }

    public void showDialogAction() {
        pemesanan = (TrPemesanan) getRequestParam("pemesanan");
        System.out.println("pemesanan : " + pemesanan);
        if (pemesanan != null) {
            listPemesananDtl = new ArrayList<>();
            listPemesananDtl = pemesananDtlRepo.findAllByTrPemesanan(pemesanan);
//            System.out.println("listPemesananDtl : " + listPemesananDtl);
            RequestContext.getCurrentInstance().reset("idDialocAct");
            RequestContext.getCurrentInstance().update("idDialocAct");
            RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");

        }
    }

    public void tes() throws SQLException, JRException, IOException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println("connection : " + connection);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            String tglDariStr = df.format(tglDari);
//            String tglSampaiStr = df.format(tglSampai);
            System.out.println("pemesanan jasper : " + pemesanan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
//            hm.put("PENJUALAN_ID", pemesanan.getPemesananId().toString());
//            System.out.println("PENJUALAN_ID : " + pemesanan.getPemesananId());

            String jrxml = "/Reports/tess.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getSession().getServletContext().getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");
            File fileJrxml = new File(pathJrxml);
            System.out.println("fileJrxml : " + fileJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"Cetak_Transaksi.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            facescontext.responseComplete();
            System.out.println("selesai..");
        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            System.out.println("error : " + e);
        } finally {
            connection.close();
        }
    }

    public void cetakTransaksi() throws ClassNotFoundException, SQLException, JRException, IOException {
        System.out.println("totalHarga : " + total);
        System.out.println("bayar : " + bayar);
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println("connection : " + connection);
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//            String tglDariStr = df.format(tglDari);
//            String tglSampaiStr = df.format(tglSampai);
//            System.out.println("pemesanan jasper : " + pemesanan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            System.out.println("PEMESANAN_ID : " + pemesanan.getPemesananId());
            hm.put("PEMESANAN_ID", pemesanan.getPemesananId().toString());

            String jrxml = "/Reports/cetak_pemesanan.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getSession().getServletContext().getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");
            File fileJrxml = new File(pathJrxml);
            System.out.println("fileJrxml : " + fileJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"Cetak_Pemesanan.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            facescontext.responseComplete();
            System.out.println("selesai..");
        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            System.out.println("error : " + e);
        } finally {
            connection.close();
        }
    }

    public void laporanTransaksi() throws ClassNotFoundException, SQLException, JRException, IOException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println("connection : " + connection);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String tglDariStr = df.format(tglDari);
            String tglSampaiStr = df.format(tglSampai);
            System.out.println("pemesanan jasper : " + pemesanan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));

            hm.put("TGL_AWAL", tglDariStr);
            System.out.println("dari : " + tglDariStr);
            hm.put("TGL_AKHIR", tglSampaiStr);
            System.out.println("sampai : " + tglSampaiStr);
            String jrxml = "/Reports/laporan_pemesanan.jrxml";
            FacesContext facescontext = FacesContext.getCurrentInstance();
            ExternalContext ext = facescontext.getExternalContext();
            HttpServletRequest request = (HttpServletRequest) ext.getRequest();
            String pathJrxml = request.getSession().getServletContext().getRealPath(jrxml);
            String pathJasper = pathJrxml.replace(".jrxml", ".jasper");
            File fileJrxml = new File(pathJrxml);
            System.out.println("fileJrxml : " + fileJrxml);
            File fileJasper = new File(pathJasper);
            if (!fileJasper.exists() || fileJasper.lastModified() < fileJrxml.lastModified()) {
                JasperCompileManager.compileReportToFile(pathJrxml, pathJasper);
            }

            JasperPrint jasperPrint = JasperFillManager.fillReport(pathJasper, hm, connection);
            HttpServletResponse response = (HttpServletResponse) ext.getResponse();
            byte[] bytes = JasperExportManager.exportReportToPdf(jasperPrint);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "inline; filename=\"Cetak_Pemesanan.pdf\"");
            response.setHeader("Pragma", "public");
            response.setHeader("Chache-Control", "cache");
            response.setHeader("Chache-Control", "must-revalidate");
            response.setContentLength(bytes.length);
            @Cleanup
            ServletOutputStream outStream = response.getOutputStream();
            outStream.write(bytes);
            outStream.flush();
            facescontext.responseComplete();
            System.out.println("selesai..");
        } catch (JRException | IOException | SQLException e) {
            FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Error",
                    "Terjadi masalah PDF dengan error " + e.getMessage());
            RequestContext.getCurrentInstance().showMessageInDialog(message);
            System.out.println("error : " + e);
        } finally {
            connection.close();
        }
    }

    public void click() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("document.getElementById('cetak').click();");
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
