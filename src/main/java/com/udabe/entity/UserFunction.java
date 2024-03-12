package com.udabe.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_function")
public class UserFunction{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_function_id")
    private Long userFunctionId;

    @Transient
    private Long userId;

    @Transient
    private Long dashboardFunctionId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;

    @Column(name = "body_config")
    private String bodyConfig;

    /**
     * Loại thống kê:
     * null: chưa chọn
     * list: danh sách
     * chart: biểu đồ
     */
    @Column(name = "type")
    private String type;

    @ManyToOne
    @JoinColumn(name = "dashboard_function_id")
    private DashboardFunction dashboardFunction;
}
