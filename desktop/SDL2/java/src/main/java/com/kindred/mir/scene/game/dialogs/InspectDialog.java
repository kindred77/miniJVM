package com.kindred.mir.scene.game.dialogs;

import com.kindred.mir.GameCommon.MirClass;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.libs.MirImage;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class InspectDialog extends MirControlWithStaticImage {

  @Getter
  @Setter
  private int level;
  @Getter
  @Setter
  private MirClass mirClass;

  public InspectDialog(MirControl parent, long renderer_id,
      MirImage image) throws Exception {
    super(parent, renderer_id, image);
  }
}
