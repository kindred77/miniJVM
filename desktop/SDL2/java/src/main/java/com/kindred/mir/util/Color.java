package com.kindred.mir.util;

public class Color {

    public static Color Empty = new Color();

    public static Color White = new Color(255,255,255, 255);
    public static Color Black = new Color(0,0,0, 255);
    public static Color Red = new Color(255,0,0, 255);
    public static Color Green = new Color(0,255,0, 255);
    public static Color Blue = new Color(0,0,255, 255);
    public static Color Yellow = new Color(255,255,0, 255);
    public static Color Gray = new Color(128,128,128, 255);
    public static Color Purple = new Color(128,0,128, 255);
    public static Color Orange = new Color(255,165,0, 255);
    public static Color DarkRed = new Color(139,0,0, 255);
    public static Color Gold = new Color(255,215,0, 255);
    public static Color LimeGreen = new Color(50,205,50, 255);

    private int r;
    private float rf;

    private int g;
    private float gf;

    private int b;
    private float bf;

    private int alpha;
    private float alphaf;

    public Color(int r, int g, int b, int alpha)
    {
        this.r=r;
        this.rf=(float)r / 255;

        this.g=g;
        this.gf=(float)g / 255;

        this.b=b;
        this.bf=(float)b / 255;

        this.alpha=alpha;
        this.alphaf=(float)alpha / 255;
    }

    public Color(float rf, float gf, float bf, float alphaf)
    {
        this.r=(int)(rf * 255);
        this.rf=rf;

        this.g=(int)(gf * 255);
        this.gf=gf;

        this.b=(int)(bf * 255);
        this.bf=bf;

        this.alpha=(int)(alphaf * 255);
        this.alphaf=alphaf;
    }

    public Color()
    {
        this.r=0;
        this.rf=0;
        this.g=0;
        this.gf=0;
        this.b=0;
        this.bf=0;
        this.alpha=0;
        this.alphaf=0;
    }

    public int getRed()
    {
        return this.r;
    }

    public int getGreen()
    {
        return this.g;
    }

    public int getBlue()
    {
        return this.b;
    }

    public int getAlpha()
    {
        return this.alpha;
    }

    public float getRedF()
    {
        return this.rf;
    }

    public float getGreenF()
    {
        return this.gf;
    }

    public float getBlueF()
    {
        return this.bf;
    }

    public float getAlphaF()
    {
        return this.alphaf;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Color))
        {
            return false;
        }

        Color color=(Color)obj;

        if (color.r == r && color.g == g && color.b == b && color.alpha == alpha)
        {
            return true;
        }

        return false;
    }
}
