package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.povstalec.sgjourney.common.block_entities.RingPanelEntity;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ServerboundRingPanelUpdatePacket implements ServerNetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundRingPanelUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundRingPanelUpdatePacket::encode, ServerboundRingPanelUpdatePacket::new);

    public final BlockPos blockPos;
	public final int number;

    public ServerboundRingPanelUpdatePacket(BlockPos blockPos, int number)
    {
		this.blockPos = blockPos;
		this.number = number;
    }

    public ServerboundRingPanelUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
    	buffer.writeBlockPos(blockPos);
    	buffer.writeInt(number);
    }

    @Override
    public void handle(Level level) {
        final BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof RingPanelEntity ringPanel)
        {
            ringPanel.activateRings(number);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.RING_UPDATE;
    }
}


