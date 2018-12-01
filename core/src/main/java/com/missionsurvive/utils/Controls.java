package com.missionsurvive.utils;

import com.badlogic.gdx.utils.XmlReader;
import com.missionsurvive.MSGame;
import com.missionsurvive.framework.Button;
import com.missionsurvive.framework.ControlPanel;
import com.missionsurvive.framework.Listener;
import com.missionsurvive.framework.XML;
import com.missionsurvive.framework.impl.ActionButton;
import com.missionsurvive.framework.impl.Icon;
import com.missionsurvive.framework.impl.ListButtons;
import com.missionsurvive.framework.impl.ListButtonsTouchListener;
import com.missionsurvive.framework.impl.Rect;

import java.util.ArrayList;
import java.util.List;

public class Controls {

    static XML xml;
    static List<String> listOfTags = new ArrayList<String>();
    public static ControlPanel[] controlPanels;

    public static void setControls(MSGame game){
       ...
    }

    public static void setRects(ControlPanel controlPanel, XmlReader.Element[] controlTags){
        ...
    }

    public static void setIcons(ControlPanel controlPanel, XmlReader.Element[] controlTags){
        ...
    }

    public static void setButtons(ControlPanel panel, XmlReader.Element[] controlTags){
        ...
    }

    public static float getFloatValue(float defaultValue, String attrStr){
        if(attrStr != null){
            return Float.parseFloat(attrStr);
        }
        return defaultValue;
    }

    /**
     * Check whether attrubute exists. If not - returns default value.
     * @param defaultValue
     * @param attrStr
     * @return
     */
    public static int getIntValue(int defaultValue, String attrStr){
        if(attrStr != null){
            return Integer.parseInt(attrStr);
        }
        return defaultValue;
    }

    public static void setListButtons(ControlPanel controlPanel, XmlReader.Element[] controlTags){
        ...
    }
}
