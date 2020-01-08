/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ruangmotor.app.web.controller;

import javax.faces.application.FacesMessage;
import lombok.Data;
import org.primefaces.context.RequestContext;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.web.WebAttributes;
import ruangmotor.app.config.UserValidator;
import ruangmotor.app.model.User;
import ruangmotor.app.repo.UserRepository;
import ruangmotor.app.service.UserService;
import ruangmotor.app.web.util.AbstractManagedBean;
import static ruangmotor.app.web.util.AbstractManagedBean.showGrowl;
import ruangmotor.app.web.util.LazyDataModelFilterJPA;

/**
 *
 * @author Owner
 */
@Controller
@Scope("view")
@Data
public class UserController extends AbstractManagedBean implements InitializingBean {

    @Autowired
    private UserService userService;
    private User user;
    private User userCek;
    private LazyDataModelFilterJPA<User> listUser;
    private List<User> listUser2;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private UserValidator userValidator;

    private boolean isLoginDisabled = true;

    private String dalogHeader;
    private String username;
    private String noHp;
    private String email;

    @Override
    public void afterPropertiesSet() throws Exception {
        listUser2 = new ArrayList<>();
        listUser2 = userRepo.findAllByStatus(User.Status.ACTIVE);
        User user = getCurrentUser();
        System.out.println("user : " + user.getNamaLengkap());
//        listUser = new LazyDataModelFilterJPA(userRepo) {
//            @Override
//            protected Page getDatas(PageRequest request, Map filters) {
//                user.setNamaLengkap((String) filters.get("namaLengkap"));
//                return userRepo.findAll(whereQuery(), request);
//            }
//
//            @Override
//            protected long getDataSize(Map filters) {
//                user.setNamaLengkap((String) filters.get("namaLengkap"));
//                return userRepo.count(whereQuery());
//            }
//        };
    }

//    public Specification<User> whereQuery() {
//        List<Predicate> predicates = new ArrayList<>();
//        return new Specification<User>() {
//            @Override
//            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
//                if (StringUtils.isNotBlank(user.getNamaLengkap())) {
//                    predicates.add(cb.like(cb.lower(root.<String>get("id")),
//                            getLikePattern(user.getNamaLengkap())));
//                }
//                
//                query.orderBy(cb.asc(root.<Integer>get("userId")));
//                return andTogether(predicates, cb);
//            }
//
//        };
//    }
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
    public void register() throws IOException {
        System.out.println("user : " + user);

        String phoneNoPattern = "(62)[\\s\\)\\-]*(\\s|(\\d){3,})";

        if (!user.getNoHp().matches(phoneNoPattern)) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Please enter valid Indonesian phone number", ""));
            return;
        }

//        if (userService.findByUsername(user.getUsername()) != null) {
//            showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Username sudah pernah terdaftar");
//        } else if (userService.findByEmail(user.getEmail()) != null) {
//            showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Email sudah pernah terdaftar");
//        } else {
//            user.setNamaLengkap(user.getNamaDepan().concat(" ").concat(user.getNamaBelakang()));
//            userService.save(user);
//            user = new User();
//            showGrowl(FacesMessage.SEVERITY_INFO, "Success", "Registrasi user berhasil");
//            isLoginDisabled = false;
//            RequestContext.getCurrentInstance().update("idList");
//            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
//        }
//        RequestContext.getCurrentInstance().update("growl");
        try {
            if (dalogHeader.equals("Tambah User")) {
                System.out.println("tambah");
                if (userService.findByUsername(user.getUsername()) != null) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Username sudah pernah terdaftar");
                } else if (userService.findByEmail(user.getEmail()) != null) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Email sudah pernah terdaftar");
                } else {
                    user.setNamaLengkap(user.getNamaDepan().concat(" ").concat(user.getNamaBelakang()));
                    userService.save(user);
                    user = new User();
                    showGrowl(FacesMessage.SEVERITY_INFO, "Success", "Registrasi user berhasil");
                    isLoginDisabled = false;
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
                }
                RequestContext.getCurrentInstance().update("growl");
            } else {
                System.out.println("update");
                if (userService.findByUsername(user.getUsername()) != null && !user.getUsername().equals(username)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Username sudah pernah terdaftar");
                } else if (userService.findByEmail(user.getEmail()) != null && !user.getEmail().equals(email)) {
                    showGrowl(FacesMessage.SEVERITY_WARN, "Warning", "Email sudah pernah terdaftar");
                } else {
                    user.setNamaLengkap(user.getNamaDepan().concat(" ").concat(user.getNamaBelakang()));
                    userService.save(user);
                    user = new User();
                    showGrowl(FacesMessage.SEVERITY_INFO, "Success", "Update user berhasil");
                    isLoginDisabled = false;
                    RequestContext.getCurrentInstance().update("idList");
                    RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
                }
                return;
            }
//            showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil disimpan");
//            RequestContext.getCurrentInstance().update("idList");
//            RequestContext.getCurrentInstance().update("growl");
//            RequestContext.getCurrentInstance().execute("PF('showDialocAct').hide()");
        } catch (Exception e) {
            log.error("error : {}", e);
            showGrowl(FacesMessage.SEVERITY_ERROR, "Peringatan", "Terjadi kesalahan simpan data");
            RequestContext.getCurrentInstance().update("growl");
        }
    }

    public void login() {
        try {
            FacesContext facesContext = FacesContext.getCurrentInstance();
            ExternalContext externalContext = facesContext.getExternalContext();

            RequestDispatcher dispatcher = ((ServletRequest) externalContext.getRequest())
                    .getRequestDispatcher("/j_spring_security_check");
            dispatcher.forward((ServletRequest) externalContext.getRequest(),
                    (ServletResponse) externalContext.getResponse());
            facesContext.responseComplete();

            // check for AuthenticationException in the session
            Map<String, Object> sessionMap = externalContext.getSessionMap();
            Exception e = (Exception) sessionMap.get(WebAttributes.AUTHENTICATION_EXCEPTION);

        } catch (ServletException | IOException ex) {
        }
    }

    public void showDialogAction() {
        userCek = (User) getRequestParam("user");
        user = new User();
        if (userCek == null) {
            System.out.println("tambah");
            dalogHeader = "Tambah User";
        } else {
            System.out.println("update");
            dalogHeader = "Ubah User";
            username = userCek.getUsername();
            noHp = userCek.getNoHp();
            email = userCek.getEmail();
            user = userCek;
        }
        RequestContext.getCurrentInstance().reset("idDialocAct");
        RequestContext.getCurrentInstance().update("idDialocAct");
        RequestContext.getCurrentInstance().execute("PF('showDialocAct').show()");
    }

    public void deleteRecord() throws InterruptedException, IOException {
        userCek = (User) getRequestParam("user");
        System.out.println("userCek : " + userCek);
        userCek.setStatus(User.Status.INACTIVE);
        userRepo.save(userCek);
        showGrowl(FacesMessage.SEVERITY_INFO, "Informasi", "Data berhasil dihapus");
        RequestContext.getCurrentInstance().update("idList");
        RequestContext.getCurrentInstance().update("growl");
        reload();
    }

    public void reload() throws IOException {
        ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
        ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }
}
