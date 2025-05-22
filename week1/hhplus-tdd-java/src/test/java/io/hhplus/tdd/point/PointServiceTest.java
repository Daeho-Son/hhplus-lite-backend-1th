package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.apache.catalina.User;
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

    @Mock
    private PointHistoryTable pointHistoryTable;

    @InjectMocks
    private PointService pointService;

    // 작성 이유: 가장 먼저 성공하는 테스트 케이스가 생각났기 때문
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


    // 작성 이유: 마찬가지로 성공하는 케이스가 생각났음
    // 의문: Mock을 반환해서 Mock에 사용된 데이터랑 비교하는게 어떤 의미가 있는지.. 제대로 작성한게 맞는건가?
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

    // 작성 이유: 기본적인 테스트 케이스
    @Test
    void history_존재하지_않는_id가_요청되면_빈_배열을_반환한다() {
        // given
        Random random = new Random();
        long randomOffset = random.nextLong(1000);
        long userId = Long.MAX_VALUE - randomOffset;
        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(List.of());

        // when
        List<PointHistory> expected = pointService.history(userId);

        // then
        assertEquals(0, expected.size());
    }


    // 작성 이유: 기본적인 테스트 케이스
    // 의문: 의미있는 테스트 코드인가?
    @Test
    void history_존재하는_id가_요청되면_리스트를_반환한다() {
        // given
        long userId = 1L;
        List<PointHistory> expected = List.of(
            new PointHistory(1L, 1L, 1L, TransactionType.CHARGE, 1L),
            new PointHistory(2L, 2L, 2L, TransactionType.USE, 2L),
            new PointHistory(3L, 3L, 3L, TransactionType.CHARGE, 3L)
        );

        when(pointHistoryTable.selectAllByUserId(userId)).thenReturn(expected);

        // when
        List<PointHistory> actual = pointService.history(userId);

        // then
        verify(pointHistoryTable, times(1)).selectAllByUserId(userId);
        assertEquals(expected.size(), actual.size());
    }

    @Test
    void charge_id_1이_100을_요청하면_id_1의_포인트_100을_충전한다() throws Exception {
        // given
        long userId = 1L;
        long amount = 100L;
        long currentTimeMillis = System.currentTimeMillis();

        when(userPointTable.selectById(userId)).thenReturn(new UserPoint(userId, 0, currentTimeMillis));
        when(userPointTable.insertOrUpdate(userId, amount)).thenReturn(new UserPoint(userId, amount, currentTimeMillis + 1000));

        // when
        UserPoint actual = pointService.charge(userId, amount);

        // then
        verify(userPointTable, times(1)).selectById(userId);
        verify(userPointTable, times(1)).insertOrUpdate(userId, amount);
        assertEquals(userId, actual.id());
        assertEquals(amount, actual.point());
    }

    @Test
    void charge_포인트가_int_최대값을_초과하면_예외_발생() throws Exception {
        // given
        long userId = 1L;
        long amount = Integer.MAX_VALUE + 1;

        // when & then
        assertThrows(Exception.class, () -> pointService.charge(userId, amount));
    }

    @Test
    void use_id_1이_100포인트를_소유하고_있을_때_100을_사용하면_id_1의_포인트는_0이_된다() throws Exception {
        // given
        long userId = 1L;
        long amount = 100L;

        UserPoint initUserPoint = new UserPoint(userId, 100L, System.currentTimeMillis());
        when(userPointTable.selectById(userId)).thenReturn(initUserPoint);
        when(userPointTable.insertOrUpdate(userId, initUserPoint.point() - amount)).thenReturn(new UserPoint(userId, initUserPoint.point() - amount, System.currentTimeMillis()));

        // when
        UserPoint actual = pointService.use(userId, amount);

        // then
        assertEquals(userId, actual.id());
        assertEquals(0, actual.point());
    }

    // 작성 이유: 예외를 위한 테스트 케이스. 깔끔하게 작성됐다.
    @Test
    void use_포인트가_음수가_되면_예외를_발생한다() {
        // given
        long userId = 1L;
        long amount = 100L;

        // when & then
        assertThrows(Exception.class, () -> pointService.use(userId, amount));
    }
}