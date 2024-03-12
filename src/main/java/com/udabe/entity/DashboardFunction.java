package com.udabe.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "dashboard_function")
public class DashboardFunction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dashboard_function_id")
    private Long dashboardFunctionId;

    @Column(name = "function_code")
    private String functionCode;

    @Column(name = "title")
    private String title;

    @Column(name = "url")
    private String url;

    @Column(name = "content")
    private String content;

    @Column(name = "api")
    private String api;

    @Column(name = "method")
    private String method;

    @JsonIgnore
    @OneToMany(mappedBy = "dashboardFunction", cascade = CascadeType.ALL)
    private List<UserFunction> userFunctions;

    @JsonIgnore
    @OneToMany(mappedBy = "dashboardFunction", cascade = CascadeType.ALL)
    private List<FunctionRole> functionRoles;

}
