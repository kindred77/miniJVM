package com.kindred.mir.util;

public class Size {
    public static Size Empty=new Size();

    private int width;

    private int height;

    public boolean isEmpty()
    {
        if (width == 0)
        {
            return height == 0;
        }

        return false;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width=width;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height=height;
    }

    public Size(Point pt)
    {
        width = pt.getX();
        height = pt.getY();
    }

    public Size(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public Size() {
        this.width = 0;
        this.height = 0;
    }

    public void add(Size sz2)
    {
        width=width + sz2.getWidth();
        height=height + sz2.getHeight();
    }

    public static Size ceiling(SizeF value)
    {
        return new Size((int)Math.ceil(value.getWidth()), (int)Math.ceil(value.getHeight()));
    }

    public void subtract(Size sz2)
    {
        width=width - sz2.getWidth();
        height=height - sz2.getHeight();
    }

    public static Size truncate(SizeF value)
    {
        return new Size((int)value.getWidth(), (int)value.getHeight());
    }

    public static Size round(SizeF value)
    {
        return new Size(Math.round(value.getWidth()), Math.round(value.getHeight()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Size))
        {
            return false;
        }
        Size size = (Size)obj;
        if (size.width == width)
        {
            return size.height == height;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return width ^ height;
    }

    @Override
    public String toString() {
        return "{Width=" + width + ", Height=" + height + "}";
    }
}
