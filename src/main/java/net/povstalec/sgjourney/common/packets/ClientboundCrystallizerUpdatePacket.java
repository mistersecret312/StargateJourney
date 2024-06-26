package net.povstalec.sgjourney.common.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundCrystallizerUpdatePacket implements NetworkMessage
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCrystallizerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundCrystallizerUpdatePacket::encode, ClientboundCrystallizerUpdatePacket::new);


    public final BlockPos pos;
    private final FluidStack fluidStack;
    public final int progress;

    public ClientboundCrystallizerUpdatePacket(BlockPos pos, FluidStack fluidStack, int progress)
    {
        this.pos = pos;
        this.fluidStack = fluidStack;
        this.progress = progress;
    }

    public ClientboundCrystallizerUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, this.fluidStack);
        buffer.writeInt(this.progress);
    }

    @Override
    public void handle() {
        //ClientAccess.updateCrystallizer(this.pos, this.fluidStack, this.progress);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.CRYSTALLIZER;
    }
}


