package com.udabe.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.udabe.cmmn.base.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

@Data
@Entity
@Table(name = "functionRole")
public class FunctionRole{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "function_role_id")
    private Long functionRoleId;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "dashboard_function_id")
    private DashboardFunction dashboardFunction;
}
