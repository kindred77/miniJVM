package com.kindred.mir.scene.charsel;

import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.controls.MirScene;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;

public class CharSelScene extends MirScene {

  private static final int BACKGROUND_IMG_INDEX=65;

  private MirControlWithStaticImage background;

  public CharSelScene(MirControl parent, long window_id, long renderer_id) throws Exception{
    super(parent, window_id, renderer_id);
    SceneType= SceneEnumType.CharSel;
    MirImage backgroundImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(BACKGROUND_IMG_INDEX);
    background=new MirControlWithStaticImage(this,renderer_id,backgroundImg);
    background.setIsUseOffSet(false);

  }

  @Override
  public void process() {

  }
}
