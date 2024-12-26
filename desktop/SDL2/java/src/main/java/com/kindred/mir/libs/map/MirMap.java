package com.kindred.mir.libs.map;

import com.kindred.mir.util.Util;
import java.io.File;
import java.io.FileInputStream;

public class MirMap {

  private int width,height;
  private MapCellInfo[][] mapCells;
  private String fileName;
  private byte[] data;

  public MirMap(String fileName) throws Exception{
    this.fileName=fileName;
    initiate();
  }

  private void initiate() throws Exception {
    File file=new File(this.fileName);
    if (file.exists()) {
      //Bytes = File.ReadAllBytes(FileName);
      //data=new byte[file.length()];
      FileInputStream fis = new FileInputStream(file);
      fis.read(data);
    } else {
      width = 1000;
      height = 1000;
      mapCells = new MapCellInfo[width][height];

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          mapCells[x][y] = new MapCellInfo();
        }
      }
      return;
    }


//c# custom map format
    if ((data[2] == 0x43) && (data[3] == 0x23))
    {
      LoadMapType100();
      return;
    }

//wemade mir3 maps have no title they just start with blank bytes
    if (data[0] == 0)
    {
      LoadMapType5();
      return;
    }
//shanda mir3 maps start with title: (C) SNDA, MIR3.
    if ((data[0] == 0x0F) && (data[5] == 0x53) && (data[14] == 0x33))
    {
      LoadMapType6();
      return;
    }
//wemades antihack map (laby maps) title start with: Mir2 AntiHack
    if ((data[0] == 0x15) && (data[4] == 0x32) && (data[6] == 0x41) && (data[19] == 0x31))
    {
      LoadMapType4();
      return;
    }
//wemades 2010 map format i guess title starts with: Map 2010 Ver 1.0
    if ((data[0] == 0x10) && (data[2] == 0x61) && (data[7] == 0x31) && (data[14] == 0x31))
    {
      LoadMapType1();
      return;
    }
//shanda's 2012 format and one of shandas(wemades) older formats share same header info, only difference is the filesize
    if ((data[4] == 0x0F) || (data[4] == 0x03) && (data[18] == 0x0D) && (data[19] == 0x0A))
    {
      int W = data[0] + (data[1] << 8);
      int H = data[2] + (data[3] << 8);
      if (data.length > (52 + (W*H*14)))
      {
        LoadMapType3();
        return;
      }
      else
      {
        LoadMapType2();
        return;
      }
    }

    //3/4 heroes map format (myth/lifcos i guess)
    if ((data[0] == 0x0D) && (data[1] == 0x4C) && (data[7] == 0x20) && (data[11] == 0x6D))
    {
      LoadMapType7();
      return;
    }

//if it's none of the above load the default old school format
    LoadMapType0();
  }

  private void LoadMapType0()
  {
    try
    {
      int offset = 0;
      width = Util.ToInt16(data, offset);
      offset += 2;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];
      offset = 52;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {//12
          mapCells[x][y] =new MapCellInfo();
          mapCells[x][y].BackIndex = 0;
          mapCells[x][y].MiddleIndex = 1;
          mapCells[x][y].BackImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].FrontImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].DoorIndex = (byte) (data[offset++] & 0x7F);
          mapCells[x][y].DoorOffset = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationTick = data[offset++];
          mapCells[x][y].FrontIndex = (short) (data[offset++] + 2);
          mapCells[x][y].Light = data[offset++];
          if ((mapCells[x][y].BackImage & 0x8000) !=0){
            mapCells[x][y].BackImage = (mapCells[x][y].BackImage & 0x7FFF) |0x20000000;
          }

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  private void LoadMapType1()
  {
    try
    {
      int offSet = 21;

      int w = Util.ToInt16(data, offSet);
      offSet += 2;
      int xor = Util.ToInt16(data, offSet);
      offSet += 2;
      int h = Util.ToInt16(data, offSet);
      width = w ^ xor;
      height = h ^ xor;
      mapCells = new MapCellInfo[width][height];

      offSet = 54;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          mapCells[x][y] = new MapCellInfo();
          mapCells[x][y].BackIndex = 0;
          mapCells[x][y].BackImage = (int) (Util.ToInt32(data, offSet) ^ 0xAA38AA38);
          mapCells[x][y].MiddleIndex = 1;
          mapCells[x][y].MiddleImage = (short) (Util.ToInt16(data, offSet += 4) ^ xor);
          mapCells[x][y].FrontImage = (short) (Util.ToInt16(data, offSet += 2) ^ xor);
          mapCells[x][y].DoorIndex = (byte) (data[offSet += 2] & 0x7F);
          mapCells[x][y].DoorOffset = data[++offSet];
          mapCells[x][y].FrontAnimationFrame = data[++offSet];
          mapCells[x][y].FrontAnimationTick = data[++offSet];
          mapCells[x][y].FrontIndex = (short) (data[++offSet] + 2);
          mapCells[x][y].Light = data[++offSet];
          mapCells[x][y].Unknown = data[++offSet];
          offSet++;

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119){
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void LoadMapType2()
  {
    try
    {
      int offset = 0;
      width = Util.ToInt16(data, offset);
      offset += 2;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];
      offset = 52;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {//14
          mapCells[x][y] = new MapCellInfo();
          mapCells[x][y].BackImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].FrontImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].DoorIndex = (byte) (data[offset++] & 0x7F);
          mapCells[x][y].DoorOffset = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationTick = data[offset++];
          mapCells[x][y].FrontIndex = (short) (data[offset++] + 120);
          mapCells[x][y].Light = data[offset++];
          mapCells[x][y].BackIndex = (short) (data[offset++] + 100);
          mapCells[x][y].MiddleIndex = (short) (data[offset++] + 110);
          if ((mapCells[x][y].BackImage & 0x8000) != 0) {
            mapCells[x][y].BackImage = (mapCells[x][y].BackImage & 0x7FFF) | 0x20000000;
          }

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  private void LoadMapType3()
  {
    try
    {
      int offset = 0;
      width = Util.ToInt16(data, offset);
      offset += 2;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];
      offset = 52;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {//36
          mapCells[x][y] = new MapCellInfo();
          mapCells[x][y].BackImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].FrontImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].DoorIndex = (byte) (data[offset++] & 0x7F);
          mapCells[x][y].DoorOffset = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationTick = data[offset++];
          mapCells[x][y].FrontIndex = (short) (data[offset++] + 120);
          mapCells[x][y].Light = data[offset++];
          mapCells[x][y].BackIndex = (short) (data[offset++] + 100);
          mapCells[x][y].MiddleIndex = (short) (data[offset++] + 110);
          mapCells[x][y].TileAnimationImage = (short) Util.ToInt16(data, offset);
          offset += 7;//2bytes from tileanimframe, 2 bytes always blank?, 2bytes potentialy 'backtiles index', 1byte fileindex for the backtiles?
          mapCells[x][y].TileAnimationFrames = data[offset++];
          mapCells[x][y].TileAnimationOffset = (short) Util.ToInt16(data, offset);
          offset += 14; //tons of light, blending, .. related options i hope
          if ((mapCells[x][y].BackImage & 0x8000) != 0) {
            mapCells[x][y].BackImage = (mapCells[x][y].BackImage & 0x7FFF) | 0x20000000;
          }
          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void LoadMapType4()
  {
    try
    {
      int offset = 31;
      int w = Util.ToInt16(data, offset);
      offset += 2;
      int xor = Util.ToInt16(data, offset);
      offset += 2;
      int h = Util.ToInt16(data, offset);
      width = w ^ xor;
      height = h ^ xor;
      mapCells = new MapCellInfo[width][height];
      offset = 64;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {//12
          mapCells[x][y] =new MapCellInfo();
          mapCells[x][y].BackIndex = 0;
          mapCells[x][y].MiddleIndex = 1;
          mapCells[x][y].BackImage = (short) (Util.ToInt16(data, offset) ^ xor);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) (Util.ToInt16(data, offset) ^ xor);
          offset += 2;
          mapCells[x][y].FrontImage = (short) (Util.ToInt16(data, offset) ^ xor);
          offset += 2;
          mapCells[x][y].DoorIndex = (byte) (data[offset++] & 0x7F);
          mapCells[x][y].DoorOffset = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationTick = data[offset++];
          mapCells[x][y].FrontIndex = (short) (data[offset++] + 2);
          mapCells[x][y].Light = data[offset++];
          if ((mapCells[x][y].BackImage & 0x8000) !=0) {
            mapCells[x][y].BackImage = (mapCells[x][y].BackImage & 0x7FFF) |0x20000000;
          }

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void LoadMapType5()
  {
    try
    {
      byte flag = 0;
      int offset = 20;
      short Attribute = (short)(Util.ToInt16(data,offset));
      width = (int)(Util.ToInt16(data,offset+=2));
      height = (int)(Util.ToInt16(data, offset += 2));
      //ignoring eventfile and fogcolor for now (seems unused in maps i checked)
      offset = 28;
      //initiate all cells
      mapCells = new MapCellInfo[width][height];
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          mapCells[x][y] =new MapCellInfo();
        }
      }
      //read all back tiles
      for (int x = 0; x < (width/2); x++) {
        for (int y = 0; y < (height / 2); y++) {
          for (int i = 0; i < 4; i++) {
            mapCells[(x * 2) + (i % 2)][(y * 2) + (i / 2)].
            BackIndex = (short) (data[offset] != 255 ? data[offset] + 200 : -1);
            mapCells[(x * 2) + (i % 2)][(y * 2) + (i / 2)].BackImage = (int) (Util.ToUInt16(data, offset + 1) + 1);
          }
          offset += 3;
        }
      }
      //read rest of data
      offset = 28 + (3 * ((width /2) + (width %2)) * (height / 2));
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {

          flag = data[offset++];
          mapCells[x][y].MiddleAnimationFrame = data[offset++];

          mapCells[x][y].FrontAnimationFrame = data[offset] == 255 ? (byte) 0 : data[offset];
          mapCells[x][y].FrontAnimationFrame &= 0x8F;
          offset++;
          mapCells[x][y].MiddleAnimationTick = 0;
          mapCells[x][y].FrontAnimationTick = 0;
          mapCells[x][y].FrontIndex = (short) (data[offset] != 255 ? data[offset] + 200 : -1);
          offset++;
          mapCells[x][y].MiddleIndex = (short) (data[offset] != 255 ? data[offset] + 200 : -1);
          offset++;
          mapCells[x][y].MiddleImage = Util.ToUInt16(data, offset) + 1;
          offset += 2;
          mapCells[x][y].FrontImage = Util.ToUInt16(data, offset) + 1;
          if ((mapCells[x][y].FrontImage == 1) && (mapCells[x][y].FrontIndex == 200)) {
            mapCells[x][y].FrontIndex = -1;
          }
          offset += 2;
          offset += 3;//mir3 maps dont have doors so dont bother reading the info
          mapCells[x][y].Light = (byte) (data[offset] & 0x0F);
          offset += 2;
          if ((flag & 0x01) != 1) {
            mapCells[x][y].BackImage |= 0x20000000;
          }
          if ((flag & 0x02) != 2) {
            mapCells[x][y].FrontImage = mapCells[x][y].FrontImage | 0x8000;
          }

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          } else {
            mapCells[x][y].Light *= 2;//expand general mir3 lighting as default range is small. Might break new colour lights.
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void LoadMapType6()
  {
    try
    {
      byte flag = 0;
      int offset = 16;
      width = Util.ToInt16(data, offset);
      offset += 2;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];
      offset = 40;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          mapCells[x][y] = new MapCellInfo();
          flag = data[offset++];
          mapCells[x][y].BackIndex = (short) (data[offset] != 255 ? data[offset] + 300 : -1);
          offset++;
          mapCells[x][y].MiddleIndex = (short) (data[offset] != 255 ? data[offset] + 300 : -1);
          offset++;
          mapCells[x][y].FrontIndex = (short) (data[offset] != 255 ? data[offset] + 300 : -1);
          offset++;
          mapCells[x][y].BackImage = (short) (Util.ToInt16(data, offset) + 1);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) (Util.ToInt16(data, offset) + 1);
          offset += 2;
          mapCells[x][y].FrontImage = (short) (Util.ToInt16(data, offset) + 1);
          offset += 2;
          if ((mapCells[x][y].FrontImage == 1) && (mapCells[x][y].FrontIndex == 200)) {
            mapCells[x][y].FrontIndex = -1;
          }
          mapCells[x][y].MiddleAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset] == 255 ? (byte) 0 : data[offset];
          if (mapCells[x][y].FrontAnimationFrame > 0x0F) {//assuming shanda used same value not sure
            mapCells[x][y].FrontAnimationFrame = (byte) (/*0x80 ^*/ (mapCells[x][y].FrontAnimationFrame & 0x0F));
          }
          offset++;
          mapCells[x][y].MiddleAnimationTick = 1;
          mapCells[x][y].FrontAnimationTick = 1;
          mapCells[x][y].Light = (byte) (data[offset] & 0x0F);
          mapCells[x][y].Light *= 4;//far wants all light on mir3 maps to be maxed :p
          offset += 8;
          if ((flag & 0x01) != 1) {
            mapCells[x][y].BackImage |= 0x20000000;
          }
          if ((flag & 0x02) != 2) {
            mapCells[x][y].FrontImage = (short) (mapCells[x][y].FrontImage | 0x8000);
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }

  }

  private void LoadMapType7()
  {
    try
    {
      int offset = 21;
      width = Util.ToInt16(data, offset);
      offset += 4;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];

      offset = 54;

      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {//total 15
          mapCells[x][y] = new MapCellInfo();
          mapCells[x][y].BackIndex = 0;
          mapCells[x][y].BackImage = (int) Util.ToInt32(data, offset);
          mapCells[x][y].MiddleIndex = 1;
          mapCells[x][y].MiddleImage = (short) Util.ToInt16(data, offset += 4);
          mapCells[x][y].FrontImage = (short) Util.ToInt16(data, offset += 2);
          mapCells[x][y].DoorIndex = (byte) (data[offset += 2] & 0x7F);
          mapCells[x][y].DoorOffset = data[++offset];
          mapCells[x][y].FrontAnimationFrame = data[++offset];
          mapCells[x][y].FrontAnimationTick = data[++offset];
          mapCells[x][y].FrontIndex = (short) (data[++offset] + 2);
          mapCells[x][y].Light = data[++offset];
          mapCells[x][y].Unknown = data[++offset];
          if ((mapCells[x][y].BackImage & 0x8000) != 0) {
            mapCells[x][y].BackImage = (mapCells[x][y].BackImage & 0x7FFF) | 0x20000000;
          }
          offset++;

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }
        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  private void LoadMapType100()
  {
    try
    {
      int offset = 4;
      if ((data[0]!= 1) || (data[1] != 0)) {
        return;//only support version 1 atm
      }
      width = Util.ToInt16(data, offset);
      offset += 2;
      height = Util.ToInt16(data, offset);
      mapCells = new MapCellInfo[width][height];
      offset = 8;
      for (int x = 0; x < width; x++) {
        for (int y = 0; y < height; y++) {
          mapCells[x][y] =new MapCellInfo();
          mapCells[x][y].BackIndex = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].BackImage = (short) Util.ToInt32(data, offset);
          offset += 4;
          mapCells[x][y].MiddleIndex = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].MiddleImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].FrontIndex = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].FrontImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].DoorIndex = (byte) (data[offset++] & 0x7F);
          mapCells[x][y].DoorOffset = data[offset++];
          mapCells[x][y].FrontAnimationFrame = data[offset++];
          mapCells[x][y].FrontAnimationTick = data[offset++];
          mapCells[x][y].MiddleAnimationFrame = data[offset++];
          mapCells[x][y].MiddleAnimationTick = data[offset++];
          mapCells[x][y].TileAnimationImage = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].TileAnimationOffset = (short) Util.ToInt16(data, offset);
          offset += 2;
          mapCells[x][y].TileAnimationFrames = data[offset++];
          mapCells[x][y].Light = data[offset++];

          if (mapCells[x][y].Light >= 100 && mapCells[x][y].Light <= 119) {
            mapCells[x][y].FishingCell = true;
          }

        }
      }
    }
    catch (Exception ex)
    {
      ex.printStackTrace();
    }
  }

  public MapCellInfo[][] getMapCells() {
    return mapCells;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }
}
