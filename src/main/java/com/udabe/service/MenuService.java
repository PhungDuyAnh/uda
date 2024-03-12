package com.udabe.service;

import com.udabe.entity.Menus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public interface MenuService {
    ResponseEntity<?> getAllMenuAndRole();

    ResponseEntity<?> getAllMenu();

    ResponseEntity<?> disableMenu(Long menuID);

    ResponseEntity<?> enableMenu(Long menuID);

    ResponseEntity<?> saveMenu(Menus menu);

    ResponseEntity<?> updateMenu(Menus menu, Long menuID);

    ResponseEntity<?> updateMenuOrder(Menus menus);

    ResponseEntity<?> removeMenuByID(Long menuID);

    ResponseEntity<?> getMenuByMenuID(Long menuID);

    ResponseEntity<?> getMenuByUpperSeq(Long upperSeq);

    ResponseEntity<?> getMenuRoleNameAndPath();
}
