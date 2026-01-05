package com.rzero.drownedandtrident.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PositionUtil {

    private static final Logger log = LoggerFactory.getLogger(PositionUtil.class);

    /**
     * search from current to position downwards on Y-axis , get the first position which the block is not air
     * @param currentPos
     * @param level
     * @return
     */
    public static BlockPos getFirstGroundPosInYAxis(BlockPos currentPos, ServerLevel level){
        while (level.isEmptyBlock(currentPos)){
            currentPos = currentPos.offset(new Vec3i(0, -1, 0));
        }
        return currentPos;
    }

    /**
     * get a random position in a diamond shape which build by center at param center and radius with param radius
     * @param center
     * @param radius
     * @param level
     * @return
     */
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
