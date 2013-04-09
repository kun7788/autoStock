/**
 * 
 */
package com.autoStock.indicator;

import com.autoStock.Co;
import com.autoStock.indicator.results.ResultsADX;
import com.autoStock.taLib.Core;
import com.autoStock.taLib.MInteger;
import com.autoStock.taLib.RetCode;
import com.autoStock.types.basic.ImmutableInteger;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndicatorOfADX extends IndicatorBase {
	public ResultsADX results;
	
	public IndicatorOfADX(ImmutableInteger periodLength, int resultLength, CommonAnalysisData commonAnlaysisData, Core taLibCore) {
		super(periodLength, resultLength, commonAnlaysisData, taLibCore);
	}
	
	public ResultsADX analyize(){
		results = new ResultsADX(resultsetLength);
		results.arrayOfDates = commonAnlaysisData.arrayOfDates;
		
		RetCode returnCode = taLibCore.adx(0, endIndex, arrayOfPriceHigh, arrayOfPriceLow, arrayOfPriceClose, periodLength.value/2, new MInteger(), new MInteger(), results.arrayOfADX);
		handleAnalysisResult(returnCode);
		
		return results;
	}
}