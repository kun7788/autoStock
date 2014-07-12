/**
 * 
 */
package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;

import com.autoStock.adjust.AdjustmentBase;
import com.autoStock.adjust.AdjustmentOfBasicInteger;
import com.autoStock.adjust.AdjustmentOfEnum;
import com.autoStock.adjust.AdjustmentOfSignalMetricThreshold;
import com.autoStock.adjust.IterableOfDouble;
import com.autoStock.adjust.IterableOfEnum;
import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.adjust.AdjustmentCampaign.AdjustmentType;
import com.autoStock.algorithm.AlgorithmBase;
import com.autoStock.indicator.IndicatorBase;
import com.autoStock.signal.SignalBase;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;

/**
 * @author Kevin
 *
 */
public class WMAdjustmentGenerator {
	
	
	public ArrayList<AdjustmentBase> getTypicalAdjustmentForSignal(SignalBase signalBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
		addSignalGuageThresholdMetAndLeft(signalBase, listOfAdjustmentBase, 0);
		addSignalGuagePeakAndTrough(signalBase, listOfAdjustmentBase, 1);
		addTypicalSignalRanges(signalBase, listOfAdjustmentBase);
		
		return listOfAdjustmentBase;
	}
	
	public ArrayList<AdjustmentBase> getTypicalAdjustmentForIndicator(IndicatorBase indicatorBase){
		ArrayList<AdjustmentBase> listOfAdjustmentBase = new ArrayList<AdjustmentBase>();
		
		addTypicalIndicatorParameters(indicatorBase, listOfAdjustmentBase);
		
		return listOfAdjustmentBase;
	}
	
	private void addTypicalIndicatorParameters(IndicatorBase indicatorBase, ArrayList<AdjustmentBase> listOfAdjustmentBase){
		listOfAdjustmentBase.add(new AdjustmentOfBasicInteger(indicatorBase.getClass().getSimpleName() + " Period Length", indicatorBase.indicatorParameters.periodLength, new IterableOfInteger(10, 50, 1)));
	}
	
	private void addTypicalSignalRanges(SignalBase signalBase, ArrayList<AdjustmentBase> listOfAdjustmentBase){
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_long_entry, new IterableOfDouble(-35, 35, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_long_exit, new IterableOfDouble(-35, 35, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_short_entry, new IterableOfDouble(-35, 35, 1)));
		listOfAdjustmentBase.add(new AdjustmentOfSignalMetricThreshold(signalBase, AdjustmentType.signal_metric_short_exit, new IterableOfDouble(-35, 35, 1)));
	}
	
	private void addSignalGuagePeakAndTrough(SignalBase signalBase, ArrayList<AdjustmentBase> listOfAdjustmentBase, int index){
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_trough, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForLongEntry[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_peak, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForLongExit[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_peak, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForShortEntry[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_trough, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForShortExit[index].mutableEnumForSignalGuageType));
	}
	
	private void addSignalGuageThresholdMetAndLeft(SignalBase signalBase, ArrayList<AdjustmentBase> listOfAdjustmentBase, int index){
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Long Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForLongEntry[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Long Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForLongExit[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Short Entry", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForShortEntry[index].mutableEnumForSignalGuageType));
		listOfAdjustmentBase.add(new AdjustmentOfEnum<SignalGuageType>(signalBase.getClass().getSimpleName() + " Short Exit", new IterableOfEnum<SignalGuageType>(SignalGuageType.guage_threshold_met, SignalGuageType.guage_threshold_left, SignalGuageType.custom_always_true, SignalGuageType.custom_always_false), signalBase.signalParameters.arrayOfSignalGuageForShortExit[index].mutableEnumForSignalGuageType));
	
	}
}