package com.kindred.mir.scene.login;

import com.kindred.mir.MirMain;
import com.kindred.mir.Settings;
import com.kindred.mir.controls.MirButton;
import com.kindred.mir.controls.MirControl;
import com.kindred.mir.controls.MirControlWithStaticImage;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import com.kindred.mir.util.Size;

public class SelectServerDialog extends MirControlWithStaticImage {

  private MirButton serverButton, closeButton;
  private String serverText;
  public ControlCommonListener onSelected;

  class ServerButton extends MirButton{

    public ServerButton(MirControl parent, long renderer_id, MirImage normalImage, MirImage hoverImage,
        MirImage pressedImage, String text) throws Exception {

      super(parent, renderer_id, normalImage, hoverImage, pressedImage, text,
          new Size((int)(normalImage.getTrueSize().getWidth()*0.3),(int)(normalImage.getTrueSize().getHeight()*0.5)),
          Settings.FONT_SIZE20, Color.Yellow, new Color(0,0,0,255), 0);
      setLocation(Center());
    }

  }

  public SelectServerDialog(MirControl parent, long renderer_id,
      MirImage image,String serverText) throws Exception {
    super(parent, renderer_id, image);
    this.serverText=serverText;
    MirImage[] imgs = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImages(new int[]{257, 258});
    serverButton = new ServerButton(this, renderer_id, imgs[0], imgs[0], imgs[1],serverText);
    serverButton.setOnMouseLeftClick((control, argObj) -> {
      this.setIsVisible(false);
      onSelected.doAction(this, this.serverText);
    });
    System.out.println("serverButton------ID: "+serverButton.getID());
    MirImage closePressedImg = MirLibFactory.getMirLib(MirLibFactory.Prguse).GetMirImage(64);
    closeButton = new MirButton(this, renderer_id, null, null, closePressedImg,
        closePressedImg.getTrueSize(), new Point(244,30));
    closeButton.setIsBorder(true);
    closeButton.setOnMouseLeftClick((control, argObj) -> {
      MirMain.exit();
    });
    System.out.println("closeButton------ID: "+closeButton.getID());

  }

  public void setOnSelected(ControlCommonListener onSelected)
  {
    this.onSelected = onSelected;
  }
}
