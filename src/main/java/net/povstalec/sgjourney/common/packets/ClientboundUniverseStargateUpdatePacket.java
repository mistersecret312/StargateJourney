package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundUniverseStargateUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUniverseStargateUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundUniverseStargateUpdatePacket::encode, ClientboundUniverseStargateUpdatePacket::new);

    public final BlockPos pos;
    public final int symbolBuffer;
    public final int[] addressBuffer;
    public final int animationTicks;
    public final int rotation;
    public final int oldRotation;

    public ClientboundUniverseStargateUpdatePacket(BlockPos pos, int symbolBuffer, int[] addressBuffer, int animationTicks, int rotation, int oldRotation)
    {
        this.pos = pos;
        this.symbolBuffer = symbolBuffer;
        this.addressBuffer = addressBuffer;
        this.animationTicks = animationTicks;
        this.rotation = rotation;
        this.oldRotation = oldRotation;
    }

    public ClientboundUniverseStargateUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readVarIntArray(), buffer.readInt(), buffer.readInt(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.symbolBuffer);
        buffer.writeVarIntArray(this.addressBuffer);
        buffer.writeInt(this.animationTicks);
        buffer.writeInt(this.rotation);
        buffer.writeInt(this.oldRotation);
    }

    @Override
    public void handle() {
        ClientAccess.updateUniverseStargate(this.pos, this.symbolBuffer, this.addressBuffer, this.animationTicks, this.rotation, this.oldRotation);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.UNIVERSE_STARGATE_UPDATE;
    }
}


