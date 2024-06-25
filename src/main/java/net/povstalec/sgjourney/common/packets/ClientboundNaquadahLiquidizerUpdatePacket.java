package net.povstalec.sgjourney.common.packets;

import java.util.function.Supplier;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.fluids.FluidStack;
import net.povstalec.sgjourney.client.ClientAccess;
import net.povstalec.sgjourney.common.init.PacketHandlerInit;

public class ClientboundNaquadahLiquidizerUpdatePacket implements NetworkMessage<ClientNetworkContext>
{
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNaquadahLiquidizerUpdatePacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundNaquadahLiquidizerUpdatePacket::encode, ClientboundNaquadahLiquidizerUpdatePacket::new);

    public final BlockPos pos;
    private final FluidStack fluidStack1;
    private final FluidStack fluidStack2;
    public final int progress;

    public ClientboundNaquadahLiquidizerUpdatePacket(BlockPos pos, FluidStack fluidStack1, FluidStack fluidStack2, int progress)
    {
        this.pos = pos;
        this.fluidStack1 = fluidStack1;
        this.fluidStack2 = fluidStack2;
        this.progress = progress;
    }

    public ClientboundNaquadahLiquidizerUpdatePacket(RegistryFriendlyByteBuf buffer)
    {
        this(buffer.readBlockPos(), FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer), FluidStack.OPTIONAL_STREAM_CODEC.decode(buffer), buffer.readInt());
    }

    public void encode(RegistryFriendlyByteBuf buffer)
    {
        buffer.writeBlockPos(this.pos);
        FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, this.fluidStack1);
        FluidStack.OPTIONAL_STREAM_CODEC.encode(buffer, this.fluidStack2);
        buffer.writeInt(this.progress);
    }

    @Override
    public void handle(ClientNetworkContext context) {
        //ClientAccess.updateNaquadahLiquidizer(pos, fluidStack1, fluidStack2, progress);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return PacketHandlerInit.NAQUADAH_LIQUIDIZER;
    }
}


