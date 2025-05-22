package io.hhplus.tdd.point;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);



    public UserPoint point(long id) throws Exception {
        UserPoint userPoint = new UserPoint(id, 0, 0);
        if (id == Long.MAX_VALUE) {
            throw new Exception();
        }
        log.info("userPoint: {}", userPoint);
        return userPoint;
    }

    public List<PointHistory> history(long id) {
        List<PointHistory> historyList = List.of();
        log.info("history의 개수: {}", historyList.size());
        return historyList;
    }

    public UserPoint charge(long id, long amount) {
        UserPoint userPoint = new UserPoint(0, 0, 0);
        log.info("userPoint: {}", userPoint);
        return userPoint;
    }

    public UserPoint use(long id, long amount) {
        UserPoint userPoint = new UserPoint(0, 0, 0);
        log.info("userPoint: {}", userPoint);
        return userPoint;
    }
}
