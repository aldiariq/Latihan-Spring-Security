package com.aldiariq.latihanspring.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pengguna {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String namaPengguna;
    private String usernamePengguna;
    private String passwordPengguna;
    @ManyToMany(fetch = FetchType.EAGER)
    private Collection<Aksespengguna> aksespenggunas = new ArrayList<>();
}
