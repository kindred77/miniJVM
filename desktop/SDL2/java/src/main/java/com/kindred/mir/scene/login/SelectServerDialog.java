package com.kindred.mir.scene.login;

import com.kindred.mir.controls.MirButton;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class SelectServerDialog extends MirControlWithStaticImage {

  private MirButton serverButton;

  public SelectServerDialog(MirControl parent, long renderer_id,
      MirImage image) throws Exception {
    super(parent, renderer_id, image);

    MirImage[] imgs = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImages(new int[]{257, 258});
    serverButton = new MirButton(this, renderer_id, imgs[0], imgs[0], imgs[1],
        new Size(76,33), new Point(170,163));
    serverButton.setIsBorder(true);
  }
}
