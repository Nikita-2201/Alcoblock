package ru.gknsv.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gknsv.model.History;
import ru.gknsv.repository.HistoryRepository;
import java.util.*;

@Service
public class HistoryService {

    public static final Logger LOG = LoggerFactory.getLogger(HistoryService.class);

    @Autowired
    HistoryRepository historyRepository;

    public List<Date> getHistory(String userId) {
        return historyRepository.findAllByUserIdOrderByDateDesc(userId);
    }

    @Transactional
    public History addHistory(History newHistory) {
        try {
            LOG.info("Server: Добавили историю {}", newHistory.getDate());
            return historyRepository.save(newHistory);

        } catch (Exception e) {
            LOG.error("Server: Ошибка в создание {}", e.getMessage());
            return null;
        }
    }

    public History getLastHistory(String userId) {
        History lastHistory = historyRepository.findFirstByUserIdOrderByDateDesc(userId);
        if (lastHistory == null) {
            return null;
        } else {
            LOG.info("Server: Последняя запись в истории, userID {}, Тело {}", userId, lastHistory);
            return lastHistory;
        }
    }

    public long calculationDate(History lastHistory, Date dateNewHistory) {
        long between = dateNewHistory.getTime() - lastHistory.getDate().getTime();
        LOG.info("Server: Разница по времени {}", between);
        return between;
    }
}
