package com.aldiariq.latihanspring.controller;

import com.aldiariq.latihanspring.model.Aksespengguna;
import com.aldiariq.latihanspring.model.Pengguna;
import com.aldiariq.latihanspring.service.PenggunaService;
import com.aldiariq.latihanspring.utils.UsernamenamaAksespengguna;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PenggunaController {
    private final PenggunaService penggunaService;

    @GetMapping("/penggunas")
    public ResponseEntity<List<Pengguna>>ambilPenggunas(){
        return ResponseEntity.ok().body(penggunaService.ambilPenggunas());
    }

    @PostMapping("/pengguna/tambah")
    public ResponseEntity<Pengguna>simpanPengguna(@RequestBody Pengguna pengguna){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/pengguna/tambah").toUriString());
        return ResponseEntity.created(uri).body(penggunaService.simpanPengguna(pengguna));
    }

    @PostMapping("/aksespengguna/tambah")
    public ResponseEntity<?>aturAksespengguna(@RequestBody UsernamenamaAksespengguna usernamenamaAksespengguna){
        penggunaService.aturAksespengguna(usernamenamaAksespengguna.getUsernamePengguna(), usernamenamaAksespengguna.getNamaAksespengguna());
        return ResponseEntity.ok().build();
    }
}
