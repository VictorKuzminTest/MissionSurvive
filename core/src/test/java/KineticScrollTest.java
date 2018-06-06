import com.missionsurvive.framework.KineticScroll;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by kuzmin on 02.06.18.
 */
public class KineticScrollTest {

    private KineticScroll kineticScroll;

    @Before
    public void initKineticScroll(){
        kineticScroll = new KineticScroll();
    }

    @Test
    public void traceTouch(){
        float lastX = 25;
        float lastY = 25;
        float currentX = 50;
        float currentY = 50;
        kineticScroll.traceTouch(KineticScroll.DELTA_TRACE_TICK, lastX, lastY);
        float nextDelta = KineticScroll.DELTA_TRACE_TICK;
        kineticScroll.traceTouch(nextDelta, currentX, currentY);
        assertTrue(kineticScroll.getSpeedX() != 0);
        assertTrue(kineticScroll.getSpeedY() != 0);
    }

    @Test
    public void braking(){
        float startSpeedX = KineticScroll.BRAKING_SPEED;
        float startSpeedY = KineticScroll.BRAKING_SPEED;
        kineticScroll.setSpeedXY(startSpeedX, startSpeedY);
        kineticScroll.brakingX();
        kineticScroll.brakingY();
        assertTrue(kineticScroll.getSpeedX() < startSpeedX);
        assertTrue(kineticScroll.getSpeedY() < startSpeedY);
    }
}
