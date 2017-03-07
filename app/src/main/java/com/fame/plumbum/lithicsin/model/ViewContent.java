package com.fame.plumbum.lithicsin.model;

/**
 * Created by pankaj on 5/3/17.
 */

public class ViewContent {
    private String hint;
    private int inputType;
    private int maxSize;
    private int minSize;

    public ViewContent(String hint, int maxSize, int minSize){
        this.hint = hint;
        this.maxSize = maxSize;
        this.minSize = minSize;

    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setMinSize(int minSize) {
        this.minSize = minSize;
    }

    public String getHint() {
        return hint;
    }

    public int getInputType() {
        return inputType;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getMinSize() {
        return minSize;
    }
}
