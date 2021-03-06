package net.channel.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import client.IItem;
import client.MapleClient;
import client.MapleInventoryType;
import net.AbstractMaplePacketHandler;
import net.packetcreator.MaplePacketCreator;
import tools.data.input.SeekableLittleEndianAccessor;

public class UseItemEffectHandler extends AbstractMaplePacketHandler {

    private static Logger log = LoggerFactory.getLogger(UseItemHandler.class);

    public UseItemEffectHandler() {

    }

    public void handlePacket(SeekableLittleEndianAccessor slea, MapleClient c) {

        int itemId = slea.readInt();
        boolean mayUse = true;
        if (itemId != 0) {
            IItem toUse = c.getPlayer().getInventory(MapleInventoryType.CASH).findById(itemId);

            if (toUse == null) {
                mayUse = false;
                log.info("[h4x] Player {} is using an item he does not have: {}", c.getPlayer().getName(), Integer.valueOf(itemId));
            }
        }

        if (mayUse) {
            c.getPlayer().setItemEffect(itemId);
            c.getPlayer().getMap().broadcastMessage(c.getPlayer(), MaplePacketCreator.itemEffect(c.getPlayer().getId(), itemId), false);
        }
    }
}
