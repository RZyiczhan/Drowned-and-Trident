package com.rzero.drownedandtrident.enchantment.programmingModel;

public class TridentSplitParamModel {

    public Integer fanSplitAngle;
    public Integer fanSplitTick;
    public Integer scatterSpreadLevel;
    public Integer scatterSplitTick;

    public TridentSplitParamModel(Integer fanSplitAngle, Integer fanSplitTick, Integer scatterSpreadLevel, Integer scatterSplitTick) {
        this.fanSplitAngle = fanSplitAngle;
        this.fanSplitTick = fanSplitTick;
        this.scatterSpreadLevel = scatterSpreadLevel;
        this.scatterSplitTick = scatterSplitTick;
    }

    public TridentSplitParamModel(TridentSplitParamModel splitParam, int delayedTick){
        this.fanSplitAngle = splitParam.fanSplitAngle;
        this.scatterSpreadLevel = splitParam.scatterSpreadLevel;
        this.fanSplitTick = splitParam.fanSplitTick - delayedTick;
        this.scatterSplitTick = splitParam.scatterSplitTick - delayedTick;
    }
}
