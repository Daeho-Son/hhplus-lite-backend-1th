package io.hhplus.tdd.point;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PointServiceTest {

    @InjectMocks
    private PointService pointService;

    @Test
    void point_id_1을_요청하면_id_1의_포인트_정보를_반환한다() throws Exception {
        // given
        long userId = 1L;

        // when
        UserPoint userPoint = pointService.point(userId);

        //then
        assertEquals(userId, userPoint.id());
    }

    @Test
    void point_존재하지_않는_id를_요청하면_500_에러가_발생한다_1() {
        // given
        Random random = new Random();
        long randomOffset = random.nextLong(1000); // 0부터 999 사이의 랜덤 값
        long userId = Long.MAX_VALUE - randomOffset;

        // when & then
        assertThrows(Exception.class, () -> pointService.point(userId));
    }
}