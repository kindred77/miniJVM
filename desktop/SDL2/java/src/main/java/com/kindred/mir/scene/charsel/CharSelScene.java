package com.kindred.mir.scene.charsel;

import com.kindred.mir.Env;
import com.kindred.mir.Settings;
import com.kindred.mir.constcode.MirEnums;
import com.kindred.mir.constcode.MirEnums.MirGender;
import com.kindred.mir.constcode.MirEnums.MirJob;
import com.kindred.mir.controls.MirAnimatedControl;
import com.kindred.mir.controls.MirButton;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.scene.MirScene;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.scene.game.GameScene.GameSceneData;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.ExecutionService.Future;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;
import com.kindred.mir.util.Util;
import com.kindred.mir.scene.beans.*;

public class CharSelScene extends MirScene {

  public static class CharSelSceneData extends MirSceneData {
    String title;
    public CharSelSceneData(String title) {
      this.title=title;
    }
  }

  private static final int BACKGROUND_IMG_INDEX=65;

  private MirControlWithStaticImage background;
  private MirLabel titleLabel;
  private MirAnimatedControl myChars[];
  private MirButton startButton;

  private Future<MirScene> gameScene;

  public CharSelScene(MirControl parent, long window_id, long renderer_id, MirSceneData sceneData) throws Exception{
    super(parent, window_id, renderer_id,SceneEnumType.CharSel,sceneData);
    MirImage backgroundImg = MirLibFactory.Prguse.GetMirImage(BACKGROUND_IMG_INDEX);
    background=new MirControlWithStaticImage(this,renderer_id,backgroundImg);
    background.setIsUseOffSet(false);

    titleLabel=new MirLabel(background,renderer_id,new Size(105,20),new Point(0,0),
      Settings.FONT_SIZE20,((CharSelSceneData)sceneData).title, Color.White, Color.Black,0);
    Point pos = titleLabel.Top();
    titleLabel.setLocation(new Point(pos.getX(),pos.getY()+4));

    MirImage startBtnPressedImg = MirLibFactory.Prguse.GetMirImage(68);
    startButton=new MirButton(background,renderer_id,null,null,
        startBtnPressedImg,startBtnPressedImg.getTrueSize(),new Point(385,457));
    startButton.setOnMouseLeftClick((mirControl,arg) -> {
      this.gameScene= Env.BackGroundExeService.submit(() -> PrepareNextScene(window_id,renderer_id,new GameSceneData()));
      if (this.gameScene==null) {
        System.out.println("Can not prepare next scene!");
      }
      if (this.gameScene != null) {
        SwitchToScene(this.gameScene.get());
      }
    });
  }

  private int[] getImgIndexes(MirEnums.MirJob job, MirEnums.MirGender gender) {
    int begin;
    if (gender== MirGender.Man) {
      begin=40;
    } else {
      begin=160;
    }

    int jobOffset;
    if (job== MirJob.Warrior) {
      jobOffset=0;
    } else if (job== MirJob.Wilzard) {
      jobOffset=40;
    } else {
      jobOffset=80;
    }

    int cnt = 15;

    return Util.genSeq(begin+jobOffset,begin+jobOffset+cnt);
  }

  private void initMyChars(long renderer_id, MyCharInfo[] myCharInfos) throws Exception{
    myChars=new MirAnimatedControl[myCharInfos.length];
    for (int i=0; i<myCharInfos.length; ++i) {
      MirImage[] imgs =MirLibFactory.ChrSel.GetMirImages(
          getImgIndexes(myCharInfos[i].getJob(),myCharInfos[i].getGender()));
      myChars[i]=new MirAnimatedControl(background,renderer_id,imgs,true,200);
      myChars[i].setIsAnimated(true);
    }
  }

  @Override
  public void process() {

  }
}
