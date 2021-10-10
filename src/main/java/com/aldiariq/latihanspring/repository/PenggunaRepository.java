package com.aldiariq.latihanspring.repository;

import com.aldiariq.latihanspring.model.Pengguna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PenggunaRepository extends JpaRepository<Pengguna, Long> {
    Pengguna findByusernamePengguna(String usernamePengguna);
}
