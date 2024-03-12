package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.dto.Dashboard.UserFunctionDashboardDTO;
import com.udabe.entity.UserFunction;
import com.udabe.repository.DashboardFunctionRepository;
import com.udabe.repository.UserFunctionRepository;
import com.udabe.repository.UsersRepository;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.UserFunctionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserFunctionServiceImpl extends BaseCrudService<UserFunction, Long> implements UserFunctionService {

    private final UserFunctionRepository userFunctionRepository;

    private final UsersRepository usersRepository;

    private final DashboardFunctionRepository DashboardFunctionRepository;

    private final DashboardServiceImpl DashboardServiceImpl;

    public UserFunctionServiceImpl(UserFunctionRepository userFunctionRepository, UsersRepository usersRepository, DashboardFunctionRepository DashboardFunctionRepository, DashboardServiceImpl DashboardServiceImpl) {
        this.userFunctionRepository = userFunctionRepository;
        this.usersRepository = usersRepository;
        this.DashboardFunctionRepository = DashboardFunctionRepository;
        this.DashboardServiceImpl = DashboardServiceImpl;
    }

    @Override
    public ResponseEntity<?> addUserFunction(Long[] DashboardFunctionId) {
        Long userID = getUserLoginId();
        Long[] DashboardFunctionIdOld = userFunctionRepository.findUserFunctionIdByUserID(userID);
        Arrays.sort(DashboardFunctionId);
        Arrays.sort(DashboardFunctionIdOld);

        List<Long> DashboardFunctionIdList = new ArrayList<>(Arrays.asList(DashboardFunctionId));
        List<Long> DashboardFunctionIdOldList = new ArrayList<>(Arrays.asList(DashboardFunctionIdOld));
//        List<Long> DashboardFunctionIdOldList = Arrays.asList(DashboardFunctionIdOld);

        int index_a = 0, index_b = 0;
        while (index_a < DashboardFunctionIdList.size() && index_b < DashboardFunctionIdOldList.size()) {
            if (DashboardFunctionIdList.get(index_a) == DashboardFunctionIdOldList.get(index_b)) {
                DashboardFunctionIdList.remove(index_a);
                DashboardFunctionIdOldList.remove(index_b);
            } else if (DashboardFunctionIdList.get(index_a) > DashboardFunctionIdOldList.get(index_b))
                index_b++;
            else index_a++;
        }

        int i = 0;
        while (i < DashboardFunctionIdOldList.size()) {
            userFunctionRepository.deleteUserFunction(userID, DashboardFunctionIdOldList.get(i));
            i++;
        }
        i = 0;
        while (i < DashboardFunctionIdList.size()) {
            userFunctionRepository.saveUserFunction(userID, DashboardFunctionIdList.get(i));
            i++;
        }
        return ResponseEntity.status(HttpStatus.OK).body("Successfully!");
    }


    @Override
    public ResponseEntity<?> deleteUserFunction(Long dashboardFunctionId) {
        Long userID = getUserLoginId();

        userFunctionRepository.deleteUserFunctionByUserFunctionId(dashboardFunctionId, userID);
        return ResponseEntity.ok("Deleted");
    }


    @Override
    public ResponseEntity<?> getUserFunctionData() {

        List<Long> getRoleByUserId = userFunctionRepository.getListFunctionIdByUserId(getUserLoginId());
        List<Object> listTemp = new ArrayList<>();
        Map<Object, Object> mapResult = new HashMap<>();
        for (Long i : getRoleByUserId) {
            switch (i.toString()) {
                case "1":
                    String getFunctionName = userFunctionRepository.getFunctionName(i);
                    listTemp.add(DashboardServiceImpl.userListDashboard());
                    mapResult.put(getFunctionName, listTemp);
                    break;
            }
        }
        return ResponseEntity.ok(mapResult);
    }

    public static Long getUserLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }


    @Override
    public ResponseEntity<?> saveBodyConfig(UserFunctionDashboardDTO userFunctionDTO) {
        Long userID = getUserLoginId();
        Long userFunctionId = userFunctionRepository.findUserFunction(userID, userFunctionDTO.getDashboardFunctionId());
        if (userFunctionId != null) {
             userFunctionRepository.saveUserFunctionBodyConfig(userFunctionId,userID, userFunctionDTO.getDashboardFunctionId(),userFunctionDTO.getBodyConfig());
            return ResponseEntity.status(HttpStatus.OK).body("Successfully!");
        }

        return ResponseEntity.badRequest().body("Fail!");
    }

    @Override
    public ResponseEntity<?> updateType(Long userFunctionId ,UserFunction userFunction) {
        UserFunction userFunctionFind = userFunctionRepository.findById(userFunctionId).get();
        if(userFunctionFind != null){
            userFunctionFind.setType(userFunction.getType());
            userFunctionRepository.save(userFunctionFind);
            return ResponseEntity.ok("Success");
        }
        else{
            return ResponseEntity.badRequest().body("Null value");
        }
    }
}
