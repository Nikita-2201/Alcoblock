package ru.gknsv.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gknsv.model.Alco;
import java.util.List;

@Repository
public interface AlcoRepository extends JpaRepository<Alco, String> {

	List<Alco> findAllByUserIdIsNullOrUserId(String userId);
	Alco findAlcoById(String alcoId);
	void deleteAlcoById(String alcoId);
}
