package com.aldiariq.latihanspring;

import com.aldiariq.latihanspring.model.Aksespengguna;
import com.aldiariq.latihanspring.model.Pengguna;
import com.aldiariq.latihanspring.service.PenggunaService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@SpringBootApplication
public class LatihanspringApplication {

	public static void main(String[] args) {
		SpringApplication.run(LatihanspringApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner runner(PenggunaService penggunaService){
		return args -> {
			penggunaService.simpanAksespengguna(new Aksespengguna(null, "ADMIN"));
			penggunaService.simpanAksespengguna(new Aksespengguna(null, "MANAGER"));
			penggunaService.simpanAksespengguna(new Aksespengguna(null, "STAFF"));

			penggunaService.simpanPengguna(new Pengguna(null, "AKUN ADMIN", "admin", "12345678", new ArrayList<>()));
			penggunaService.simpanPengguna(new Pengguna(null, "AKUN MANAGER", "manager", "12345678", new ArrayList<>()));
			penggunaService.simpanPengguna(new Pengguna(null, "AKUN STAFF", "staff", "12345678", new ArrayList<>()));

			penggunaService.aturAksespengguna("admin", "ADMIN");
			penggunaService.aturAksespengguna("admin", "MANAGER");
			penggunaService.aturAksespengguna("admin", "STAFF");
			penggunaService.aturAksespengguna("manager", "MANAGER");
			penggunaService.aturAksespengguna("manager", "STAFF");
			penggunaService.aturAksespengguna("staff", "STAFF");
		};
	}

}
