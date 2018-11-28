package org.android.dragonbones.layer;

import org.android.dragonbones.parser.Kass_Display;

public class DisplayNode extends SKNode {
    public static DisplayNode fromBean(Kass_Display display) {
//                         let components = display.name.components(separatedBy: "/")
//                        let atlasName = components[0]
//                        let textureName = components[1].addingPercentEncoding(withAllowedCharacters: CharacterSet.urlQueryAllowed)!
//                        let atlas = SKTextureAtlas(named: atlasName)
//                        let texture = atlas.textureNamed(textureName)
//
//                        let spriteNode = EDDisplayNode(transform: display.transform, texture: texture)
        DisplayNode node = new DisplayNode();
        return node;
    }
}
