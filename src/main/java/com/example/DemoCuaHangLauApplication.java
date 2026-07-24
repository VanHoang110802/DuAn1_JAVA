package com.example;

import com.example.Controller.BanController;
import com.example.Controller.ChiTietHoaDonController;
import com.example.Controller.HoaDonController;
import com.example.Controller.IndexController;
import com.example.Controller.LoginController;
import com.example.Controller.LogoutController;
import com.example.Controller.NguoiDungController;
import com.example.Controller.ThucDonController;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoCuaHangLauApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoCuaHangLauApplication.class, args);
    }

    @Bean
    ServletContextInitializer servletInitializer() {
        return this::registerServlets;
    }

    private void registerServlets(ServletContext context) throws ServletException {
        context.addServlet("indexController", new IndexController()).addMapping("/index");
        context.addServlet("loginController", new LoginController()).addMapping("/login");
        context.addServlet("logoutController", new LogoutController()).addMapping("/logout");
        context.addServlet("banController", new BanController()).addMapping("/ban");
        context.addServlet("thucDonController", new ThucDonController()).addMapping("/thucdon");
        context.addServlet("nguoiDungController", new NguoiDungController()).addMapping("/nguoidung");
        context.addServlet("hoaDonController", new HoaDonController()).addMapping("/hoadon");
        context.addServlet("chiTietHoaDonController", new ChiTietHoaDonController()).addMapping("/chitiethoadon");
    }
}
