package com.udabe.socket;

import com.udabe.dto.notifi.NotifyVIDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface NotifyRepository extends JpaRepository<Notify, Long> {


    @Query("select new com.udabe.dto.notifi.NotifyVIDTO(" +
            "n.notifyId, n.notifyContentVi, n.status, n.disable, n.link, n.createdAt, n.updatedAt, u.userID, n.tempDelete" +
            ")" +
            "from Notify n left join n.user u where u.userID = ?1 and n.disable = FALSE ORDER BY n.notifyId DESC")
    List<NotifyVIDTO> findAllNotifyVIByUser(Long userId);


    @Query("select new com.udabe.dto.notifi.NotifyVIDTO(" +
            "n.notifyId, n.notifyContentVi, n.status, n.disable, n.link, n.createdAt, n.updatedAt, u.userID, n.tempDelete" +
            ")" +
            "from Notify n left join n.user u where u.userID = ?1 and n.disable = TRUE and n.tempDelete = TRUE ORDER BY n.notifyId DESC")
    List<NotifyVIDTO> findAllToUndo(Long userId);


    @Query("select new com.udabe.dto.notifi.NotifyVIDTO(" +
            "n.notifyId, n.notifyContentVi, n.status, n.disable, n.link, n.createdAt, n.updatedAt, u.userID, n.tempDelete" +
            ")" +
            "from Notify n left join n.user u where u.userID = ?1 and n.disable = TRUE and n.tempDelete = TRUE ORDER BY n.notifyId DESC")
    List<NotifyVIDTO> findAllNotifyToDisableAll(Long userId);


    @Query("select new com.udabe.dto.notifi.NotifyVIDTO(" +
            "n.notifyId, n.notifyContentVi, n.status, n.disable, n.link, n.createdAt, n.updatedAt, n.user.userID, n.tempDelete" +
            ")" +
            "from Notify n where n.notifyId = ?1")
    NotifyVIDTO findNotifyById(Long userId);



    //MarkAllRead
    @Modifying
    @Transactional
    @Query("update Notify n set n.status = true where n.user.userID = :userId")
    void markAllRead(@Param("userId") Long userId);


    //MarkAsRead
    @Modifying
    @Transactional
    @Query("update Notify n set n.status = :status where n.notifyId = :notifyId")
    void markAsRead(@Param("notifyId") Long notifyId, @Param("status") boolean status);


    //MarkAllRead:
    @Modifying
    @Transactional
    @Query("update Users u set u.countNotify = 0 where u.userID = :userId")
    void checkNotify(@Param("userId") Long userId);


    @Query("select new com.udabe.dto.notifi.NotifyVIDTO(" +
            "n.notifyId, n.notifyContentVi, n.status, n.disable, n.link, n.createdAt, n.updatedAt, u.userID, n.tempDelete) " +
            "FROM Notify n LEFT JOIN n.user u " +
            "WHERE n.disable = TRUE AND FUNCTION('DATEDIFF', CURRENT_DATE, n.updatedAt) >= 30")
    List<NotifyVIDTO> findAllToDelete();


}
