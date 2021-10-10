package com.aldiariq.latihanspring.service;

import com.aldiariq.latihanspring.model.Aksespengguna;
import com.aldiariq.latihanspring.model.Pengguna;

import java.util.List;

public interface PenggunaService {
    Pengguna simpanPengguna(Pengguna pengguna);

    Aksespengguna simpanAksespengguna(Aksespengguna aksespengguna);
    void aturAksespengguna(String usernamePengguna, String namaAksespengguna);
    Pengguna ambilPengguna(String usernamePengguna);
    List<Pengguna>ambilPenggunas();
}
