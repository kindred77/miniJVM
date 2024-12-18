package com.kindred.mir.scene;

import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirItemCell;
import com.kindred.mir.controls.MirScene;

public class GameScene extends MirScene {

    public static class GameSceneData extends MirSceneData {

    }

    public static MirItemCell SelectedCell;
    public static boolean PickedUpGold;

    public static boolean CanMove, CanRun;

    public GameScene(MirControl parent, long window_id, long renderer_id,MirSceneData sceneData) {
        super(parent, window_id, renderer_id,sceneData);
        SceneType= SceneEnumType.Game;
    }

    @Override
    public void process() {

    }
}
