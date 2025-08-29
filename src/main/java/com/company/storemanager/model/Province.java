package com.company.storemanager.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "provinces")
@Data
public class Province {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Boolean isActive = true;

    @OneToMany(mappedBy = "province", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("province")
    private List<Branch> branches;
}