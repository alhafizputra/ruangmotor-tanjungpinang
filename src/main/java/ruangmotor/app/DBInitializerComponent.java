package ruangmotor.app;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import ruangmotor.app.model.Kategori;
import ruangmotor.app.model.Kota;
import ruangmotor.app.model.MstBarang;
import ruangmotor.app.model.MstKaryawan;
import ruangmotor.app.model.MstPelanggan;
import ruangmotor.app.model.Role;
import ruangmotor.app.model.User;
import ruangmotor.app.repo.BarangRepo;
import ruangmotor.app.repo.KaryawanRepo;
import ruangmotor.app.repo.KategoriRepo;
import ruangmotor.app.repo.KotaRepo;
import ruangmotor.app.repo.PelangganRepo;
import ruangmotor.app.repo.RoleRepository;
import ruangmotor.app.repo.UserRepository;

@Component
//ConditionalOnProperty is an annotation that checks if the property has value "true" and then it runs
@ConditionalOnProperty(name = "app.db-init", havingValue = "true")
//https://dzone.com/articles/spring-boot-applicationrunner-and-commandlinerunne

public class DBInitializerComponent implements CommandLineRunner {

    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    KategoriRepo kategoriRepo;
    @Autowired
    KotaRepo kotaRepo;
    @Autowired
    BarangRepo barangRepo;
    @Autowired
    PelangganRepo pelangganRepo;
    @Autowired
    KaryawanRepo karyawanRepo;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public void run(String... strings) {
        initlialiseRoles();
        initlialiseUser();
        initlialiseKategori();
        initlialiseKota();
        initlialiseBarang();
        initlialisePelanggan();
        initlialiseKaryawan();
    }

    //initialises the DB with Roles
    private boolean initlialiseRoles() {
        System.out.println("======initlialiseRoles======");
        try {
            roleRepository.deleteAll();
            System.out.println("roleRepository");
            List<Role> roleList = new ArrayList<>();
            roleList.add(new Role("SUPERADMIN"));
            roleList.add(new Role("ADMIN"));
            System.out.println("roleList : " + roleList);
            roleRepository.save(roleList);
            System.out.println("Success Initialising Roles");
        } catch (Exception e) {
            System.out.println("Error Initialising Roles");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    //initialises the DB with User
    private boolean initlialiseUser() {
        System.out.println("======initlialiseUser======");
        try {
            Role userRole = roleRepository.findOne(1L);
            System.out.println("userRole : " + userRole);
            User user = new User("superadmin", "SUPERADMIN", bCryptPasswordEncoder.encode("1234"), "1234", "superadmin@gmail.com", "SUPER", "ADMIN", "6282185548660", new SimpleDateFormat("dd/MM/yyyy").parse("31/12/1998"), "L", userRole, new java.util.Date(), new java.util.Date());
            userRepository.save(user);
            System.out.println("Success Initialising User");
        } catch (Exception e) {
            System.out.println("Error Initialising User");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    //initialises the DB with Kategori
    private boolean initlialiseKategori() {
        System.out.println("======initlialiseKategori======");
        try {
            List<Kategori> listKategori = new ArrayList<>();
            listKategori.add(new Kategori("KATEGORI SATU", new java.util.Date()));
            listKategori.add(new Kategori("KATEGORI DUA", new java.util.Date()));
            listKategori.add(new Kategori("KATEGORI TIGA", new java.util.Date()));
            System.out.println("listKategori : " + listKategori);
            kategoriRepo.save(listKategori);
            System.out.println("Success Initialising Kategori");
        } catch (Exception e) {
            System.out.println("Error Initialising Kategori");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    //initialises the DB with Kota
    private boolean initlialiseKota() {
        System.out.println("======initlialiseKategori======");
        try {
            List<Kota> listKota = new ArrayList<>();
            listKota.add(new Kota("JAKARTA", "JKT", new java.util.Date()));
            listKota.add(new Kota("BANDUNG", "BDG", new java.util.Date()));
            listKota.add(new Kota("SEMARANG", "SMR", new java.util.Date()));
            listKota.add(new Kota("SURABAYA", "SBY", new java.util.Date()));
            listKota.add(new Kota("PADANG", "PDG", new java.util.Date()));
            kotaRepo.save(listKota);
            System.out.println("Success Initialising Kota");
        } catch (Exception e) {
            System.out.println("Error Initialising Kota");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    //initialises the DB with Barang
    private boolean initlialiseBarang() {
        System.out.println("======initlialiseBarang======");
        try {
            List<MstBarang> listBarang = new ArrayList<>();
            listBarang.add(new MstBarang("B001", "BARANG SATU", 100000, 150000, 25, 5, kategoriRepo.findTop1ByNamaKategori("KATEGORI SATU"), new java.util.Date()));
            listBarang.add(new MstBarang("B002", "BARANG DUA", 120000, 170000, 30, 5, kategoriRepo.findTop1ByNamaKategori("KATEGORI DUA"), new java.util.Date()));
            listBarang.add(new MstBarang("B003", "BARANG TIGA", 100000, 160000, 20, 5, kategoriRepo.findTop1ByNamaKategori("KATEGORI TIGA"), new java.util.Date()));
            System.out.println("listBarang : " + listBarang);
            barangRepo.save(listBarang);
            System.out.println("Success Initialising Barang");
        } catch (Exception e) {
            System.out.println("Error Initialising Barang");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

    //initialises the DB with Pelanggan
    private boolean initlialisePelanggan() {
        System.out.println("======initlialisePelanggan======");
        try {
            List<MstPelanggan> listPelanggan = new ArrayList<>();
            listPelanggan.add(new MstPelanggan("P001", "PELANGGAN SATU", "L", new SimpleDateFormat("dd/MM/yyyy").parse("01/12/1998"), "JL. SUDIRMAN NO 49", kotaRepo.findTop1ByKode("JKT"), "6282147756435", new java.util.Date()));
            listPelanggan.add(new MstPelanggan("P002", "PELANGGAN DUA", "L", new SimpleDateFormat("dd/MM/yyyy").parse("21/11/1998"), "JL. MURADI NO 22", kotaRepo.findTop1ByKode("SBY"), "6282146742324", new java.util.Date()));
            listPelanggan.add(new MstPelanggan("P003", "PELANGGAN TIGA", "P", new SimpleDateFormat("dd/MM/yyyy").parse("02/06/1998"), "JL. IMAM BONJOL NO 34", kotaRepo.findTop1ByKode("PDG"), "6282147743523", new java.util.Date()));
            pelangganRepo.save(listPelanggan);
            System.out.println("Success Initialising Pelanggan");
        } catch (Exception e) {
            System.out.println("Error Initialising Pelanggan");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
    
    //initialises the DB with Karyawan
    private boolean initlialiseKaryawan() {
        System.out.println("======initlialiseKaryawann======");
        try {
            List<MstKaryawan> listKaryawan = new ArrayList<>();
            listKaryawan.add(new MstKaryawan("K001", "KARYAWAN SATU", "JL. GATOT SUBROTO NO.30", "P", "6281296403848", kotaRepo.findTop1ByKode("SBY"), "karyawan01@gmail.com", new java.util.Date()));
            listKaryawan.add(new MstKaryawan("K002", "KARYAWAN DUA", "JL. HARYONO NO.12", "L", "6282364584866", kotaRepo.findTop1ByKode("SBY"), "karyawan02@gmail.com", new java.util.Date()));
            karyawanRepo.save(listKaryawan);
            System.out.println("Success Initialising Karyawan");
        } catch (Exception e) {
            System.out.println("Error Initialising Karyawan");
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }

}
