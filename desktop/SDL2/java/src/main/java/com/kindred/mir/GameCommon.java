package com.kindred.mir;

import com.kindred.mir.util.Point;

public class GameCommon {
  public class Door {
    public byte index;
    public byte DoorState;//0: closed, 1: opening, 2: open, 3: closing
    public byte ImageIndex;
    public long LastTick;
    public Point Location;
  }

  public enum MapObjectType
  {
    None((byte)0,"None"),
    Player((byte)1,"Player"),
    Item((byte)2,"Item"),
    Merchant((byte)3,"Merchant"),
    Spell((byte)4,"Spell"),
    Monster((byte)5,"Monster"),
    Deco((byte)6,"Deco"),
    Creature((byte)7,"Creature"),;

    private final byte code;
    private final String value;

    MapObjectType(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum MirDirection {
    Up((byte)0,"Up"),
    UpRight((byte)1,"UpRight"),
    Right((byte)2,"Right"),
    DownRight((byte)3,"DownRight"),
    Down((byte)4,"Down"),
    DownLeft((byte)5,"DownLeft"),
    Left((byte)6,"Left"),
    UpLeft((byte)7,"UpLeft"),;

    private final byte code;
    private final String value;

    MirDirection(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum LightSetting
  {
    Normal((byte)0,"Normal"),
    Dawn((byte)1,"Dawn"),
    Day((byte)2,"Day"),
    Evening((byte)3,"Evening"),
    Night((byte)4,"Night"),;

    private final byte code;
    private final String value;

    LightSetting(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum PoisonType
  {
    None(0,"None"),
    Green(1,"Green"),
    Red(2,"Red"),
    Slow(4,"Slow"),
    Frozen(8,"Frozen"),
    Stun(16,"Stun"),
    Paralysis(32,"Paralysis"),
    DelayedExplosion(64,"DelayedExplosion"),
    Bleeding(128,"Bleeding"),
    LRParalysis(256,"LRParalysis"),;

    private final int code;
    private final String value;

    PoisonType(int code,String value) {
      this.code = code;
      this.value=value;
    }

    public final int code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }

  public enum ChatType
  {
    Normal((byte)0,"Normal"),
    Shout((byte)1,"Shout"),
    System((byte)2,"System"),
    Hint((byte)3,"Hint"),
    Announcement((byte)4,"Announcement"),
    Group((byte)5,"Group"),
    WhisperIn((byte)6,"WhisperIn"),
    WhisperOut((byte)7,"WhisperOut"),
    Guild((byte)8,"Guild"),
    Trainer((byte)9,"Trainer"),
    LevelUp((byte)10,"LevelUp"),
    System2((byte)11,"System2"),
    Relationship((byte)12,"Relationship"),
    Mentor((byte)13,"Mentor"),
    Shout2((byte)14,"Shout2"),
    Shout3((byte)15,"Shout3"),;

    private final byte code;
    private final String value;

    ChatType(byte code,String value) {
      this.code = code;
      this.value=value;
    }

    public final byte code() {
      return this.code;
    }

    public final String value() {
      return this.value;
    }

    @Override
    public String toString() {
      return "{" +
          "code='" + code + '\'' +
          ", value='" + value + '\'' +
          '}';
    }
  }
}
