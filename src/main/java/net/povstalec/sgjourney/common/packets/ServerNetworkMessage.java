package net.povstalec.sgjourney.common.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;

public interface ServerNetworkMessage extends CustomPacketPayload {

    void handle(Level level);

}
