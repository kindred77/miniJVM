package com.kindred.mir.scene.game.objects;

import com.kindred.mir.GameCommon.MirAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Data;

public class FrameSet {

  public static FrameSet Players=new FrameSet();
  public static List<FrameSet> Npcs=new ArrayList<>(); //Make Array
  public static List<FrameSet> Monsters=new ArrayList<>();
  public static List<FrameSet> HelperPets=new ArrayList<>(); //IntelligentCreature
  public static List<FrameSet> Gates=new ArrayList<>();
  public static List<FrameSet> Walls=new ArrayList<>();

  public Map<MirAction, Frame> Frames = new HashMap<>();

  static {
    Players.Frames.put(MirAction.Standing, new Frame(0, 4, 0, 500, 0, 8, 0, 250));
    Players.Frames.put(MirAction.Walking, new Frame(32, 6, 0, 100, 64, 6, 0, 100));
    Players.Frames.put(MirAction.Running, new Frame(80, 6, 0, 100, 112, 6, 0, 100));
    Players.Frames.put(MirAction.Stance, new Frame(128, 1, 0, 1000, 160, 1, 0, 1000));
    Players.Frames.put(MirAction.Stance2, new Frame(300, 1, 5, 1000, 332, 1, 5, 1000));
    Players.Frames.put(MirAction.Attack1, new Frame(136, 6, 0, 100, 168, 6, 0, 100));
    Players.Frames.put(MirAction.Attack2, new Frame(184, 6, 0, 100, 216, 6, 0, 100));
    Players.Frames.put(MirAction.Attack3, new Frame(232, 8, 0, 100, 264, 8, 0, 100));
    Players.Frames.put(MirAction.Attack4, new Frame(416, 6, 0, 100, 448, 6, 0, 100));
    Players.Frames.put(MirAction.Spell, new Frame(296, 6, 0, 100, 328, 6, 0, 100));
    Players.Frames.put(MirAction.Harvest, new Frame(344, 2, 0, 300, 376, 2, 0, 300));
    Players.Frames.put(MirAction.Struck, new Frame(360, 3, 0, 100, 392, 3, 0, 100));
    Players.Frames.put(MirAction.Die, new Frame(384, 4, 0, 100, 416, 4, 0, 100));
    Players.Frames.put(MirAction.Dead, new Frame(387, 1, 3, 1000, 419, 1, 3, 1000));

    Frame frame=new Frame(384, 4, 0, 100, 416, 4, 0, 100);
    frame.setReverse(true);
    Players.Frames.put(MirAction.Revive,  frame);

    Players.Frames.put(MirAction.Mine, new Frame(184, 6, 0, 100, 216, 6, 0, 100));
    Players.Frames.put(MirAction.Lunge, new Frame(139, 1, 5, 1000, 300, 1, 5, 1000)); //slashingburst test

//Assassin
    Players.Frames.put(MirAction.Sneek, new Frame(464, 6, 0, 100, 496, 6, 0, 100));
    Players.Frames.put(MirAction.DashAttack, new Frame(80, 3, 3, 100, 112, 3, 3, 100));

//Archer
    Players.Frames.put(MirAction.WalkingBow, new Frame(0, 6, 0, 100, 0, 6, 0, 100));
    Players.Frames.put(MirAction.RunningBow, new Frame(48, 6, 0, 100, 48, 6, 0, 100));
    Players.Frames.put(MirAction.AttackRange1, new Frame(96, 8, 0, 100, 96, 8, 0, 100));
    Players.Frames.put(MirAction.AttackRange2, new Frame(160, 8, 0, 100, 160, 8, 0, 100));
    Players.Frames.put(MirAction.AttackRange3, new Frame(224, 8, 0, 100, 224, 8, 0, 100));
    Players.Frames.put(MirAction.Jump, new Frame(288, 8, 0, 100, 288, 8, 0, 100));

//Mounts
    Players.Frames.put(MirAction.MountStanding, new Frame(416, 4, 0, 500, 448, 4, 0, 500));
    Players.Frames.put(MirAction.MountWalking, new Frame(448, 8, 0, 100, 480, 8, 0, 500));
    Players.Frames.put(MirAction.MountRunning, new Frame(512, 6, 0, 100, 544, 6, 0, 100));
    Players.Frames.put(MirAction.MountStruck, new Frame(560, 3, 0, 100, 592, 3, 0, 100));
    Players.Frames.put(MirAction.MountAttack, new Frame(584, 6, 0, 100, 616, 6, 0, 100));

//Fishing
    Players.Frames.put(MirAction.FishingCast, new Frame(632, 8, 0, 100));
    Players.Frames.put(MirAction.FishingWait, new Frame(696, 6, 0, 120));
    Players.Frames.put(MirAction.FishingReel, new Frame(744, 8, 0, 100));
  }

  static {
    //0 4 frames + direction + harvest(10 frames)
    FrameSet frame=null;
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 4, 0, 450));
    frame.Frames.put(MirAction.Harvest, new Frame(12, 10, 0, 200));

//1 4 frames + direction + harvest(20 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(4, 4, 0, 450));
    frame.Frames.put(MirAction.Harvest, new Frame(12, 20, 0, 200));

//2 4 frames, 4 frames + direction
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 4, 0, 450));

//3 12 frames + animation(10 frames) (large tele)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 12, 0, 250, 12, 10, 2, 250));

//4 2 frames + animation(9 frames) (small tele)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 2, 0, 1250, 2, 9, 1, 250));

//5 2 frame + animation(6 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 2, 0, 300, 2, 6, 0, 100));

//6 1 frame
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 1, 0, 1500));

//7 10 frames
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 10, 0, 250));

//8 12 frames
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 12, 0, 250));

//9 8 frames
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 8, 0, 250));

//10 6 frames + direction
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 6, 0, 250));

//11 1 frame + animation(8 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 2, 0, 400, 2, 8, 0, 100));

//12 11 frames
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 11, 0, 250));

//13 20 frames + animation(20 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 20, 0, 450, 20, 20, 0, 450));

//14 4 frames + direction + animation(4 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 4, 0, 250, 12, 4, 0, 250));

//15 4 frames + harvest(6 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 4, 0, 250));
    frame.Frames.put(MirAction.Harvest, new Frame(12, 6, 0, 200));

//16 6 frames + animation(12 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 6, 0, 400, 6, 12, 0, 200));

//17 9 frames + direction
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 9, 0, 650));

//18 5 frames + direction
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 5, 0, 400));

//19 7 frames + direction + harvest(10 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 7, 0, 550));
    frame.Frames.put(MirAction.Harvest, new Frame(21, 10, 0, 200));

//20 1 frame + animation(9 frames)
    Npcs.add(frame = new FrameSet());
    frame.Frames.put(MirAction.Standing, new Frame(0, 1, 0, 900, 1, 9, 0, 100));
  }

}
