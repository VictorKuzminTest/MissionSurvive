package com.missionsurvive.framework;

import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;

public interface Physics {

    public Vector calculateVector(MapTer[][] map, ScrollMap scrollMap,
                                  int vectStartX, int vectStartY, Vector vector, int tileSize, int isAction);
}
