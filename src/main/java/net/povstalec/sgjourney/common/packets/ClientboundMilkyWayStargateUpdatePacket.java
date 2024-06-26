package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundMilkyWayStargateUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundMilkyWayStargateUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundMilkyWayStargateUpdatePacket::encode, ClientboundMilkyWayStargateUpdatePacket::new);

    public final BlockPos pos;
    public final int rotation;
    public final int oldRotation;
    public final boolean isChevronRaised;
    public final int signalStrength;
    public final boolean computerRotation;
    public final boolean rotateClockwise;
    public final int desiredSymbol;

    public ClientboundMilkyWayStargateUpdatePacket(BlockPos pos, int rotation, int oldRotation, boolean isChevronRaised, int signalStrength, boolean computerRotation, boolean rotateClockwise, int desiredSymbol)
    {
        this.pos = pos;
        this.rotation = rotation;
        this.oldRotation = oldRotation;
        this.isChevronRaised = isChevronRaised;
        this.signalStrength = signalStrength;
        this.computerRotation = computerRotation;
        this.rotateClockwise = rotateClockwise;
        this.desiredSymbol = desiredSymbol;
    }

    public ClientboundMilkyWayStargateUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readInt(), buffer.readBoolean(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        buffer.writeInt(this.rotation);
        buffer.writeInt(this.oldRotation);
        buffer.writeBoolean(this.isChevronRaised);
        buffer.writeInt(this.signalStrength);
        buffer.writeBoolean(this.computerRotation);
        buffer.writeBoolean(this.rotateClockwise);
        buffer.writeInt(this.desiredSymbol);
    }

    @Override
    public void handle() {
        ClientAccess.updateMilkyWayStargate(this.pos, this.rotation, this.oldRotation, this.isChevronRaised, this.signalStrength, this.computerRotation, this.rotateClockwise, this.desiredSymbol);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.MW;
    }
}


