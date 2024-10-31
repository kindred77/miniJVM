package com.kindred.mir.util;

public class Vector2 {

    private float x;
    private float y;

    public Vector2()
    {
        y = 0f;
        x = 0f;
    }

    public static Vector2 Empty()
    {
        return new Vector2(0f, 0f);
    }

    public Vector2(float x, float y)
    {
        this.x=x;
        this.y=y;
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
        return (int)(double)y ^ (int)(double)x;
    }
}
