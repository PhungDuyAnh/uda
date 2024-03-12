package com.udabe.entity;


import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;


@Data
@Entity
@Table(name = "access_control", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"menu_id", "role_id"})
})
public class AccessControl implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "access_control_id")
    private Long accessControlId;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menus menu;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

}
