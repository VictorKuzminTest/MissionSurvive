import com.missionsurvive.framework.Observer;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.framework.impl.ListButtonsTouchListener;
import com.missionsurvive.tests.commands.OnClickButtonLogCommand;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.junit.Assert.*;

/**
 * Created by kuzmin on 30.04.18.
 */

public class ListButtonsTouchListenerTest {

    private ListButtons rootList;
    private Observer listOne;
    private Observer listTwo;
    private ListButtonsTouchListener listener;

    private int listButtonWidth = 16;
    private int listButtonHeight = 16;
    private int numRowsInList = 5;
    private int numColsInList = 5;
    private int spaceBetweenButtonsInList = 5;
    private int listingWidth = 50;
    private int listingHeight = 50;

    @Before
    public void initListsAndListeners(){
        listOne = new ListButtons("list1", null, spaceBetweenButtonsInList,
                100, 100, 0, 0, listingWidth, listingHeight, "grid");

        listTwo = new ListButtons("list2", null, spaceBetweenButtonsInList,
                300, 100, 0, 0, listingWidth, listingHeight, "grid");


        fillListWithButtons((ListButtons) listOne);
        fillListWithButtons((ListButtons) listTwo);

        listener = new ListButtonsTouchListener();
        rootList = new ListButtons("root", listener);

        listener.attach(listOne);
        listener.attach(listTwo);
        rootList.addList((ListButtons) listOne);
        rootList.addList((ListButtons) listTwo);
    }

    public void fillListWithButtons(ListButtons listButtons){
        for(int row = 0; row < numRowsInList; row++){
            for(int col = 0; col < numColsInList; col++){
                listButtons.addNewButton(null, col, row, col * listButtonWidth, row * listButtonHeight,
                        listButtonWidth, listButtonHeight, null, new OnClickButtonLogCommand(row, col));
            }
        }
    }

    /**
     * To start working with listing of buttons, touch down event has to be occured inside listing.
     */
    @Test
    public void onTouchDownList(){
        int touchDownX = ((ListButtons)listOne).getStartX() + 5;
        int touchDownY = ((ListButtons)listOne).getStartY() + 5;

        listener.getTouchDownEvent(touchDownX, touchDownY);

        assertTrue(rootList.getLists().get(0).getName().equalsIgnoreCase("list1"));
    }

    @Test
    public void onDraggedList(){
        int touchDownX = ((ListButtons)listOne).getStartX() + 5;
        int touchDownY = ((ListButtons)listOne).getStartY() + 5;

        listener.getTouchDownEvent(touchDownX, touchDownY);

        //2 pixels less then min threshold requires to start scrolling:
        int thresholdX = ListButtonsTouchListener.SCROLLING_THRESHOLD - 2;
        int thresholdY = 0;
        listener.getTouchDraggedEvent(thresholdX, thresholdY, 0, 0);

        assertFalse(listener.isScrolling());

        thresholdX = ListButtonsTouchListener.SCROLLING_THRESHOLD + 1;
        listener.getTouchDraggedEvent(thresholdX, thresholdY, 0, 0);

        assertTrue(listener.isScrolling());
    }

}
