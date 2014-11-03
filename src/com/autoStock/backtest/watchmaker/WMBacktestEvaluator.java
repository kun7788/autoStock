package com.autoStock.backtest.watchmaker;

import java.util.ArrayList;
import java.util.List;

import org.uncommons.watchmaker.framework.FitnessEvaluator;

import com.autoStock.Co;
import com.autoStock.algorithm.core.AlgorithmDefinitions.AlgorithmMode;
import com.autoStock.backtest.AlgorithmModel;
import com.autoStock.backtest.BacktestEvaluation;
import com.autoStock.backtest.BacktestEvaluationBuilder;
import com.autoStock.backtest.SingleBacktest;
import com.autoStock.finance.SecurityTypeHelper.SecurityType;
import com.autoStock.position.PositionGovernor;
import com.autoStock.position.PositionManager;
import com.autoStock.signal.SignalDefinitions.SignalGuageType;
import com.autoStock.tools.Benchmark;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class WMBacktestEvaluator implements FitnessEvaluator<AlgorithmModel>{
	private HistoricalData historicalData;
	
	public WMBacktestEvaluator(HistoricalData historicalData){
		this.historicalData = historicalData;
		historicalData.setStartAndEndDatesToExchange();
	}
	
	@Override
	public double getFitness(AlgorithmModel algorithmModel, List<? extends AlgorithmModel> notUsed) {
		BacktestEvaluation backtestEvaluation = getBacktestEvaluation(algorithmModel, false);
		return backtestEvaluation.getScore();
	}
	
	public synchronized BacktestEvaluation getBacktestEvaluation(AlgorithmModel algorithmModel, boolean includeExtras){
		SingleBacktest singleBacktest = new SingleBacktest(historicalData, AlgorithmMode.mode_backtest_single);
		singleBacktest.remodel(algorithmModel);
		singleBacktest.selfPopulateBacktestData();
		singleBacktest.runBacktest();
		
		BacktestEvaluation backtestEvaluation = new BacktestEvaluationBuilder().buildEvaluation(singleBacktest.backtestContainer, includeExtras, includeExtras);
		
		return backtestEvaluation;
	}

	@Override
	public boolean isNatural() {
		return true;
	}
}
