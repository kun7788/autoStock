/**
 * 
 */
package com.autoStock.premise;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import com.autoStock.Co;
import com.autoStock.backtest.BacktestUtils;
import com.autoStock.backtest.BacktestUtils.LookDirection;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArg;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.exchange.request.RequestHistoricalData;
import com.autoStock.exchange.request.base.RequestHolder;
import com.autoStock.exchange.request.listener.RequestHistoricalDataListener;
import com.autoStock.exchange.results.ExResultHistoricalData.ExResultSetHistoricalData;
import com.autoStock.generated.basicDefinitions.TableDefinitions.DbStockHistoricalPrice;
import com.autoStock.internal.ApplicationStates;
import com.autoStock.signal.extras.EncogFrame;
import com.autoStock.signal.extras.EncogFrame.FrameType;
import com.autoStock.signal.extras.EncogFrameSupport;
import com.autoStock.signal.extras.EncogFrameSupport.EncogFrameSource;
import com.autoStock.signal.extras.EncogSubframe;
import com.autoStock.tools.DateTools;
import com.autoStock.tools.QuoteSliceTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalDataDefinitions.Resolution;
import com.autoStock.trading.types.HistoricalData;
import com.autoStock.types.Exchange;
import com.autoStock.types.QuoteSlice;
import com.autoStock.types.Symbol;

/**
 * @author Kevin
 * So hacky but meh
 *
 */
public class PremiseOfOHLC extends PremiseBase implements EncogFrameSource {
	public Exchange exchange;
	public Symbol symbol;
	public Date dateStart;
	public int days;
	public Resolution resolution;
	public ArrayList<QuoteSlice> listOfQuotes;
	public static final int ITEM_LENGTH = 4;
	
	public PremiseOfOHLC(Exchange exchange, Symbol symbol, Date dateStart, Resolution resolution, int days) {
		this.exchange = exchange;
		this.symbol = symbol;
		this.dateStart = dateStart;
		this.resolution = resolution;
		this.days = days;
	}

	@Override
	public void run(){
		populate();
	}
	
	private void populate(){
		ArrayList<HistoricalData> list = BacktestUtils.getHistoricalDataListForDates(new HistoricalData(exchange, symbol, dateStart, null, resolution), LookDirection.backward, days);
		ArrayList<DbStockHistoricalPrice> results = new ArrayList<DbStockHistoricalPrice>();
		ArrayList<QuoteSlice> listOfOHLC = new ArrayList<QuoteSlice>();
		
		for (HistoricalData historicalData : list){
			results.addAll((ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(BasicQueries.basic_historical_price_range, new QueryArg(QueryArgs.symbol, historicalData.symbol.symbolName), new QueryArg(QueryArgs.exchange, historicalData.exchange.exchangeName), new QueryArg(QueryArgs.resolution, historicalData.resolution.asMinutes()), new QueryArg(QueryArgs.startDate, DateTools.getSqlDate(historicalData.startDate)), new QueryArg(QueryArgs.endDate, DateTools.getSqlDate(historicalData.endDate))));			
		}
		
		for (DbStockHistoricalPrice price : results){
			QuoteSlice quoteSlice = new QuoteSlice(symbol, price.priceOpen, price.priceHigh, price.priceLow, price.priceClose, 0, 0, price.sizeVolume, price.dateTime, resolution);
			listOfOHLC.add(quoteSlice);
		}
	}

	@Override
	public EncogFrame asEncogFrame() {
		EncogFrame encogFrame = new EncogFrame("OHLC for: " + symbol.symbolName + ", " + DateTools.getPrettyDate(dateStart) + ", " + resolution.name(), FrameType.raw);
		ArrayList<Double> values = new ArrayList<Double>();
		
		for (QuoteSlice quote : listOfQuotes){
			values.add(quote.priceOpen);
			values.add(quote.priceHigh);
			values.add(quote.priceLow);
			values.add(quote.priceClose);
		}
		
		encogFrame.addSubframe(new EncogSubframe(values, FrameType.raw));
		return encogFrame;
	}
}
