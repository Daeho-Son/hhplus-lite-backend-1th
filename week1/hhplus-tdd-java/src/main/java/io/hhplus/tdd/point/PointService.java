package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PointService {

    private static final Logger log = LoggerFactory.getLogger(PointService.class);

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    @Autowired
    PointService(UserPointTable userPointTable, PointHistoryTable pointHistoryTable) {
        this.userPointTable = userPointTable;
        this.pointHistoryTable = pointHistoryTable;
    }

    public UserPoint point(long id) {
        UserPoint userPoint = userPointTable.selectById(id);
        log.debug("userPoint: {}", userPoint);
        return userPoint;
    }

    public List<PointHistory> history(long id) {
        List<PointHistory> historyList = pointHistoryTable.selectAllByUserId(id);
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
