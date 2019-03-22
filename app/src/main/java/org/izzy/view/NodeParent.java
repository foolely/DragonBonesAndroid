package org.izzy.view;

public interface NodeParent {
    public void addChild(Node child);
    public void removeChild(Node child);
    public void requestDraw();
}
