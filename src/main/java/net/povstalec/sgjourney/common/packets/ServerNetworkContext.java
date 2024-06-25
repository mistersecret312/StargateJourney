package net.povstalec.sgjourney.common.packets;

import net.minecraft.server.level.ServerPlayer;

public interface ServerNetworkContext {
    ServerPlayer getSender();
}
