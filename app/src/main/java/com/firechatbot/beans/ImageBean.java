package com.firechatbot.beans;


import java.io.File;

public class ImageBean {

    private File imageFile;
    private boolean selected;

    public ImageBean()
    {
    }
    public ImageBean(File imageFile,boolean selected)
    {
        this.imageFile = imageFile;
        this.selected = selected;
    }

    public void setImageFile(File imageFile) {
        this.imageFile = imageFile;
    }

    public File getImageFile() {
        return imageFile;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }
}
