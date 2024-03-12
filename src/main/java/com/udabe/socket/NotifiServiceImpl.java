package com.udabe.socket;

import com.udabe.cmmn.exception.ResourceNotFoundException;
import com.udabe.cmmn.util.Constant;
import com.udabe.dto.notifi.NotifyVIDTO;
import com.udabe.entity.CriteriaSet;
import com.udabe.entity.EvaluationVersion;
import com.udabe.entity.Users;
import com.udabe.repository.CriteriaSetRepository;
import com.udabe.repository.EvaluationVersionRepository;
import com.udabe.repository.UsersRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NotifiServiceImpl implements NotifyService {

    private static NotifyRepository notifyRepository;

    private static UsersRepository usersRepository;

    private final ModelMapper modelMapper;

    private static WSService wsService;

    private static CriteriaSetRepository criteriaSetRepository;

    private static EvaluationVersionRepository evaluationVersionRepository;

    @Autowired
    public NotifiServiceImpl(ModelMapper modelMapper, NotifyRepository notifyRepository, UsersRepository usersRepository, WSService wsService, CriteriaSetRepository criteriaSetRepository, EvaluationVersionRepository evaluationVersionRepository) {
        this.modelMapper = modelMapper;
        NotifiServiceImpl.notifyRepository = notifyRepository;
        NotifiServiceImpl.usersRepository = usersRepository;
        this.wsService = wsService;
        this.criteriaSetRepository = criteriaSetRepository;
        this.evaluationVersionRepository = evaluationVersionRepository;
    }

    @Override
    public Object findAll(Long userID, String language) {
        Map<Object, Object> test = new HashMap<>();
        Users userFind = usersRepository.findById(userID).orElseThrow(()->new ResourceNotFoundException("User","Id",userID));
        test.put("countNotify",userFind.getCountNotify());
        test.put("listNotify",notifyRepository.findAllNotifyVIByUser(userID));
        return test;
    }

    @Override
    public Notify save(Notify entity) {
        return notifyRepository.save(entity);
    }

    @Override
    public NotifyVIDTO findById(Long notifyId) {
        Optional<Notify> notifyFind = notifyRepository.findById(notifyId);
        if(notifyFind.isPresent()){
            notifyFind.get().setStatus(true);
            notifyRepository.save(notifyFind.get());
            return notifyRepository.findNotifyById(notifyId);
        }else {
            throw new ResourceNotFoundException("Notify", "Id", notifyId);
        }
    }


    @Override
    public void disable(Long notifyId, String statusDelete) {
        Notify notifyFind = notifyRepository.findById(notifyId).orElseThrow(()->new ResourceNotFoundException("Notify","Id",notifyId));
        if(statusDelete.equals("temp")){
            notifyFind.setTempDelete(true);
        }else if(statusDelete.equals("delete")){
            notifyFind.setTempDelete(false);
            notifyFind.setDisable(true);
        }else if(statusDelete.equals("undo")){
            notifyFind.setTempDelete(false);
            notifyFind.setDisable(false);
        }else {
            return;
        }
        notifyRepository.save(notifyFind);

    }



    @Override
    public void disableAll(Long userId, String statusDelete) {
        if(statusDelete.equals("temp")){
            List<NotifyVIDTO> lst = notifyRepository.findAllNotifyVIByUser(userId);
            List<Notify> lstSave = new ArrayList<>();
            lst.forEach(x->{
                Notify notifyFind = notifyRepository.findById(x.getNotifyId()).get();
                notifyFind.setDisable(true);
                notifyFind.setTempDelete(true);
                lstSave.add(notifyFind);
            });
            notifyRepository.saveAll(lstSave);
        }else if(statusDelete.equals("delete")){
            List<NotifyVIDTO> lst = notifyRepository.findAllNotifyToDisableAll(userId);
            List<Notify> lstSave = new ArrayList<>();
            lst.forEach(x->{
                Notify notifyFind = notifyRepository.findById(x.getNotifyId()).get();
                notifyFind.setDisable(true);
                notifyFind.setTempDelete(false);
                lstSave.add(notifyFind);
            });
            notifyRepository.saveAll(lstSave);
        }else if(statusDelete.equals("undo")){
            List<NotifyVIDTO> lst = notifyRepository.findAllToUndo(userId);
            List<Notify> lstUndo = new ArrayList<>();
            lst.forEach(x->{
                Notify notifyFind = notifyRepository.findById(x.getNotifyId()).get();
                notifyFind.setDisable(false);
                notifyFind.setTempDelete(false);
                lstUndo.add(notifyFind);
            });
            notifyRepository.saveAll(lstUndo);
        }else {
            return;
        }
    }


    @Override
    public void checkNotify(Long userId) {
        notifyRepository.checkNotify(userId);
    }


    @Override
    public void markAllRead(Long userId) {
        notifyRepository.markAllRead(userId);
    }

    @Override
    public void markAsRead(Long notifyId, boolean status) {
        notifyRepository.markAsRead(notifyId, status);
    }


    public static void haveNewNotify(Long userId){
        Users userFind = usersRepository.findById(userId).orElseThrow(()->new ResourceNotFoundException("User","Id",userId));
        userFind.setCountNotify(userFind.getCountNotify() + 1);
        usersRepository.save(userFind);
    }


    /*HÀM THÔNG BÁO CHO CẬP NHẬT PHIÊN BẢN BỘ CHỈ SỐ:*/
    public static void printMessageUpdateApplied(Long criteriaSetIdApply, Long criteriaSetIdRefuse, String typeUpdate){
        CriteriaSet criteriaSetApplied = criteriaSetRepository.findById(criteriaSetIdApply).get();
        List<Users> usersList = usersRepository.findUserActive();
        List<Notify> listTemp = new ArrayList<>();
        for(Users i : usersList) {
            Notify notify = new Notify();
            if(typeUpdate.equals("applied")) {
                notify.setNotifyContentVi(String.format(Constant.criteriaSetApplied, criteriaSetApplied.getCriteriaSetCode(), criteriaSetApplied.getCriteriaSetName(), criteriaSetApplied.getCriteriaVersion()));
            } else {
                CriteriaSet criteriaSetRefuse = criteriaSetRepository.findById(criteriaSetIdRefuse).get();
                notify.setNotifyContentVi(String.format(Constant.criteriaSetRefused, criteriaSetRefuse.getCriteriaSetCode(), criteriaSetRefuse.getCriteriaSetName(), criteriaSetRefuse.getCriteriaVersion(),
                criteriaSetApplied.getCriteriaSetCode(), criteriaSetApplied.getCriteriaSetName(), criteriaSetApplied.getCriteriaVersion()));
            }
            notify.setUser(usersRepository.findById(i.getUserID()).get());
            notify.setUserNotifyLst(Arrays.asList(i.getUserID().toString()));
            notify.setLink(Constant.criteriaSetVerLink);
            listTemp.add(notify);
            wsService.notifyUser(notify);
        }
        notifyRepository.saveAll(listTemp);
    }


    /*HÀM THÔNG BÁO ĐĂNG KÝ ĐÁNH GIÁ:*/
    public static void printMessageUpdateScore(Long evaluationVersionId) {
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId).get();
        Users usersFind = evaluationVersion.getUsers();
        List<Notify> listTemp = new ArrayList<>();
        /*THÔNG BÁO ĐĂNG KÝ ĐÁNH GIÁ THÀNH CÔNG:*/
        Notify notify = new Notify();
        notify.setNotifyContentVi(String.format(Constant.criteriaSetAppliedSuccess, evaluationVersion.getVersionName()));
        notify.setUser(usersFind);
        notify.setUserNotifyLst(Arrays.asList(usersFind.getUserID().toString()));
        notify.setLink(Constant.statusUrbanLink);
        listTemp.add(notify);
        wsService.notifyUser(notify);

        /*THÔNG BÁO KHI HỆ THỐNG TỰ ĐỘNG ĐÁNH GIÁ THÀNH CÔNG VÀ CẬP NHẬT TIẾN TRÌNH ĐIỂM.*/
        if(evaluationVersion.getSumPercent() != null && evaluationVersion.getSumScore() != null) {
            Notify notifyScore = new Notify();
            notifyScore.setNotifyContentVi(String.format(Constant.caculateScoreSuccess, evaluationVersion.getVersionName()));
            notifyScore.setUser(usersFind);
            notifyScore.setUserNotifyLst(Arrays.asList(usersFind.getUserID().toString()));
            notifyScore.setLink(Constant.statusUrbanLink);
            listTemp.add(notifyScore);
            wsService.notifyUser(notifyScore);
        }
        List<Users> allChairManAndSecretary = usersRepository.getChairManAndSecretary();

        /*Thông báo khi bản đăng ký đánh giá đạt 100%/điểm và gửi lên Hội đồng*/
        if(evaluationVersion.getSumScore() == 100 && evaluationVersion.getSumPercent() == 100) {
            /*Thông báo khi có bản đăng ký đánh giá của đô thị đạt điều kiện (100%) và gửi lên hội đồng*/
            for(Users i : allChairManAndSecretary) {
                Notify notifyScore100 = new Notify();
                notifyScore100.setNotifyContentVi(String.format(Constant.complete100AndSendToCouncil, evaluationVersion.getUsers().getFullName(), evaluationVersion.getVersionName()));
                notifyScore100.setUser(usersRepository.findById(i.getUserID()).get());
                notifyScore100.setUserNotifyLst(Arrays.asList(i.getUserID().toString()));
                notifyScore100.setLink(Constant.listEvaluateUrban);
                listTemp.add(notifyScore100);
                wsService.notifyUser(notifyScore100);
            }
            Notify notifyScore100Urban = new Notify();
            notifyScore100Urban.setNotifyContentVi(String.format(Constant.caculateScore100, evaluationVersion.getVersionName()));
            notifyScore100Urban.setUser(usersFind);
            notifyScore100Urban.setUserNotifyLst(Arrays.asList(usersFind.getUserID().toString()));
            notifyScore100Urban.setLink(Constant.statusUrbanLink);
            listTemp.add(notifyScore100Urban);
            wsService.notifyUser(notifyScore100Urban);
        }
        notifyRepository.saveAll(listTemp);
    }



    /*Thông báo khi hội đồng công nhận hoặc từ chối bản đăng ký đánh giá và cập nhật tiến trình, điểm, trạng thái đô thị*/
    public static void printMessageRecognitionCouncil(String versionName, Long userID, String typeRecognition) {
        Users usersFind = usersRepository.findById(userID).orElseThrow(() -> new ResourceNotFoundException("User", "ID", userID));
        List<Notify> listTemp = new ArrayList<>();
        //Thông báo khi hội đồng công nhận bản đăng ký đánh giá và cập nhật tiến trình, điểm, trạng thái đô thị
        Notify notify = new Notify();
        if(typeRecognition.equals("applied")) {
            notify.setNotifyContentVi(String.format(Constant.recognitionCouncilAgree, versionName));
        } else {
            notify.setNotifyContentVi(String.format(Constant.recognitionCouncilRefuse, versionName));
        }
        notify.setUser(usersFind);
        notify.setUserNotifyLst(Arrays.asList(usersFind.getUserID().toString()));
        notify.setLink(Constant.receiveResultLink);
        listTemp.add(notify);
        wsService.notifyUser(notify);
        notifyRepository.saveAll(listTemp);
    }



    /*HÀM THÔNG BÁO THAY ĐỔI THÀNH VIÊN VÀO HỘI ĐỒNG:*/
    public static void councilNotify(List<Users> usersList, String status){
        List<Notify> listTemp = new ArrayList<>();
        for(Users i : usersList) {
            Notify notify = new Notify();
            String roleCouncil;
            if(i.getCouncilType() == 1L) {
                roleCouncil = "Chủ tịch hội đồng";
            } else if(i.getCouncilType() == 2L) {
                roleCouncil = "Thư kí hội đồng";
            } else {
                roleCouncil = "Thành viên hội đồng";
            }
            if(status.equals("add")) {
                notify.setNotifyContentVi(String.format(Constant.addToCouncil, roleCouncil));
            } else if (status.equals("updateRole")) {
                notify.setNotifyContentVi(String.format(Constant.updateRoleCouncil, roleCouncil));
            } else if(status.equals("disable")) {
                notify.setNotifyContentVi(String.format(Constant.disableCouncilAcc));
            } else {
                notify.setNotifyContentVi(String.format(Constant.enableCouncilAcc));
            }
            notify.setUser(usersRepository.findById(i.getUserID()).get());
            notify.setUserNotifyLst(Arrays.asList(i.getUserID().toString()));
            notify.setLink(null);
            listTemp.add(notify);
            wsService.notifyUser(notify);
        }
        notifyRepository.saveAll(listTemp);
    }


    /*HÀM THÔNG BÁO VỀ HỘI ĐỒNG ĐÁNH GIÁ:*/
    public static void addCouncilMark(List<Users> usersList, String type, Long evaluationVersionId){
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId)
        .orElseThrow(() -> new ResourceNotFoundException("EvaluationVersion", "Id", evaluationVersionId));
        List<Notify> listTemp = new ArrayList<>();
        for(Users i : usersList) {
            Notify notify = new Notify();
            if(type.equals("add")) {
                notify.setNotifyContentVi(String.format(Constant.addToCouncilScore, evaluationVersion.getUsers().getFullName(), evaluationVersion.getVersionName()));
            } else {
                notify.setNotifyContentVi(String.format(Constant.deleteToCouncilScore, evaluationVersion.getUsers().getFullName(), evaluationVersion.getVersionName()));
            }
            notify.setUser(usersRepository.findById(i.getUserID()).get());
            notify.setUserNotifyLst(Arrays.asList(i.getUserID().toString()));
            notify.setLink(null);
            listTemp.add(notify);
            wsService.notifyUser(notify);
        }
        notifyRepository.saveAll(listTemp);
    }


    /*Hàm Thông báo khi tất cả thành viên hội đồng đánh giá hoàn tất*/
    public static void allCouncilMemCompleteScore(Long evaluationVersionId){
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId)
        .orElseThrow(() -> new ResourceNotFoundException("EvaluationVersion", "Id", evaluationVersionId));
        List<Notify> listTemp = new ArrayList<>();
        List<Users> allChairManAndSecretary = usersRepository.getChairManAndSecretary();
        for(Users i : allChairManAndSecretary) {
            Notify notify = new Notify();
            notify.setNotifyContentVi(String.format(Constant.completeAllCouncilScore, evaluationVersion.getVersionName(), evaluationVersion.getUsers().getFullName()));
            notify.setUser(usersRepository.findById(i.getUserID()).get());
            notify.setUserNotifyLst(Arrays.asList(i.getUserID().toString()));
            notify.setLink(Constant.listEvaluateUrban);
            listTemp.add(notify);
            wsService.notifyUser(notify);
        }
        notifyRepository.saveAll(listTemp);
    }

}
