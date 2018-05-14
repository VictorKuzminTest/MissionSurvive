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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kuzmin on 05.05.18.
 */

public class Controls {

    static XML xml;
    static List<String> listOfTags = new ArrayList<String>();    //в этом листиннге содержится инфа о каждом тэге (имя тэга, и его аттрибуты).
    public static ControlPanel[] controlPanels;

    public static void setControls(MSGame game){
        xml = game.getXMLParser();

        XmlReader.Element root = xml.getRoot("xml/control.xml");
        XmlReader.Element[] cpanelsTags = xml.getChildNodes(root, "controlPanel");
        int controlPanelsCount = cpanelsTags.length;

        controlPanels = new ControlPanel[controlPanelsCount];

        for(int i = 0; i < controlPanelsCount; i++){
            String screen = xml.getAttrValue(cpanelsTags[i], "screen");
            String name = xml.getAttrValue(cpanelsTags[i], "name");

            ControlPanel controlPanel = new ControlPanel(screen, name);
            controlPanels[i] = controlPanel;

            setButtons(controlPanel, xml.getChildNodes(cpanelsTags[i], "button"));
            setListButtons(controlPanel, xml.getChildNodes(cpanelsTags[i], "listbuttons"));
            setIcons(controlPanel, xml.getChildNodes(cpanelsTags[i], "icon"));
        }
    }

    public static void setIcons(ControlPanel controlPanel, XmlReader.Element[] controlTags){
        if(controlTags != null){
            int iconsCount = controlTags.length;
            for(int i = 0; i < iconsCount; i++){
                String iconName = null;
                String assetName = null;
                int x = 0;
                int y = 0;
                int srcX = 0;
                int srcY = 0;
                int width = 0;
                int height = 0;

                iconName = xml.getAttrValue(controlTags[i], "iconName");
                assetName = xml.getAttrValue(controlTags[i], "assetName");
                x = getIntValue(x, xml.getAttrValue(controlTags[i], "x"));
                y = getIntValue(y, xml.getAttrValue(controlTags[i], "y"));
                srcX = getIntValue(srcX, xml.getAttrValue(controlTags[i], "srcX"));
                srcY = getIntValue(srcY, xml.getAttrValue(controlTags[i], "srcY"));
                width = getIntValue(width, xml.getAttrValue(controlTags[i], "width"));
                height = getIntValue(height, xml.getAttrValue(controlTags[i], "height"));

                controlPanel.addIcon(new Icon(iconName, assetName, x, y, srcX, srcY, width, height));
            }
        }
    }

    public static void setButtons(ControlPanel panel, XmlReader.Element[] controlTags){
        if(controlTags != null){
            int buttonsCount = controlTags.length;
            for(int i = 0; i < buttonsCount; i++){
                String assetName = null;
                int x = 0;
                int y = 0;
                int srcX = 0;
                int srcY = 0;
                int width = 0;
                int height = 0;
                String buttonType = null;
                String action = null;

                assetName = xml.getAttrValue(controlTags[i], "assetName");
                x = getIntValue(x, xml.getAttrValue(controlTags[i], "x"));
                y = getIntValue(y, xml.getAttrValue(controlTags[i], "y"));
                srcX = getIntValue(srcX, xml.getAttrValue(controlTags[i], "srcX"));
                srcY = getIntValue(srcY, xml.getAttrValue(controlTags[i], "srcY"));
                width = getIntValue(width, xml.getAttrValue(controlTags[i], "width"));
                height = getIntValue(height, xml.getAttrValue(controlTags[i], "height"));
                buttonType = xml.getAttrValue(controlTags[i], "buttonType");
                action = xml.getAttrValue(controlTags[i], "action");

                panel.addButton(new ActionButton(assetName, x, y, srcX, srcY, width, height,
                        Commands.getCommand(action)));
            }
        }
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
        if(controlTags != null){
            int listsCount = controlTags.length;
            if(listsCount > 0){
                Listener listener = new ListButtonsTouchListener();
                ListButtons rootList = new ListButtons(listener);
                controlPanel.addListButtons(rootList);

                for(int i = 0; i < listsCount; i++){
                    String listingName = null;
                    String assetName = null;
                    int x = 0;
                    int y = 0;
                    int srcBgX = 0;
                    int srcBgY = 0;
                    int width = 0;
                    int height = 0;
                    int spaceBetweenButtons = 5;
                    String layout = null;

                    listingName = xml.getAttrValue(controlTags[i], "listingName");
                    assetName = xml.getAttrValue(controlTags[i], "assetName");
                    x = getIntValue(x, xml.getAttrValue(controlTags[i], "x"));
                    y = getIntValue(y, xml.getAttrValue(controlTags[i], "y"));
                    srcBgX = getIntValue(srcBgX, xml.getAttrValue(controlTags[i], "srcBgX"));
                    srcBgY = getIntValue(srcBgY, xml.getAttrValue(controlTags[i], "srcBgY"));
                    width = getIntValue(width, xml.getAttrValue(controlTags[i], "width"));
                    height = getIntValue(height, xml.getAttrValue(controlTags[i], "height"));
                    spaceBetweenButtons = getIntValue(spaceBetweenButtons,
                            xml.getAttrValue(controlTags[i], "spaceBetweenButtons"));
                    layout = xml.getAttrValue(controlTags[i], "layout");

                    ListButtons listButtons = new ListButtons(listingName, assetName, spaceBetweenButtons,
                            x, y, srcBgX, srcBgY, width, height, layout);
                    listener.attach(listButtons);
                    rootList.addList(listButtons);
                }
            }
        }
    }



}
