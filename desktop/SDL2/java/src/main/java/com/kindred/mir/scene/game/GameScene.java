package com.kindred.mir.scene.game;

import com.kindred.mir.GameCommon.ChatType;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirItemCell;
import com.kindred.mir.scene.MirScene;
import com.kindred.mir.scene.game.objects.UserObject;

public class GameScene extends MirScene {

    public static class GameSceneData extends MirSceneData {

    }

    public static MirItemCell SelectedCell;
    public static boolean PickedUpGold;
    public static UserObject User;

    public static boolean CanMove, CanRun;

    public GameScene(MirControl parent, long window_id, long renderer_id,MirSceneData sceneData) {
        super(parent, window_id, renderer_id,SceneEnumType.Game,sceneData);
    }

    public void receiveChat(String msg, ChatType chatType) {

    }

    @Override
    public void process() {

    }
}
