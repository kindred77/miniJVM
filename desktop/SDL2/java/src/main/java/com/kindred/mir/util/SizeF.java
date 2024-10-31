package com.kindred.mir.util;

public class SizeF {
    public static SizeF Empty=new SizeF();

    private float width;

    private float height;

    public boolean isEmpty()
    {
        if (width == 0f)
        {
            return height == 0f;
        }

        return false;
    }

    public float getWidth()
    {
        return width;
    }

    public void setWidth(float width)
    {
        this.width=width;
    }

    public float getHeight()
    {
        return height;
    }

    public void setHeight(float height)
    {
        this.height=height;
    }

    public SizeF(SizeF size)
    {
        width = size.width;
        height = size.height;
    }

    public SizeF()
    {
        width = 0;
        height = 0;
    }

    public SizeF(PointF pt)
    {
        width = pt.getX();
        height = pt.getY();
    }

    public SizeF(float width, float height)
    {
        this.width = width;
        this.height = height;
    }

    public void add(SizeF sz2)
    {
        width=width + sz2.getWidth();
        height=height + sz2.getHeight();
    }

    public void subtract(SizeF sz2)
    {
        width=width - sz2.getWidth();
        height=height - sz2.getHeight();
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof SizeF))
        {
            return false;
        }

        SizeF sizeF=(SizeF)obj;

        if (sizeF.getWidth() == width && sizeF.getHeight() == height)
        {
            return sizeF.getClass().getTypeName().equals(this.getClass().getTypeName());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    public PointF toPointF()
    {
        return new PointF(this.height,this.height);
    }

    public Size toSize()
    {
        return Size.truncate(this);
    }

    @Override
    public String toString() {
        return "{Width=" + width + ", Height=" + height + "}";
    }
}
