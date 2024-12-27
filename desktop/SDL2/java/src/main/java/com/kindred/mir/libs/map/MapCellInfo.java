package com.kindred.mir.libs.map;

import com.kindred.mir.GameCommon.ObjectType;
import com.kindred.mir.scene.game.objects.MapObject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
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

  public void AddObject(MapObject ob)
  {
    if (CellObjects == null) {
      CellObjects = new ArrayList<>();
    }

    CellObjects.add(0, ob);
    Sort();
  }
  public void RemoveObject(MapObject ob)
  {
    if (CellObjects == null) {
      return;
    }

    CellObjects.remove(ob);

    if (CellObjects.size() == 0) {
      CellObjects = null;
    } else {
      Sort();
    }
  }
  public MapObject FindObject(long ObjectID)
  {
    List<MapObject> longWords = CellObjects.stream()
        .filter(mapObj -> mapObj.ObjectID > ObjectID)
        .collect(Collectors.toList());
    if (longWords.size()>0) {
      return longWords.get(0);
    }
    return null;
  }

//  public void DrawObjects()
//  {
//    if (CellObjects == null) return;
//
//    for (int i = 0; i < CellObjects.Count; i++)
//    {
//      if (!CellObjects[i].Dead)
//      {
//        CellObjects[i].Draw();
//        continue;
//      }
//
//      if(CellObjects[i].Race == ObjectType.Monster)
//      {
//        switch(((MonsterObject)CellObjects[i]).BaseImage)
//        {
//          case Monster.PalaceWallLeft:
//          case Monster.PalaceWall1:
//          case Monster.PalaceWall2:
//          case Monster.SSabukWall1:
//          case Monster.SSabukWall2:
//          case Monster.SSabukWall3:
//          case Monster.HellLord:
//            CellObjects[i].Draw();
//            break;
//          default:
//            continue;
//        }
//      }
//    }
//  }
//
//  public void DrawDeadObjects()
//  {
//    if (CellObjects == null) return;
//    for (int i = 0; i < CellObjects.Count; i++)
//    {
//      if (!CellObjects[i].Dead) continue;
//
//      if (CellObjects[i].Race == ObjectType.Monster)
//      {
//        switch (((MonsterObject)CellObjects[i]).BaseImage)
//        {
//          case Monster.PalaceWallLeft:
//          case Monster.PalaceWall1:
//          case Monster.PalaceWall2:
//          case Monster.SSabukWall1:
//          case Monster.SSabukWall2:
//          case Monster.SSabukWall3:
//          case Monster.HellLord:
//            continue;
//        }
//      }
//
//      CellObjects[i].Draw();
//    }
//  }
//
  public void Sort()
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

      int i = Boolean.compare(ob2.isDead,ob1.isDead);
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
