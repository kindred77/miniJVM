package com.kindred.mir.libs.map;

import static com.kindred.mir.GameCommon.Monster.*;
import static com.kindred.mir.GameCommon.ObjectType.Monster;

import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.libs.MirImage;
import com.kindred.mir.libs.MirImage.ImageEffect;
import com.kindred.mir.libs.MirLibFactory;
import com.kindred.mir.scene.game.objects.MapObject;
import com.kindred.mir.scene.game.objects.MonsterObject;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapCellInfo {
  public short BackIndex;
  public int BackImage;
  public short MiddleIndex;
  public int MiddleImage;
  public short FrontIndex;
  public int FrontImage;

  public byte DoorIndex;
  public byte DoorOffset;

  public byte FrontAnimationFrame;
  public byte FrontAnimationTick;

  public byte MiddleAnimationFrame;
  public byte MiddleAnimationTick;

  public short TileAnimationImage;
  public short TileAnimationOffset;
  public byte  TileAnimationFrames;

  public byte Light;
  public byte Unknown;
  public boolean FishingCell;
  public List<MapObject> CellObjects;

  public void addObject(MapObject ob)
  {
    if (CellObjects == null) {
      CellObjects = new ArrayList<>();
    }

    CellObjects.add(0, ob);
    sort();
  }
  public void removeObject(MapObject ob)
  {
    if (CellObjects == null) {
      return;
    }

    CellObjects.remove(ob);

    if (CellObjects.size() == 0) {
      CellObjects = null;
    } else {
      sort();
    }
  }
  public MapObject findObject(long ObjectID)
  {
    List<MapObject> longWords = CellObjects.stream()
        .filter(mapObj -> mapObj.ObjectID > ObjectID)
        .collect(Collectors.toList());
    if (longWords.size()>0) {
      return longWords.get(0);
    }
    return null;
  }

  public void drawObjects(long surface) {
    if (CellObjects == null) {
      return;
    }

    for (int i = 0; i < CellObjects.size(); i++)
    {
      if (!CellObjects.get(i).Dead)
      {
        CellObjects.get(i).draw(surface);
        continue;
      }

      if(CellObjects.get(i).getRace() == ObjectType.Monster)
      {
        switch(((MonsterObject)CellObjects.get(i)).BaseImage)
        {
          case PalaceWallLeft:
          case PalaceWall1:
          case PalaceWall2:
          case SSabukWall1:
          case SSabukWall2:
          case SSabukWall3:
          case HellLord:
            CellObjects.get(i).draw(surface);
            break;
          default:
            continue;
        }
      }
    }
  }
//
  public void drawDeadObjects(long surface)
  {
    if (CellObjects == null) {
      return;
    }
    for (int i = 0; i < CellObjects.size(); i++)
    {
      if (!CellObjects.get(i).Dead) {
        continue;
      }

      if (CellObjects.get(i).getRace() == Monster)
      {
        switch (((MonsterObject)CellObjects.get(i)).BaseImage)
        {
          case PalaceWallLeft:
          case PalaceWall1:
          case PalaceWall2:
          case SSabukWall1:
          case SSabukWall2:
          case SSabukWall3:
          case HellLord:
            continue;
        }
      }

      CellObjects.get(i).draw(surface);
    }
  }

  public void sort()
  {
    CellObjects.sort((ob1, ob2) -> {
      if (ob1.getRace() == ObjectType.Item && ob2.getRace() != ObjectType.Item) {
        return -1;
      }
      if (ob2.getRace() == ObjectType.Item && ob1.getRace() != ObjectType.Item) {
        return 1;
      }
      if (ob1.getRace() == ObjectType.Spell && ob2.getRace() != ObjectType.Spell) {
        return -1;
      }
      if (ob2.getRace() == ObjectType.Spell && ob1.getRace() != ObjectType.Spell) {
        return 1;
      }

      int i = Boolean.compare(ob2.Dead,ob1.Dead);
      return i == 0 ? Long.compare(ob1.ObjectID,ob2.ObjectID) : i;
    });

//    CellObjects.Sort(delegate(MapObject ob1, MapObject ob2)
//    {
//      if (ob1.Race == ObjectType.Item && ob2.Race != ObjectType.Item)
//        return -1;
//      if (ob2.Race == ObjectType.Item && ob1.Race != ObjectType.Item)
//        return 1;
//      if (ob1.Race == ObjectType.Spell && ob2.Race != ObjectType.Spell)
//        return -1;
//      if (ob2.Race == ObjectType.Spell && ob1.Race != ObjectType.Spell)
//        return 1;
//
//      int i = ob2.Dead.CompareTo(ob1.Dead);
//      return i == 0 ? ob1.ObjectID.CompareTo(ob2.ObjectID) : i;
//    });
  }
}
