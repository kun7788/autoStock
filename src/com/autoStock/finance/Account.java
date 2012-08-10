/**
 * 
 */
package com.autoStock.finance;

import java.util.concurrent.atomic.AtomicInteger;

import com.autoStock.tools.MathTools;
import com.google.common.util.concurrent.AtomicDouble;


/**
 * @author Kevin Kowalewski
 *
 */
public class Account {
	public static Account instance = new Account();
	private final double bankBalanceDefault = 100000.00;
	private final double transactionFeesDefault = 0;
	private AtomicDouble bankBalance = new AtomicDouble();
	private AtomicDouble transactionFeesPaid = new AtomicDouble();
	private AtomicInteger transactions = new AtomicInteger();
	
	private Account(){
		resetAccount();
	}
	
	public synchronized double getBankBalance(){
		synchronized (this) {
			return MathTools.round(bankBalance.get());
		}
	}
	
	public double getTransactionFeesPaid(){
		return MathTools.round(transactionFeesPaid.get());
	}
	
	public int getTransactions(){
		return transactions.get();
	}
	
	private void changeBankBalance(double amount){
		bankBalance.addAndGet(amount);
	}
	
	public synchronized void changeBankBalance(double positionCost, double transactionCost){
		synchronized (this) {
			bankBalance.addAndGet(positionCost);
			bankBalance.addAndGet(transactionCost *-1);
			transactionFeesPaid.getAndAdd(transactionCost);
			transactions.incrementAndGet();
		}
	}
	
	public synchronized double getTransactionCost(int units, double price){
		double cost = 0;
		if (units <= 500){
			cost = Math.max(1.30, units * 0.013);	
		}else{
			cost = Math.max(1.30, units * 0.008);
		}
		
		return Math.min(cost, units * price * 0.005);
	}
	
	public synchronized void resetAccount(){
		synchronized(this){
			bankBalance.set(bankBalanceDefault);
			transactionFeesPaid.set(transactionFeesDefault);
			transactions.set(0);
		}
	}
}
