package com.kindred.mir.scene.game.dialogs;

import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.libs.MirImage;

public class ChatDialog extends MirControlWithStaticImage {

  public ChatDialog(MirControl parent, long renderer_id,
      MirImage image) throws Exception {
    super(parent, renderer_id, image);
  }
}
