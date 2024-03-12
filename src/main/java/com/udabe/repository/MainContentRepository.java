package com.udabe.repository;

import com.udabe.entity.MainContent;
import com.udabe.entity.NewsImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MainContentRepository extends JpaRepository<MainContent, Long> {

    @Query(nativeQuery = true,
            value = "SELECT * FROM main_content " +
                    "WHERE group_id like ?1%")
    Optional<MainContent> getByGroupIdContent(String groupId);

    List<MainContent> findByGroupId(String groupId);
}
