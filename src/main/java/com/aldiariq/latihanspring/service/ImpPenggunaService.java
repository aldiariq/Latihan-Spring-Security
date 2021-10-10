package com.aldiariq.latihanspring.service;

import com.aldiariq.latihanspring.model.Aksespengguna;
import com.aldiariq.latihanspring.model.Pengguna;
import com.aldiariq.latihanspring.repository.AksespenggunaRepository;
import com.aldiariq.latihanspring.repository.PenggunaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ImpPenggunaService implements PenggunaService, UserDetailsService {
    private final PenggunaRepository penggunaRepository;
    private final AksespenggunaRepository aksespenggunaRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String usernamePengguna) throws UsernameNotFoundException {
        Pengguna pengguna = penggunaRepository.findByusernamePengguna(usernamePengguna);
        if (pengguna == null){
            log.error("Pengguna tidak ditemukan");
            throw new UsernameNotFoundException("Pengguna tidak ditemukan");
        }else {
            log.info("Pengguna dengan username " +usernamePengguna+" ditemukan");
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        pengguna.getAksespenggunas().forEach(aksespengguna -> {
            authorities.add(new SimpleGrantedAuthority(aksespengguna.getNamaAksespengguna()));
        });
        return new org.springframework.security.core.userdetails.User(pengguna.getUsernamePengguna(), pengguna.getPasswordPengguna(), authorities);
    }

    @Override
    public Pengguna simpanPengguna(Pengguna pengguna) {
        log.info("Menyimpan pengguna dengan username " + pengguna.getUsernamePengguna());
        pengguna.setPasswordPengguna(passwordEncoder.encode(pengguna.getPasswordPengguna()));
        return penggunaRepository.save(pengguna);
    }

    @Override
    public Aksespengguna simpanAksespengguna(Aksespengguna aksespengguna) {
        log.info("Menyimpan akses pengguna dengan nama " + aksespengguna.getNamaAksespengguna());
        return aksespenggunaRepository.save(aksespengguna);
    }

    @Override
    public void aturAksespengguna(String usernamePengguna, String namaAksespengguna) {
        log.info("Mengubah akses pengguna dengan username " + usernamePengguna + " dan dengan akses " + namaAksespengguna);
        Pengguna pengguna = penggunaRepository.findByusernamePengguna(usernamePengguna);
        Aksespengguna aksespengguna = aksespenggunaRepository.findBynamaAksespengguna(namaAksespengguna);
        pengguna.getAksespenggunas().add(aksespengguna);
    }

    @Override
    public Pengguna ambilPengguna(String usernamePengguna) {
        log.info("Mengambil data pengguna dengan username " + usernamePengguna);
        return penggunaRepository.findByusernamePengguna(usernamePengguna);
    }

    @Override
    public List<Pengguna> ambilPenggunas() {
        log.info("Mengambil semua data pengguna");
        return penggunaRepository.findAll();
    }
}
