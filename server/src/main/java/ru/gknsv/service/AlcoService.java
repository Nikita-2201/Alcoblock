package ru.gknsv.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gknsv.model.Alco;
import ru.gknsv.repository.AlcoRepository;

import java.util.List;

@Service
public class AlcoService {

	@Autowired
	private AlcoRepository alcoRepository;

	public List<Alco> getAlco(String userId) {
		return alcoRepository.findAllByUserIdIsNullOrUserId(userId);
	}

	@Transactional
	public Alco addPersonalAlco(final Alco newAlco) {
		if (newAlco.getUserId() != null) {
			newAlco.setPictureId("6");
			Alco saved = alcoRepository.save(newAlco);
			return saved;
		} else return null;
	}

	@Transactional
	public void deletePersonalAlco(String alcoId) {
		if (getAlcoById(alcoId) != null) {
			alcoRepository.deleteAlcoById(alcoId);
		}
	}

	public Alco getAlcoById(String alcoId) {
		return alcoRepository.findAlcoById(alcoId);
	}
}
