package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.povstalec.sgjourney.common.block_entities.dhd.AbstractDHDEntity;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ServerboundDHDUpdatePacket implements ServerNetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundDHDUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundDHDUpdatePacket::encode, ServerboundDHDUpdatePacket::new);

    public final BlockPos blockPos;
	public final int symbol;

    public ServerboundDHDUpdatePacket(BlockPos blockPos, int symbol)
    {
		this.blockPos = blockPos;
		this.symbol = symbol;
    }

    public ServerboundDHDUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
    	buffer.writeBlockPos(blockPos);
    	buffer.writeInt(symbol);
    }

    @Override
    public void handle(Level level) {
        final BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if(blockEntity instanceof AbstractDHDEntity dhd)
        {
            dhd.engageChevron(this.symbol);
        }
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.DHD_UPDATE;
    }
}


