package com.kindred.mir.util;

public class PointF {
    public static PointF Empty=new PointF();

    private float x;

    private float y;

    public boolean isEmpty()
    {
        if (x == 0f)
        {
            return y == 0f;
        }

        return false;
    }

    public float getX()
    {
        return x;
    }

    public void setX(float x)
    {
        this.x=x;
    }

    public float getY()
    {
        return y;
    }
    public void sety(float y)
    {
        this.y=y;
    }

    public PointF(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public PointF()
    {
        this.x = 0;
        this.y = 0;
    }

    public void add(Size sz)
    {
        x=x + (float)sz.getWidth();
        y=y+ (float)sz.getHeight();
    }

    public void subtract(Size sz)
    {
        x=x- (float)sz.getWidth();
        y=y- (float)sz.getHeight();
    }

    public static PointF add(PointF pt, SizeF sz)
    {
        return new PointF(pt.getX() + sz.getWidth(), pt.getY() + sz.getHeight());
    }

    public static PointF subtract(PointF pt, SizeF sz)
    {
        return new PointF(pt.getX() - sz.getWidth(), pt.getY() - sz.getHeight());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PointF))
        {
            return false;
        }

        PointF pointF=(PointF)obj;

        if (pointF.getX() == getX() && pointF.getY() == getY())
        {
            return pointF.getClass().getTypeName().equals(this.getClass().getTypeName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "{X=" + x + ", Y=" + y + "}";
    }
}
