package org.izzy.view;

import java.util.ArrayList;

public class NodeGroup extends Node implements NodeParent {

    // child树结构
    protected ArrayList<Node> subNodes = new ArrayList<>();

    @Override
    public void addChild(Node child) {
        child.parent = this;
        subNodes.add(child);
    }

    @Override
    public void removeChild(Node child) {
        subNodes.remove(child);
        child.parent = null;
    }

    @Override
    public void requestDraw() {
        if (parent!=null) {
            parent.requestDraw();
        }
    }
}
