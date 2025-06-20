package com.kindred.mir.scene.game.bean;

import com.kindred.mir.GameCommon.BuffType;
import com.kindred.mir.GameCommon.LevelEffects;
import com.kindred.mir.GameCommon.MirClass;
import com.kindred.mir.GameCommon.MirDirection;
import com.kindred.mir.GameCommon.PoisonType;
import com.kindred.mir.GameCommon.SpellEffect;
import com.kindred.mir.constcode.MirEnums.MirGender;
import com.kindred.mir.util.Color;
import com.kindred.mir.util.Point;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class PlayerInfo {
  private String name;
  private Color nameColor;
  private String guildName;
  private String guildRankName;
  private MirClass mirClass;
  private MirGender gender;
  private int level;
  private Point currentLocation;
  private Point mapLocation;
  @Builder.Default
  private MirDirection direction=MirDirection.Down;
  private byte hair;
  private int weapon;
  private int armour;
  private int Light;
  @Builder.Default
  private PoisonType poison = PoisonType.None;
  @Builder.Default
  private boolean isDead=false;
  @Builder.Default
  private boolean isHidden=false;
  private byte wingEffect;
  @Builder.Default
  private SpellEffect currentEffect=SpellEffect.None;
  private short mountType;
  @Builder.Default
  private boolean isRidingMount=false;
  @Builder.Default
  private boolean isFishing=false;
  private short transformType;
  @Builder.Default
  private boolean isExtra=false;
  private int elementEffect;
  private int elementsLevel;
  private int elementOrbMax;
  @Builder.Default
  private List<BuffType> buffs=new ArrayList<>();
  @Builder.Default
  private LevelEffects levelEffects=LevelEffects.None;
}
