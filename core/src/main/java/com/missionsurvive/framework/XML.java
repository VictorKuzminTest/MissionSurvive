package com.missionsurvive.framework;

import com.badlogic.gdx.utils.XmlReader;

import java.util.List;

/**
 * Created by kuzmin on 22.04.18.
 */

public interface XML {

    public String getAttrValue(XmlReader.Element element, String attrKey);

    public XmlReader.Element[] getChildNodes(XmlReader.Element element, String name);

    public XmlReader.Element getRoot(String filePath);
}
