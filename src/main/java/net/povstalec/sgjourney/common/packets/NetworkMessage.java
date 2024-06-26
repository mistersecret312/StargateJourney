package net.povstalec.sgjourney.common.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface NetworkMessage extends CustomPacketPayload {

    void handle();
}
