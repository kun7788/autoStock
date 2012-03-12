/**
 * 
 */
package com.autoStock.scanner;

import java.sql.Connection;
import java.util.ArrayList;

import com.autoStock.Co;
import com.autoStock.algorithm.AlgorithmListener;
import com.autoStock.algorithm.AlgorithmTest;
import com.autoStock.algorithm.reciever.ReceiverOfQuoteSlice;
import com.autoStock.backtest.Backtest;
import com.autoStock.database.DatabaseCore;
import com.autoStock.database.DatabaseDefinitions.BasicQueries;
import com.autoStock.database.DatabaseDefinitions.QueryArgs;
import com.autoStock.database.DatabaseQuery;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbStockHistoricalPrice;
import com.autoStock.generated.basicDefinitions.BasicTableDefinitions.DbSymbol;
import com.autoStock.signal.Signal;
import com.autoStock.tools.DateTools;
import com.autoStock.trading.platform.ib.definitions.HistoricalData.Resolution;
import com.autoStock.trading.types.TypeHistoricalData;
import com.autoStock.types.TypeQuoteSlice;

/**
 * @author Kevin Kowalewski
 *
 */
public class MarketScanner implements ReceiverOfQuoteSlice, AlgorithmListener {
	
	private ArrayList<DbSymbol> listOfSymbolsFromDatabase;
	private ArrayList<Backtest> listOfBacktest = new ArrayList<Backtest>();
	private ArrayList<AlgorithmTest> listOfAlgorithmTest = new ArrayList<AlgorithmTest>();
	
	public MarketScanner(){
		Connection connection = DatabaseCore.getConnection();
		listOfSymbolsFromDatabase = (ArrayList<DbSymbol>) new DatabaseQuery().getQueryResults(BasicQueries.basic_get_symbol_list_from_exchange, QueryArgs.exchange.setValue("NYSE"));
		
		Co.println("Symbols: " + listOfSymbolsFromDatabase.size());
	}
	
	public void startScan(){
		Co.println("Fetching symbol historical data...");
		for (DbSymbol symbol : listOfSymbolsFromDatabase){
			TypeHistoricalData typeHistoricalData = new TypeHistoricalData(symbol.symbol, "STK", DateTools.getDateFromString("2011-01-05 09:30:00"), DateTools.getDateFromString("2011-01-05 15:30:00"), Resolution.min);
			ArrayList<DbStockHistoricalPrice> listOfResults = (ArrayList<DbStockHistoricalPrice>) new DatabaseQuery().getQueryResults(
					BasicQueries.basic_historical_price_range,
					QueryArgs.symbol.setValue(typeHistoricalData.symbol),
					QueryArgs.startDate.setValue(DateTools.getSqlDate(typeHistoricalData.startDate)),
					QueryArgs.endDate.setValue(DateTools.getSqlDate(typeHistoricalData.endDate)));
					
			Backtest backtest = new Backtest(typeHistoricalData, listOfResults);
		
			if (listOfResults.size() != 0){
				Co.println("Has size: " + listOfResults.size());
				listOfBacktest.add(backtest);
			}
		}
		
		Co.println("Initializing backtests... ");
		
		for (Backtest backtest : listOfBacktest){
			AlgorithmTest algorithmTest = new AlgorithmTest();
			algorithmTest.setAlgorithmListener(this);
			backtest.performBacktest(algorithmTest.getReceiver());
		}
	}

	@Override
	public void receiveQuoteSlice(TypeQuoteSlice quoteSlice) {
		Co.println("Received slice: " + quoteSlice.symbol);
	}

	@Override
	public void endOfFeed() {
		
	}

	@Override
	public void recieveSignal(Signal signal, TypeQuoteSlice typeQuoteSlice) {
		if (signal.getCombinedSignal() > 50){
			Co.println("Recieved buy signal: " + signal.getCombinedSignal());
		}else if (signal.getCombinedSignal() < - 50){
			Co.println("Recieved sell signal: " + signal.getCombinedSignal());
		}
	}
}
