package com.udabe.repository;

import com.udabe.dto.Dashboard.DashboardAddressDTO;
import com.udabe.dto.Urban.UrbanDTO;
import com.udabe.dto.user.AddressCodeDTO;
import com.udabe.dto.user.AddressCodeDTOInter;
import com.udabe.dto.user.AddressDetailDTO;
import com.udabe.entity.AddressCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public interface AddressCodeRepository extends JpaRepository<AddressCode, Long> {

    @Query("SELECT NEW com.udabe.dto.user.AddressCodeDTO" +
            "(ac.addressCodeId, ac.addressName)" +
            " FROM AddressCode ac WHERE ac.upperAddressSeq = 0 ORDER BY ac.addressName ASC ")
    List<AddressCodeDTO> findAllProvince();

    @Query("SELECT NEW com.udabe.dto.user.AddressCodeDTO" +
            "(ac.addressCodeId, ac.addressName)" +
            " FROM AddressCode ac WHERE ac.upperAddressSeq = ?1 ORDER BY ac.addressName ASC")
    List<AddressCodeDTO> findAllDistrict(Long addressCodeId);

    @Query("SELECT NEW com.udabe.dto.user.AddressDetailDTO" +
            "(ac.addressCodeId, ac.addressName, ac.upperAddressSeq)" +
            " FROM AddressCode ac WHERE ac.addressCodeId = ?1 OR ac.addressCodeId =?2")
    List<AddressDetailDTO> findAddressUserDetail(Long addressDistrict, Long addressProvince);

    @Query("SELECT NEW com.udabe.dto.Dashboard.DashboardAddressDTO" +
            "(ac.addressCodeId, ac.addressName, ac.upperAddressSeq, ac.latitude, ac.longitude)" +
            " FROM AddressCode ac WHERE ac.addressCodeId = ?1 OR ac.addressCodeId =?2")
    List<DashboardAddressDTO> findAddressUserDetailDashboard(Long addressDistrict, Long addressProvince) ;


    @Query(value = "select upper_address_seq from address_code where address_code_id = ?1", nativeQuery = true)
    Long findAddressProvince(Long id);


    @Query(nativeQuery = true,
            value = "SELECT ac.address_code_id as addressCodeId, ac.address_name as AddressName, u.user_id as userId " +
                    "FROM address_code ac JOIN users u on ac.address_code_id = u.address_code_id " +
                    "WHERE u.user_id = ?1 ")
    Optional<AddressCodeDTOInter> addressByUser(Long userId);

    @Query(nativeQuery = true,
    value = "SELECT address_name as addressName FROM address_code " +
            "WHERE address_code_id = ?1")
    String findNameById(Long addressCodeId);

    @Query("SELECT NEW com.udabe.dto.Urban.UrbanDTO(u.userID, u.fullName)" +
            "from Users u " +
            "join AddressCode ac on u.addressCodeId = ac.addressCodeId " +
            "where (:urbanName IS NULL OR u.fullName like CONCAT('%',:urbanName,'%')) " +
            "AND ac.upperAddressSeq = :addressCodeId and u.disable = 'N' " +
            "OR ac.addressCodeId = :addressCodeId " +
            "ORDER BY u.fullName ASC")
    List<UrbanDTO> getUrbanList(Long addressCodeId, String urbanName);

    @Query("SELECT NEW com.udabe.dto.user.AddressCodeDTO" +
            "(ac.addressCodeId, ac.addressName)" +
            " FROM AddressCode ac " +
            "WHERE (:provinceName IS NULL OR ac.addressName like CONCAT('%',:provinceName,'%'))" +
            "AND ac.upperAddressSeq = 0 " +
            "ORDER BY ac.addressName ASC ")
    List<AddressCodeDTO> searchProvince(String provinceName);
}
