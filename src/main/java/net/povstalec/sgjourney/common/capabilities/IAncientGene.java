package net.povstalec.sgjourney.common.capabilities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;

import java.util.Random;

public interface IAncientGene {

    public AncientGene.ATAGene getGeneType();

    public boolean canUseAncientTechnology();

    public boolean isAncient();

    public boolean isInherited();

    public boolean isArtificial();

    public boolean isLacking();

    public void giveGene();

    public void inheritGene();

    public void implantGene();

    public void removeGene();

    public boolean firstJoin();

    public void markJoined();

    public void copyFrom(IAncientGene source);
}
