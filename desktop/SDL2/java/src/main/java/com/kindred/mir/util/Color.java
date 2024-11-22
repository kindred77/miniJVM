package com.kindred.mir.util;

public class Color {

    public static Color Empty = new Color();

    public static Color White = new Color(255,255,255, 255);
    public static Color Black = new Color(0,0,0, 255);
    public static Color Red = new Color(255,0,0, 255);
    public static Color Green = new Color(0,255,0, 255);
    public static Color Blue = new Color(0,0,255, 255);

    private int r;
    private int g;
    private int b;
    private int alpha;

    public Color(int r, int g, int b, int alpha)
    {
        this.r=r;
        this.g=g;
        this.b=b;
        this.alpha=alpha;
    }

    public Color()
    {
        this.r=0;
        this.g=0;
        this.b=0;
        this.alpha=0;
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
