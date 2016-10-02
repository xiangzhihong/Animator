# Animator
动画分类
Android动画可以分3种：View动画，帧动画和属性动画；属性动画为API11的新特性，在低版本是无法直接使用属性动画的，但可以用nineoldAndroids来实现（但是本质还是viiew动画）。学习本篇内容主要掌握以下知识：
1,View动画以及自定义View动画。
2,View动画的一些特殊使用场景。
3,对属性动画做了一个全面的介绍。
4,使用动画的一些注意事项。

view动画
View动画的四种变换效果对应着Animation的四个子类：TranslateAnimation（平移动画）、ScaleAnimation（缩放动画）、RotateAnimation（旋转动画）和AlphaAnimation（透明度动画）,他们即可以用代码来动态创建也可以用XML来定义，推荐使用可读性更好的XML来定义。
<set>标签表示动画集合，对应AnimationSet类，它可以包含若干个动画，并且他的内部也可以嵌套其他动画集合。android:interpolator 表示动画集合所采用的插值器，插值器影响动画速度，比如非匀速动画就需要通过插值器来控制动画的播放过程。
android:shareInterpolator表示集合中的动画是否和集合共享同一个插值器，如果集合不指定插值器，那么子动画就需要单独指定所需的插值器或默认值。
Animation通过setAnimationListener方法可以给View动画添加过程监听。
自定义View动画只需要继承Animation这个抽象类，并重写initialize和applyTransformation方法，在initialize方法中做一些初始化工作，在applyTransformation中进行相应的矩形变换，很多时候需要采用Camera来简化矩形变换过程。
帧动画是顺序播放一组预先定义好的图片，类似电影播放；使用简单但容易引发OOM,尽量避免使用过多尺寸较大的图片。

view动画应用场景
LayoutAnimation作用于ViewGroup,为ViewGroup指定一个动画，当他的子元素出场的时候都会具有这种动画，ListView上用的多，LayoutAnimation也是一个View动画。
代码实现：

[html] view plain copy print?在CODE上查看代码片派生到我的代码片
<?xml version="1.0" encoding="utf-8"?>  
<layoutAnimation xmlns:android="http://schemas.android.com/apk/res/android"  
 android:animationOrder="normal"  
 android:delay="0.3" android:animation="@anim/anim_item"/>  
  
//--- animationOrder 表示子元素的动画的顺序，有三种选项：  
//normal(顺序显示）、reverse（逆序显示）和random（随机显示）。  
  
<?xml version="1.0" encoding="utf-8"?>  
<set xmlns:android="http://schemas.android.com/apk/res/android"  
 android:duration="300"  
 android:shareInterpolator="true">  
 <alpha  
     android:fromAlpha="0.0"  
     android:toAlpha="1.0" />  
 <translate  
     android:fromXDelta="300"  
     android:toXDelta="0" />  
</set>  
第一种，在布局中引用LayoutAnimation
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
<ListView  
     android:id="@+id/lv"  
     android:layout_width="match_parent"  
     android:layout_height="0dp"  
     android:layout_weight="1"  
     android:layoutAnimation="@anim/anim_layout"/>  
第二种，代码种使用
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
Animation animation = AnimationUtils.loadAnimation(this, R.anim.anim_item);  
LayoutAnimationController controller = new LayoutAnimationController(animation);  
controller.setDelay(0.5f);  
controller.setOrder(LayoutAnimationController.ORDER_NORMAL);  
listview.setLayoutAnimation(controller);  

帧动画
  逐帧动画（Frame-by-frame Animations）从字面上理解就是一帧挨着一帧的播放图片，类似于播放电影的效果。不同于View动画，Android系统提供了一个类AnimationDrawable来实现帧动画，帧动画比较简单，我们看一个例子就行了。
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
<?xml version="1.0" encoding="utf-8"?>  
<animation-list xmlns:android="http://schemas.android.com/apk/res/android"  
    android:oneshot="false">  
  
    <item  
        android:drawable="@mipmap/lottery_1"  
        android:duration="200" />  
  // ...省略很多  
    <item  
        android:drawable="@mipmap/lottery_6"  
        android:duration="200" />  
  
</animation-list>  

然后
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
imageView.setImageResource(R.drawable.frame_anim);  
AnimationDrawable animationDrawable = (AnimationDrawable) imageView.getDrawable();  
animationDrawable.start();//启动start，关闭stop  

属性动画
属性动画是Android 3.0新加入(api 11)的功能，不同于之前的view动画（看过的都知道，view动画比如实现的位移其实不是真正的位置移动，只是实现了一些简单的视觉效果）。属性动画对之前的动画做了很大的拓展，毫不夸张的说，属性动画可以实现任何动画效果，因为在作用的对象是属性（对象），属性动画中有几个概念需要我们注意下，

ValueAnimator、ObjectAnimator、AnimatorSet等。

属性动画作用属性
1，属性动画可以对任意对象的属性进行动画而不仅仅是View，属性动画默认间隔300ms，默认帧率10ms/帧。
2，看一段代码
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
<set  
  android:ordering=["together" | "sequentially"]>  
  
    <objectAnimator  
        android:propertyName="string"  
        android:duration="int"  
        android:valueFrom="float | int | color"  
        android:valueTo="float | int | color"  
        android:startOffset="int"  
        android:repeatCount="int"  
        android:repeatMode=["repeat" | "reverse"]  
        android:valueType=["intType" | "floatType"]/>  
  
    <animator  
        android:duration="int"  
        android:valueFrom="float | int | color"  
        android:valueTo="float | int | color"  
        android:startOffset="int"  
        android:repeatCount="int"  
        android:repeatMode=["repeat" | "reverse"]  
        android:valueType=["intType" | "floatType"]/>  
  
    <set>  
        ...  
    </set>  
</set>  

<set>
它代表的就是一个AnimatorSet对象。里面有一个 ordering属性，主要是指定动画的播放顺序。

<objectAnimator> 
它表示一个ObjectAnimator对象。它里面有很多属性，我们重点需要了解的也是它。
android:propertyName -------属性名称，例如一个view对象的”alpha”和”backgroundColor”。
android:valueFrom   --------变化开始值
android:valueTo ------------变化结束值
android:valueType -------变化值类型 ，它有两种值：intType和floatType，默认值floatType。
android:duration ---------持续时间
android:startOffset ---------动画开始延迟时间
android:repeatCount --------重复次数，-1表示无限重复，默认为-1
android:repeatMode 重复模式，前提是android:repeatCount为-1 ，它有两种值：”reverse”和”repeat”，分别表示反向和顺序方向。

<animator>
它对应的就是ValueAnimator对象。它主要有以下属性。
android:valueFrom
android:valueTo
android:duration
android:startOffset
android:repeatCount
android:repeatMode
android:valueType

定义了一组动画之后，我们怎么让它运行起来呢？
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
AnimatorSet set = (AnimatorSet) AnimatorInflater.loadAnimator(myContext,  
    R.anim.property_animator);  
set.setTarget(myObject);//myObject表示作用的对象  
set.start();  

插值器和估值器
时间插值器（TimeInterpolator）的作用是根据时间流逝的百分比来计算出当前属性值改变的百分比，系统预置的有LinearInterpolator（线性插值器：匀速动画），AccelerateDecelerateInterpolator（加速减速插值器：动画两头慢中间快），DecelerateInterpolator(减速插值器：动画越来越慢）。

注：这里的插值器很多，可以翻看我之前关于插值器的讲解。

估值器（TypeEvaluator）的作用是根据当前属性改变的百分比来计算改变后的属性值。系统预置有IntEvaluator 、FloatEvaluator 、ArgbEvaluator。

举个简单的例子吧
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
public class IntEvaluator implements TypeEvaluator<Integer> {  
 public Integer evaluate(float fraction, Integer startValue, Integer endValue) {  
     int startInt = startValue;  
     return (int)(startInt + fraction * (endValue - startInt));  
 }  
}  
上述代码就是计算当前属性所占总共的百分百。

插值器和估值器除了系统提供之外，我们还可以自定义实现，自定义插值器需要实现Interpolator或者TimeInterpolator；自定义估值器算法需要实现TypeEvaluator。

属性动画监听器
属性动画监听器用于监听动画的播放过程，主要有两个接口：AnimatorUpdateListener和AnimatorListener 。
AnimatorListener 
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
public static interface AnimatorListener {  
    void onAnimationStart(Animator animation); //动画开始  
    void onAnimationEnd(Animator animation); //动画结束  
    void onAnimationCancel(Animator animation); //动画取消  
    void onAnimationRepeat(Animator animation); //动画重复播放  
}  
AnimatorUpdateListener
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
public static interface AnimatorUpdateListener {  
    void onAnimationUpdate(ValueAnimator animator);  
}  

应用场景

这里我们先提一个问题：给Button加一个动画，让Button在2秒内将宽带从当前宽度增加到500dp，也行你会说，很简单啊，直接用view动画就可以实现，view动画不是有个缩放动画，但是你可以试试，view动画是不支持对宽度和高度进行改变的。Button继承自TextView，setWidth是对TextView的，所以直接对Button做setWidth是不行的。那么要怎么做呢？
针对上面的问题，官网api给出了如下的方案：
给你的对象加上get和set方法，如果你有权限的话
用一个类来包装原始对象，间接提高get和set方法
采用ValueAnimator，监听动画执行过程，实现属性的改变

有了上面的说明，我们大致明白了，要实现开始说的这个问题的效果，我们需要用一个间接的类来实现get和set方法或者自己实现一个ValueAnimator。
第一种，自己封装一个类实现get和set方法，这也是我们常用的，拓展性强

[html] view plain copy print?在CODE上查看代码片派生到我的代码片
public class ViewWrapper {  
 private View target;  
 public ViewWrapper(View target) {  
     this.target = target;  
 }  
 public int getWidth() {  
     return target.getLayoutParams().width;  
 }  
 public void setWidth(int width) {  
     target.getLayoutParams().width = width;  
     target.requestLayout();  
 }  
}  

第二种，采用ValueAnimator，监听动画过程。

[html] view plain copy print?在CODE上查看代码片派生到我的代码片
private void startValueAnimator(final View target, final int start, final int end) {  
   ValueAnimator valueAnimator = ValueAnimator.ofInt(1, 100);  
   valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {  
       private IntEvaluator mEvaluation = new IntEvaluator();//新建一个整形估值器作为临时变量  
  
       @Override  
       public void onAnimationUpdate(ValueAnimator animation) {  
           //获得当前动画的进度值 1~100之间  
           int currentValue = (int) animation.getAnimatedValue();  
           //获得当前进度占整个动画过程的比例，浮点型，0~1之间  
           float fraction = animation.getAnimatedFraction();  
           //调用估值器，通过比例计算出宽度   
           int targetWidth = mEvaluation.evaluate(fraction, start, end);  
           target.getLayoutParams().width = targetWidth;  
           //设置给作用的对象，刷新页面  
           target.requestLayout();  
       }  
   });  
}  
属性动画的工作原理
属性动画的工作原理，主要是对作用的对象不断的调用get/set方法来改变初始值和最终值，然后set到动画属性上即可。然后通过系统的消息机制（Handler和Looper去将动画执行出来）。比如我们调用ObjectAnimator.start()
[html] view plain copy print?在CODE上查看代码片派生到我的代码片
private void start(boolean playBackwards) {  
        if(Looper.myLooper() == null) {  
            throw new AndroidRuntimeException("Animators may only be run on Looper threads");  
        } else {  
            this.mPlayingBackwards = playBackwards;  
            this.mCurrentIteration = 0;  
            this.mPlayingState = 0;  
            this.mStarted = true;  
            this.mStartedDelay = false;  
            ((ArrayList)sPendingAnimations.get()).add(this);  
            if(this.mStartDelay == 0L) {  
                this.setCurrentPlayTime(this.getCurrentPlayTime());  
                this.mPlayingState = 0;  
                this.mRunning = true;  
                if(this.mListeners != null) {  
                    ArrayList animationHandler = (ArrayList)this.mListeners.clone();  
                    int numListeners = animationHandler.size();  
  
                    for(int i = 0; i < numListeners; ++i) {  
                        ((AnimatorListener)animationHandler.get(i)).onAnimationStart(this);  
                    }  
                }  
            }  
  
            ValueAnimator.AnimationHandler var5 = (ValueAnimator.AnimationHandler)sAnimationHandler.get();  
            if(var5 == null) {  
                var5 = new ValueAnimator.AnimationHandler(null);  
                sAnimationHandler.set(var5);  
            }  
  
            var5.sendEmptyMessage(0);  
        }  
    }  

[html] view plain copy print?在CODE上查看代码片派生到我的代码片
private static final ThreadLocal<ArrayList<ValueAnimator>> sAnimations = new ThreadLocal() {  
       protected ArrayList<ValueAnimator> initialValue() {  
           return new ArrayList();  
       }  
   };  
   private static final ThreadLocal<ArrayList<ValueAnimator>> sPendingAnimations = new ThreadLocal() {  
       protected ArrayList<ValueAnimator> initialValue() {  
           return new ArrayList();  
       }  
   };  
   private static final ThreadLocal<ArrayList<ValueAnimator>> sDelayedAnims = new ThreadLocal() {  
       protected ArrayList<ValueAnimator> initialValue() {  
           return new ArrayList();  
       }  
   };  
   private static final ThreadLocal<ArrayList<ValueAnimator>> sEndingAnims = new ThreadLocal() {  
       protected ArrayList<ValueAnimator> initialValue() {  
           return new ArrayList();  
       }  
   };  
   private static final ThreadLocal<ArrayList<ValueAnimator>> sReadyAnims = new ThreadLocal() {  
       protected ArrayList<ValueAnimator> initialValue() {  
           return new ArrayList();  
       }  
   };  

这里就不做具体的分析了，大家可以去看看源码。
使用属性动画需要注意的事项
使用帧动画时，当图片数量较多且图片分辨率较大的时候容易出现OOM，需注意，尽量避免使用帧动画。
使用无限循环的属性动画时，在Activity退出时即使停止，否则将导致Activity无法释放从而造成内存泄露。
View动画是对View的影像做动画，并不是真正的改变了View的状态，因此有时候会出现动画完成后View无法隐藏（setVisibility(View.GONE）失效），这时候调用view.clearAnimation()清理View动画即可解决。
不要使用px，使用px会导致不同设备上有不同的效果。
View动画是对View的影像做动画，View的真实位置没有变动，也就导致点击View动画后的位置触摸事件不会响应，属性动画不存在这个问题。
使用动画的过程中，使用硬件加速可以提高动画的流畅度。
动画在3.0以下的系统存在兼容性问题，特殊场景可能无法正常工作，需做好适配工作。
