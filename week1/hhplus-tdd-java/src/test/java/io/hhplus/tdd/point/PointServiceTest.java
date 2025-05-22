package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @Mock
    private UserPointTable userPointTable;

    @InjectMocks
    private PointService pointService;

    @Test
    void point_id_1을_요청하면_id_1의_포인트_정보를_반환한다() {
        // given
        long userId = 1L;
        long expectedPoint = 100L;
        long currentTimeMillis = System.currentTimeMillis();

        UserPoint expectedUserPoint = new UserPoint(userId, expectedPoint, currentTimeMillis);
        when(userPointTable.selectById(userId)).thenReturn(expectedUserPoint);

        // when
        UserPoint userPoint = pointService.point(userId);

        //then
        assertEquals(userId, userPoint.id());
        assertEquals(expectedPoint, userPoint.point());
        assertEquals(currentTimeMillis, userPoint.updateMillis());
    }

    @Test
    void point_존재하지_않는_id를_요청하면_Userpoint_empty를_반환한다() {
        // given
        Random random = new Random();
        long randomOffset = random.nextLong(1000);
        long userId = Long.MAX_VALUE - randomOffset;
        long currentTimeMillis = System.currentTimeMillis();

        when(userPointTable.selectById(userId)).thenReturn(UserPoint.empty(userId));

        // when
        UserPoint userPoint = pointService.point(userId);

        // then
        assertEquals(userId, userPoint.id());
        assertEquals(0, userPoint.point());
        assertTrue(currentTimeMillis <= userPoint.updateMillis());
        assertTrue(userPoint.updateMillis() <= System.currentTimeMillis());
    }

    @Test
    void history_존재하지_않는_id가_요청되면_빈_배열을_반환한다() {
        // given
        long userId = 1L;

        // when
        List<PointHistory> expected = pointService.history(userId);

        // then
        assertEquals(0, expected.size());
    }
}