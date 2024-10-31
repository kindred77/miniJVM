package com.kindred.mir.util;

public class Rectangle {

    public static Rectangle Empty = new Rectangle();

    private int x;

    private int y;

    private int width;

    private int height;

    public Point getLocation()
    {
        return new Point(x, y);
    }

    public void setLocation(Point point) {
        x = point.getX();
        y = point.getY();
    }

    public Size getSize()
    {
        return new Size(width, height);
    }

    public void setSize(Size size)
    {
        width = size.getWidth();
        height = size.getHeight();
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

    public int getLeft()
    {
        return x;
    }

    public int getTop()
    {
        return y;
    }

    public int getRight()
    {
        return x + width;
    }

    public int getBottom()
    {
        return y + height;
    }

    public boolean isEmpty()
    {
        if (height == 0 && width == 0 && x == 0)
        {
            return y == 0;
        }

        return false;
    }

    public Rectangle(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public Rectangle()
    {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
    }

    public Rectangle(Point location, Size size)
    {
        x = location.getX();
        y = location.getY();
        width = size.getWidth();
        height = size.getHeight();
    }

    public static Rectangle fromLTRB(int left, int top, int right, int bottom)
    {
        return new Rectangle(left, top, right - left, bottom - top);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Rectangle))
        {
            return false;
        }

        Rectangle rectangle=(Rectangle)obj;

        if (rectangle.getX() == x && rectangle.getY() == y && rectangle.getWidth() == width)
        {
            return rectangle.getHeight() == height;
        }

        return false;
    }

    public static Rectangle ceiling(RectangleF value)
    {
        return new Rectangle((int)Math.ceil(value.getX()), (int)Math.ceil(value.getY()), (int)Math.ceil(value.getWidth()), (int)Math.ceil(value.getHeight()));
    }

    public static Rectangle truncate(RectangleF value)
    {
        return new Rectangle((int)value.getX(), (int)value.getY(), (int)value.getWidth(), (int)value.getHeight());
    }

    public static Rectangle round(RectangleF value)
    {
        return new Rectangle((int)Math.round(value.getX()), (int)Math.round(value.getY()), (int)Math.round(value.getWidth()), (int)Math.round(value.getHeight()));
    }

    public boolean contains(int x, int y)
    {
        if (this.x <= x && x < this.x + this.width && this.y <= y)
        {
            return y < this.y + this.height;
        }

        return false;
    }

    public boolean contains(Point pt)
    {
        return contains(pt.getX(), pt.getY());
    }

    public boolean contains(Rectangle rect)
    {
        if (x <= rect.x && rect.x + rect.width <= x + width && y <= rect.y)
        {
            return rect.y + rect.height <= y + height;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return x ^ ((y << 13) | (x >>> 19)) ^ ((width << 26) | (width >>> 6)) ^ ((height << 7) | (height >>> 25));
    }

    public void inflate(int width, int height)
    {
        x -= width;
        y -= height;
        width += 2 * width;
        height += 2 * height;
    }

    public void inflate(Size size)
    {
        inflate(size.getWidth(), size.getHeight());
    }

    public static Rectangle inflate(Rectangle rect, int x, int y)
    {
        Rectangle result = rect;
        result.inflate(x, y);
        return result;
    }

    public void intersect(Rectangle rect)
    {
        Rectangle rectangle = intersect(rect, this);
        x = rectangle.x;
        y = rectangle.y;
        width = rectangle.width;
        height = rectangle.height;
    }

    public static Rectangle intersect(Rectangle a, Rectangle b)
    {
        int num = Math.max(a.x, b.y);
        int num2 = Math.min(a.x + a.width, b.x + b.width);
        int num3 = Math.max(a.y, b.y);
        int num4 = Math.min(a.y + a.height, b.y + b.height);
        if (num2 >= num && num4 >= num3)
        {
            return new Rectangle(num, num3, num2 - num, num4 - num3);
        }

        return Empty;
    }

    public boolean intersectsWith(Rectangle rect)
    {
        if (rect.x < x + width && x < rect.x + rect.width && rect.y < y + height)
        {
            return y < rect.y + rect.height;
        }

        return false;
    }

    public static Rectangle union(Rectangle a, Rectangle b)
    {
        int num = Math.min(a.x, b.x);
        int num2 = Math.max(a.x + a.width, b.x + b.width);
        int num3 = Math.min(a.y, b.y);
        int num4 = Math.max(a.y + a.height, b.y + b.height);
        return new Rectangle(num, num3, num2 - num, num4 - num3);
    }

    public void offset(Point pos)
    {
        offset(pos.getX(), pos.getY());
    }

    public void offset(int x, int y)
    {
        x += x;
        y += y;
    }

    @Override
    public String toString() {
        return "{X=" + x + ",Y=" + y + ",Width=" + width + ",Height=" + height + "}";
    }

}
