package com.missionsurvive.scenarios.controlscenarios;

import com.missionsurvive.framework.TouchControl;
import com.missionsurvive.map.Map;
import com.missionsurvive.map.MapEditor;
import com.missionsurvive.scenarios.PlatformerScenario;
import com.missionsurvive.scenarios.PlayScript;
import com.missionsurvive.screens.GameScreen;

public class EditorScenario extends PlatformerScenario{

    private static boolean isMapLoading = false;

    public EditorScenario(GameScreen screen, MapEditor mapEditor,
                              PlayScript playScript,
                              TouchControl touchControl){
        super(screen, mapEditor,
                playScript,
                touchControl);
    }

    @Override
    public void update(Map map, TouchControl touchControl, float deltaTime) {
        super.update(map, touchControl, deltaTime);
        if(isMapLoading){
            super.fillSpawnHolders();
            isMapLoading = false;
        }
    }

    /**
     * When we load map through from external storage, we set isMapLoading to
     * true in order for parent class could fill array lists for spawn holders.
     * @param isLoading
     */
    public static void setMapLoading(boolean isLoading){
        isMapLoading = isLoading;
    }

}
