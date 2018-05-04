package com.missionsurvive.framework;

import com.missionsurvive.map.MapTer;
import com.missionsurvive.map.ScrollMap;

/**
 * Created by kuzmin on 03.05.18.
 */

public interface Physics {

    public Vector calculateVector(MapTer[][] map, ScrollMap scrollMap,
                                  int vectStartX, int vectStartY, Vector vector, int tileSize, int isAction);
}
