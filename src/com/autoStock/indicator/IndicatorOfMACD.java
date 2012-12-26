package com.autoStock.indicator;

import com.autoStock.adjust.IterableOfInteger;
import com.autoStock.indicator.results.ResultsMACD;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MAType;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfMACD extends IndicatorBase {
	public ResultsMACD results;
	public ImmutableInteger immutableIntegerForShort = new ImmutableInteger(12);
	public ImmutableInteger immutableIntegerForLong = new ImmutableInteger(26);
	public ImmutableInteger immutableIntegerForEma = new ImmutableInteger(9);
	
	public IndicatorOfMACD(int periodLength, CommonAnlaysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsMACD analize(){
		results = new ResultsMACD(endIndex+1);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode;
		
		if (periodLength > 30){
			returnCode = taLibCore.macdExt(0, endIndex, arrayOfPriceClose, periodLength/3, MAType.Ema, (int)(periodLength/2), MAType.Ema, periodLength/2, MAType.Ema, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		}else{
			returnCode = taLibCore.macd(0, endIndex, arrayOfPriceClose, immutableIntegerForShort.value, immutableIntegerForLong.value, immutableIntegerForEma.value, new MInteger(), new MInteger(), results.arrayOfMACD, results.arrayOfMACDSignal, results.arrayOfMACDHistogram);
		}
		
		handleAnalysisResult(returnCode);
		
		return results;
	}
}
