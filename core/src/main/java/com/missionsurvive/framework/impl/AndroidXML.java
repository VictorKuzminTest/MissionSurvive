package com.missionsurvive.framework.impl;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.missionsurvive.framework.XML;

/**
 * Created by kuzmin on 22.04.18.
 */
public class AndroidXML implements XML {

    XmlReader xmlReader;

    public AndroidXML(){
        xmlReader = new XmlReader();
    }


    @Override
    public String getAttrValue(XmlReader.Element element, String attrKey) {
        String value = null;
        if(element.hasAttribute(attrKey)){
            value = element.getAttribute(attrKey);
        }
        return value;
    }

    @Override
    public XmlReader.Element[] getChildNodes(XmlReader.Element element, String name){
        XmlReader.Element[] childNodes = null;
        int numNodes = element.getChildrenByName(name).size;
        if(numNodes > 0){
            childNodes = new XmlReader.Element[numNodes];
            for(int i = 0; i < numNodes; i++){
                childNodes[i] = element.getChildrenByName(name).get(i);
            }
        }
        return childNodes;
    }

    @Override
    public XmlReader.Element getRoot(String filePath){
        XmlReader.Element root;
        FileHandle file = Gdx.files.internal(filePath);
        root = xmlReader.parse(file);
        return root;
    }
}
