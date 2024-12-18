package com.kindred.mir.scene.charsel;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.controls.MirLabel;
import com.kindred.mir.controls.MirScene;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

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

  public CharSelScene(MirControl parent, long window_id, long renderer_id, MirSceneData sceneData) throws Exception{
    super(parent, window_id, renderer_id,sceneData);
    SceneType= SceneEnumType.CharSel;
    MirImage backgroundImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(BACKGROUND_IMG_INDEX);
    background=new MirControlWithStaticImage(this,renderer_id,backgroundImg);
    background.setIsUseOffSet(false);

    titleLabel=new MirLabel(background,renderer_id,new Size(105,20),new Point(0,0),
      Settings.FONT_SIZE20,((CharSelSceneData)sceneData).title, Color.White, Color.Black,0);
    Point pos = titleLabel.Top();
    titleLabel.setLocation(new Point(pos.getX(),pos.getY()+4));

  }

  @Override
  public void process() {

  }
}
