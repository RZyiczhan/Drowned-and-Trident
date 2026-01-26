package com.rzero.drownedandtrident.programmingModel;

public class EnchantmentsUpgradeSummary {

    private byte fanSplitUpgradeStatus = 0;
    private byte scatterSplitUpgradeStatus = 0;
    private byte explosiveShootUpgradeStatus = 0;
    private byte erosionUpgradeStatus = 0;
    private byte thunderStormUpgradeStatus = 0;
    private byte thunderTrajectoryUpgradeStatus = 0;

    public EnchantmentsUpgradeSummary(byte fanSplitUpgradeStatus, byte scatterSplitUpgradeStatus, byte explosiveShootUpgradeStatus, byte erosionUpgradeStatus, byte thunderStormUpgradeStatus, byte thunderTrajectoryUpgradeStatus) {
        this.fanSplitUpgradeStatus = fanSplitUpgradeStatus;
        this.scatterSplitUpgradeStatus = scatterSplitUpgradeStatus;
        this.explosiveShootUpgradeStatus = explosiveShootUpgradeStatus;
        this.erosionUpgradeStatus = erosionUpgradeStatus;
        this.thunderStormUpgradeStatus = thunderStormUpgradeStatus;
        this.thunderTrajectoryUpgradeStatus = thunderTrajectoryUpgradeStatus;
    }

    public byte getFanSplitUpgradeStatus() {
        return fanSplitUpgradeStatus;
    }

    public byte getScatterSplitUpgradeStatus() {
        return scatterSplitUpgradeStatus;
    }

    public byte getExplosiveShootUpgradeStatus() {
        return explosiveShootUpgradeStatus;
    }

    public byte getErosionUpgradeStatus() {
        return erosionUpgradeStatus;
    }

    public byte getThunderStormUpgradeStatus() {
        return thunderStormUpgradeStatus;
    }

    public byte getThunderTrajectoryUpgradeStatus() {
        return thunderTrajectoryUpgradeStatus;
    }
}
