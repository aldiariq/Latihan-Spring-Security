package com.aldiariq.latihanspring.repository;

import com.aldiariq.latihanspring.model.Aksespengguna;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AksespenggunaRepository extends JpaRepository<Aksespengguna, Long> {
    Aksespengguna findBynamaAksespengguna(String namaAksespengguna);
}
