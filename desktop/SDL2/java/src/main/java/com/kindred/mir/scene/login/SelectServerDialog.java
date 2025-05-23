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
import java.util.ArrayList;
import java.util.List;

public class SelectServerDialog extends MirControlWithStaticImage {

  private MirButton closeButton;
  private List<ServerButton> serverButtons=new ArrayList<>();
  private String serverNames[];
  public ControlCommonListener onSuccessClose;

  class ServerButton extends MirButton{

    public ServerButton(MirControl parent, long renderer_id, MirImage normalImage, MirImage hoverImage,
        MirImage pressedImage, String text,int idx) throws Exception {

      super(parent, renderer_id, normalImage, hoverImage, pressedImage, text,
          new Size((int)(normalImage.getTrueSize().getWidth()*0.3),(int)(normalImage.getTrueSize().getHeight()*0.5)),
          Settings.FONT_SIZE20, Color.Yellow, new Color(0,0,0,255), 0);
      Point center = Center();
      Size size = getSize();
      int gapY=10;
      int topGap=60;
      setLocation(new Point(center.getX(),(idx*size.getHeight()+gapY*(idx))+topGap));
    }

  }

  public SelectServerDialog(MirControl parent, long renderer_id,
      MirImage image,String[] serverNames) throws Exception {
    super(parent, renderer_id, image);
    this.serverNames=serverNames;
    MirImage[] imgs = MirLibFactory.Prguse.GetMirImages(new int[]{257, 258});
    for (int i=0; i<this.serverNames.length; ++i) {
      String serverName=this.serverNames[i];
      ServerButton serverButton = new ServerButton(this, renderer_id, imgs[0], imgs[0], imgs[1],serverName,i);
      serverButton.setOnMouseLeftClick((control, argObj) -> {
        this.setIsVisible(false);
        if (onSuccessClose != null) {
          onSuccessClose.doAction(this, serverName);
        }
      });
      serverButtons.add(serverButton);
    }

    MirImage closePressedImg = MirLibFactory.Prguse.GetMirImage(64);
    closeButton = new MirButton(this, renderer_id, null, null, closePressedImg,
        closePressedImg.getTrueSize(), new Point(244,30));
    closeButton.setIsBorder(true);
    closeButton.setOnMouseLeftClick((control, argObj) -> {
      MirMain.exit();
    });
    System.out.println("closeButton------ID: "+closeButton.getID());

  }

  public void setOnSuccessClose(ControlCommonListener onSuccessClose)
  {
    this.onSuccessClose = onSuccessClose;
  }
}
