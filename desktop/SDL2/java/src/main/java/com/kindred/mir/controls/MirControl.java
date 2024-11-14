package com.kindred.mir.controls;

import com.kindred.mir.MirMain;
import com.kindred.mir.Settings;
import com.kindred.mir.constcode.MirBlendMode;
import com.kindred.mir.controls.events.MouseClickEvent;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.util.*;

import java.util.ArrayList;
import java.util.List;

public class MirControl implements AutoCloseable {
    
    public static MirControl ActiveControl, MouseControl;

    protected MirControl parent;
    private ControlCommonListener parentChanged;

    protected Size size;
    private ControlCommonListener sizeChanged;

    private Point location;
    private ControlCommonListener locationChanged;

    protected boolean isTextureValid;
    protected boolean isGrayScale;
    protected boolean isBlending;
    protected float blendingRate;
    private MirBlendMode.BlendMode blendMode;

    protected Color backColor;
    private ControlCommonListener backColorChanged;

    private Rectangle borderRectangle;
    private boolean isBorder;
    private Vector2[] borderInfo;
    private ControlCommonListener borderChanged;

    private Color borderColor;
    private ControlCommonListener borderColorChanged;

    private long cleanTime;
    protected MirTexture controlTexture;
    protected boolean isDrawControlTexture;
    //protected Size textureSize;

    private ArrayList<MirControl> children;
    private ControlCommonListener childAdded;
    private ControlCommonListener childRemoved;

    protected boolean isEnabled;
    private ControlCommonListener enabledChanged;

    protected boolean isHasShown;
    protected ControlCommonListener click , doubleClick, beforeDraw , afterDraw , mouseEnter , mouseLeave , shown , beforeShown, disposing;

    private ControlCommonListener mouseMove, mouseDown, mouseUp;
    //private MouseEventHandler mouseWheel,mouseMove, mouseDown, mouseUp;
    //private KeyEventHandler keyDown , keyUp;
    //private KeyPressEventHandler keyPress;

    protected Color foreColor;
    private ControlCommonListener foreColorChanged;

    private String hint;
    private ControlCommonListener hintChanged;

    private boolean isModal;
    private ControlCommonListener modalChanged;

    protected boolean isMoving;
    private boolean isMovable;
    private Point movePoint;
    private ControlCommonListener movableChanged;
    private ControlCommonListener onMoving;

    protected boolean isNotControl;
    private ControlCommonListener notControlChanged;

    protected float opacity;
    private ControlCommonListener opacityChanged;

    protected int sound;
    private ControlCommonListener soundChanged;

    private boolean isSort;
    private ControlCommonListener sortChanged;

    protected boolean isVisible;
    private ControlCommonListener visibleChanged;

    protected boolean isDisposed;

    public MirControl(MirControl parent)
    {
        children = new ArrayList();
        opacity = 1F;
        isEnabled = true;
        foreColor = Color.White;
        isVisible = true;
        sound = SoundList.None;
        setParent(parent);
    }

    public MirControl getParent()
    {
        return parent;
    }

    public void setParent(MirControl parent)
    {
        if (this.parent == parent) return;

        if (this.parent != null)
            this.parent.removeChild(this);
        this.parent = parent;
        if (this.parent != null)
            this.parent.addChild(this);
        onParentChanged();
    }

    protected void onParentChanged()
    {
        onLocationChanged();
        if (parentChanged != null)
            parentChanged.doAction(this, null);
    }

    public Point getLocation()
    {
        return location;
    }

    public void setLocation(Point location)
    {
        if (this.location == location)
            return;
        this.location = location;
        onLocationChanged();
    }

    protected void onLocationChanged()
    {
        redraw();
        if (children != null)
            for (int i = 0; i < children.size(); i++)
                children.get(i).onLocationChanged();

        if (locationChanged != null)
            locationChanged.doAction(this, null);
    }

    public Point getDisplayLocation() {
        return parent == null ? location : Point.add(parent.getDisplayLocation(), location);
    }

    public Size getSize()
    {
        return size;
    }

    public void setSize(Size size)
    {
        if (this.size == size)
            return;
        this.size = size;
        onSizeChanged();
    }

    public Size getTrueSize()
    {
        return size;
    }


    protected void onSizeChanged()
    {
        isTextureValid = false;
        redraw();

        if (sizeChanged != null)
            sizeChanged.doAction(this, null);
    }

    public Rectangle getDisplayRectangle()
    {
        return new Rectangle(getDisplayLocation(), size);
    }

    public boolean getIsGrayScale()
    {
        return isGrayScale;
    }
    public void setIsGrayScale(boolean isGrayScale)
    {
        this.isGrayScale=isGrayScale;
    }
    public boolean getIsBlending()
    {
        return isBlending;
    }
    public void setIsBlending(boolean isBlending)
    {
        this.isBlending=isBlending;
    }
    public float getBlendingRate()
    {
        return blendingRate;
    }
    public void setBlendingRate(float blendingRate)
    {
        this.blendingRate=blendingRate;
    }
    public MirBlendMode.BlendMode getBlendMode()
    {
        return blendMode;
    }

    public void setBlendMode(MirBlendMode.BlendMode blendMode)
    {
        this.blendMode=blendMode;
    }


    public Color getBackColor()
    {
        return backColor;
    }

    public void setBackColor(Color backColor)
    {
        if (this.backColor == backColor)
            return;
        this.backColor = backColor;
        onBackColorChanged();
    }

    protected void onBackColorChanged()
    {
        isTextureValid = false;
        redraw();
        if (backColorChanged != null)
            backColorChanged.doAction(this, null);
    }


    protected Vector2[] getBorderInfo()
    {
        if (size == Size.Empty)
            return null;

        Rectangle displayRectangle=getDisplayRectangle();
        if (borderRectangle != displayRectangle)
        {
            borderInfo = new Vector2[]
            {
                new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getTop() - 1),
                        new Vector2(displayRectangle.getRight(), displayRectangle.getTop() - 1),
                        new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getTop() - 1),
                        new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getBottom()),
                        new Vector2(displayRectangle.getLeft() - 1, displayRectangle.getBottom()),
                        new Vector2(displayRectangle.getRight(), displayRectangle.getBottom()),
                        new Vector2(displayRectangle.getRight(), displayRectangle.getTop() - 1),
                        new Vector2(displayRectangle.getRight(), displayRectangle.getBottom())
            };

            borderRectangle = displayRectangle;
        }
        return borderInfo;
    }

    public boolean getIsBorder()
    {
        return isBorder;
    }

    public void setIsBorder(boolean isBorder)
    {
        if (this.isBorder == isBorder)
            return;
        this.isBorder = isBorder;
        onBorderChanged();
    }


    private void onBorderChanged()
    {
        redraw();
        if (borderChanged != null)
            borderChanged.doAction(this, null);
    }

    public Color getBorderColor()
    {
        return borderColor;
    }

    public void setBorderColor(Color borderColor)
    {
        if (this.borderColor == borderColor)
            return;
        this.borderColor = borderColor;
        onBorderColourChanged();
    }

    private void onBorderColourChanged()
    {
        redraw();
        if (borderColorChanged != null)
            borderColorChanged.doAction(this, null);
    }

    public boolean getIsDrawControlTexture()
    {
        return isDrawControlTexture;
    }
    public void setIsDrawControlTexture(boolean isDrawControlTexture)
    {
        if (this.isDrawControlTexture == isDrawControlTexture)
            return;
        this.isDrawControlTexture = isDrawControlTexture;
        redraw();
    }

    protected void createTexture()
    {
        if (controlTexture != null && !controlTexture.getIsDisposed() && !controlTexture.getSize().equals(size))
            controlTexture.dispose();

        //controlTexture = new MirTexture(size.getWidth(), size.getHeight(), SDL_PixelFormatEnum.SDL_PIXELFORMAT_ARGB8888, getBackColor());
        //isTextureValid = true;
    }

    protected void controlTexture_Disposing()
    {
        controlTexture = null;
        isTextureValid = false;
        //textureSize = Size.Empty;

        //DXManager.ControlList.Remove(this);
    }
    private void disposeTexture()
    {
        if (controlTexture == null || controlTexture.getIsDisposed()) return;
        controlTexture.dispose();
    }

    public List<MirControl> getChildren()
    {
        return children;
    }

    private void addChild(MirControl control)
    {
        children.add(control);
        onChildAdded();
    }
    public void insertChild(int index, MirControl control)
    {
        if (control.parent != this)
        {
            control.setParent(null);
            control.parent = this;
        }

        if (index >= children.size())
            children.add(control);
        else
        {
            children.add(index, control);
            onChildAdded();
        }
    }
    private void removeChild(MirControl control)
    {
        children.remove(control);
        onChildRemoved();
    }
    private void onChildAdded()
    {
        redraw();
        if (childAdded != null)
            childAdded.doAction(this, null);
    }
    private void onChildRemoved()
    {
        redraw();
        if (childRemoved != null)
            childRemoved.doAction(this, null);
    }

    public boolean getIsEnabled()
    {
        return parent == null ? isEnabled : parent.isEnabled && isEnabled;
    }
    public void setIsEnabled(boolean isEnabled)
    {
        if (this.isEnabled == isEnabled)
            return;
        this.isEnabled = isEnabled;
        onEnabledChanged();
    }

    protected void onEnabledChanged()
    {
        redraw();

        if (enabledChanged != null)
            enabledChanged.doAction(this, null);

        if (!this.isEnabled && ActiveControl == this)
            ActiveControl.deactivate();

        if (this.children != null)
        {
            for(MirControl control : children)
            {
                control.onEnabledChanged();
            }
        }
    }

    public Color getForeColor()
    {
        return foreColor;
    }
    public void setForeColor(Color foreColor)
    {
        if (this.foreColor == foreColor)
            return;
        this.foreColor = foreColor;
        onForeColorChanged();
    }

    protected void onForeColorChanged()
    {
        isTextureValid = false;
        if (foreColorChanged != null)
            foreColorChanged.doAction(this, null);
    }

    public String getHint()
    {
        return hint;
    }
    public void setHint(String hint)
    {
        if (this.hint == hint)
            return;

        this.hint = hint;
        onHintChanged();
    }

    private void onHintChanged()
    {
        redraw();
        if (hintChanged != null)
            hintChanged.doAction(this, null);
    }

    public boolean getIsModal()
    {
        return isModal;
    }
    public void setIsModal(boolean isModal)
    {
        if (this.isModal == isModal)
            return;
        this.isModal = isModal;
        onModalChanged();
    }

    private void onModalChanged()
    {
        redraw();
        if (modalChanged != null)
            modalChanged.doAction(this, null);
    }

    public boolean getIsMovable()
    {
        return isMovable;
    }
    public void setIsMovable(boolean isMovable)
    {
        if (this.isMovable == isMovable)
            return;
        this.isMovable = isMovable;
        onMovableChanged();
    }

    private void onMovableChanged()
    {
        redraw();
        if (movableChanged != null)
            movableChanged.doAction(this, null);
    }

    public boolean getIsNotControl()
    {
        return isNotControl;
    }
    public void setIsNotControl(boolean isNotControl)
    {
        if (this.isNotControl == isNotControl)
            return;
        this.isNotControl = isNotControl;
        onNotControlChanged();
    }

    private void onNotControlChanged()
    {
        redraw();
        if (notControlChanged != null)
            notControlChanged.doAction(this, null);
    }

    public float getOpacity()
    {
        return opacity;
    }
    public void setOpacity(float opacity)
    {
        if (opacity > 1F)
            opacity = 1F;
        if (opacity < 0F)
            opacity = 0;

        if (this.opacity == opacity)
            return;

        this.opacity = opacity;
        onOpacityChanged();
    }

    private void onOpacityChanged()
    {
        redraw();
        if (opacityChanged != null)
            opacityChanged.doAction(this, null);
    }

    public int getSound()
    {
        return sound;
    }
    public void setSound(int sound)
    {
        if (this.sound == sound)
            return;
        this.sound = sound;
        onSoundChanged();
    }

    private void onSoundChanged()
    {
        if (soundChanged != null)
            soundChanged.doAction(this, null);
    }

    public boolean getIsSort()
    {
        return isSort;
    }
    public void setIsSort(boolean isSort)
    {
        if (this.isSort == isSort)
            return;
        this.isSort = isSort;
        onSortChanged();
    }

    private void onSortChanged()
    {
        redraw();
        if (sortChanged != null)
            sortChanged.doAction(this, null);
    }
    public void trySort()
    {
        if (parent == null)
            return;

        parent.trySort();

        if (parent.children.get(parent.children.size() - 1) == this)
            return;

        if (!isSort) return;

        parent.children.remove(this);
        parent.children.add(this);

        redraw();
    }

    public boolean getIsVisible()
    {
        return parent == null ? isVisible : parent.isVisible && isVisible;
    }
    public void setIsVisible(boolean isVisible)
    {
        if (this.isVisible == isVisible)
            return;
        this.isVisible = isVisible;
        onVisibleChanged();
    }

    protected void onVisibleChanged()
    {
        redraw();
        if (visibleChanged != null)
            visibleChanged.doAction(this, null);

        isMoving = false;
        movePoint = Point.Empty;

        if (isSort && parent != null)
        {
            parent.children.remove(this);
            parent.children.add(this);
        }

        if (MouseControl == this && !isVisible)
        {
            dehighlight();
            deactivate();
        }
        //else if (isMouseOver(CMain.MPoint)) {
        //    highlight();
        //}


        if (children != null)
        {
            for (MirControl control : children)
                control.onVisibleChanged();
        }
    }
    protected void onBeforeShown()
    {
        if (isHasShown)
            return;

//        if (isVisible && isMouseOver(CMain.MPoint))
//            highlight();

        if (beforeShown != null)
            beforeShown.doAction(this, null);
    }
    protected void onShown()
    {
        if (isHasShown)
            return;

        if (shown != null)
            shown.doAction(this, null);

        isHasShown = true;
    }

    public void setMultiLine()
    {
    }

    protected Point Center()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    protected Point Left()
    {
        return new Point(0, (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    protected Point Top()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, 0);
    }

    protected Point Right()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    protected Point Bottom()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, Settings.ScreenHeight - size.getHeight());
    }

    protected Point TopLeft()
    {
        return new Point(0, 0);
    }

    protected Point TopRight()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), 0);
    }

    protected Point BottomRight()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), Settings.ScreenHeight - size.getHeight());
    }

    protected Point BottomLeft()
    {
        return new Point(0, Settings.ScreenHeight - size.getHeight());
    }

    public void bringToFront()
    {
        if (parent == null) return;
        int index = parent.children.indexOf(this);
        if (index == parent.children.size() - 1) return;

        parent.children.remove(index);
        parent.children.add(this);
        redraw();
    }

    public final void draw(long renderer_id)
    {
        if (isDisposed || !getIsVisible() /*|| Size.Width == 0 || Size.Height == 0*/ || size.getWidth() > Settings.ScreenWidth || size.getHeight() > Settings.ScreenHeight)
            return;

        onBeforeShown();

        beforeDrawControl();
        drawControl(renderer_id);
        drawChildren(renderer_id);
        drawBorder();
        afterDrawControl();

        //cleanTime = CMain.Time + Settings.CleanDelay;

        onShown();
    }

    protected void beforeDrawControl()
    {
        if (beforeDraw != null)
            beforeDraw.doAction(this, null);
    }
    protected void drawControl(long renderer_id)
    {
        if (!isDrawControlTexture)
            return;

        if (!isTextureValid)
            createTexture();

        if (controlTexture == null || controlTexture.getIsDisposed())
            return;

        //float oldOpacity = DXManager.Opacity;

        //DXManager.SetOpacity(opacity);
        //DXManager.Sprite.Draw2D(controlTexture, Point.Empty, 0F, DisplayLocation, Color.White);
        //DXManager.SetOpacity(oldOpacity);

        //cleanTime = CMain.Time + Settings.CleanDelay;
    }
    protected void drawChildren(long renderer_id)
    {
        if (children != null)
            for (int i = 0; i < children.size(); i++)
                if (children.get(i) != null)
                    children.get(i).draw(renderer_id);
    }
    protected void drawBorder()
    {
        if (!isBorder || borderInfo == null)
            return;
        //DXManager.Sprite.Flush();
        //DXManager.Line.Draw(borderInfo, borderColor);
    }
    protected void afterDrawControl()
    {
        if (afterDraw != null)
            afterDraw.doAction(this, null);
    }

    protected void deactivate()
    {
        if (ActiveControl != this)
            return;

        ActiveControl = null;
        isMoving = false;
        movePoint = Point.Empty;
    }
    protected void dehighlight()
    {
        if (MouseControl != this)
            return;
        MouseControl.onMouseLeave();
        MouseControl = null;
    }
    protected void activate()
    {
        if (ActiveControl == this)
            return;

        if (ActiveControl != null)
            ActiveControl.deactivate();

        ActiveControl = this;
    }
    protected void highlight()
    {
        if (MouseControl == this)
            return;
        if (isNotControl)
        {

        }
        if (MouseControl != null)
            MouseControl.dehighlight();

        if (ActiveControl != null && ActiveControl != this) return;

        onMouseEnter();
        MouseControl = this;
    }

    public boolean isMouseOver(Point p)
    {
        return isVisible && (getDisplayRectangle().contains(p) || isMoving || isModal) && !isNotControl;
    }
    protected void onMouseEnter()
    {
        if (!isEnabled)
            return;

        redraw();

        if (mouseEnter != null)
            mouseEnter.doAction(this, null);
    }
    protected void onMouseLeave()
    {
        if (!isEnabled)
            return;

        redraw();

        if (mouseLeave != null)
            mouseLeave.doAction(this, null);
    }
    public void onMouseClick(MouseClickEvent e)
    {
        if (!isEnabled)
            return;

        if (sound != SoundList.None)
            SoundManager.playSound(sound, false);

        if (click != null)
            invokeMouseClick(e);
    }

    public void onMouseDoubleClick(MouseClickEvent e)
    {
        if (!isEnabled)
            return;

        if (doubleClick != null)
        {
            if (sound != SoundList.None)
                SoundManager.playSound(sound, false);
            invokeMouseDoubleClick(e);
        }
        else
            onMouseClick(e);
    }

    public void invokeMouseClick(MouseClickEvent e)
    {
        if (click != null)
            click.doAction(this, e);
    }

    public void invokeMouseDoubleClick(MouseClickEvent e)
    {
        doubleClick.doAction(this, e);
    }

    public void onMouseMove(Point pos)
    {
        if (!isEnabled)
            return;


        if (isMoving)
        {
            Point tempPoint = Point.subtract(MirMain.MPoint, movePoint);
            Size trueSize=getTrueSize();

            if (parent == null)
            {
                if (tempPoint.getY() + trueSize.getHeight() > Settings.ScreenHeight)
                    tempPoint.setY(Settings.ScreenHeight - trueSize.getHeight() - 1);

                if (tempPoint.getX() + trueSize.getWidth() > Settings.ScreenWidth)
                    tempPoint.setX(Settings.ScreenWidth - trueSize.getWidth() - 1);
            }
            else
            {
                Size parentTrueSize=parent.getTrueSize();
                if (tempPoint.getY() + trueSize.getHeight() > parentTrueSize.getHeight())
                    tempPoint.setY(parentTrueSize.getHeight() - trueSize.getHeight());

                if (tempPoint.getX() + trueSize.getWidth() > parentTrueSize.getWidth())
                    tempPoint.setX(parentTrueSize.getWidth() - trueSize.getWidth());
            }

            if (tempPoint.getX() < 0)
                tempPoint.setX(0);
            if (tempPoint.getY() < 0)
                tempPoint.setY(0);

            setLocation(tempPoint);
            if (onMoving != null)
                onMoving.doAction(this, null);
            return;
        }

        if (children != null)
            for (int i = children.size() - 1; i >= 0; i--)
                if (children.get(i).isMouseOver(MirMain.MPoint))
                {
                    children.get(i).onMouseMove(pos);
                    return;
                }

        highlight();

        if (mouseMove != null)
            mouseMove.doAction(this, null);
    }

    public void onMouseDown(Point pos)
    {
        if (!isEnabled)
            return;

        activate();

        trySort();

        if (isMovable)
        {
            isMoving = true;
            movePoint = Point.subtract(MirMain.MPoint, location);
        }

        if (mouseDown != null)
            mouseDown.doAction(this, null);
    }

    public void onMouseUp(Point pos)
    {
        if (!isEnabled)
            return;

        if (isMoving)
        {
            isMoving = false;
            movePoint = Point.Empty;
        }

        if (ActiveControl != null) ActiveControl.deactivate();

        if (mouseUp != null)
            mouseUp.doAction(this, null);
    }

//    public void onMouseWheel(MouseEventArgs e)
//    {
//        if (!isEnabled)
//            return;
//
//        if (mouseWheel != null)
//            mouseWheel(this, e);
//    }
//    public void onKeyPress(KeyPressEventArgs e)
//    {
//        if (!isEnabled)
//            return;
//
//        if (children != null)
//            for (int i = children.size() - 1; i >= 0; i--)
//                if (e.Handled)
//                    return;
//                else
//                    children.get(i).onKeyPress(e);
//
//        if (keyPress == null)
//            return;
//        keyPress.invoke(this, e);
//    }
//    public void onKeyDown(KeyEventArgs e)
//    {
//        if (!isEnabled)
//            return;
//
//        if (children != null)
//            for (int i = children.size() - 1; i >= 0; i--)
//                if (e.Handled)
//                    return;
//                else
//                    children.get(i).onKeyDown(e);
//
//        if (keyDown == null)
//            return;
//        keyDown.invoke(this, e);
//    }
//    public void onKeyUp(KeyEventArgs e)
//    {
//        if (!isEnabled)
//            return;
//
//        if (children != null)
//            for (int i = children.size() - 1; i >= 0; i--)
//                if (e.Handled)
//                    return;
//                else
//                    children.get(i).onKeyUp(e);
//
//        if (keyUp == null)
//            return;
//        keyUp.invoke(this, e);
//    }

    public void redraw()
    {
        if (parent != null) parent.redraw();
    }

    public boolean getIsDisposed()
    {
        return isDisposed;
    }

    public void dispose()
    {
        if (isDisposed)
            return;
        dispose(true);
    }

    protected void dispose(boolean isDisposing)
    {
        if (isDisposing)
        {
            if (disposing != null)
                disposing.doAction(this, null);

            disposing = null;

            backColorChanged = null;
            backColor = Color.Empty;

            borderChanged = null;
            isBorder = false;
            borderRectangle = Rectangle.Empty;
            borderInfo = null;

            borderColorChanged = null;
            borderColor = Color.Empty;

            isDrawControlTexture = false;
            if (controlTexture != null && !controlTexture.getIsDisposed())
                controlTexture.dispose();
            controlTexture = null;
            isTextureValid = false;

            childAdded = null;
            childRemoved = null;

            if (children != null)
            {
                for (int i = children.size() - 1; i >= 0; i--)
                {
                    if (children.get(i) != null && !children.get(i).isDisposed)
                        children.get(i).dispose();
                }

                children = null;
            }
            isEnabled = false;
            enabledChanged = null;

            isHasShown = false;

            beforeDraw = null;
            afterDraw = null;
            shown = null;
            beforeShown = null;

            click = null;
            doubleClick = null;
            mouseEnter = null;
            mouseLeave = null;
//            mouseMove = null;
//            mouseDown = null;
//            mouseUp = null;
//            mouseWheel = null;
//
//            keyPress = null;
//            keyUp = null;
//            keyDown = null;

            foreColorChanged = null;
            foreColor = Color.Empty;

            locationChanged = null;
            location = Point.Empty;

            modalChanged = null;
            isModal = false;

            movableChanged = null;
            movePoint = Point.Empty;
            isMoving = false;
            onMoving = null;
            isMovable = false;

            notControlChanged = null;
            isNotControl = false;

            opacityChanged = null;
            opacity = 0F;

            if (parent != null && parent.children != null)
                parent.children.remove(this);
            parentChanged = null;
            parent = null;

            sizeChanged = null;
            size = Size.Empty;

            soundChanged = null;
            sound = 0;

            visibleChanged = null;
            isVisible = false;

            if (ActiveControl == this) ActiveControl = null;
            if (MouseControl == this) MouseControl = null;
        }

        isDisposed = true;
    }

    @Override
    public void close() throws Exception {

    }
}
