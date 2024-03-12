package com.udabe.socket;

import com.udabe.cmmn.base.BaseEntity;
import com.udabe.entity.Users;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Data
@Entity
@Table(name = "notify")
public class Notify extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notify_id")
    private Long notifyId;

    @Column(name = "notify_content_vi")
    private String notifyContentVi;

    /*  status = T : Đã đọc.
        status = F : Chưa đọc. */
    private boolean status;

    /*  status = F : Không bị ẩn.
        status = T : Bị ẩn. */
    private boolean disable;

    /*Định nghĩa các định tuyến tới FE.*/
    private String link;

    /*true && false*/
    private boolean tempDelete;

    /*  Định nghĩa các user gửi thông báo qua id.*/
    @Transient
    private List<String> userNotifyLst;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users user;

    @Override
    public Long getSeq() {
        return this.notifyId;
    }

    @PrePersist
    public void setDefaultValue() {
        status = false;
        disable = false;
        tempDelete = false;
        ZoneId zone = ZoneId.of("Asia/Ho_Chi_Minh");
        LocalDateTime currentDateTime = LocalDateTime.now(zone);
        createdAt = currentDateTime;
        updatedAt = currentDateTime;
    }

    @PostPersist
    public void haveNewNotify() {
        NotifiServiceImpl.haveNewNotify(this.user.getUserID());
    }

}
