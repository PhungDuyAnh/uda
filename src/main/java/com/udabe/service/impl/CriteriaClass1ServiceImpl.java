package com.udabe.service.impl;

import com.udabe.cmmn.base.BaseCrudService;
import com.udabe.cmmn.base.Response;
import com.udabe.dto.criteria.CriteriaClassDTO;
import com.udabe.dto.criteria.form.CriteriaSetDTOForm;
import com.udabe.dto.criteria.preview.CriteriaClass1DTOPr;
import com.udabe.dto.criteria.preview.CriteriaDetailDTOPr;
import com.udabe.dto.criteria.preview.CriteriaSetDTOPr;
import com.udabe.entity.CriteriaClass1;
import com.udabe.entity.CriteriaClass2;
import com.udabe.entity.CriteriaClass3;
import com.udabe.entity.CriteriaSet;
import com.udabe.payload.response.MessageResponse;
import com.udabe.repository.*;
import com.udabe.service.CriteriaClass1Service;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class CriteriaClass1ServiceImpl extends BaseCrudService<CriteriaClass1, Long> implements CriteriaClass1Service {

    private final ModelMapper modelMapper;

    private final CriteriaClass1Repository criteriaClass1Repository;

    private final CriteriaSetRepository criteriaSetRepository;

    private final CriteriaClass2Repository criteriaClass2Repository;

    private final CriteriaClass3Repository criteriaClass3Repository;

    private final CriteriaDetailRepository criteriaDetailRepository;

    private final EvaluationRepository evaluationRepository;

    //Ánh xạ classNumber và repository:
    private Map<Long, JpaRepository<?, Long>> repositoryMap = new HashMap<>();

    @Autowired
    public CriteriaClass1ServiceImpl(ModelMapper modelMapper, CriteriaClass1Repository criteriaClass1Repository,
                                     CriteriaSetRepository criteriaSetRepository, CriteriaClass2Repository criteriaClass2Repository,
                                     CriteriaClass3Repository criteriaClass3Repository, CriteriaDetailRepository criteriaDetailRepository,
                                     EvaluationRepository evaluationRepository) {
        this.modelMapper = modelMapper;
        this.criteriaClass1Repository = criteriaClass1Repository;
        this.criteriaSetRepository = criteriaSetRepository;
        this.criteriaClass2Repository = criteriaClass2Repository;
        this.criteriaClass3Repository = criteriaClass3Repository;
        this.criteriaDetailRepository = criteriaDetailRepository;
        this.evaluationRepository = evaluationRepository;
        repositoryMap.put(1L, criteriaClass1Repository);
        repositoryMap.put(2L, criteriaClass2Repository);
        repositoryMap.put(3L, criteriaClass3Repository);
        repositoryMap.put(4L, criteriaDetailRepository);
        repositoryMap.put(5L, evaluationRepository);
        super.setRepository(criteriaClass1Repository);
    }


    @Override
    public ResponseEntity<?> previewAllClass(Long criteriaSetId) {
        Optional<CriteriaSet> entity = criteriaSetRepository.findById(criteriaSetId);
        if (entity.isPresent()) {
            return ResponseEntity.ok(new Response().setData(criteriaClass1Repository.preview(criteriaSetId).stream()
                    .map(criteriaClass1 -> modelMapper.map(criteriaClass1, CriteriaClass1DTOPr.class))
            .collect(Collectors.toList())).setMessage("Found!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't not found with ID: " + criteriaSetId));
        }
    }


    @Override
    public ResponseEntity<?> getAllCriteriaClass(Long parentId, Long classNumber) {
        if(classNumber == 1L) {
            return new ResponseEntity<>(criteriaClass1Repository.getAllCriteriaClass1(parentId), HttpStatus.OK);
        } else if(classNumber == 2L) {
            return new ResponseEntity<>(criteriaClass2Repository.getAllCriteriaClass2ByClass1Id(parentId), HttpStatus.OK);
        } else if(classNumber == 3L) {
            return new ResponseEntity<>(criteriaClass3Repository.getAllCriteriaClass3ByClass2Id(parentId), HttpStatus.OK);
        } else if(classNumber == 4L) {
            CriteriaClass3 criteriaClass3 = criteriaClass3Repository.findById(parentId).get();
            return new ResponseEntity<>(criteriaDetailRepository.findAllByCriteriaClass3(criteriaClass3).stream().map(criteriaClassDetail -> modelMapper.map(criteriaClassDetail, CriteriaDetailDTOPr.class))
                    .collect(Collectors.toList()), HttpStatus.OK);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't find Criteria Class"));
        }
    }


    @Override
    public ResponseEntity<?> findCriteriaClassById(Long id, Long classNumber) {
        Object result;
        if(classNumber == 1L) {
            result = criteriaClass1Repository.findCriteriaClass1ById(id);
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        }else if(classNumber == 2L) {
            result = criteriaClass2Repository.findcriteriaClass2ById(id);
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        }else if(classNumber == 3L) {
            result = criteriaClass3Repository.findcriteriaClass3ById(id);
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        }else if(classNumber == 4L) {
            result = criteriaDetailRepository.findCriteriaDetailById(id);
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        } else if(classNumber == 5L) {
            result = evaluationRepository.findEvalutionDetailById(id);
            return ResponseEntity.ok(new Response().setData(result).setMessage("Successfully!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't find Criteria Class"));
        }
    }


    @Override
    public ResponseEntity<?> updateCriteriaClass(CriteriaClassDTO criteriaClassDTO, Long id) {
        if(criteriaClassDTO.getClassNumber() == 1L) {
            criteriaClass1Repository.updateCriteriaClass1(criteriaClassDTO.getContentVi() , id);
            return ResponseEntity.ok("Successfully!");
        } else if (criteriaClassDTO.getClassNumber() == 2L) {
            criteriaClass2Repository.updateCriteriaClass2(criteriaClassDTO.getContentVi(), id);
            return ResponseEntity.ok("Successfully!");
        }
        else if(criteriaClassDTO.getClassNumber() == 3L) {
            criteriaClass3Repository.updateCriteriaClass3(criteriaClassDTO.getContentVi(), id);
            return ResponseEntity.ok("Successfully!");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't update Criteria Class"));
        }
    }


    @Override
    public ResponseEntity<?> deleteCriteriaClass(Long id, Long classNumber) {
        JpaRepository<?, Long> repository = repositoryMap.get(classNumber);
        if (repository != null) {
            if(classNumber == 4L) {
                CriteriaSetServiceImpl.division(id, "delete");
            }
            if(classNumber == 5L) {
                evaluationRepository.deleteEvaluationByID(id);
                return ResponseEntity.ok(new MessageResponse("Deleted!"));
            }
            repository.deleteById(id);
            return ResponseEntity.ok(new MessageResponse("Deleted!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't delete Criteria Class"));
        }
    }


    @Override
    public ResponseEntity<?> saveCriteriaClass1(CriteriaClass1 criteriaClass1) {
        if(criteriaClass1.getClassNumber() == 1L) {
            criteriaClass1.setCriteriaSet(criteriaSetRepository.findById(criteriaClass1.getParentId()).get());
            CriteriaClass1 criteriaClass1Save = criteriaClass1Repository.save(criteriaClass1);
            return ResponseEntity.ok(new Response().setData(criteriaClass1Save).setMessage("Saved!"));
        } else if (criteriaClass1.getClassNumber() == 2L) {
            CriteriaClass2 criteriaClass2 = new CriteriaClass2();
            criteriaClass2.setContentVi(criteriaClass1.getContentVi());
            criteriaClass2.setCriteriaClass1(criteriaClass1Repository.findById(criteriaClass1.getParentId()).get());
            CriteriaClass2 criteriaClass2Save = criteriaClass2Repository.save(criteriaClass2);
            return ResponseEntity.ok(new Response().setData(criteriaClass2Save).setMessage("Saved!"));
        } else if (criteriaClass1.getClassNumber() == 3L) {
            CriteriaClass3 criteriaClass3 = new CriteriaClass3();
            criteriaClass3.setContentVi(criteriaClass1.getContentVi());
            criteriaClass3.setCriteriaClass2(criteriaClass2Repository.findById(criteriaClass1.getParentId()).get());
            CriteriaClass3 criteriaClass3Save = criteriaClass3Repository.save(criteriaClass3);
            return ResponseEntity.ok(new Response().setData(criteriaClass3Save).setMessage("Saved!"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new MessageResponse("Can't save Criteria Class"));
        }
    }

}
