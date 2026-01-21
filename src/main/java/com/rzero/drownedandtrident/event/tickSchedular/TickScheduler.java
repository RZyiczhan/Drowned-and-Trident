package com.rzero.drownedandtrident.event.tickSchedular;

import net.minecraft.server.level.ServerLevel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tick内的对监听后需执行任务的调度器
 */
public class TickScheduler {
    private static final Map<Long, List<Runnable>> tasks = new HashMap<>();

    public static void schedule(ServerLevel level, int delay, Runnable task) {
        // 如果延后服务端逻辑tick<=0，则当即执行，否则
        if (delay <= 0){
            task.run();
            return;
        }
        long executeTick = level.getGameTime() + delay;
        tasks.computeIfAbsent(executeTick, x -> new ArrayList<>()).add(task);
    }

    public static void tick(ServerLevel level) {
        long now = level.getGameTime();
        List<Runnable> list = tasks.remove(now);
        if (list != null) {
            list.forEach(Runnable::run);
        }
    }
}
