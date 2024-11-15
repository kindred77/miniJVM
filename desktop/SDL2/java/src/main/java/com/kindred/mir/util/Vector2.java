package com.kindred.mir.util;

public class Vector2 {

    private int x;
    private int y;

    public Vector2()
    {
        y = 0;
        x = 0;
    }

    public static Vector2 Empty()
    {
        return new Vector2(0, 0);
    }

    public Vector2(int x, int y)
    {
        this.x=x;
        this.y=y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
        {
            return false;
        }

        if (obj.getClass() != getClass())
        {
            return false;
        }

        Vector2 vector = (Vector2)((obj instanceof Vector2) ? obj : null);
        Vector2 vector2 = this;

        return vector.x == vector2.x && vector.y == vector2.y;
    }

    @Override
    public int hashCode() {
        return y ^ x;
    }
}
