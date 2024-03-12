package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.ParamMap;
import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.exception.ResourceNotFoundException;
import com.udabe.cmmn.util.Constant;
import com.udabe.cmmn.util.TimeZoneUtils;
import com.udabe.cmmn.util.ValidateUtilCriteria;
import com.udabe.dto.criteria.CriteriaReceiveDTO;
import com.udabe.dto.criteria.CriteriaReceiveDTO2;
import com.udabe.dto.criteria.CriteriaSetDTO;
import com.udabe.dto.criteria.form.CriteriaClass2DTOForm;
import com.udabe.dto.criteria.form.CriteriaSetDTOForm;
import com.udabe.dto.criteria.receive.CriteriaSetDTOFormReceive;
import com.udabe.dto.criteria.receive.EvaluationReceiveDTO;
import com.udabe.entity.*;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.*;
import com.udabe.security.service.UserDetailsImpl;
import com.udabe.service.CriteriaSetService;

import com.udabe.socket.NotifiServiceImpl;
import com.udabe.socket.Notify;
import com.udabe.socket.NotifyRepository;
import com.udabe.socket.WSService;
import org.apache.commons.lang.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CriteriaSetServiceImpl extends BaseCrudService<CriteriaSet, Long> implements CriteriaSetService {

    private static CriteriaSetRepository criteriaSetRepository;

    private final CriteriaClass1Repository criteriaClass1Repository;

    private final CriteriaClass2Repository criteriaClass2Repository;

    private static CriteriaDetailRepository criteriaDetailRepository;

    private final ModelMapper modelMapper;

    private static EvaluationVersionRepository evaluationVersionRepository;

    private static UsersRepository usersRepository;

    private static EvaluationVersionUserRepository evaluationVersionUserRepository;

    private static CouncilScoreRepository councilScoreRepository;

    private static AnswerRepository answerRepository;


    @Autowired
    public CriteriaSetServiceImpl(CriteriaSetRepository criteriaSetRepository,
                                  CriteriaClass1Repository criteriaClass1Repository,
                                  CriteriaClass2Repository criteriaClass2Repository,
                                  CriteriaDetailRepository criteriaDetailRepository, ModelMapper modelMapper,
                                  EvaluationVersionRepository evaluationVersionRepository,
                                  UsersRepository usersRepository,
                                  EvaluationVersionUserRepository evaluationVersionUserRepository,
                                  CouncilScoreRepository councilScoreRepository,
                                  AnswerRepository answerRepository
    ) {
        this.criteriaSetRepository = criteriaSetRepository;
        this.criteriaClass1Repository = criteriaClass1Repository;
        this.criteriaClass2Repository = criteriaClass2Repository;
        this.criteriaDetailRepository = criteriaDetailRepository;
        this.modelMapper = modelMapper;
        this.evaluationVersionRepository = evaluationVersionRepository;
        this.usersRepository = usersRepository;
        this.evaluationVersionUserRepository = evaluationVersionUserRepository;
        this.councilScoreRepository = councilScoreRepository;
        this.answerRepository = answerRepository;
        super.setRepository(criteriaSetRepository);
    }


    @Override
    public ResponseEntity<?> saveCriteriaSet(CriteriaSet criteriaSet) {
        // Tạo CriteriaSet:
        criteriaSet.setAppliedStatus("N");
        CriteriaSet criteriaSetSave = criteriaSetRepository.save(criteriaSet);

        // Tạo CriteriaClass1:
        CriteriaClass1 criteriaClass1Save = new CriteriaClass1();
        criteriaClass1Save.setContentVi(null);
        criteriaClass1Save.setCriteriaSet(criteriaSetSave);
        CriteriaClass1 criteriaClass1Reponse = criteriaClass1Repository.save(criteriaClass1Save);


        // Trả về dữ liệu dạng map:
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("criteriaSetSave", criteriaSetSave);
        responseData.put("criteriaClass1Reponse", criteriaClass1Reponse);
        return ResponseEntity.ok(responseData);
    }


    @Override
    public ResponseEntity<?> findCriteriaSetById(Long criteriaSetId) {
        Optional<CriteriaSetDTO> result = criteriaSetRepository.findCriteriaSetById(criteriaSetId);
        if (result.isPresent()) {
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + criteriaSetId));
        }
    }


    @Override
    public ResponseEntity<?> updateCriteriaSet(CriteriaSet criteriaSet, Long criteriaSetId) {
        Optional<CriteriaSet> criteriaSetFind = criteriaSetRepository.findById(criteriaSetId);
        if (criteriaSetFind.isPresent()) {
            if (!criteriaSetFind.get().getAppliedStatus().equals("N")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Cannot update criteria applied!");
            }
//            String[] criteriaSetCode = criteriaSetRepository.findByCriteriaSetCode(criteriaSet.getCriteriaSetCode(), criteriaSetId);
//            if (criteriaSetCode.length > 0) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Criteria code already exists!"));
//            }
            criteriaSetRepository.updateCriteriaSet(criteriaSet.getCriteriaSetCode(), criteriaSet.getCriteriaSetName(), criteriaSetId);
            return ResponseEntity.ok("Update successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + criteriaSetId));
        }
    }


    public ResponseEntity<?> paging(CriteriaSet entity, Pageable pageable) {
        if (pageable.getSort() == Sort.unsorted()) {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "createdAt");
            Sort sort = Sort.by(order);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        ParamMap param = ParamMap.init(entity, pageable);
        String criteriaSetCode = param.getString("criteriaSetCode");
        String criteriaSetName = param.getString("criteriaSetName");
        String appliedStatus = param.getString("appliedStatus");
        String createdAt = param.getString("searchCreatedAt");
        LocalDateTime timeMinOfDay = null;
        LocalDateTime timeMaxOfDay = null;
        if (!StringUtils.isEmpty(createdAt)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeMinOfDay = LocalDate.parse(createdAt, formatter).atStartOfDay();
            timeMaxOfDay = LocalDate.parse(createdAt, formatter).atTime(LocalTime.MAX);
        }
        Page<CriteriaSetDTO> dataList = criteriaSetRepository.selectCriteriaSetPage(criteriaSetCode, criteriaSetName, appliedStatus, timeMinOfDay, timeMaxOfDay, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("criteriaSets", dataList.toList());
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> versionCriteriaFilter(CriteriaSet entity, Pageable pageable) {
        if (pageable.getSort() == Sort.unsorted()) {
            Sort.Order order = new Sort.Order(Sort.Direction.DESC, "updatedAt");
            Sort sort = Sort.by(order);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        ParamMap param = ParamMap.init(entity, pageable);
        String criteriaSetCode = param.getString("criteriaSetCode");
        String criteriaSetName = param.getString("criteriaSetName");
        String appliedStatus = param.getString("appliedStatus");
        String criteriaVersion = param.getString("criteriaVersion");
        String createdAt = param.getString("searchDate");
        LocalDateTime timeMinOfDay = null;
        LocalDateTime timeMaxOfDay = null;
        if (!StringUtils.isEmpty(createdAt)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeMinOfDay = LocalDate.parse(createdAt, formatter).atStartOfDay();
            timeMaxOfDay = LocalDate.parse(createdAt, formatter).atTime(LocalTime.MAX);
        }

        Page<CriteriaSetDTO> dataList = criteriaSetRepository.versionCriteriaSetPage(criteriaSetCode, criteriaSetName,
                appliedStatus, criteriaVersion, timeMinOfDay, timeMaxOfDay, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("versionCriteria", dataList.toList());
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> selectAllDraft() {
        List<CriteriaSetDTO> criteriaSets = criteriaSetRepository.findByAppliedStatus("N");
        return ResponseEntity.ok(criteriaSets);
    }


    @Override
    public ResponseEntity<?> checkNullForm(Long criteriaSetId) {
        CriteriaSet criDraft = criteriaSetRepository.findById(criteriaSetId).orElseThrow(() -> new RuntimeException("criteriaVersion is not found with ID: " + criteriaSetId));
        if (ValidateUtilCriteria.validateApplied(criDraft) == false) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Criteria form must not be null!");
        } else {
            return ResponseEntity.status(HttpStatus.OK).body("Successfully!");
        }
    }


    @Override
    public ResponseEntity<?> updateStatusApplied(Long criDraftId, String criteriaVersion) {
        CriteriaSet criDraft = criteriaSetRepository.findById(criDraftId).orElseThrow(() -> new RuntimeException("criteriaVersion is not found with ID: " + criDraftId));
        if (criteriaSetRepository.findByCriteriaVersion(criteriaVersion) != null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Version already exists"));
        }
        criDraft.setCriteriaVersion(criteriaVersion);
        criteriaSetRepository.updateStatusApplied();
        criDraft.setAppliedStatus("Y");
        ZoneId zone = ZoneId.of(TimeZoneUtils.timeZoneMap.get("VST"));
        LocalDateTime currentDateTime = LocalDateTime.now(zone);
        criDraft.setAppliedDate(currentDateTime);
        CriteriaSet criteriaSetApplied = criteriaSetRepository.save(criDraft);
        NotifiServiceImpl.printMessageUpdateApplied(criteriaSetApplied.getCriteriaSetId(), null, "applied");
        return ResponseEntity.ok("successfully");
    }


    @Override
    public ResponseEntity<?> updateStatusRecall(Long criDraftId, String criteriaVersion) {
        CriteriaSet criDraft = criteriaSetRepository.findById(criDraftId).orElseThrow(() -> new RuntimeException("criteriaVersion is not found with ID: " + criDraftId));
        if (criteriaSetRepository.findByCriteriaVersion(criteriaVersion) != null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Version already exists"));
        }
        criDraft.setCriteriaVersion(criteriaVersion);
        Long refuseId = criteriaSetRepository.getCriteriaSetIdApply();
        criteriaSetRepository.updateStatusRecall();
        criDraft.setAppliedStatus("Y");
        ZoneId zone = ZoneId.of(TimeZoneUtils.timeZoneMap.get("VST"));
        LocalDateTime currentDateTime = LocalDateTime.now(zone);
        criDraft.setAppliedDate(currentDateTime);
        CriteriaSet criteriaSetSave = criteriaSetRepository.save(criDraft);
        NotifiServiceImpl.printMessageUpdateApplied(criteriaSetSave.getCriteriaSetId(), refuseId, "refuse");
        return ResponseEntity.ok("successfully");
    }


    public static void division(Long criteriaDetailId, String type) {
        Optional<CriteriaDetail> entity = criteriaDetailRepository.findById(criteriaDetailId);
        if (entity.isPresent()) {
            Long criteriaSetId = entity.get().getCriteriaClass3().getCriteriaClass2().getCriteriaClass1().getCriteriaSet().getCriteriaSetId();
            double totalClassDetailCount = criteriaSetRepository.getTotalClassDetail(criteriaSetId);
            if (totalClassDetailCount == 0) {
                return;
            }
            if(type.equals("add")) {
                double result = 100.0 / totalClassDetailCount;
                String formattedResult = String.format("%.2f", result);
                double doubleFormattedResult = Double.parseDouble(formattedResult);
                criteriaSetRepository.updatePoint(doubleFormattedResult, criteriaSetId);
            } else {
                double totalClassDetailCountResult = totalClassDetailCount - 1;
                if (totalClassDetailCountResult <= 0) {
                    return;
                }
                double result = 100.0 / totalClassDetailCountResult;
                String formattedResult = String.format("%.2f", result);
                double doubleFormattedResult = Double.parseDouble(formattedResult);
                criteriaSetRepository.updatePoint(doubleFormattedResult, criteriaSetId);
            }

        } else {
            return;
        }
    }


    public static boolean checkIsNull(Long criteriaSetId) {
        CriteriaSet criteriaSetFind = criteriaSetRepository.findById(criteriaSetId).get();
        if (criteriaSetFind.getCriteriaSetCode() == null || criteriaSetFind.getCriteriaSetCode().trim().isEmpty() || criteriaSetFind.getCriteriaSetName() == null || criteriaSetFind.getCriteriaSetName().trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }


    public ResponseEntity<?> getApplied(Long typeClass, Long criteriaClass1Id, Long criteriaSetId, Long evaluationVersionId, Long userId, Long userReceiveId) {
        if (typeClass == 1L) {
            if (criteriaSetId == null) {
                //Lấy bộ chỉ số hiện hành(Form gửi của đô thị).
                CriteriaSet criteriaSet = criteriaSetRepository.getApplied();
                if (criteriaSet != null) {
                    CriteriaSetDTOForm criteriaSetDTOForm = modelMapper.map(criteriaSet, CriteriaSetDTOForm.class);
                    return ResponseEntity.ok(new Response().setData(criteriaSetDTOForm).setMessage("Found!"));
                } else {
                    return ResponseEntity.notFound().build();
                }
            } else {
                //Lấy bộ chỉ số theo ID bộ chỉ số kết quả đánh giá (Đô thị và Admin).
                CriteriaSet criteriaSet = criteriaSetRepository.getAppliedReceive(criteriaSetId);
                if (criteriaSet != null) {
                    CriteriaSetDTOForm criteriaSetDTOForm = modelMapper.map(criteriaSet, CriteriaSetDTOForm.class);
                    return ResponseEntity.ok(new Response().setData(criteriaSetDTOForm).setMessage("Found!"));
                } else {
                    return ResponseEntity.notFound().build();
                }
            }
        } else {
            if (criteriaClass1Id == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("Error: CriteriaClass1Id is null"));
            } else {
                if (criteriaClass1Id != null && evaluationVersionId == null) {
                    //Lấy thông tin lớp 2->5 từ lớp 1 + câu trả lời(Form gửi của đô thị).
                    List<CriteriaClass2DTOForm> result = criteriaClass2Repository.getByClass1Preview(criteriaClass1Id).stream()
                            .map(criteriaClass2 -> {
                                CriteriaClass2DTOForm criteriaClass2DTOForm = modelMapper.map(criteriaClass2, CriteriaClass2DTOForm.class);
                                criteriaClass2DTOForm.getCriteriaClass3s().forEach(criteriaClass3DTOForm -> {
                                    criteriaClass3DTOForm.getCriteriaDetails().forEach(criteriaDetailDTOForm -> {
                                        criteriaDetailDTOForm.setAnswerUser(CriteriaDetailServiceImpl.getAnswerOfUser(criteriaDetailDTOForm.getCriteriaDetailId()));
                                    });
                                });
                                return criteriaClass2DTOForm;
                            })
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(new Response().setData(result).setMessage("Found!"));
                } else if (criteriaClass1Id != null && evaluationVersionId != null && userId != null) {
                    //Lấy thông tin lớp 2->5 từ lớp 1 + câu trả lời form kết quả đánh giá (Đô thị và Admin theo phiên bản và userId.
                    List<CriteriaClass2DTOForm> result = criteriaClass2Repository.getByClass1Preview(criteriaClass1Id).stream()
                            .map(criteriaClass2 -> {
                                CriteriaClass2DTOForm criteriaClass2DTOForm = modelMapper.map(criteriaClass2, CriteriaClass2DTOForm.class);
                                criteriaClass2DTOForm.getCriteriaClass3s().forEach(criteriaClass3DTOForm ->
                                        criteriaClass3DTOForm.getCriteriaDetails().forEach(criteriaDetailDTOForm -> {
                                            Map<String, Object> councilScore = CriteriaSetServiceImpl.getPointCouncil(criteriaDetailDTOForm.getCriteriaDetailId(), evaluationVersionId, userId);
                                            if (councilScore == null) {
                                                criteriaDetailDTOForm.setPointCouncil(null);
                                                criteriaDetailDTOForm.setComment(null);
                                            } else {
                                                criteriaDetailDTOForm.setPointCouncil((Float) councilScore.get("score"));
                                                criteriaDetailDTOForm.setComment((String) councilScore.get("comment"));
                                            }
                                            if (userReceiveId == null) {
                                                criteriaDetailDTOForm.setAnswerUserReceive(CriteriaDetailServiceImpl.getAnswerOfUserReceive(criteriaDetailDTOForm.getCriteriaDetailId(), evaluationVersionId, userId));
                                                if (criteriaDetailDTOForm.getAnswerUserReceive() != null) {
                                                    if (criteriaDetailDTOForm.getAnswerUserReceive().getIsPass() == null) {
                                                        Long answerID = answerRepository.findIncomplete(criteriaDetailDTOForm.getAnswerUserReceive().getAnswerId());
                                                        if (answerID == null) {
                                                            criteriaDetailDTOForm.getAnswerUserReceive().setIsPass(false);
                                                        }
                                                    }
                                                }

                                            } else {
                                                criteriaDetailDTOForm.setAnswerUserReceive(CriteriaDetailServiceImpl.getAnswerOfUserReceive(criteriaDetailDTOForm.getCriteriaDetailId(), evaluationVersionId, userReceiveId));
                                                if (criteriaDetailDTOForm.getAnswerUserReceive() != null) {
                                                    if (criteriaDetailDTOForm.getAnswerUserReceive().getIsPass() == null) {
                                                        Long answerID = answerRepository.findIncomplete(criteriaDetailDTOForm.getAnswerUserReceive().getAnswerId());
                                                        if (answerID == null) {
                                                            criteriaDetailDTOForm.getAnswerUserReceive().setIsPass(false);
                                                        }
                                                    }
                                                }
                                            }
                                        })
                                );
                                return criteriaClass2DTOForm;
                            })
                            .collect(Collectors.toList());

                    return ResponseEntity.ok(new Response().setData(result));
                } else {
                    return ResponseEntity.badRequest().body("Null Id");
                }
            }
        }
    }


    public ResponseEntity<?> getReceiveForm(EvaluationVersion entity, Pageable pageable, Long userId) {
        ParamMap param = ParamMap.init(entity, pageable);
        String createdAt = param.getString("searchDate");
        String versionName = param.getString("versionName");
        String status = param.getString("status");
        LocalDateTime timeMinOfDay = null;
        LocalDateTime timeMaxOfDay = null;
        if (!StringUtils.isEmpty(createdAt)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            timeMinOfDay = LocalDate.parse(createdAt, formatter).atStartOfDay();
            timeMaxOfDay = LocalDate.parse(createdAt, formatter).atTime(LocalTime.MAX);
        }
        Page<EvaluationReceiveDTO> dataList = criteriaSetRepository.getReceiveForm(userId, versionName, timeMinOfDay, timeMaxOfDay, status, pageable);
        Map<String, Object> response = new HashMap<>();
        response.put("evaluationReceiveDTOS", dataList.toList());
        response.put("currentPage", dataList.getNumber());
        response.put("totalItems", dataList.getTotalElements());
        response.put("totalPages", dataList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> getReceiveFormFirst(Long userId) {
        List<EvaluationReceiveDTO> dataList = criteriaSetRepository.getReceiveFormFirst(userId);
        if (dataList == null) {
            return new ResponseEntity<>(null, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(dataList.get(0), HttpStatus.OK);
        }
    }


    public ResponseEntity<?> getReceiveFormDetail(Long evaluationVersionId) {
        return new ResponseEntity<>(criteriaSetRepository.getReceiveFormDetail(evaluationVersionId), HttpStatus.OK);
    }


    public static CriteriaSetDTOFormReceive getReceiveFormSet(Long criteriaSetId) {
        return criteriaSetRepository.getReceiveFormSet(criteriaSetId);
    }


    public ResponseEntity<?> getCriteriaDetailIdList(Long criteriaClass1Id) {
        return ResponseEntity.ok(new Response().setData(criteriaSetRepository.getCriteriaDetailIdList(criteriaClass1Id)).setMessage("Found!"));
    }


    public ResponseEntity<String> addCouncilMark(Long evaluationVersionId, List<Long> userIds, String type) {
        List<Users> temp = new ArrayList<>();
        List<EvaluationVersionUser> evaluationVersionUsers = new ArrayList<>();

        for (Long userId : userIds) {
            EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId)
                    .orElseThrow(() -> new ResourceNotFoundException("EvaluationVersion", "Id", evaluationVersionId));
            Users users = usersRepository.findById(userId)
                    .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
            temp.add(users);
            EvaluationVersionUser evaluationVersionUser;

            if (type.equals("add")) {
                if (evaluationVersionUserRepository.existsByEvaluationVersionAndUsers(evaluationVersion, users)) {

                } else {
                    evaluationVersionUser = new EvaluationVersionUser();
                    evaluationVersionUser.setEvaluationVersion(evaluationVersion);
                    evaluationVersionUser.setUsers(users);
                    evaluationVersionUsers.add(evaluationVersionUser);
                }
            } else {
                evaluationVersionUser = evaluationVersionUserRepository.findByEvaluationVersionAndUsers(evaluationVersion, users);
                evaluationVersionUsers.add(evaluationVersionUser);
            }
        }

        if (type.equals("add")) {
            evaluationVersionUserRepository.saveAll(evaluationVersionUsers);
            NotifiServiceImpl.addCouncilMark(temp, "add", evaluationVersionId);
            return ResponseEntity.ok("Save successfully");
        } else {
            evaluationVersionUserRepository.deleteAll(evaluationVersionUsers);
            NotifiServiceImpl.addCouncilMark(temp, "delete", evaluationVersionId);
            return ResponseEntity.ok("Delete successfully");
        }
    }


    public ResponseEntity<String> councilScore(List<CouncilScore> councilScores) {
        List<CouncilScore> resultList = new ArrayList<>();

        for (CouncilScore councilScore : councilScores) {
            CriteriaDetail criteriaDetailFind = criteriaDetailRepository.findById(councilScore.getCriteriaDetailId()).orElse(null);
            EvaluationVersionUser evaluationVersionUserFind = evaluationVersionUserRepository.findById(councilScore.getEvaluationVersionUserId()).orElse(null);

            if (criteriaDetailFind != null && evaluationVersionUserFind != null) {
                CouncilScore councilScoreFind = councilScoreRepository.findByCriteriaDetailAndEvaluationVersionUser(criteriaDetailFind, evaluationVersionUserFind);

                if (councilScoreFind != null) {
                    councilScoreFind.setNumberScore(councilScore.getNumberScore());
                    councilScoreFind.setComment(councilScore.getComment());
                    resultList.add(councilScoreFind);
                } else {
                    councilScore.setCriteriaDetail(criteriaDetailFind);
                    councilScore.setEvaluationVersionUser(evaluationVersionUserFind);
                    resultList.add(councilScore);
                }
            }
        }

        if (!resultList.isEmpty()) {
            List<CouncilScore> councilScoreList = councilScoreRepository.saveAll(resultList);
            Integer evaluationVersionUserId = councilScoreList.get(0).getEvaluationVersionUser().getEvaluationVersionUserId();
            Float totalPoint = evaluationVersionUserRepository.getTotalScore(evaluationVersionUserId);
            evaluationVersionUserRepository.updatePointCouncil(totalPoint, evaluationVersionUserId);
        }

        return ResponseEntity.ok("Save successfully");
    }


    public static Integer findEvaluationVersionUserId(Long evaluationVersionId) {
        Long userIdLogin = getUserLoginId();
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationVersion", "Id", evaluationVersionId));
        Users users = usersRepository.findById(userIdLogin)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userIdLogin));
        EvaluationVersionUser evaluationVersionUserFind = evaluationVersionUserRepository.findByEvaluationVersionAndUsers(evaluationVersion, users);
        if (evaluationVersionUserFind == null) {
            return null;
        } else {
            return evaluationVersionUserFind.getEvaluationVersionUserId();
        }
    }


    public static Integer findEvaluationVersionUserId2(Long evaluationVersionId, Long userId) {
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId)
                .orElseThrow(() -> new ResourceNotFoundException("EvaluationVersion", "Id", evaluationVersionId));
        Users users = usersRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "Id", userId));
        EvaluationVersionUser evaluationVersionUserFind = evaluationVersionUserRepository.findByEvaluationVersionAndUsers(evaluationVersion, users);
        if (evaluationVersionUserFind == null) {
            return null;
        } else {
            return evaluationVersionUserFind.getEvaluationVersionUserId();
        }
    }


    public static Map<String, Object> getPointCouncil(Long criteriaDetailId, Long evaluationVersionId, Long userId) {
        Map<String, Object> result = new HashMap<>();
        CouncilScore pointCouncil = councilScoreRepository.getPointCouncil(criteriaDetailId, userId, evaluationVersionId);
        if (pointCouncil == null) {
            return null;
        } else {
            result.put("score", pointCouncil.getNumberScore());
            result.put("comment", pointCouncil.getComment());
            return result;
        }
    }


    public ResponseEntity<?> getCouncilScore(Integer evaluationVersionUserId) {
        return new ResponseEntity<>(evaluationVersionUserRepository.getCouncilScore(evaluationVersionUserId), HttpStatus.OK);
    }


    public ResponseEntity<?> sendResultCouncil(Integer evaluationVersionUserId) {
        Optional<EvaluationVersionUser> evaluationVersionUser = evaluationVersionUserRepository.findById(evaluationVersionUserId);
        if (evaluationVersionUser.isPresent()) {
            int numCouncilScore = evaluationVersionUserRepository.countNumCouncilScore(evaluationVersionUserId);
            Long criteriaSetId = evaluationVersionUser.get().getEvaluationVersion().getCriteriaSet().getCriteriaSetId();
            int numCriteriaDetail = evaluationVersionUserRepository.countNumCriteriaDetail(criteriaSetId);
            if (numCouncilScore < numCriteriaDetail) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new MessageResponse("Form is full!"));
            }

            evaluationVersionUser.get().setStatusEvaluate("Y");
            EvaluationVersionUser evaluationVersionUserSave = evaluationVersionUserRepository.save(evaluationVersionUser.get());
            List<EvaluationVersionUser> evaluationVersionUserList = evaluationVersionUserRepository.findByEvaluationVersion(evaluationVersionUser.get().getEvaluationVersion());
            boolean allStatusEvaluateY = evaluationVersionUserList.stream()
                    .allMatch(user -> "Y".equals(user.getStatusEvaluate()));
            if (allStatusEvaluateY == true) {
                NotifiServiceImpl.allCouncilMemCompleteScore(evaluationVersionUserSave.getEvaluationVersion().getEvaluationVersionId());
            }
            return new ResponseEntity<>("Send successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + evaluationVersionUserId));
        }
    }


    public ResponseEntity<?> recognitionCouncil(EvaluationVersion evaluationVersion, Long evaluationVersionId, Integer evaluationVersionUserId) {
        Optional<EvaluationVersion> evaluationVersionFind = evaluationVersionRepository.findById(evaluationVersionId);
        if (evaluationVersionFind.isPresent()) {
            evaluationVersionFind.get().setStatusRecognition(evaluationVersion.getStatusRecognition());
            ZoneId zone = ZoneId.of(TimeZoneUtils.timeZoneMap.get("VST"));
            LocalDateTime currentDateTime = LocalDateTime.now(zone);
            evaluationVersionFind.get().setTimeReturn(currentDateTime);
            evaluationVersionRepository.save(evaluationVersionFind.get());
            if (evaluationVersion.getStatusRecognition().equals("1")) {
                Optional<EvaluationVersionUser> evaluationVersionUserFind = evaluationVersionUserRepository.findById(evaluationVersionUserId);
                if (evaluationVersionUserFind.isPresent()) {
                    evaluationVersionUserFind.get().setStatusRecognition(evaluationVersion.getStatusRecognition());
                    ZoneId zone1 = ZoneId.of(TimeZoneUtils.timeZoneMap.get("VST"));
                    LocalDateTime currentDateTime1 = LocalDateTime.now(zone1);
                    evaluationVersionUserFind.get().setTimeReturn(currentDateTime1);
                    EvaluationVersionUser evaluationVersionUserSave = evaluationVersionUserRepository.save(evaluationVersionUserFind.get());
                    NotifiServiceImpl.printMessageRecognitionCouncil(evaluationVersionUserSave.getEvaluationVersion().getVersionName(), evaluationVersionUserSave.getEvaluationVersion().getUsers().getUserID(), "applied");
                }
                Users userFind = usersRepository.findById(evaluationVersionFind.get().getUsers().getUserID()).get();
                userFind.setUrbanStatus(2);
                usersRepository.save(userFind);
                return new ResponseEntity<>("Send successfully", HttpStatus.OK);

            } else {
                Optional<EvaluationVersionUser> evaluationVersionUserFind = evaluationVersionUserRepository.findById(evaluationVersionUserId);
                if (evaluationVersionUserFind.isPresent()) {
                    evaluationVersionUserFind.get().setStatusRecognition(evaluationVersion.getStatusRecognition());
                    evaluationVersionUserFind.get().setCommentRecognition(evaluationVersion.getCommentRecognition());
                    ZoneId zone1 = ZoneId.of(TimeZoneUtils.timeZoneMap.get("VST"));
                    LocalDateTime currentDateTime1 = LocalDateTime.now(zone1);
                    evaluationVersionUserFind.get().setTimeReturn(currentDateTime1);
                    EvaluationVersionUser evaluationVersionUserSave = evaluationVersionUserRepository.save(evaluationVersionUserFind.get());
                    NotifiServiceImpl.printMessageRecognitionCouncil(evaluationVersionUserSave.getEvaluationVersion().getVersionName(), evaluationVersionUserSave.getEvaluationVersion().getUsers().getUserID(), "refuse");
                }
                Users userFind = usersRepository.findById(evaluationVersionFind.get().getUsers().getUserID()).get();
                userFind.setUrbanStatus(1);
                usersRepository.save(userFind);
            }
            return new ResponseEntity<>("Send successfully", HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + evaluationVersionId));
        }
    }


    public ResponseEntity<?> receiveResult(Long userID, Pageable pageable) {
        List<CriteriaReceiveDTO> receiveResultSendCouncil = criteriaSetRepository.receiveResult(userID);
        List<CriteriaReceiveDTO> receiveResultNotSendCouncil = criteriaSetRepository.receiveResultNotSendCouncil(userID);

        // Tạo một danh sách mới để kết hợp cả hai danh sách
        List<CriteriaReceiveDTO> combinedList = new ArrayList<>();

        // Thêm danh sách đầu tiên
        combinedList.addAll(receiveResultSendCouncil);

        // Thêm danh sách thứ hai
        combinedList.addAll(receiveResultNotSendCouncil);

//        combinedList = combinedList.stream().sorted()

        Collections.sort(combinedList, new Comparator<CriteriaReceiveDTO>() {
            public int compare(CriteriaReceiveDTO o1, CriteriaReceiveDTO o2) {
                String column = StringUtils.substringBefore(pageable.getSort().toString(), ":");
                String sortDirection = StringUtils.substringAfter(pageable.getSort().toString(), ": ");
                if (column.equals("createdAt")) {
                    return sortDirection.toLowerCase().equals("asc") ? o1.getCreatedAt().compareTo(o2.getCreatedAt()) : o2.getCreatedAt().compareTo(o1.getCreatedAt());
                }
                if (column.equals("versionName")) {
                    return sortDirection.toLowerCase().equals("asc") ? o1.getVersionName().compareTo(o2.getVersionName()) : o2.getVersionName().compareTo(o1.getVersionName());
                }
                if (column.equals("criteriaVersion")) {
                    return sortDirection.toLowerCase().equals("asc") ? o1.getCriteriaVersion().compareTo(o2.getCriteriaVersion()) : o2.getCriteriaVersion().compareTo(o1.getCriteriaVersion());
                }
                if (column.equals("timeReturn")) {
                    if (sortDirection.toLowerCase().equals("asc")) {
                        if (o1.getTimeReturn() == null)
                            if (o2.getTimeReturn() == null)
                                return 0; //equal
                            else
                                return -1; // null is before other strings
                        else // this.member != null
                            if (o2.getTimeReturn() == null)
                                return 1;  // all other strings are after null
                            else
                                return o1.getTimeReturn().compareTo(o2.getTimeReturn());
                    }else{
                        if (o2.getTimeReturn() == null)
                            if (o1.getTimeReturn() == null)
                                return 0; //equal
                            else
                                return -1; // null is before other strings
                        else // this.member != null
                            if (o1.getTimeReturn() == null)
                                return 1;  // all other strings are after null
                            else
                                return o2.getTimeReturn().compareTo(o1.getTimeReturn());
                    }
                }
                if (column.equals("statusEvaluate")) {
                    return sortDirection.toLowerCase().equals("asc") ? o1.getStatusEvaluate().compareTo(o2.getStatusEvaluate()) : o2.getStatusEvaluate().compareTo(o1.getStatusEvaluate());
                }
                if (column.equals("statusRecognition")) {
                    if (sortDirection.toLowerCase().equals("asc")) {
                        if (o1.getStatusRecognition() == null)
                            if (o2.getStatusRecognition() == null)
                                return 0; //equal
                            else
                                return -1; // null is before other strings
                        else // this.member != null
                            if (o2.getStatusRecognition() == null)
                                return 1;  // all other strings are after null
                            else
                                return o1.getStatusRecognition().compareTo(o2.getStatusRecognition());
                    }else{
                        if (o2.getStatusRecognition() == null)
                            if (o1.getStatusRecognition() == null)
                                return 0; //equal
                            else
                                return -1; // null is before other strings
                        else // this.member != null
                            if (o1.getStatusRecognition() == null)
                                return 1;  // all other strings are after null
                            else
                                return o2.getStatusRecognition().compareTo(o1.getStatusRecognition());
                    }
                }
                return o1.getCreatedAt().compareTo(o2.getCreatedAt());
            }
        });
        int max = (pageable.getPageSize() * (pageable.getPageNumber() + 1) > combinedList.size()) ? combinedList.size() : pageable.getPageSize() * (pageable.getPageNumber() + 1);
        int totalRows = combinedList.size();

        Page<CriteriaReceiveDTO> resultList = new PageImpl<CriteriaReceiveDTO>(combinedList.subList(pageable.getPageNumber() * pageable.getPageSize(), max), pageable, totalRows);

        Map<String, Object> response = new HashMap<>();
        response.put("assessmentResults", resultList.toList());
        response.put("currentPage", resultList.getNumber());
        response.put("totalItems", resultList.getTotalElements());
        response.put("totalPages", resultList.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    public ResponseEntity<?> detailReceiveResult(Long userID, Long evaluationVersionId) {
        return new ResponseEntity<>(criteriaSetRepository.detailReceiveResult(userID, evaluationVersionId), HttpStatus.OK);
    }


    public static Long getUserLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userDetails.getId();
    }


}
