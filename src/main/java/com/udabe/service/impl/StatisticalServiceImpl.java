package com.udabe.service.impl;

import com.udabe.cmmn.base.Response;
import com.udabe.cmmn.base.ResponseChart;
import com.udabe.dto.EvaluationVersion.StaEvaluationVersionDTO;
import com.udabe.dto.EvaluationVersion.EvaluationVersionScorePercentDTO;
import com.udabe.dto.Statistical.StatisticalDTO;
import com.udabe.dto.Statistical.UrbanStatisticalDTO;
import com.udabe.dto.Urban.UrbanDTO;
import com.udabe.dto.criteria.*;
import com.udabe.dto.user.AddressCodeDTO;
import com.udabe.entity.*;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.*;
import com.udabe.service.StatisticalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class StatisticalServiceImpl implements StatisticalService {

    private final StatisticalRepository statisticalRepository;
    private final AddressCodeRepository addressCodeRepository;
    private final EvaluationVersionRepository evaluationVersionRepository;
    private final UsersRepository usersRepository;
    private final CriteriaSetRepository criteriaSetRepository;
    private final CriteriaClass1Repository criteriaClass1Repository;
    private final CriteriaClass2Repository criteriaClass2Repository;
    private final CriteriaClass3Repository criteriaClass3Repository;
    private final CriteriaDetailRepository criteriaDetailRepository;

    private static final Random random = new Random();

    @Autowired
    public StatisticalServiceImpl(StatisticalRepository statisticalRepository, AddressCodeRepository addressCodeRepository, EvaluationVersionRepository evaluationVersionRepository, UsersRepository usersRepository, CriteriaSetRepository criteriaSetRepository, CriteriaClass1Repository criteriaClass1Repository, CriteriaClass2Repository criteriaClass2Repository, CriteriaClass3Repository criteriaClass3Repository, CriteriaDetailRepository criteriaDetailRepository) {
        this.statisticalRepository = statisticalRepository;
        this.addressCodeRepository = addressCodeRepository;
        this.evaluationVersionRepository = evaluationVersionRepository;
        this.usersRepository = usersRepository;
        this.criteriaSetRepository = criteriaSetRepository;
        this.criteriaClass1Repository = criteriaClass1Repository;
        this.criteriaClass2Repository = criteriaClass2Repository;
        this.criteriaClass3Repository = criteriaClass3Repository;
        this.criteriaDetailRepository = criteriaDetailRepository;
    }


    @Override
    public ResponseEntity<?> getStatusGrowClass(Long userId, String year) {
        String formated = null;
        int years = Integer.parseInt(year);
        Long getMaxEvaluationVersionId = statisticalRepository.getMaxEvaluationVersionId(userId, years);
        if (getMaxEvaluationVersionId == null) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(getMaxEvaluationVersionId).get();
        CriteriaSet criteriaSetFind = evaluationVersion.getCriteriaSet();
        Long criteriaSetId = criteriaSetFind.getCriteriaSetId();
        int class3Size = statisticalRepository.getNumClass3(criteriaSetId);
        int class2Size = statisticalRepository.getNumClass2(criteriaSetId);
        int class1Size = statisticalRepository.getNumClass1(criteriaSetId);

        double perCentPassClass3 = (statisticalRepository.getNumPassClass3(userId, years) / class3Size) * 100;
        formated = String.format("%.1f", perCentPassClass3);
        double roundedPercentage3 = Double.parseDouble(formated);
        double roundedNotPercentage3 = 100.0 - roundedPercentage3;
        formated = String.format("%.1f", roundedNotPercentage3);
        roundedNotPercentage3 = Double.parseDouble(formated);

        double perCentPassClass2 = (statisticalRepository.getNumPassClass2(userId, years) / class2Size) * 100;
        formated = String.format("%.1f", perCentPassClass2);
        double roundedPercentage2 = Double.parseDouble(formated);
        double roundedNotPercentage2 = 100.0 - roundedPercentage2;
        formated = String.format("%.1f", roundedNotPercentage2);
        roundedNotPercentage2 = Double.parseDouble(formated);

        double perCentPassClass1 = (statisticalRepository.getNumPassClass1(userId, years) / class1Size) * 100;
        formated = String.format("%.1f", perCentPassClass1);
        double roundedPercentage1 = Double.parseDouble(formated);
        double roundedNotPercentage1 = 100.0 - roundedPercentage1;
        formated = String.format("%.1f", roundedNotPercentage1);
        roundedNotPercentage1 = Double.parseDouble(formated);

        Float getPassNum = statisticalRepository.getPassNum(userId, years);
        Float getNotPassNum = statisticalRepository.getNotPassNum(userId, years);
        if (getPassNum == null || getNotPassNum == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Null number!"));
        }
        float total = getPassNum + getNotPassNum;
        float percentPassClass4 = (getPassNum / total) * 100;
        formated = String.format("%.1f", percentPassClass4);
        double roundedPercentage4 = Double.parseDouble(formated);
        double roundedNotPercentage4 = 100.0 - roundedPercentage4;
        formated = String.format("%.1f", roundedNotPercentage4);
        roundedNotPercentage4 = Double.parseDouble(formated);


        Map<Object, Object> mapResult = new HashMap<>();
        UrbanStatisticalDTO urbanStatisticalDTOClass1 = new UrbanStatisticalDTO(roundedPercentage1, roundedNotPercentage1);
        mapResult.put("perCentClass1", urbanStatisticalDTOClass1);
        UrbanStatisticalDTO urbanStatisticalDTOClass2 = new UrbanStatisticalDTO(roundedPercentage2, roundedNotPercentage2);
        mapResult.put("perCentClass2", urbanStatisticalDTOClass2);
        UrbanStatisticalDTO urbanStatisticalDTOClass3 = new UrbanStatisticalDTO(roundedPercentage3, roundedNotPercentage3);
        mapResult.put("perCentClass3", urbanStatisticalDTOClass3);
        UrbanStatisticalDTO urbanStatisticalDTOClass4 = new UrbanStatisticalDTO(roundedPercentage4, roundedNotPercentage4);
        mapResult.put("perCentClass4", urbanStatisticalDTOClass4);
        return ResponseEntity.ok(new Response().setData(mapResult).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getUrbanList(Long addressCodeId, String urbanName) {
        List<UrbanDTO> resultList = addressCodeRepository.getUrbanList(addressCodeId, urbanName);
        return ResponseEntity.ok(new Response().setData(resultList).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getUrbanDevStatus(StatisticalDTO statisticalDTO) {
        List<ResponseChart> responseChartList = new ArrayList<>();
        Long[] urbanList = statisticalDTO.getUrbanList();
        Number[] years = statisticalDTO.getYear();
        LocalDate yearNow = LocalDate.now();
        if (years.length == 1) {
//            List<ResponseChart> responseChartList = new ArrayList<>();
            int i = 0;
            while (i < urbanList.length) {
                LocalDate year = year = LocalDate.of(years[0].intValue(), 1, 1);
                int countMonth = yearNow.getYear() == year.getYear() ? yearNow.getMonth().getValue() : 12;
                ResponseChart chart = new ResponseChart();
                Float[] data = new Float[countMonth];
                if (statisticalDTO.getUnitChart() == 1) {
                    int j = 0;
                    while (j < countMonth) {
                        year = LocalDate.of(years[0].intValue(), j + 1, 1);
                        data[j] = evaluationVersionRepository.getScore(urbanList[i], year.toString());
                        if (j != 0) {
                            data[j] = data[j] == null ? data[j - 1] : data[j];
                        }
                        year = year.plusMonths(1);
                        j++;
                    }
                }
                if (statisticalDTO.getUnitChart() == 2) {
                    int j = 0;
                    while (j < countMonth) {
                        year = LocalDate.of(years[0].intValue(), j + 1, 1);
                        data[j] = evaluationVersionRepository.getPercent(urbanList[i], year.toString());
                        if (j != 0) {
                            data[j] = data[j] == null ? data[j - 1] : data[j];
                        }
                        year = year.plusMonths(1);
                        j++;
                    }
                }
                chart.setUserID(urbanList[i]);
                chart.setFullName(usersRepository.getFullNameByUserID(urbanList[i]));
                chart.setData(data);
                chart.setColor(StatisticalServiceImpl.getRandomHexColor());
                responseChartList.add(chart);
                i++;
            }
            return ResponseEntity.ok(new Response().setDataList(responseChartList).setMessage("Successfully!"));
        }
        if (years.length == 2) {
            int i = 0;
            while (i < urbanList.length) {
                int startYear = years[0].intValue();
                int endYear = years[1].intValue();
                ResponseChart chart = new ResponseChart();
                Float[] data = new Float[endYear - startYear + 1];
                if (statisticalDTO.getUnitChart() == 1) {
                    int j = 0;
                    while (startYear <= endYear) {
                        LocalDate year = LocalDate.of(startYear, 12, 1);
                        data[j] = evaluationVersionRepository.getScoreYear(urbanList[i], year.toString());
                        if (j != 0) {
                            data[j] = data[j] == null ? data[j - 1] : data[j];
                        }
                        startYear++;
                        j++;
                    }
                }
                if (statisticalDTO.getUnitChart() == 2) {
                    int j = 0;
                    while (startYear <= endYear) {
                        LocalDate year = LocalDate.of(startYear, 12, 1);
                        data[j] = evaluationVersionRepository.getPercentYear(urbanList[i], year.toString());
                        if (j != 0) {
                            data[j] = data[j] == null ? data[j - 1] : data[j];
                        }
                        startYear++;
                        j++;
                    }
                }
                chart.setUserID(urbanList[i]);
                chart.setFullName(usersRepository.getFullNameByUserID(urbanList[i]));
                chart.setData(data);
                chart.setColor(StatisticalServiceImpl.getRandomHexColor());
                responseChartList.add(chart);
                i++;
            }
            return ResponseEntity.ok(new Response().setDataList(responseChartList).setMessage("Successfully!"));
        }

        return null;
    }

    @Override
    public ResponseEntity<?> searchProvince(String provinceName) {
        List<AddressCodeDTO> addressCodeDTOS = addressCodeRepository.searchProvince(provinceName);
        return ResponseEntity.ok(new Response().setData(addressCodeDTOS).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getBasicInfo(Byte unitChart) {
        //Tổng số đô thị tham gia đăng ký đánh giá
        int sumUrban = 0;
        //Tổng số đô thì có điểm/phần trăm phát triển đô thị trên 50
        int sumUrbanThanFifty = 0;
        //Tổng số đô thì có điểm/phần trăm phát triển đô thị đạt 100
        int sumUrbanOneHundred = 0;

        int[] urbanArr = evaluationVersionRepository.countUrbanEvaluatedAuto();
        sumUrban = urbanArr.length;
        int i = 0;
        while (i < sumUrban) {
            EvaluationVersionScorePercentDTO versionScorePercentDTO = evaluationVersionRepository.getLatestVersionByUserId(urbanArr[i]);
            if (unitChart == 1) {
                sumUrbanThanFifty = versionScorePercentDTO.getSumScore() >= 50 ? sumUrbanThanFifty + 1 : sumUrbanThanFifty;
                sumUrbanOneHundred = versionScorePercentDTO.getSumScore() == 100 ? sumUrbanOneHundred + 1 : sumUrbanOneHundred;
            } else if (unitChart == 2) {
                sumUrbanThanFifty = versionScorePercentDTO.getSumPercent() >= 50 ? sumUrbanThanFifty + 1 : sumUrbanThanFifty;
                sumUrbanOneHundred = versionScorePercentDTO.getSumPercent() == 100 ? sumUrbanOneHundred + 1 : sumUrbanOneHundred;
            }
            i++;
        }
        Map<String, Object> response = new HashMap<>();
        response.put("sumUrban", sumUrban);
        response.put("sumUrbanThanFifty", sumUrbanThanFifty);
        response.put("sumUrbanOneHundred", sumUrbanOneHundred);
        return ResponseEntity.ok(new Response().setData(response).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getStaUrbanList(StatisticalDTO statisticalDTO, Long addressCodeId, Pageable pageable) {
        if (pageable.getSort() == Sort.unsorted()) {
            Sort.Order order = new Sort.Order(Sort.Direction.ASC, "fullName");
            Sort sort = Sort.by(order);
            pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
        }
        Page<StaEvaluationVersionDTO> dataPage = null;
        Number[] years = statisticalDTO.getYear();
        if (years.length == 1) {
            LocalDate year = LocalDate.of(years[0].intValue(), 10, 1);
            dataPage = evaluationVersionRepository.staAllUrbanScorePercent(addressCodeId, year, pageable);
        } else {
            LocalDate yearNow = LocalDate.now();
            LocalDate startYear = LocalDate.of(years[0].intValue(), 1, 1);
            LocalDate endYear = LocalDate.of(years[1].intValue(), 12, 1);
            dataPage = evaluationVersionRepository.staAllUrbanScorePercentMultiYear(addressCodeId, startYear, endYear, pageable);
        }
        Map<String, Object> response = new HashMap<>();
        response.put("dataList", dataPage.toList());
        response.put("currentPage", dataPage.getNumber());
        response.put("totalItems", dataPage.getTotalElements());
        response.put("totalPages", dataPage.getTotalPages());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getBasicInfoEvaluated(StatisticalDTO statisticalDTO) {
        //Tổng số đô thị tham gia đăng ký đánh giá trong năm
        int sumUrban = 0;
        //Tổng số đăng ký đáng giá
        int sumEvaluationVersion = 0;
        //Xu hướng phát triển so với năm trước
        float increaseRate = 0;

        String formated = null;

        Number[] years = statisticalDTO.getYear();

        if (years.length == 1) {
            LocalDate year = LocalDate.of(years[0].intValue(), 12, 1);
            sumUrban = evaluationVersionRepository.countUrbanEvaluatedAutoInYear(year);
            sumEvaluationVersion = evaluationVersionRepository.countEvaluationVersionInYear(year);
        } else {
            LocalDate startYear = LocalDate.of(years[0].intValue(), 12, 1);
            LocalDate endYear = LocalDate.of(years[1].intValue(), 12, 1);
            sumUrban = evaluationVersionRepository.countUrbanEvaluatedAutoInMultiYear(startYear, endYear);
            sumEvaluationVersion = evaluationVersionRepository.countEvaluationVersionInMultiYear(startYear, endYear);
        }
        LocalDate currentYear = LocalDate.now();

        int sumEvaluationVersionNow = evaluationVersionRepository.countEvaluationVersionInYear(currentYear);
        int sumEvaluationVersionBefore = evaluationVersionRepository.countEvaluationVersionInYear(currentYear.minusYears(1));
        if (sumEvaluationVersionBefore != 0) {
            increaseRate = ((float) (sumEvaluationVersionNow - sumEvaluationVersionBefore) / sumEvaluationVersionBefore) * 100;
        } else {
            increaseRate = (float) sumEvaluationVersionNow * 100;
        }


        formated = String.format("%.1f", increaseRate);
        float roundedIncreaseRate = Float.parseFloat(formated);

        Map<String, Object> response = new HashMap<>();
        response.put("sumUrban", sumUrban);
        response.put("sumEvaluationVersion", sumEvaluationVersion);
        response.put("increaseRate", roundedIncreaseRate);
        return ResponseEntity.ok(new Response().setData(response).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getUrbanRegisterStatus(StatisticalDTO statisticalDTO) {
        List<ResponseChart> responseChartList = new ArrayList<>();
        Number[] years = statisticalDTO.getYear();
        LocalDate yearNow = LocalDate.now();
        if (years.length == 1) {
            LocalDate year = LocalDate.of(years[0].intValue(), 1, 1);
            int countMonth = year.getYear() == yearNow.getYear() ? yearNow.getMonth().getValue() : 12;
            int[] urbanData = new int[countMonth];
            int[] urbanRegisData = new int[countMonth];
            int i = 0;
            while (i < countMonth) {
                urbanData[i] = evaluationVersionRepository.countUrbanEvaluatedByMonth(year);
                urbanRegisData[i] = evaluationVersionRepository.countRegisEvaluatedByMonth(year);
                year = year.plusMonths(1);
                i++;
            }
            ResponseChart responseUrbanData = new ResponseChart();
            responseUrbanData.setDataInt(urbanData);
            responseUrbanData.setColor(StatisticalServiceImpl.getRandomHexColor());
            responseChartList.add(responseUrbanData);

            ResponseChart responseRegisData = new ResponseChart();
            responseRegisData.setDataInt(urbanRegisData);
            responseRegisData.setColor(StatisticalServiceImpl.getRandomHexColor());
            responseChartList.add(responseRegisData);
        } else {
//            LocalDate startYear = LocalDate.of(years[0].intValue(), 1, 1);
//            LocalDate endYear = LocalDate.of(years[1].intValue(), 1, 1);
            int startYear = years[0].intValue();
            int endYear = years[1].intValue();
            int[] urbanData = new int[endYear - startYear + 1];
            int[] urbanRegisData = new int[endYear - startYear + 1];
            int i = 0;
            while (startYear <= endYear) {
                LocalDate year = LocalDate.of(startYear, 1, 1);
                urbanData[i] = evaluationVersionRepository.countUrbanEvaluatedByYear(year);
                urbanRegisData[i] = evaluationVersionRepository.countRegisEvaluatedByYear(year);
                year = year.plusMonths(1);
                startYear++;
                i++;
            }
            ResponseChart responseUrbanData = new ResponseChart();
            responseUrbanData.setFullName("responseUrbanData");
            responseUrbanData.setDataInt(urbanData);
            responseUrbanData.setColor(StatisticalServiceImpl.getRandomHexColor());
            responseChartList.add(responseUrbanData);

            ResponseChart responseRegisData = new ResponseChart();
            responseRegisData.setFullName("responseRegisData");
            responseRegisData.setDataInt(urbanRegisData);
            responseRegisData.setColor(StatisticalServiceImpl.getRandomHexColor());
            responseChartList.add(responseRegisData);
        }
        return ResponseEntity.ok(new Response().setDataList(responseChartList).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getBasicInfoUrban(Byte unitChart, Long userId) {
        Long evaluationVersionId = statisticalRepository.getEvaluationVersion(userId);
        EvaluationVersion evaluationVersion = evaluationVersionRepository.findById(evaluationVersionId).get();
        Integer status = statisticalRepository.getUrbanStatus(userId);
        if (status == null) {
            status = 1;
        }
        String version = statisticalRepository.getCriteriaVersion();
        float score = 0;
        if (unitChart == 1) {
            score = evaluationVersion.getSumScore();
        } else if (unitChart == 2) {
            score = evaluationVersion.getSumPercent();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("process", score);
        response.put("status", status);
        response.put("version", version);
        return ResponseEntity.ok(new Response().setData(response).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getCriteriaClass1() {
        Long criteriaSetAppliedID = criteriaSetRepository.getCriteriaSetAppliedID();
        List<CriteriaClass1DTO> criteriaClass1DTOs = criteriaClass1Repository.getAllCriteriaClass1(criteriaSetAppliedID);
        return ResponseEntity.ok(new Response().setDataList(criteriaClass1DTOs).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getStaCriteriaClass1(Long criteriaClass1Id, int year) {
        StatisticalCriteriaDTO statisticalCriteriaDTO = new StatisticalCriteriaDTO();
        LocalDate yearLD = LocalDate.of(year, 1, 1);
        CriteriaClass1DTO criteriaClass1DTO = criteriaClass1Repository.findCriteriaClass1ById(criteriaClass1Id);
        statisticalCriteriaDTO.setCriteriaID(criteriaClass1DTO.getCriteriaClass1Id());
        statisticalCriteriaDTO.setContentVi(criteriaClass1DTO.getContentVi());
        int[] urbanJoinCriteriaSetIDs = evaluationVersionRepository.countUrbanEvaluatedAutoInYearBy(yearLD);
        List<CriteriaClass2DTO> criteriaClass2DTOList = criteriaClass2Repository.getAllCriteriaClass2ByClass1Id(criteriaClass1Id);
        statisticalCriteriaDTO.setCriteriaClass2DTOList(criteriaClass2DTOList);
        int sumUrbanPassCriteria = 0;

        int i = 0;
        while (i < urbanJoinCriteriaSetIDs.length) {
            int m = 0;
            int checkPassCriteriaClass2ByUserID = 0;
            while (m < criteriaClass2DTOList.size()) {
                List<CriteriaClass3DTO> criteriaClass3DTOList = criteriaClass3Repository.getAllCriteriaClass3ByClass2Id(criteriaClass2DTOList.get(m).getCriteriaClass2Id());
                int j = 0;
                int checkPassCriteriaClass3ByUserID = 0;
                while (j < criteriaClass3DTOList.size()) {
                    List<CriteriaDetailDTO> criteriaDetailDTOList = criteriaDetailRepository.getAllCriteriaDetailByClass3Id(criteriaClass3DTOList.get(j).getCriteriaClass3Id());
                    int k = 0;
                    int checkPassCriteriaDetailsByUserID = 0;
                    while (k < criteriaDetailDTOList.size()) {
                        checkPassCriteriaDetailsByUserID = checkPassCriteriaDetailsByUserID + evaluationVersionRepository.checkPassCriteriaDetailsByUserID(urbanJoinCriteriaSetIDs[i], criteriaDetailDTOList.get(k).getCriteriaDetailId());
                        k++;
                    }
                    if (checkPassCriteriaDetailsByUserID == criteriaDetailDTOList.size()) {
                        checkPassCriteriaClass3ByUserID++;
                    }
                    j++;
                }
                if (checkPassCriteriaClass3ByUserID == criteriaClass3DTOList.size()) {
                    checkPassCriteriaClass2ByUserID++;
                }
                m++;
            }
            if (checkPassCriteriaClass2ByUserID == criteriaClass2DTOList.size()) {
                sumUrbanPassCriteria++;
            }
            i++;
        }
        float percentUrbanPassCriteria = ((float) sumUrbanPassCriteria / urbanJoinCriteriaSetIDs.length) * 100;
        String formattedPercentUrbanPassCriteria = String.format("%.1f", percentUrbanPassCriteria);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(urbanJoinCriteriaSetIDs.length);
        statisticalCriteriaDTO.setSumUrbanPassCriteria(sumUrbanPassCriteria);
        statisticalCriteriaDTO.setPercentUrbanPassCriteria(Float.parseFloat(formattedPercentUrbanPassCriteria));
        return ResponseEntity.ok(new Response().setData(statisticalCriteriaDTO).setMessage("Successfully!"));

    }

    @Override
    public ResponseEntity<?> getStaCriteriaClass2(Long criteriaClass2Id, int year) {
        StatisticalCriteriaDTO statisticalCriteriaDTO = new StatisticalCriteriaDTO();
        LocalDate yearLD = LocalDate.of(year, 1, 1);
        CriteriaClass2DTO criteriaClass2DTO = criteriaClass2Repository.findcriteriaClass2ById(criteriaClass2Id);
        statisticalCriteriaDTO.setCriteriaID(criteriaClass2DTO.getCriteriaClass2Id());
        statisticalCriteriaDTO.setContentVi(criteriaClass2DTO.getContentVi());
        int[] urbanJoinCriteriaSetIDs = evaluationVersionRepository.countUrbanEvaluatedAutoInYearBy(yearLD);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(urbanJoinCriteriaSetIDs.length);
        List<CriteriaClass3DTO> criteriaClass3DTOList = criteriaClass3Repository.getAllCriteriaClass3ByClass2Id(criteriaClass2Id);
        statisticalCriteriaDTO.setCriteriaClass3DTOList(criteriaClass3DTOList);
        int sumUrbanPassCriteria = 0;
        int i = 0;
        while (i < urbanJoinCriteriaSetIDs.length) {
            int j = 0;
            int checkPassCriteriaClass3ByUserID = 0;
            while (j < criteriaClass3DTOList.size()) {
                List<CriteriaDetailDTO> criteriaDetailDTOList = criteriaDetailRepository.getAllCriteriaDetailByClass3Id(criteriaClass3DTOList.get(j).getCriteriaClass3Id());
                int k = 0;
                int checkPassCriteriaDetailsByUserID = 0;
                while (k < criteriaDetailDTOList.size()) {
                    checkPassCriteriaDetailsByUserID = checkPassCriteriaDetailsByUserID + evaluationVersionRepository.checkPassCriteriaDetailsByUserID(urbanJoinCriteriaSetIDs[i], criteriaDetailDTOList.get(k).getCriteriaDetailId());
                    k++;
                }
                if (checkPassCriteriaDetailsByUserID == criteriaDetailDTOList.size()) {
                    checkPassCriteriaClass3ByUserID++;
                }
                j++;
            }
            if (checkPassCriteriaClass3ByUserID == criteriaClass3DTOList.size()) {
                sumUrbanPassCriteria++;
            }
            i++;
        }
        float percentUrbanPassCriteria = ((float) sumUrbanPassCriteria / urbanJoinCriteriaSetIDs.length) * 100;
        String formattedPercentUrbanPassCriteria = String.format("%.1f", percentUrbanPassCriteria);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(urbanJoinCriteriaSetIDs.length);
        statisticalCriteriaDTO.setSumUrbanPassCriteria(sumUrbanPassCriteria);
        statisticalCriteriaDTO.setPercentUrbanPassCriteria(Float.parseFloat(formattedPercentUrbanPassCriteria));

        return ResponseEntity.ok(new Response().setData(statisticalCriteriaDTO).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getStaCriteriaClass3(Long criteriaClass3Id, int year) {

//        Long criteriaSetIds = criteriaSetRepository.getCriteriaSetAppliedID();

        StatisticalCriteriaDTO statisticalCriteriaDTO = new StatisticalCriteriaDTO();

        LocalDate yearLD = LocalDate.of(year, 1, 1);
        CriteriaClass3DTO criteriaClass3DTO = criteriaClass3Repository.findcriteriaClass3ById(criteriaClass3Id);
        statisticalCriteriaDTO.setCriteriaID(criteriaClass3DTO.getCriteriaClass3Id());
        statisticalCriteriaDTO.setContentVi(criteriaClass3DTO.getContentVi());
        int[] urbanJoinCriteriaSetIDs = evaluationVersionRepository.countUrbanEvaluatedAutoInYearBy(yearLD);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(urbanJoinCriteriaSetIDs.length);
        List<CriteriaDetailDTO> criteriaDetailDTOList = criteriaDetailRepository.getAllCriteriaDetailByClass3Id(criteriaClass3Id);
        statisticalCriteriaDTO.setCriteriaDetailDTOList(criteriaDetailDTOList);
        int sumUrbanPassCriteria = 0;
        int i = 0;
        while (i < urbanJoinCriteriaSetIDs.length) {
            int j = 0;
            int checkPassCriteriaDetailsByUserID = 0;
            while (j < criteriaDetailDTOList.size()) {
                checkPassCriteriaDetailsByUserID = checkPassCriteriaDetailsByUserID + evaluationVersionRepository.checkPassCriteriaDetailsByUserID(urbanJoinCriteriaSetIDs[i], criteriaDetailDTOList.get(j).getCriteriaDetailId());
                j++;
            }
            if (checkPassCriteriaDetailsByUserID == criteriaDetailDTOList.size()) {
                sumUrbanPassCriteria++;
            }
            i++;
        }
        float percentUrbanPassCriteria = ((float) sumUrbanPassCriteria / urbanJoinCriteriaSetIDs.length) * 100;
        String formattedPercentUrbanPassCriteria = String.format("%.1f", percentUrbanPassCriteria);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(urbanJoinCriteriaSetIDs.length);
        statisticalCriteriaDTO.setSumUrbanPassCriteria(sumUrbanPassCriteria);
        statisticalCriteriaDTO.setPercentUrbanPassCriteria(Float.parseFloat(formattedPercentUrbanPassCriteria));
        return ResponseEntity.ok(new Response().setData(statisticalCriteriaDTO).setMessage("Successfully!"));
    }

    @Override
    public ResponseEntity<?> getStaCriteriaDetail(Long criteriaDetailId, int year) {

        Long criteriaID = criteriaDetailId;
        StatisticalCriteriaDTO statisticalCriteriaDTO = new StatisticalCriteriaDTO();
        LocalDate yearLD = LocalDate.of(year, 12, 1);
        CriteriaDetailDTO criteriaDetailDTO = criteriaDetailRepository.findCriteriaDetailById(criteriaDetailId);
        statisticalCriteriaDTO.setCriteriaID(criteriaDetailDTO.getCriteriaDetailId());
        statisticalCriteriaDTO.setContentVi(criteriaDetailDTO.getContentVi());
        int[] UrbanJoinCriteriaSetIDs = evaluationVersionRepository.countUrbanEvaluatedAutoInYearBy(yearLD);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(UrbanJoinCriteriaSetIDs.length);
        int sumUrbanPassCriteria = evaluationVersionRepository.countUrbanJoinOrPassCriteriaCInYear(criteriaDetailId, yearLD, true);
        float percentUrbanPassCriteria = ((float) sumUrbanPassCriteria / UrbanJoinCriteriaSetIDs.length) * 100;
        String formattedPercentUrbanPassCriteria = String.format("%.1f", percentUrbanPassCriteria);
        statisticalCriteriaDTO.setSumUrbanJoinCriteria(UrbanJoinCriteriaSetIDs.length);
        statisticalCriteriaDTO.setSumUrbanPassCriteria(sumUrbanPassCriteria);
        statisticalCriteriaDTO.setPercentUrbanPassCriteria(Float.parseFloat(formattedPercentUrbanPassCriteria));
        return ResponseEntity.ok(new Response().setData(statisticalCriteriaDTO).setMessage("Successfully!"));
    }

    public static String getRandomHexColor() {
        Random obj = new Random();
        int rand_num = obj.nextInt(0xffffff + 1);
        return String.format("#%06x", rand_num);
    }
}
