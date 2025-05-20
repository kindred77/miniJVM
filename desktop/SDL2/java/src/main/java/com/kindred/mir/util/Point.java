package com.kindred.mir.util;

public class Point {

    public static Point Empty=new Point();

    private int x;

    private int y;

    public boolean isEmpty()
    {
        if (x == 0)
        {
            return y == 0;
        }

        return false;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x=x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y=y;
    }

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public Point()
    {
        this.x = 0;
        this.y = 0;
    }

    public Point(Size sz)
    {
        x = sz.getWidth();
        y = sz.getHeight();
    }

    public Point(int dw)
    {
        x = (short)LOWORD(dw);
        y = (short)HIWORD(dw);
    }

    public void add(Size sz)
    {
        x=x + sz.getWidth();
        y=y + sz.getHeight();
    }

    public void add(Point pt)
    {
        x=x + pt.getX();
        y=y + pt.getY();
    }

    public void subtract(Size sz)
    {
        x=x - sz.getWidth();
        y=y - sz.getHeight();
    }

    public void subtract(Point pt)
    {
        x=x - pt.getX();
        y=y - pt.getY();
    }

    public static Point add(Point pt, Size sz)
    {
        return new Point(pt.getX() + sz.getWidth(), pt.getY() + sz.getHeight());
    }

    public static Point add(Point pt, Point pt2)
    {
        return new Point(pt.getX() + pt2.getX(), pt.getY() + pt2.getY());
    }

    public static Point subtract(Point pt, Size sz)
    {
        return new Point(pt.getX() - sz.getWidth(), pt.getY() - sz.getHeight());
    }

    public static Point subtract(Point pt, Point pt2)
    {
        return new Point(pt.getX() - pt2.getX(), pt.getY() - pt2.getY());
    }

    public static Point ceiling(PointF value)
    {
        return new Point((int)Math.ceil(value.getX()), (int)Math.ceil(value.getY()));
    }

    public static Point truncate(PointF value)
    {
        return new Point((int)value.getX(), (int)value.getY());
    }

    public static Point round(PointF value)
    {
        return new Point(Math.round(value.getX()), Math.round(value.getY()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Point))
        {
            return false;
        }

        Point point=(Point)obj;

        if (point.x == x)
        {
            return point.y == y;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return x ^ y;
    }

    public void Offset(int dx, int dy)
    {
        x += dx;
        y += dy;
    }

    public void Offset(Point p)
    {
        Offset(p.x, p.y);
    }

    @Override
    public String toString() {
        return "{X=" + x + ",Y=" + y + "}";
    }

    private static int HIWORD(int n)
    {
        return (n >> 16) & 0xFFFF;
    }

    private static int LOWORD(int n)
    {
        return n & 0xFFFF;
    }
}
