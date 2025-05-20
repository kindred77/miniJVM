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
  private MirDirection direction;
  private byte hair;
  private int weapon;
  private int armour;
  private int Light;
  private PoisonType poison;
  private boolean isDead;
  private boolean isHidden;
  private byte wingEffect;
  private SpellEffect currentEffect;
  private short mountType;
  private boolean isRidingMount;
  private boolean isFishing;
  private short transformType;
  private boolean isExtra;
  private int elementEffect;
  private int elementsLevel;
  private int elementOrbMax;
  private List<BuffType> buffs;
  private LevelEffects levelEffects;
}
