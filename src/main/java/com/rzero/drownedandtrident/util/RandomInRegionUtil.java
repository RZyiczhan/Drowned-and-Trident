package com.rzero.drownedandtrident.util;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;

public class RandomInRegionUtil {

    public static BlockPos getRandomPosInDiamond(BlockPos center, int radius, ServerLevel level) {
        RandomSource random = level.getRandom();
        BlockPos pos;

        while (true) {
            int dx = random.nextInt(radius * 2 + 1) - radius;
            int dz = random.nextInt(radius * 2 + 1) - radius;

            if (Math.abs(dx) + Math.abs(dz) <= radius) {
                // 找到符合曼哈顿距离的位置
                pos = center.offset(dx, 0, dz);
                break;
            }
        }
        return pos;
    }


}
