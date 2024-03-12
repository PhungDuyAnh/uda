package com.udabe.socket;


import com.udabe.dto.notifi.NotifyVIDTO;

public interface NotifyService {

    Object findAll(Long userId, String language);

    Notify save(Notify entity);

    NotifyVIDTO findById(Long notifyId);

    void disable(Long notifyId, String statusDelete);

    void disableAll(Long userId, String statusDelete);

    void checkNotify(Long userId);

    void markAllRead(Long userId);

    void markAsRead(Long notifyId, boolean status);
}
