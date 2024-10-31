package com.kindred.mir.util;

public class RectangleF {

    public static RectangleF Empty = new RectangleF();

    private float x;

    private float y;

    private float width;

    private float height;

    public PointF getLocation()
    {
        return new PointF(x, y);
    }

    public void setLocation(PointF pointf)
    {
        x = pointf.getX();
        y = pointf.getY();
    }

    public SizeF getSize()
    {
        return new SizeF(width, height);
    }

    public void setSize(SizeF sizef)
    {
        width = sizef.getWidth();
        height = sizef.getHeight();
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

    public void setY(float y)
    {
        this.y=y;
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

    public float getLeft()
    {
        return x;
    }

    public float getTop()
    {
        return y;
    }

    public float getRigth()
    {
        return x+width;
    }

    public float getBottom()
    {
        return y+height;
    }

    public boolean isEmpty()
    {
        if (!(width <= 0f))
        {
            return height <= 0f;
        }

        return true;
    }

    public RectangleF(float x, float y, float width, float height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public RectangleF()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public RectangleF(PointF location, SizeF size)
    {
        x = location.getX();
        y = location.getY();
        width = size.getWidth();
        height = size.getHeight();
    }

    public static RectangleF fromLTRB(float left, float top, float right, float bottom)
    {
        return new RectangleF(left, top, right - left, bottom - top);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof RectangleF))
        {
            return false;
        }

        RectangleF rectangleF=(RectangleF)obj;

        if (rectangleF.x == x && rectangleF.y == y && rectangleF.width == width)
        {
            return rectangleF.height == height;
        }

        return false;
    }

    public boolean contains(float x, float y)
    {
        if (x <= x && x < x + width && y <= y)
        {
            return y < y + height;
        }

        return false;
    }

    public boolean contains(PointF pt)
    {
        return contains(pt.getX(), pt.getY());
    }

    public boolean contains(RectangleF rect)
    {
        if (x <= rect.x && rect.x + rect.width <= x + width && y <= rect.y)
        {
            return rect.y + rect.height <= y + height;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return (int)((long)x ^ (((long)y << 13) | ((long)y >> 19)) ^ (((long)width << 26) | ((long)width >> 6)) ^ (((long)height << 7) | ((long)height >> 25)));
    }

    public void inflate(float x, float y)
    {
        x -= x;
        y -= y;
        width += 2f * x;
        height += 2f * y;
    }

    public void inflate(SizeF size)
    {
        inflate(size.getWidth(), size.getHeight());
    }

    public static RectangleF inflate(RectangleF rect, float x, float y)
    {
        RectangleF result = rect;
        result.inflate(x, y);
        return result;
    }

    public void intersect(RectangleF rect)
    {
        RectangleF rectangleF = intersect(rect, this);
        x = rectangleF.x;
        y = rectangleF.y;
        width = rectangleF.width;
        height = rectangleF.height;
    }

    public static RectangleF intersect(RectangleF a, RectangleF b)
    {
        float num = Math.max(a.x, b.x);
        float num2 = Math.min(a.x + a.width, b.x + b.width);
        float num3 = Math.max(a.y, b.y);
        float num4 = Math.min(a.y + a.height, b.y + b.height);
        if (num2 >= num && num4 >= num3)
        {
            return new RectangleF(num, num3, num2 - num, num4 - num3);
        }

        return Empty;
    }

    public boolean intersectsWith(RectangleF rect)
    {
        if (rect.x < x + width && x < rect.x + rect.width && rect.y < y + height)
        {
            return y < rect.y + rect.height;
        }

        return false;
    }

    public static RectangleF union(RectangleF a, RectangleF b)
    {
        float num = Math.min(a.x, b.x);
        float num2 = Math.max(a.x + a.width, b.x + b.width);
        float num3 = Math.min(a.y, b.y);
        float num4 = Math.max(a.y + a.height, b.y + b.height);
        return new RectangleF(num, num3, num2 - num, num4 - num3);
    }

    public void offset(PointF pos)
    {
        offset(pos.getX(), pos.getY());
    }

    public void offset(float x, float y)
    {
        x += x;
        y += y;
    }

    @Override
    public String toString() {
        return "{X=" + x + ",Y=" + y + ",Width=" + width + ",Height=" + height + "}";
    }
}
