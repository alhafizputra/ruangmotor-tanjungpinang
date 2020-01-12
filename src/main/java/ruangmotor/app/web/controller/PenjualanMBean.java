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
import ruangmotor.app.model.MstPelanggan;
import ruangmotor.app.model.TrPenjualan;
import ruangmotor.app.model.TrPenjualanDtl;
import ruangmotor.app.repo.BarangRepo;
import ruangmotor.app.repo.PelangganRepo;
import ruangmotor.app.repo.PenjualanDtlRepo;
import ruangmotor.app.repo.PenjualanRepo;
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
public class PenjualanMBean extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private PenjualanRepo penjualanRepo;
    private TrPenjualan penjualan;
    private List<TrPenjualan> listPenjualan;

    @Autowired
    private PenjualanDtlRepo penjualanDtlRepo;
    private TrPenjualanDtl penjualanDtl;
    private List<TrPenjualanDtl> listPenjualanDtl;

    @Autowired
    private BarangRepo barangRepo;
    private MstBarang barang;
    private List<MstBarang> listBarang;

    @Autowired
    private PelangganRepo pelangganRepo;
    private MstPelanggan pelanggan;
    private List<MstPelanggan> listPelanggan;

    private String notaJual;
    private String cariPelanggan;
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
        penjualan = new TrPenjualan();
        penjualanDtl = new TrPenjualanDtl();
        barang = new MstBarang();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        listPenjualan = penjualanRepo.findAllByStatus(TrPenjualan.Status.ACTIVE);
        TrPenjualan pPenjualan = penjualanRepo.findTop1ByStatusOrderByPenjualanIdDesc(TrPenjualan.Status.ACTIVE);
//        System.out.println("pPenjualan : " + pPenjualan);
        Integer nextPenjualanId = pPenjualan != null ? pPenjualan.getPenjualanId() + 1 : 1;
//        System.out.println("nextPenjualanId : " + nextPenjualanId);
//        System.out.println("nextPenjualanId size : " + nextPenjualanId.toString().length());
        notaJual = nextPenjualanId.toString().length() == 1 ? "NOT00".concat(nextPenjualanId.toString())
                : nextPenjualanId.toString().length() == 2 ? "NOT0".concat(nextPenjualanId.toString())
                : "NOT".concat(nextPenjualanId.toString());
//        System.out.println("notaJual : " + notaJual);
        init();
        penjualan.setNotaJual(notaJual);
        penjualan.setTglJual(new Date());

        listBarang = barangRepo.findAllByStatusOrderByNamaBarangAsc(MstBarang.Status.ACTIVE);
        listPelanggan = pelangganRepo.findAllByStatusOrderByNamaPelangganAsc(MstPelanggan.Status.ACTIVE);

        listPenjualanDtl = new ArrayList<>();
//        System.out.println("penjualanDtl : " + penjualanDtl);
        listPenjualanDtl.add(penjualanDtl);
    }

    public Specification<TrPenjualan> whereQuery() {
        List<Predicate> predicates = new ArrayList<>();
        return new Specification<TrPenjualan>() {
            @Override
            public Predicate toPredicate(Root<TrPenjualan> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                query.orderBy(cb.asc(root.<BigDecimal>get("penjualanId")));
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
        TrPenjualanDtl pjd = (TrPenjualanDtl) getRequestParam("penjualandtl");
        System.out.println("pjd : " + pjd);
        if (pjd != null) {
            listPenjualanDtl.remove(pjd);
        }
    }

    public void simpan() throws IOException {
//        penjualan.set();
        penjualan.setStatus(TrPenjualan.Status.ACTIVE);
        penjualan = penjualanRepo.save(penjualan);
        System.out.println("penjualan : " + penjualan);
//        for (TrPenjualanDtl pd : listPenjualanDtl) {
//            barang = pd.getMstBarang();
//            barang.setStok(barang.getStok() - pd.getJumlahJual());
//            barangRepo.save(barang);
//        }
        penjualanDtlRepo.save(listPenjualanDtl);
        System.out.println("selesai..");

//        for (TrPenjualanDtl pjd : listPenjualanDtl) {
//            penjualanDtlRepo.save(listPenjualanDtl);
//        }
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Transaksi Berhasil");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
//        click();
//        init();
//        listPenjualanDtl = new ArrayList<>();
//        reload();
    }

    public void preCetak() {
        System.out.println("listPenjualanDtl : " + listPenjualanDtl);
        for (TrPenjualanDtl pd : listPenjualanDtl) {
            barang = pd.getMstBarang();
            barang.setStok(barang.getStok() - pd.getJumlahJual());
            barangRepo.save(barang);
            barang = pd.getMstBarang();
        }
        System.out.println("totalHarga : " + total);
        System.out.println("bayar : " + bayar);
        System.out.println("barang : " + barang);
        click();
    }

    public void cetak() {
        TrPenjualan penjualanTmp = penjualanRepo.findTop1ByNotaJual(penjualan.getNotaJual());
        if (penjualanTmp == null) {
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
        if (penjualanDtl.getJumlahJual() != null) {
//            System.out.println("stok minimal : " + penjualanDtl.getMstBarang().getStok_minimal());
//            System.out.println("stok setelah dikurang : " + (penjualanDtl.getMstBarang().getStok() - penjualanDtl.getJumlahJual()));
            if ((penjualanDtl.getMstBarang().getStok() - penjualanDtl.getJumlahJual()) < penjualanDtl.getMstBarang().getStok_minimal()) {
                showGrowl(FacesMessage.SEVERITY_WARN, "Peringatan", "Stok tersisa tidak boleh kurang dari jumlah stok minimal");
                RequestContext.getCurrentInstance().update("idList");
                RequestContext.getCurrentInstance().update("growl");
                penjualanDtl.setJumlahJual(null);
                return;
            }
        }

        Integer subTotal = 0;
        total = 0;
        if (penjualanDtl != null && penjualanDtl.getMstBarang() != null) {
            penjualanDtl.setHargaSatuan(penjualanDtl.getMstBarang().getHargaJual());
            penjualanDtl.setKodeBarang(penjualanDtl.getMstBarang().getKodeBarang());
            penjualanDtl.setNamaBarang(penjualanDtl.getMstBarang().getNamaBarang());
            penjualanDtl.setTrPenjualan(penjualan);
            if (penjualanDtl.getJumlahJual() != null) {
                subTotal = penjualanDtl.getJumlahJual() * penjualanDtl.getHargaSatuan();
                penjualanDtl.setSubTotal(BigInteger.valueOf(subTotal));
                for (TrPenjualanDtl pd : listPenjualanDtl) {
                    total += pd.getSubTotal().intValue();
                }
                penjualan.setTotalHarga(BigInteger.valueOf(total));
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
        penjualanDtl = new TrPenjualanDtl();
        System.out.println("penjualanDtl : " + penjualanDtl);
        listPenjualanDtl.add(penjualanDtl);
    }

//    public void onChangeBayar() {
//        kembali = bayar - penjualan.getTotalHarga().intValue();
//    }
    public void cariPenjualanByNama() {
        System.out.println("cariPelanggan : " + cariPelanggan);
        MstPelanggan mstPelanggan = pelangganRepo.findTop1ByNamaPelangganAndStatus(cariPelanggan, MstPelanggan.Status.ACTIVE);
        listPenjualan = new ArrayList<>();
        if (cariPelanggan == null || cariPelanggan.equals("")) {
            listPenjualan = penjualanRepo.findAllByStatus(TrPenjualan.Status.ACTIVE);
        } else {
            if (mstPelanggan != null) {
                listPenjualan = penjualanRepo.findAllByMstPelanggan(mstPelanggan);
            }
        }
    }

    public void showDialogAction() {
        penjualan = (TrPenjualan) getRequestParam("penjualan");
        System.out.println("penjualan : " + penjualan);
        if (penjualan != null) {
            listPenjualanDtl = new ArrayList<>();
            listPenjualanDtl = penjualanDtlRepo.findAllByTrPenjualan(penjualan);
//            System.out.println("listPenjualanDtl : " + listPenjualanDtl);
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
            System.out.println("penjualan jasper : " + penjualan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
//            hm.put("PENJUALAN_ID", penjualan.getPenjualanId().toString());
//            System.out.println("PENJUALAN_ID : " + penjualan.getPenjualanId());

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
//            System.out.println("penjualan jasper : " + penjualan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));
            System.out.println("PENJUALAN_ID : " + penjualan.getPenjualanId());
            hm.put("PENJUALAN_ID", penjualan.getPenjualanId().toString());
            System.out.println("BAYAR : " + bayar.toString());
            hm.put("BAYAR", bayar.toString());
            System.out.println("KEMBALI : " + kembali.toString());
            hm.put("KEMBALI", kembali.toString());

            String jrxml = "/Reports/cetak_transaksi.jrxml";
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

    public void laporanTransaksi() throws ClassNotFoundException, SQLException, JRException, IOException {
        Connection connection = null;
        try {
            connection = dataSource.getConnection();
            System.out.println("connection : " + connection);
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String tglDariStr = df.format(tglDari);
            String tglSampaiStr = df.format(tglSampai);
            System.out.println("penjualan jasper : " + penjualan);
            HashMap hm = new HashMap();
            hm.put(JRParameter.REPORT_LOCALE, new java.util.Locale("id"));

            hm.put("TGL_AWAL", tglDariStr);
            System.out.println("dari : " + tglDariStr);
            hm.put("TGL_AKHIR", tglSampaiStr);
            System.out.println("sampai : " + tglSampaiStr);
            String jrxml = "/Reports/laporan_transaksi.jrxml";
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

    public void click() {
        RequestContext context = RequestContext.getCurrentInstance();
        context.execute("document.getElementById('cetak').click();");
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
