/**
 * 
 */
package com.autoStock.tools;

import java.util.ArrayList;

import com.autoStock.exchange.results.ExResultMarketData.ExResultRowMarketData;
import com.autoStock.exchange.results.ExResultMarketIndexData.ExResultRowMarketIndexData;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickPriceFields;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickSizeFields;
import com.autoStock.trading.platform.ib.definitions.MarketDataDefinitions.TickTypes;
import com.autoStock.types.Index;
import com.autoStock.types.IndexSlice;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin Kowalewski
 *
 */
public class IndexSliceTools {
	public synchronized IndexSlice getIndexSlice(ArrayList<ExResultRowMarketIndexData> listOfExResultRowMarketIndexData, Index index){
		IndexSlice indexSlice = new IndexSlice();
		indexSlice.index = index;
			
		ArrayList<Double> listOfValueLast = new ArrayList<Double>();
		ArrayList<Integer> listOfSizeVolume = new ArrayList<Integer>();
		
		for (ExResultRowMarketIndexData resultRow : listOfExResultRowMarketIndexData){		
			if (resultRow.tickType == TickTypes.type_price){
				
				if (resultRow.tickPriceField == TickPriceFields.field_last){
					listOfValueLast.add(resultRow.value);
				}
				
				else {
//					Co.println("No tickPriceField matched: " + resultRow.tickPriceField.name());
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_size){
				if (resultRow.tickSizeField == TickSizeFields.field_volume){
					listOfSizeVolume.add((int) resultRow.value);
				}
			}
			
			else if (resultRow.tickType == TickTypes.type_string){
				if (resultRow.tickStringValue != null){
					//Parse date
				}
			}
			
			else {
//				Co.println("No tickType matched: " + resultRow.tickType);
			}
		}
		
		indexSlice.valueOpen = getValueOpen(listOfValueLast);
		indexSlice.valueHigh = getValueHigh(listOfValueLast);
		indexSlice.valueLow = getValueLow(listOfValueLast);
		indexSlice.valueClose = getValueClose(listOfValueLast);
		indexSlice.sizeVolume = getSizeVolume(listOfSizeVolume);
		
		return indexSlice;
	}
	
	private double getValueOpen(ArrayList<Double> listOfDouble){
		if (listOfDouble.size() > 0){return listOfDouble.get(0);}
		return 0;
	}
	
	private double getValueHigh(ArrayList<Double> listOfDouble){
		double priceHigh = 0;
		for (double price : listOfDouble){
			if (price > priceHigh){
				priceHigh = price;
			}
		}
		
		return priceHigh;
	}
	
	private double getValueLow(ArrayList<Double> listOfDouble){
		double priceLow = Double.MAX_VALUE;
		for (double price : listOfDouble){
			if (price < priceLow){
				priceLow = price;
			}
		}
		
		if (priceLow == Double.MAX_VALUE){
			return 0;
		}
		
		return priceLow;
	}
	
	private double getValueClose(ArrayList<Double> listOfDouble){
		return listOfDouble.size() > 0 ? listOfDouble.get(listOfDouble.size()-1) : 0;
	}
	
	private int getSizeVolume(ArrayList<Integer> listOfInteger){		
		return MathTools.getMaxInt(listOfInteger) - MathTools.getMinInt(listOfInteger);
	}
}