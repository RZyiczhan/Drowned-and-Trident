package com.rzero.drownedandtrident.playerBlock.tridentEnchantingTable;

import com.rzero.drownedandtrident.modRegistry.block.DATBlock;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;

public class TridentEnchantmentMenu extends EnchantmentMenu {

    private final ContainerLevelAccess access;

    public TridentEnchantmentMenu(int containerId, Inventory playerInventory, ContainerLevelAccess access) {
        super(containerId, playerInventory, access);
        this.access = access;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(this.access, player, DATBlock.TRIDENT_ENCHANTING_TABLE.get());
    }
}
