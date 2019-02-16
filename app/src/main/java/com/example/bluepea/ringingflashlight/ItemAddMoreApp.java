package com.example.bluepea.ringingflashlight;


import android.graphics.drawable.Drawable;

public class ItemAddMoreApp {
    private String nameApp;
    private String namePackage;
    private Drawable icon;
    private boolean selected;

    public ItemAddMoreApp(String nameApp, String namePackage, Drawable icon, boolean selected ) {
        this.nameApp = nameApp;
        this.icon = icon;
        this.selected = selected;
        this.namePackage = namePackage;
    }

    public ItemAddMoreApp(String nameApp, String namePackage, Drawable icon) {
        this.nameApp = nameApp;
        this.icon = icon;
        this.selected = false;
        this.namePackage = namePackage;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getNamePackage() {
        return namePackage;
    }

    public void setNamePackage(String namePackage) {
        this.namePackage = namePackage;
    }
}
