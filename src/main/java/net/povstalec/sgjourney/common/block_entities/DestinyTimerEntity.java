package net.povstalec.sgjourney.common.block_entities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.TickingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.povstalec.sgjourney.common.blocks.TimerInterfaceBlock;
import net.povstalec.sgjourney.common.compatibility.cctweaked.CCTweakedCompatibility;
import net.povstalec.sgjourney.common.compatibility.cctweaked.StargatePeripheralWrapper;
import net.povstalec.sgjourney.common.compatibility.cctweaked.TimerPeripheralWrapper;
import net.povstalec.sgjourney.common.init.BlockEntityInit;
import org.jetbrains.annotations.Nullable;

public class DestinyTimerEntity extends BlockEntity {
    public static final String TIME = "time";
    public static final String EXPIRATION_TIME = "expiration_time";
    public static final String TIME_PASSED = "passed";
    public static final String STATE = "state";

    public static final String STATE_CHANGE = "onStateChanged";
    public static final String RESET = "onReset";

    public int baseTimerValue = 20*60*20;

    private int time = 0;
    private int expirationTime = 1200;
    private int timePassed = 0;
    private TimerState state;

    public DestinyTimerEntity(BlockPos pos, BlockState state) {
        super(BlockEntityInit.DESTINY_TIMER.get(), pos, state);
    }

    public enum TimerState{
        COUNTING("counting"),
        PAUSED("paused"),
        EXPIRING("expiring"),
        ENDED("ended");

        private String stateName;

        TimerState(String state){
            stateName = state;
        }

        public String getStateName() {
            return stateName;
        }
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DestinyTimerEntity timer){
        if(timer.getState() != TimerState.PAUSED || timer.getState() != TimerState.ENDED){
            timer.setTime(timer.getTime()-1);
            timer.setTimePassed(timer.getTimePassed()+1);
        } else if(timer.getTime() == 0 || timer.getState() == TimerState.ENDED){
            timer.setState(TimerState.ENDED);
            timer.setTime(0);
        } else if(timer.getTime() <= timer.getExpirationTime()){
            timer.setState(TimerState.EXPIRING);
        } else if(timer.getTime() > timer.getExpirationTime()){
            timer.setState(TimerState.COUNTING);
        } else timer.setState(TimerState.COUNTING);
    }

    public void resetTimer(){
        this.setTime(baseTimerValue);
        if(this.getInterface() != null){
            this.getInterface().queueEvent(RESET, this.time);
        }
    }

    public TimerState getState(){
        return state;
    }

    public void setState(TimerState state){
        this.state = state;
        if(this.getInterface() != null){
            this.getInterface().queueEvent(STATE_CHANGE, state.getStateName(), this.getTimePassed());
        }
    }

    @Nullable
    public TimerInterfaceEntity getInterface(){
        return this.level.getBlockEntity(this.worldPosition.relative(this.level.getBlockState(this.worldPosition).getValue(TimerInterfaceBlock.FACING).getOpposite(), 2)) instanceof TimerInterfaceEntity timerInterfaceEntity ? timerInterfaceEntity : null;
    }

    public int getTime(){
        return time;
    }

    public int getTimePassed(){
        return timePassed;
    }

    public int getResetValue(){
        return baseTimerValue;
    }

    public int getExpirationTime(){
        return expirationTime;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public void setTimePassed(int time){
        this.timePassed = time;
    }

    public void setResetValue(int time){
        this.baseTimerValue = time;
    }

    public void setExpirationTime(int time){
        this.expirationTime = time;
    }

    public void registerInterfaceMethods(TimerPeripheralWrapper wrapper)
    {
        CCTweakedCompatibility.registerDestinyTimerMethods(wrapper);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        tag.putInt(TIME, time);
        tag.putInt(TIME_PASSED, timePassed);
        tag.putInt(EXPIRATION_TIME, expirationTime);
        tag.putString(STATE, state.getStateName());
    }

    @Override
    public void load(CompoundTag tag) {
        this.time = tag.getInt(TIME);
        this.timePassed = tag.getInt(TIME_PASSED);
        this.expirationTime = tag.getInt(EXPIRATION_TIME);
        this.state = TimerState.valueOf(tag.getString(STATE));
    }


}
