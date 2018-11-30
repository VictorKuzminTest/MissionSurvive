
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.Observer;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.framework.impl.ButtonTouchListener;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ButtonTouchListenerTest {

    private ButtonTouchListener buttonTouchListener;

    private int width, height;
    private int numButtons;
    private int screenX;
    private int spaceYBetweenButtons;

    @Before
    public void initListener() {
        buttonTouchListener = new ButtonTouchListener();

        width = 60;
        height = 30;
        screenX = 10;
        spaceYBetweenButtons = 10;
        numButtons = 5;

        for(int i = 0; i < numButtons; i++){
            Observer button = new ActionButton(null, screenX, spaceYBetweenButtons + i * height,
                    0, 0, width, height, null);
            buttonTouchListener.attach(button);
        }
    }

    @Test
    public void instanceofListener(){
        assertTrue(buttonTouchListener instanceof Listener);
    }

    @Test
    public void onTouchDownButtonEvent(){
        int touchDownX = 15;
        int touchDownY = 15;
        //...getButtons().get(i) (could be chosen from 0 to numButtons). If it is chosen 1 (for example),
        // test fails, because it is outside of a buttonTouchListener.getButtons().get(0).
        Button touchDownButton = buttonTouchListener.getButtons().get(0);

        buttonTouchListener.getTouchDownEvent(touchDownX, touchDownY);

        assertEquals(touchDownButton, buttonTouchListener.getCurrentButton());
    }

    @Test
    public void onDraggedEvent(){
        int touchDownX = 15;
        int touchDownY = 15;

        buttonTouchListener.getTouchDownEvent(touchDownX, touchDownY);

        //DRAG INSIDE BUTTON:
        //one of the touchDragged could be changed (for example to touchDownX + 100) to get failed
        int touchDraggedX = touchDownX + 5;
        int touchDraggedY = touchDownY + 5;

        buttonTouchListener.getTouchDraggedEvent(touchDraggedX, touchDraggedY);

        assertEquals(ButtonTouchListener.STATE_DRAGGED_INSIDE,
                buttonTouchListener.getState());

        //DRAG OUTSIDE BUTTON:
        //go outside of the currentTouchDownButton event (in previous assert of changing
        // the touchDraggedX = touchDownX + 100 you would be failed)
        touchDraggedX = touchDownX + 100;
        buttonTouchListener.getTouchDraggedEvent(touchDraggedX, touchDraggedY);
        assertEquals(ButtonTouchListener.STATE_DRAGGED_OUTSIDE,
                buttonTouchListener.getState());
    }

    @Test
    public void onTouchUpEvent(){
        int touchDownX = 15;
        int touchDownY = 15;

        buttonTouchListener.getTouchDownEvent(touchDownX, touchDownY);


        //You can change the deltaX coord.
        int deltaX = 20;
        int touchUpX = touchDownX + deltaX;
        int touchUpY = touchDownY + 5;

        if(deltaX > (screenX + width) || deltaX < screenX){
            //UP EVENT OUTSIDE THE BUTTON:
            touchUpX = touchDownX + 100;
            buttonTouchListener.getTouchUpEvent(touchUpX, touchUpY);
            assertEquals(ButtonTouchListener.STATE_UP_OUTSIDE, buttonTouchListener.getState());
        }
        else{
            //UP EVENT INSIDE THE BUTTON:
            buttonTouchListener.getTouchUpEvent(touchUpX, touchUpY);
            assertEquals(ButtonTouchListener.STATE_UP_INSIDE, buttonTouchListener.getState());
        }
    }
}
