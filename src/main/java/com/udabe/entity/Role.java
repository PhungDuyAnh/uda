package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
@Entity
@Table(name = "role")
@NoArgsConstructor
public class Role implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long roleID;

    /**
     * Tên quyền, định nghĩa qua ENUM.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role_name", length = 20)
    private ERole roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<UserRole> userRoles;

    @JsonIgnore
    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private List<FunctionRole> functionRoles;

    public Role(Long roleID, ERole roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }
}
