package com.larissa.mvp.bean;
import android.graphics.drawable.Drawable;
import androidx.databinding.Bindable;
import com.larissa.mvp.BR;

public class ActionBarBean extends BaseBean {
    private String title;
    private Drawable left;
    private Drawable right;
    private String rightTv;



    public ActionBarBean(String title, Drawable left, Drawable right) {
        this.title = title;
        this.left = left;
        this.right = right;
    }

    public ActionBarBean(String title, Drawable left, String rightTv, Drawable right) {
        this.title = title;
        this.left = left;
        this.rightTv = rightTv;
        this.right = right;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public Drawable getLeft() {
        return left;
    }

    public void setLeft(Drawable left) {
        this.left = left;
        notifyPropertyChanged(BR.left);
    }

    @Bindable
    public Drawable getRight() {
        return right;
    }

    public void setRight(Drawable right) {
        this.right = right;
        notifyPropertyChanged(BR.right);
    }

    @Bindable
    public String getRightTv() {
        return rightTv;
    }

    public void setRightTv(String rightTv) {
        this.rightTv = rightTv;
        notifyPropertyChanged(BR.rightTv);
    }

    @Override
    public String toString() {
        return "ActionBarBean{" +
                "title='" + title + '\'' +
                ", left=" + left +
                ", right=" + right +
//                ", rightTv='" + rightTv + '\'' +
                '}';
    }
}
