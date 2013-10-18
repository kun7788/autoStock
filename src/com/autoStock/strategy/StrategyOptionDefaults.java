package com.autoStock.strategy;

import com.autoStock.signal.SignalPointResolver.SignalPointTactic;

/**
 * @author Kevin Kowalewski
 *
 */
public class StrategyOptionDefaults {
	public static StrategyOptionDefaults instance = new StrategyOptionDefaults();
	
	public static StrategyOptionDefaults getInstance(){
		return instance;
	}	
	public StrategyOptions getDefaultStrategyOptions(){
		StrategyOptions strategyOptions = new StrategyOptions();
		strategyOptions.canGoLong = true;
		strategyOptions.canGoShort = true;
		strategyOptions.canReenter = false;
		strategyOptions.mustHavePositiveSlice = false;
		strategyOptions.disableAfterNilChanges = true;
		strategyOptions.disableAfterNilVolumes = true;
		strategyOptions.disableAfterLoss = false;
		strategyOptions.prefillEnabled = false;
		strategyOptions.signalPointTacticForEntry = SignalPointTactic.tatic_combined;
		strategyOptions.signalPointTacticForExit = SignalPointTactic.tatic_combined;
		
		strategyOptions.maxTransactionsDay = 16;
		strategyOptions.maxStopLossPercent.value = -0.15d;
		strategyOptions.maxProfitDrawdownPercent.value = -0.25d;
		strategyOptions.maxNilChangePrice = 15;
		strategyOptions.maxNilChangeVolume = 15;
		strategyOptions.maxPositionEntryTime = 30;
		strategyOptions.maxPositionExitTime = 10;
		strategyOptions.maxPositionLossTime = 0;
		strategyOptions.maxReenterTimesPerPosition.value = 3;
		strategyOptions.intervalForReentryMins.value = 3;
		strategyOptions.minReentryPercentGain.value = 0.25;
		strategyOptions.prefillShift.value = 0;
		strategyOptions.intervalForEntryAfterExitWithLossMins.value = 10;
//		strategyOptions.intervalForExitEntryMins.value = 5;
		
		return strategyOptions;
	}
}
