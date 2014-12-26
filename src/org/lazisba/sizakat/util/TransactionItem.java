package org.lazisba.sizakat.util;

public class TransactionItem {

	private String txType;
	private String txAmount;
	private String txDate;
	
	public TransactionItem(String trxType, String trxAmount, String trxDate) {
		// TODO Auto-generated constructor stub
		txType = trxType;
		txAmount = trxAmount;
		txDate = trxDate;
	}

	public String getType() {return txType;}
	public String getAmount() {return txAmount;}
	public String getDate() {return txDate;}
}
