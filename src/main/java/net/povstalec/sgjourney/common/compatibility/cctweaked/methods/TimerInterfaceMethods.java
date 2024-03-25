package net.povstalec.sgjourney.common.compatibility.cctweaked.methods;

import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IComputerAccess;
import net.povstalec.sgjourney.common.block_entities.DestinyTimerEntity;
import net.povstalec.sgjourney.common.block_entities.TimerInterfaceEntity;

public class TimerInterfaceMethods {

    public static class getStatus implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "getStatus";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> new Object[]{blockEntity.getState().getStateName()});
            return result;
        }
    }

    public static class getTime implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "getTime";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> new Object[]{blockEntity.getTime()});
            return result;
        }
    }

    public static class getExpirationTime implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "getExpirationTime";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> new Object[]{blockEntity.getExpirationTime()});
            return result;
        }
    }

    public static class getResetValue implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "getResetValue";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> new Object[]{blockEntity.getResetValue()});
            return result;
        }
    }

    public static class getTimePassed implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "getTimePassed";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> new Object[]{blockEntity.getTimePassed()});
            return result;
        }
    }

    public static class setTime implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "setTime";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                int input = arguments.getInt(0);

                blockEntity.setTime(input);
                return new Object[]{"Time set successfully"};
            });

            return result;
        }
    }

    public static class setExpirationTime implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "setExpirationTime";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                int input = arguments.getInt(0);

                blockEntity.setExpirationTime(input);
                return new Object[]{"Time for expiration set successfully"};
            });

            return result;
        }
    }

    public static class setResetValue implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "setResetValue";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                int input = arguments.getInt(0);

                blockEntity.setResetValue(input);
                return new Object[]{"Reset value set successfully"};
            });

            return result;
        }
    }

    public static class setTimePassed implements TimerInterfaceMethod<DestinyTimerEntity>{

        @Override
        public String getName() {
            return "setTimePassed";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                int input = arguments.getInt(0);

                blockEntity.setTimePassed(input);
                return new Object[]{"Time set successfully"};
            });

            return result;
        }
    }
    public static class setState implements TimerInterfaceMethod<DestinyTimerEntity> {

        @Override
        public String getName() {
            return "setState";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                String value = arguments.getString(0);

                for (DestinyTimerEntity.TimerState timerState : DestinyTimerEntity.TimerState.values()) {
                    if(timerState.getStateName().equals(value)){
                        blockEntity.setState(DestinyTimerEntity.TimerState.valueOf(value));
                    } else return new Object[]{"Invalid State"};
                }
                return new Object[]{"State changed successfully"};
            });
            return result;
        }
    }

    public static class reset implements TimerInterfaceMethod<DestinyTimerEntity> {

        @Override
        public String getName() {
            return "reset";
        }

        @Override
        public MethodResult use(IComputerAccess computer, ILuaContext context, TimerInterfaceEntity interfaceEntity, DestinyTimerEntity blockEntity, IArguments arguments) throws LuaException {
            MethodResult result = context.executeMainThreadTask(() -> {
                blockEntity.resetTimer();
                return new Object[]{"Reset successfully"};
            });
            return result;
        }
    }
}
