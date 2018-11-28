package org.android.dragonbones.layer;

import org.android.dragonbones.parser.Ka_Slot;

public class SlotNode extends SKNode {
    public int z;
    public static SlotNode fromBean(Ka_Slot slot) {
        SlotNode node = new SlotNode();
        node.name = slot.name;
        node.z = slot.z; // /100
        return node;
    }
}
