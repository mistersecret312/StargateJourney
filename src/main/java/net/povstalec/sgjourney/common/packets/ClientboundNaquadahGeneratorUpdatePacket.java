package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundNaquadahGeneratorUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNaquadahGeneratorUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundNaquadahGeneratorUpdatePacket::encode, ClientboundNaquadahGeneratorUpdatePacket::new);

    public final BlockPos pos;
    public final int reactionProgress;
    public final long energy;

    public ClientboundNaquadahGeneratorUpdatePacket(BlockPos pos, int reactionProgress, long energy)
    {
        this.pos = pos;
        this.reactionProgress = reactionProgress;
        this.energy = energy;
    }

    public ClientboundNaquadahGeneratorUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readLong());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.reactionProgress);
        buffer.writeLong(this.energy);
    }

    @Override
    public void handle() {
        //ClientAccess.updateNaquadahGenerator(this.pos, this.reactionProgress, this.energy);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.NAQUADAH_GENERATOR;
    }
}


