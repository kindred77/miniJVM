package com.kindred.mir.controls;

import com.kindred.mir.Settings;
import com.kindred.mir.controls.events.CommonEvent;
import com.kindred.mir.controls.listener.ControlCommonListener;
import com.kindred.mir.engine.SoundList;
import com.kindred.mir.engine.SoundManager;
import com.kindred.mir.util.*;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicInteger;

/*
所有控件的基类，
默认是不会显示子控件的
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

    private CopyOnWriteArrayList<MirControl> children;
    private ControlCommonListener onChildAdded;
    private ControlCommonListener onChildRemoved;

    protected boolean isEnabled=true;
    private ControlCommonListener onEnabledChanged;

    protected boolean isDrawControlTexture=false;
    protected boolean isShowChildren=true;

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

    //private boolean isSort;
    //private ControlCommonListener onSortChanged;

    //是否可见
    protected boolean isVisible=true;
    private ControlCommonListener onVisibleChanged;

    //即使处理了事件,是否继续往下传递
    protected boolean isByPassEvent=false;

    protected boolean isDisposed=false;

    public MirControl(MirControl parent)
    {
        children = new CopyOnWriteArrayList<>();
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

    public MirControl getParent()
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

    public Point getLocation()
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
        for (int i = 0; i < children.size(); i++) {
            children.get(i).onLocationChanged();
        }

        if (onLocationChanged != null) {
            onLocationChanged.doAction(this, null);
        }
    }

    /**
     * 绝对坐标
     * @return
     */
    public Point getAbsoluteLocation() {
//        if (parent!=null && parent instanceof MirControlCanBeDrawn) {
//            MirControlCanBeDrawn controlCanBeDrawn = (MirControlCanBeDrawn)parent;
//            return Point.add(controlCanBeDrawn.getDisplayLocation(), getLocation());
//        }
        if (parent!=null) {
            return Point.add(parent.getAbsoluteLocation(), getLocation());
        }
        return getLocation();
    }

    public final Rectangle getAbsoluteRectangle()
    {
        return new Rectangle(getAbsoluteLocation(), getSize());
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

    /*
    插入尾部
     */
    private void addChild(MirControl control)
    {
        children.add(control);
        onChildAdded();
    }

//    private void insertChild(int index, MirControl control)
//    {
//        if (control.parent != this)
//        {
//            control.setParent(null);
//            control.parent = this;
//        }
//
//        if (index >= children.size()) {
//            children.add(control);
//        }
//        else {
//            children.add(index, control);
//            onChildAdded();
//        }
//    }

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

        for(MirControl control : children) {
            control.onEnabledChanged();
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

//    public final boolean getIsSort()
//    {
//        return isSort;
//    }
//    public final void setIsSort(boolean isSort)
//    {
//        if (this.isSort == isSort) {
//            return;
//        }
//        this.isSort = isSort;
//        onSortChanged();
//    }
//
//    protected final void onSortChanged()
//    {
//        //redraw();
//        if (onSortChanged != null) {
//            onSortChanged.doAction(this, null);
//        }
//    }

//    public final void trySort()
//    {
//        if (parent == null) {
//            return;
//        }
//
//        parent.trySort();
//
//        if (parent.children.get(parent.children.size() - 1) == this) {
//            return;
//        }
//
//        if (!isSort) {
//            return;
//        }
//
//        parent.children.remove(this);
//        parent.children.add(this);
//
//        //redraw();
//    }

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

//        if (/*isSort &&*/ parent != null) {
//            parent.removeChild(this);
//            parent.addChild(this);
//        }
        if (isVisible) {
            bringToFront();
        }

        if (MouseControl == this && !isVisible) {
            dehighlight();
            deactivate();
        }
        //else if (isMouseOver(CMain.MPoint)) {
        //    highlight();
        //}


        for (MirControl control : children) {
            control.onVisibleChanged();
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
        int parentWidth=Settings.ScreenWidth;
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
            parentHeight=parent.getSize().getHeight();
        }

        return new Point((parentWidth - size.getWidth()) / 2, (parentHeight - size.getHeight()) / 2);
    }

    public final Point Left()
    {
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentHeight=parent.getSize().getHeight();
        }
        return new Point(0, (parentHeight - size.getHeight()) / 2);
    }

    public final Point Top()
    {
        int parentWidth=Settings.ScreenWidth;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
        }
        return new Point((parentWidth - size.getWidth()) / 2, 0);
    }

    public final Point Right()
    {
        int parentWidth=Settings.ScreenWidth;
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
            parentHeight=parent.getSize().getHeight();
        }
        return new Point(parentWidth - size.getWidth(), (parentHeight - size.getHeight()) / 2);
    }

    public final Point Bottom()
    {
        int parentWidth=Settings.ScreenWidth;
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
            parentHeight=parent.getSize().getHeight();
        }
        return new Point((parentWidth - size.getWidth()) / 2, parentHeight - size.getHeight());
    }

    public final Point TopLeft()
    {
        return new Point(0, 0);
    }

    public final Point TopRight()
    {
        int parentWidth=Settings.ScreenWidth;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
        }
        return new Point(parentWidth - size.getWidth(), 0);
    }

    public final Point BottomRight()
    {
        int parentWidth=Settings.ScreenWidth;
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentWidth=parent.getSize().getWidth();
            parentHeight=parent.getSize().getHeight();
        }
        return new Point(parentWidth - size.getWidth(), parentHeight - size.getHeight());
    }

    public final Point BottomLeft()
    {
        int parentHeight=Settings.ScreenHeight;
        if (parent!=null) {
            parentHeight=parent.getSize().getHeight();
        }
        return new Point(0, parentHeight - size.getHeight());
    }

    public final void bringToFront()
    {
        if (parent == null) {
            return;
        }
        //这里应该也不用加锁了
        if (parent.children.remove(this)) {
            parent.children.add(this);
        }

//        int index = parent.children.indexOf(this);
//        if (index == parent.children.size() - 1) {
//            return;
//        }
        //parent.children.remove(index);
        //parent.children.add(this);
    }

    protected boolean _drawControl()
    {
        return true;
    }

    /*
    用于imgui
     */
    protected boolean _beginDraw() {
        return true;
    }

    /*
    用于imgui
     */
    protected boolean _endDraw() {
        return true;
    }

    /*
      show有两种方式，
      一种是先画父控件，再画子控件
      另一种是父控件begin，然后再子控件begin，子控件end，父控件end，imgui式的画法
    */
    protected final boolean showImpl() {
        if (!_beginDraw()) {
            return false;
        }

        _drawControl();

        if (isShowChildren) {
            showChildren();
        }

        _endDraw();
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

        if(!isDrawControlTexture || !showImpl()) {
            return false;
        }

        //cleanTime = CMain.Time + Settings.CleanDelay;

        onShown();

        return true;
    }

    protected final void showChildren()
    {
        //从头到尾遍历
        //if (children != null) {
            for (MirControl child : children) {
                if (child != null) {
                    child.show();
                }
            }
//            for (int i = 0; i < children.size(); i++) {
//                if (children.get(i) != null) {
//                    children.get(i).show();
//                }
//            }
        //}
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
    protected boolean isMouseOver(Point posInParent)
    {
        //return isVisible && (getLocationRectangle().contains(p) || isStartToMove || isModal) && !isNotControl;
        return isVisible && (getLocationRectangle().contains(posInParent) || isStartToMove || isModal) && !isNotControl;
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

    /*
    如果是原始事件子控件处理,否则是派生出来的子控件不处理
     */
    private boolean onMouseLeftClick(Point posInParent, boolean isOriginal)
    {
        System.out.println("--------onMouseLeftClick--------ID: "+getID()+"-----posInParent: "+posInParent);
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());
        //派生出来的事件不再过子控件，因为派生前已经过了子控件
        if (isOriginal) {
            //优先处理子控件
            //从尾往头遍历
            for (int i = children.size()-1; i>=0; --i) {
                MirControl child = children.get(i);
                if (child.onMouseLeftClick(posInMe, isOriginal) && !child.IsByPassEvent()) {
                    return true;
                }
            }
//            ListIterator<MirControl> iterator = children.listIterator(children.size());
//            while (iterator.hasPrevious()) {
//                MirControl child = iterator.previous();
//                if (child.onMouseLeftClick(posInMe, isOriginal)) {
//                    return true;
//                }
//            }
        }

        if (lastClickTime + Settings.DoubleClickIntervalTime >= Settings.getTime()) {
            lastClickTime=Settings.getTime();
            //派生双击事件
            onMouseLeftDoubleClick(posInParent, false);
            return false;
        }
        lastClickTime=Settings.getTime();

        if (sound != SoundList.None) {
            SoundManager.playSound(sound, false);
        }

        if (onMouseLeftClick != null) {
            onMouseLeftClick.doAction(this, posInMe);
        }

        return true;
    }

    /*
    如果是原始事件子控件处理,否则是派生出来的子控件不处理
     */
    private boolean onMouseRightClick(Point posInParent, boolean isOriginal)
    {
        Point posInMe = Point.subtract(posInParent, getLocation());
        //派生出来的事件不再过子控件，因为派生前已经过了子控件
        if (isOriginal) {

        }
        return false;
    }

    /*
    如果是原始事件子控件处理,否则是派生出来的子控件不处理
     */
    private boolean onMouseLeftDoubleClick(Point posInParent, boolean isOriginal)
    {
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());
        //优先处理子控件
        //派生出来的事件不再过子控件，因为派生前已经过了子控件
        if (isOriginal) {
            for (int i = children.size()-1; i>=0; --i) {
                MirControl child = children.get(i);
                if (child.onMouseLeftDoubleClick(posInMe, isOriginal) && !child.IsByPassEvent()) {
                    return true;
                }
            }

//            synchronized (children) {
//                ListIterator<MirControl> iterator = children.listIterator(children.size());
//                while (iterator.hasPrevious()) {
//                    MirControl child = iterator.previous();
//                    if (child.onMouseLeftDoubleClick(posInMe, isOriginal)) {
//                        return true;
//                    }
//                }
//            }
        }

        if (onMouseLeftDoubleClick != null) {
            if (sound != SoundList.None) {
                SoundManager.playSound(sound, false);
            }
            onMouseLeftDoubleClick.doAction(this, posInMe);
        } else {
            //派生单击事件
//            if(!onMouseLeftClick(posInMe, false)) {
//                return false;
//            }
        }

        return true;
    }

    private boolean isMouseIn = false;
    /*
    pos是在父控件中的座标
     */
    private void onMouseMove(Point posInParent, boolean isMouseLeave)
    {
        if (isMouseLeave) {
            System.out.println("--------MouseLeave: "+getID()+"---------"+posInParent);
            isMouseIn=false;
            this.onMouseLeave();
            return;
        }
        if(!isMouseIn) {
            System.out.println("--------MouseEnter: "+getID()+"---------"+posInParent);
            isMouseIn=true;
            this.onMouseEnter();
        }

//        if(isMouseOver(posInParent)) {
//            System.out.println("--------onMouseMove------isMouseOver: "+getID()+"---------"+posInParent);
//            //进入本控件
//            if(!isMouseIn) {
//                System.out.println("--------MouseEnter: "+getID()+"---------"+posInParent);
//                isMouseIn=true;
//                this.onMouseEnter();
//            }
//        } else {
//            System.out.println("--------onMouseMove------isMouseOver-false: "+getID()+"---------"+posInParent);
//            //离开本控件
//            if(isMouseIn) {
//                System.out.println("--------MouseLeave: "+getID()+"---------"+posInParent);
//                isMouseIn=false;
//                this.onMouseLeave();
//            }
//        }

        //moving
        if (isStartToMove) {
            Point tempPoint = Point.subtract(posInParent, startToMovePos);

            //不能移动超过父控件的边界
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

        //trySort();

        if (isMovable) {
            isStartToMove = true;
            startToMovePos = Point.subtract(posInParent, location);
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
            //派生单击事件
            onMouseLeftClick(posInParent, false);
        }

        if (onMouseLeftUp != null) {
            onMouseLeftUp.doAction(this, null);
        }
    }

    private void onMouseRightUp(Point posInParent)
    {
        if (isMouseRightDown) {
            isMouseRightDown=false;
            onMouseRightClick(posInParent, false);
        }

        if (onMouseRightUp != null) {
            onMouseRightUp.doAction(this, null);
        }
    }

    /*
    在父控件中的坐标
     */
    private boolean onOriginalMouseEvent(CommonEvent.EventEnum eventEnum, Point posInParent) {
        //不在我的区域不处理
        if (!isMouseOver(posInParent) /*&& eventEnum!=CommonEvent.EventEnum.MouseMove*/) {
            return false;
        }
        //优先处理子控件
        //在本控件的座标
        Point posInMe = Point.subtract(posInParent, getLocation());

        for (int i = children.size()-1; i>=0; --i) {
            MirControl child = children.get(i);
            if (child.onOriginalMouseEvent(eventEnum, posInMe) && !child.IsByPassEvent()) {
                return true;
            }
            //传递一下MouseLeave
            if (!child.isMouseOver(posInMe) && child.isMouseIn) {
                child.onMouseMove(posInParent,true);
            }
        }
//        synchronized (children) {
//            ListIterator<MirControl> iterator = children.listIterator(children.size());
//            while (iterator.hasPrevious()) {
//                MirControl child = iterator.previous();
//                //子控件处理了就不再处理
//                if (child.onOriginalMouseEvent(eventEnum, posInMe)) {
//                    return true;
//                }
//            }
//        }

        if (eventEnum==CommonEvent.EventEnum.MouseLeftDown) {
            onMouseLeftDown(posInParent);
        } else if(eventEnum==CommonEvent.EventEnum.MouseLeftUp) {
            onMouseLeftUp(posInParent);
        } else if (eventEnum==CommonEvent.EventEnum.MouseRightDown) {
            onMouseRightDown(posInParent);
        } else if(eventEnum==CommonEvent.EventEnum.MouseRightUp) {
            onMouseRightUp(posInParent);
        } else if (eventEnum==CommonEvent.EventEnum.MouseMove) {
            onMouseMove(posInParent,false);
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

    public final boolean IsByPassEvent() {
        return this.isByPassEvent;
    }

    public final void setIsByPassEvent(boolean isByPassEvent) {
        this.isByPassEvent=isByPassEvent;
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

            for (int i = children.size() - 1; i >= 0; i--) {
                if (children.get(i) != null && !children.get(i).isDisposed) {
                    children.get(i).dispose();
                }
            }

            children.clear();

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
