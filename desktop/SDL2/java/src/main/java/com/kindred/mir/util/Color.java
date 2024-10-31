package com.kindred.mir.util;

public class Color {

    public static Color Empty = new Color();

    public static Color White = new Color(255,255,255, 255);
    public static Color Black = new Color(0,0,0, 255);

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
}
