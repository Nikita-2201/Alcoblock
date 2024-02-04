package ru.gknsv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.gknsv.model.History;

import java.util.Date;
import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<History, String> {
    @Query("select distinct DATE(date) from History where userId = :userId")
    List<Date> findAllByUserIdOrderByDateDesc(@Param("userId") String userId);
    History findFirstByUserIdOrderByDateDesc(String userId);
}
