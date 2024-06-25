package net.povstalec.sgjourney.common.packets;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public interface NetworkMessage<T> extends CustomPacketPayload {

    void handle(T context);
}
