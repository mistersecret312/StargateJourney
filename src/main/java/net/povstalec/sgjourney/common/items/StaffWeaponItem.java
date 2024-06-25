package net.povstalec.sgjourney.common.items;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.entities.PlasmaProjectile;
import net.povstalec.sgjourney.common.init.EntityInit;
import net.povstalec.sgjourney.common.init.SoundInit;

public class StaffWeaponItem extends Item
{
	public static final String IS_OPEN = "IsOpen";
	
	private static final float OPEN_ATTACK_DAMAGE = 3.0F;
	private static final float CLOSED_ATTACK_DAMAGE = 6.0F;

	private static final float OPEN_ATTACK_SPEED = -2.4F;
	private static final float CLOSED_ATTACK_SPEED = -2.8F;
	
	private static final float LIQUID_NAQUADAH_EXPLOSION_POWER = 0;
	private static final float HEAVY_LIQUID_NAQUADAH_EXPLOSION_POWER = 1;

	private static final int LIQUID_NAQUADAH_DEPLETION = 1;
	private static final int HEAVY_LIQUID_NAQUADAH_DEPLETION = 5;

    public StaffWeaponItem(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand)
	{
		ItemStack itemstack = player.getItemInHand(hand);
		// Shooting
		if(!player.isShiftKeyDown()/* && canShoot(player, player.getItemInHand(hand))*/)
		{
            level.playSound(player, player.blockPosition(), SoundInit.MATOK_FIRE.get(), SoundSource.PLAYERS, 0.25F, 1.0F);
				if(!level.isClientSide())
				{
					// If the player is in Creative and it's empty, the explosions will be the same size as if it was filled with Liquid Naquadah
                    PlasmaProjectile plasmaProjectile = new PlasmaProjectile(EntityInit.JAFFA_PLASMA.get(), player, level, LIQUID_NAQUADAH_EXPLOSION_POWER);
					plasmaProjectile.shootFromRotation(player, player.getXRot(), player.getYRot(), 0.0F, 5.0F, 1.0F);
					level.addFreshEntity(plasmaProjectile);
				}
				player.awardStat(Stats.ITEM_USED.get(this));
				player.getCooldowns().addCooldown(this, 25);
			}
			else
				return InteractionResultHolder.fail(itemstack);

		return InteractionResultHolder.sidedSuccess(itemstack, level.isClientSide());
	}
	
	/*@Override
	public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity player)
	{
		player.getLevel().playSound(player, player.blockPosition(), SoundInit.MATOK_ATTACK.get(), SoundSource.PLAYERS, 0.25F, 1.0F);
		return super.hurtEnemy(stack, target, player);
	}*/
	
	@Override
	public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player)
	{
		return !player.isCreative();
	}
}
