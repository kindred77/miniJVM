package com.kindred.mir.controls;

import com.kindred.mir.MirMain;
import com.kindred.mir.Settings;
import com.kindred.mir.constcode.MirBlendMode;
import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.MirJNI;
import com.kindred.mir.engine.MirTexture;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.util.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/*
所有控件的基类
 */
public class MirControl {
    
    public static MirControl ActiveControl, MouseControl;

    private static AtomicInteger id_counter=new AtomicInteger(0);

    private String ID;

    protected MirControl parent;
    private ControlCommonListener onParentChanged;

    protected Size size = Size.Empty;
    protected ControlCommonListener onSizeChanged;

    private Point location = new Point(0,0);
    private ControlCommonListener onLocationChanged;

    //protected boolean isTextureValid;

    private long cleanTime;
//    protected MirTexture controlTexture;
//    protected boolean isDrawControlTexture=true;
    //protected Size textureSize;

    private ArrayList<MirControl> children;
    private ControlCommonListener onChildAdded;
    private ControlCommonListener onChildRemoved;

    protected boolean isEnabled=true;
    private ControlCommonListener onEnabledChanged;

    protected boolean isDrawControlTexture=false;

    //是否已显示
    protected boolean isHasShown;
    private long lastClickTime=0L;
    protected ControlCommonListener onMouseLeftClick , onMouseLeftDoubleClick, onMouseEnter , onMouseLeave , onShown , onBeforeShown, onDisposing;

    protected ControlCommonListener onMouseMove, onMouseLeftDown, onMouseLeftUp, onMouseRightDown, onMouseRightUp;
    //private MouseEventHandler mouseWheel,mouseMove, mouseDown, mouseUp;
    //private KeyEventHandler keyDown , keyUp;
    //private KeyPressEventHandler keyPress;

    private String hint;
    private ControlCommonListener onHintChanged;

    private boolean isModal;
    private ControlCommonListener onModalChanged;

    protected boolean isStartToMove=false;
    private boolean isMovable=false;
    private Point startToMovePos;
    private ControlCommonListener onMovableChanged;
    private ControlCommonListener onMoving;

    private boolean isMouseLeftDown=false;
    private boolean isMouseRightDown=false;

    protected boolean isNotControl=false;
    private ControlCommonListener onNotControlChanged;

    protected float opacity;
    private ControlCommonListener onOpacityChanged;

    protected int sound;
    private ControlCommonListener onSoundChanged;

    private boolean isSort;
    private ControlCommonListener onSortChanged;

    //是否可见
    protected boolean isVisible=true;
    private ControlCommonListener onVisibleChanged;

    protected boolean isDisposed=false;

    public MirControl(MirControl parent)
    {
        children = new ArrayList();
        opacity = 1F;
        isEnabled = true;
        isVisible = true;
        sound = SoundList.None;
        setParent(parent);

        ID=String.valueOf(id_counter.incrementAndGet());
    }

    public final String getID() {
        return ID;
    }

    public final MirControl getParent()
    {
        return parent;
    }

    public final void setParent(MirControl parent)
    {
        if (this.parent == parent) {
            return;
        }

        if (this.parent != null) {
            this.parent.removeChild(this);
        }
        this.parent = parent;
        if (this.parent != null) {
            this.parent.addChild(this);
        }
        onParentChanged();
    }

    protected final void onParentChanged()
    {
        onLocationChanged();
        if (onParentChanged != null) {
            onParentChanged.doAction(this, null);
        }
    }

    public final Point getLocation()
    {
        return location;
    }

    public final void setLocation(Point location)
    {
        if (this.location == location) {
            return;
        }
        this.location = location;
        onLocationChanged();
    }

    protected final void onLocationChanged()
    {
        //redraw();
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                children.get(i).onLocationChanged();
            }
        }

        if (onLocationChanged != null) {
            onLocationChanged.doAction(this, null);
        }
    }

    public final Size getSize()
    {
        return size;
    }

    public final void setSize(Size size)
    {
        if (this.size == size) {
            return;
        }
        this.size = size;
        onSizeChanged();
    }

    protected final void onSizeChanged()
    {
        //isTextureValid = false;
        //redraw();

        if (onSizeChanged != null) {
            onSizeChanged.doAction(this, null);
        }
    }

    public final List<MirControl> getChildren()
    {
        return children;
    }

    private void addChild(MirControl control)
    {
        children.add(control);
        onChildAdded();
    }

    private void insertChild(int index, MirControl control)
    {
        if (control.parent != this)
        {
            control.setParent(null);
            control.parent = this;
        }

        if (index >= children.size()) {
            children.add(control);
        }
        else {
            children.add(index, control);
            onChildAdded();
        }
    }

    private void removeChild(MirControl control)
    {
        children.remove(control);
        onChildRemoved();
    }
    protected final void onChildAdded()
    {
        //redraw();
        if (onChildAdded != null) {
            onChildAdded.doAction(this, null);
        }
    }
    protected final void onChildRemoved()
    {
        //redraw();
        if (onChildRemoved != null) {
            onChildRemoved.doAction(this, null);
        }
    }

    public final boolean getIsEnabled()
    {
        return parent == null ? isEnabled : parent.isEnabled && isEnabled;
    }
    public final void setIsEnabled(boolean isEnabled)
    {
        if (this.isEnabled == isEnabled) {
            return;
        }
        this.isEnabled = isEnabled;
        onEnabledChanged();
    }

    protected final void onEnabledChanged()
    {
        //redraw();

        if (onEnabledChanged != null) {
            onEnabledChanged.doAction(this, null);
        }

        if (!this.isEnabled && ActiveControl == this) {
            ActiveControl.deactivate();
        }

        if (this.children != null) {
            for(MirControl control : children) {
                control.onEnabledChanged();
            }
        }
    }

    public final String getHint()
    {
        return hint;
    }
    public final void setHint(String hint)
    {
        if (this.hint == hint) {
            return;
        }

        this.hint = hint;
        onHintChanged();
    }

    protected final void onHintChanged()
    {
        //redraw();
        if (onHintChanged != null) {
            onHintChanged.doAction(this, null);
        }
    }

    public final boolean getIsModal()
    {
        return isModal;
    }
    public final void setIsModal(boolean isModal)
    {
        if (this.isModal == isModal) {
            return;
        }
        this.isModal = isModal;
        onModalChanged();
    }

    protected final void onModalChanged()
    {
        //redraw();
        if (onModalChanged != null) {
            onModalChanged.doAction(this, null);
        }
    }

    public final boolean getIsMovable()
    {
        return isMovable;
    }
    public final void setIsMovable(boolean isMovable)
    {
        if (this.isMovable == isMovable) {
            return;
        }
        this.isMovable = isMovable;
        onMovableChanged();
    }

    protected final void onMovableChanged()
    {
        //redraw();
        if (onMovableChanged != null) {
            onMovableChanged.doAction(this, null);
        }
    }

    public final boolean getIsNotControl()
    {
        return isNotControl;
    }
    public final void setIsNotControl(boolean isNotControl)
    {
        if (this.isNotControl == isNotControl) {
            return;
        }
        this.isNotControl = isNotControl;
        onNotControlChanged();
    }

    protected final void onNotControlChanged()
    {
        //redraw();
        if (onNotControlChanged != null) {
            onNotControlChanged.doAction(this, null);
        }
    }

    public final float getOpacity()
    {
        return opacity;
    }
    public final void setOpacity(float opacity)
    {
        if (opacity > 1F) {
            opacity = 1F;
        }
        if (opacity < 0F) {
            opacity = 0;
        }

        if (this.opacity == opacity) {
            return;
        }

        this.opacity = opacity;
        onOpacityChanged();
    }

    protected final void onOpacityChanged()
    {
        //redraw();
        if (onOpacityChanged != null) {
            onOpacityChanged.doAction(this, null);
        }
    }

    public final int getSound()
    {
        return sound;
    }
    public final void setSound(int sound)
    {
        if (this.sound == sound) {
            return;
        }
        this.sound = sound;
        onSoundChanged();
    }

    protected final void onSoundChanged()
    {
        if (onSoundChanged != null) {
            onSoundChanged.doAction(this, null);
        }
    }

    public final boolean getIsSort()
    {
        return isSort;
    }
    public final void setIsSort(boolean isSort)
    {
        if (this.isSort == isSort) {
            return;
        }
        this.isSort = isSort;
        onSortChanged();
    }

    protected final void onSortChanged()
    {
        //redraw();
        if (onSortChanged != null) {
            onSortChanged.doAction(this, null);
        }
    }
    public final void trySort()
    {
        if (parent == null) {
            return;
        }

        parent.trySort();

        if (parent.children.get(parent.children.size() - 1) == this) {
            return;
        }

        if (!isSort) {
            return;
        }

        parent.children.remove(this);
        parent.children.add(this);

        //redraw();
    }

    public final boolean getIsVisible()
    {
        return parent == null ? isVisible : parent.isVisible && isVisible;
    }
    public final void setIsVisible(boolean isVisible)
    {
        if (this.isVisible == isVisible) {
            return;
        }
        this.isVisible = isVisible;
        onVisibleChanged();
    }

    protected final void onVisibleChanged()
    {
        //redraw();
        if (onVisibleChanged != null) {
            onVisibleChanged.doAction(this, null);
        }

        isStartToMove = false;
        startToMovePos = Point.Empty;

        if (isSort && parent != null) {
            parent.children.remove(this);
            parent.children.add(this);
        }

        if (MouseControl == this && !isVisible) {
            dehighlight();
            deactivate();
        }
        //else if (isMouseOver(CMain.MPoint)) {
        //    highlight();
        //}


        if (children != null) {
            for (MirControl control : children) {
                control.onVisibleChanged();
            }
        }
    }
    private void onBeforeShown()
    {
        if (isHasShown) {
            return;
        }

//        if (isVisible && isMouseOver(CMain.MPoint))
//            highlight();

        if (onBeforeShown != null) {
            onBeforeShown.doAction(this, null);
        }
    }
    private void onShown()
    {
        if (isHasShown) {
            return;
        }

        if (onShown != null) {
            onShown.doAction(this, null);
        }

        isHasShown = true;
    }

    public final Point Center()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    public final Point Left()
    {
        return new Point(0, (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    public final Point Top()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, 0);
    }

    public final Point Right()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), (Settings.ScreenHeight - size.getHeight()) / 2);
    }

    public final Point Bottom()
    {
        return new Point((Settings.ScreenWidth - size.getWidth()) / 2, Settings.ScreenHeight - size.getHeight());
    }

    public final Point TopLeft()
    {
        return new Point(0, 0);
    }

    public final Point TopRight()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), 0);
    }

    public final Point BottomRight()
    {
        return new Point(Settings.ScreenWidth - size.getWidth(), Settings.ScreenHeight - size.getHeight());
    }

    public final Point BottomLeft()
    {
        return new Point(0, Settings.ScreenHeight - size.getHeight());
    }

    public final void bringToFront()
    {
        if (parent == null) {
            return;
        }
        int index = parent.children.indexOf(this);
        if (index == parent.children.size() - 1) {
            return;
        }

        parent.children.remove(index);
        parent.children.add(this);
        //redraw();
    }

    protected boolean drawControl()
    {
        return true;
    }

    public final boolean show()
    {
        if (isDisposed || !getIsVisible()) {
            return false;
        }

        Point pos = getLocation();

        int limitX=0;
        int limitY=0;
        if (parent!=null) {
            limitX=parent.getSize().getWidth();
            limitY=parent.getSize().getHeight();
        } else {
            limitX=Settings.ScreenWidth;
            limitY=Settings.ScreenHeight;
        }
        if (isDisposed || !getIsVisible() || pos.getX() > limitX || pos.getY() > limitY) {
            return false;
        }


        onBeforeShown();

        if (isDrawControlTexture) {
            drawControl();
        }

        showChildren();

        //cleanTime = CMain.Time + Settings.CleanDelay;

        onShown();

        return true;
    }

    private void showChildren()
    {
        if (children != null) {
            for (int i = 0; i < children.size(); i++) {
                if (children.get(i) != null) {
                    children.get(i).show();
                }
            }
        }
    }

    protected void deactivate()
    {
        if (ActiveControl != this) {
            return;
        }

        ActiveControl = null;
        isStartToMove = false;
        startToMovePos = Point.Empty;
    }
    protected void dehighlight()
    {
        if (MouseControl != this) {
            return;
        }
        MouseControl.onMouseLeave();
        MouseControl = null;
    }
    protected void activate()
    {
        if (ActiveControl == this) {
            return;
        }

        if (ActiveControl != null) {
            ActiveControl.deactivate();
        }

        ActiveControl = this;
    }
    protected void highlight()
    {
        if (MouseControl == this) {
            return;
        }
        if (isNotControl) {

        }
        if (MouseControl != null) {
            MouseControl.dehighlight();
        }

        if (ActiveControl != null && ActiveControl != this) {
            return;
        }

        onMouseEnter();
        MouseControl = this;
    }

    public final Rectangle getLocationRectangle()
    {
        return new Rectangle(getLocation(), size);
    }

    /*
    判断坐标在本控件中
     */
    protected boolean isMouseOver(Point p)
    {
        return isVisible && (getLocationRectangle().contains(p) || isStartToMove || isModal) && !isNotControl;
    }

    private void onMouseEnter()
    {

        //redraw();

        if (onMouseEnter != null) {
            onMouseEnter.doAction(this, null);
        }
    }

    private void onMouseLeave()
    {
        this.isMouseLeftDown=false;
        this.isMouseRightDown=false;
        //redraw();

        if (onMouseLeave != null) {
            onMouseLeave.doAction(this, null);
        }
    }

    public void setOnMouseLeftClick(ControlCommonListener onMouseLeftClick)
    {
        this.onMouseLeftClick = onMouseLeftClick;
    }

    private void onMouseLeftClick(Point posInParent)
    {
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());
        //优先处理子控件
        if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
                if (children.get(i).isMouseOver(posInMe)) {
                    children.get(i).onMouseLeftClick(posInMe);
                    return;
                }
            }
        }

        if (lastClickTime + Settings.DoubleClickIntervalTime >= Settings.getTime()) {
            lastClickTime=Settings.getTime();
            //派生双击事件
            onMouseLeftDoubleClick(posInParent);
            return;
        }
        lastClickTime=Settings.getTime();

        if (sound != SoundList.None) {
            SoundManager.playSound(sound, false);
        }

        if (onMouseLeftClick != null) {
            onMouseLeftClick.doAction(this, posInMe);
        }
    }

    private void onMouseRightClick(Point pos)
    {

    }

    private void onMouseLeftDoubleClick(Point posInParent)
    {
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());
        //优先处理子控件
        if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
                if (children.get(i).isMouseOver(posInMe)) {
                    children.get(i).onMouseLeftDoubleClick(posInMe);
                    return;
                }
            }
        }

        if (onMouseLeftDoubleClick != null) {
            if (sound != SoundList.None) {
                SoundManager.playSound(sound, false);
            }
            onMouseLeftDoubleClick.doAction(this, posInMe);
        } else {
            onMouseLeftClick(posInMe);
        }
    }

    private boolean isMouseIn = false;
    /*
    pos是在父控件中的座标
     */
    private void onMouseMove(Point posInParent)
    {
        //在本控件中
        if(isMouseOver(posInParent)) {
            //第一次在本控件中
            if(!isMouseIn) {
                isMouseIn=true;
                this.onMouseEnter();
            }
        } else {
            //第一次在本控件中
            if(isMouseIn) {
                isMouseIn=false;
                this.onMouseLeave();
            }
        }

        //moving
        if (isStartToMove) {
            Point tempPoint = Point.subtract(posInParent, startToMovePos);

            Size parentSize = parent == null ? new Size(Settings.ScreenWidth, Settings.ScreenHeight)
                    : parent.getSize();
            if (tempPoint.getY() + size.getHeight() > parentSize.getHeight()) {
                tempPoint.setY(parentSize.getHeight() - size.getHeight());
            }
            if (tempPoint.getX() + size.getWidth() > parentSize.getWidth()) {
                tempPoint.setX(parentSize.getWidth() - size.getWidth());
            }

            //TODO maybe bug
            if (tempPoint.getX() < 0) {
                tempPoint.setX(0);
            }
            if (tempPoint.getY() < 0) {
                tempPoint.setY(0);
            }

            setLocation(tempPoint);
            if (onMoving != null) {
                onMoving.doAction(this, null);
            }
            return;
        }

        highlight();

        if (onMouseMove != null) {
            onMouseMove.doAction(this, null);
        }
    }

    private void onMouseRightDown(Point posInParent)
    {
        isMouseRightDown=true;

        if (onMouseRightDown != null) {
            onMouseRightDown.doAction(this, null);
        }
    }

    private void onMouseLeftDown(Point posInParent)
    {
        activate();

        trySort();

        if (isMovable) {
            isStartToMove = true;
            //startToMovePos = Point.subtract(posInParent, location);
            startToMovePos = posInParent;
        }

        isMouseLeftDown=true;

        if (onMouseLeftDown != null) {
            onMouseLeftDown.doAction(this, null);
        }
    }

    private void onMouseLeftUp(Point posInParent)
    {
        if (isStartToMove) {
            isStartToMove = false;
            startToMovePos = Point.Empty;
        }

        if (ActiveControl != null) {
            ActiveControl.deactivate();
        }

        if (isMouseLeftDown) {
            isMouseLeftDown=false;
            onMouseLeftClick(posInParent);
        }

        if (onMouseLeftUp != null) {
            onMouseLeftUp.doAction(this, null);
        }
    }

    private void onMouseRightUp(Point posInParent)
    {
        if (isMouseRightDown) {
            isMouseRightDown=false;
            onMouseRightClick(posInParent);
        }

        if (onMouseRightUp != null) {
            onMouseRightUp.doAction(this, null);
        }
    }

    /*
    在父控件中的坐标
     */
    private boolean onOriginalMouseEvent(CommonEvent.EventEnum eventEnum, Point posInParent) {
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());
        //优先处理子控件
        if (children != null) {
            for (int i = children.size() - 1; i >= 0; i--) {
                if (children.get(i).onOriginalMouseEvent(eventEnum, posInMe)) {
                    return true;
                }
            }
        }

        if (eventEnum==CommonEvent.EventEnum.MouseLeftDown) {
            onMouseLeftDown(posInParent);
        } else if(eventEnum==CommonEvent.EventEnum.MouseLeftUp) {
            onMouseLeftUp(posInParent);
        } else if (eventEnum==CommonEvent.EventEnum.MouseRightDown) {
            onMouseRightDown(posInParent);
        } else if(eventEnum==CommonEvent.EventEnum.MouseRightUp) {
            onMouseRightUp(posInParent);
        } else if (eventEnum==CommonEvent.EventEnum.MouseMove) {
            onMouseMove(posInParent);
        } else {
            return false;
        }

        return true;
    }

    private boolean onOriginalKeyBoardEvent(CommonEvent.EventEnum eventEnum, Object arg) {
        return false;
    }

    /*
    处理了返回true，否则返回false
     */
    public final boolean onCommonEvent(CommonEvent event) throws Exception{
        if (!isEnabled) {
            return false;
        }

        CommonEvent.EventEnum eventEnum=event.getEventType();
        Object arg=event.getArg();
        if ((eventEnum==CommonEvent.EventEnum.MouseLeftDown ||
            eventEnum==CommonEvent.EventEnum.MouseLeftUp ||
            eventEnum==CommonEvent.EventEnum.MouseRightDown ||
            eventEnum==CommonEvent.EventEnum.MouseRightUp ||
            eventEnum==CommonEvent.EventEnum.MouseMove) && arg instanceof Point) {
            return onOriginalMouseEvent(eventEnum, (Point)arg);
        } else if (eventEnum==CommonEvent.EventEnum.KeyBoardPressed) {
            return onOriginalKeyBoardEvent(eventEnum, arg);
        } else {
            throw new Exception("Can not deal this type of event.");
        }
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

//    public void redraw()
//    {
//        if (parent != null) parent.redraw();
//    }

    public boolean getIsDisposed()
    {
        return isDisposed;
    }

    public void dispose()
    {
        if (isDisposed) {
            return;
        }
        dispose(true);
    }

    protected void dispose(boolean isDisposing)
    {
        if (isDisposing) {
            if (onDisposing != null) {
                onDisposing.doAction(this, null);
            }

            onDisposing = null;

            //isTextureValid = false;

            onChildAdded = null;
            onChildRemoved = null;

            if (children != null) {
                for (int i = children.size() - 1; i >= 0; i--) {
                    if (children.get(i) != null && !children.get(i).isDisposed) {
                        children.get(i).dispose();
                    }
                }

                children = null;
            }
            isEnabled = false;
            onEnabledChanged = null;

            isHasShown = false;

            onShown = null;
            onBeforeShown = null;

            onMouseLeftClick = null;
            onMouseLeftDoubleClick = null;
            onMouseEnter = null;
            onMouseLeave = null;
//            mouseMove = null;
//            mouseDown = null;
//            mouseUp = null;
//            mouseWheel = null;
//
//            keyPress = null;
//            keyUp = null;
//            keyDown = null;

            onLocationChanged = null;
            location = Point.Empty;

            onModalChanged = null;
            isModal = false;

            onMovableChanged = null;
            startToMovePos = Point.Empty;
            isStartToMove = false;
            onMoving = null;
            isMovable = false;

            onNotControlChanged = null;
            isNotControl = false;

            onOpacityChanged = null;
            opacity = 0F;

            if (parent != null && parent.children != null) {
                parent.children.remove(this);
            }
            onParentChanged = null;
            parent = null;

            onSizeChanged = null;
            size = Size.Empty;

            onSoundChanged = null;
            sound = 0;

            onVisibleChanged = null;
            isVisible = false;

            if (ActiveControl == this) {
                ActiveControl = null;
            }
            if (MouseControl == this) {
                MouseControl = null;
            }
        }

        isDisposed = true;
    }
}
