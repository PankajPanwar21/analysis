package com.bbby.spark.entities;

public class AnalysisData {

	private long totalTransactionsLoaded;

	private long totalSalesTranssactions;

	private long totalRefundTransactions;

	private long totalSuccessfulTransactions;

	private long totalTransactionsLoadedWithErrors;

	private TransactionsErrors errorTypeCounts;

	public long getTotalTransactionsLoaded() {
		return totalTransactionsLoaded;
	}

	public void setTotalTransactionsLoaded(long totalTransactionsLoaded) {
		this.totalTransactionsLoaded = totalTransactionsLoaded;
	}

	public long getTotalSalesTranssactions() {
		return totalSalesTranssactions;
	}

	public void setTotalSalesTranssactions(long totalSalesTranssactions) {
		this.totalSalesTranssactions = totalSalesTranssactions;
	}

	public long getTotalRefundTransactions() {
		return totalRefundTransactions;
	}

	public void setTotalRefundTransactions(long totalRefundTransactions) {
		this.totalRefundTransactions = totalRefundTransactions;
	}

	public long getTotalSuccessfulTransactions() {
		return totalSuccessfulTransactions;
	}

	public void setTotalSuccessfulTransactions(long totalSuccessfulTransactions) {
		this.totalSuccessfulTransactions = totalSuccessfulTransactions;
	}

	public long getTotalTransactionsLoadedWithErrors() {
		return totalTransactionsLoadedWithErrors;
	}

	public void setTotalTransactionsLoadedWithErrors(long totalTransactionsLoadedWithErrors) {
		this.totalTransactionsLoadedWithErrors = totalTransactionsLoadedWithErrors;
	}

	public TransactionsErrors getErrorTypeCounts() {
		return errorTypeCounts;
	}

	public void setErrorTypeCounts(TransactionsErrors errorTypeCounts) {
		this.errorTypeCounts = errorTypeCounts;
	}
}

class TransactionsErrors {

	private long duplicateTransactions;

	private long zeroUPC;

	private long invalidCaseValue;

	private long encryptedCaseNum;

	private long unknownCases;

	public long getDuplicateTransactions() {
		return duplicateTransactions;
	}

	public void setDuplicateTransactions(long duplicateTransactions) {
		this.duplicateTransactions = duplicateTransactions;
	}

	public long getZeroUPC() {
		return zeroUPC;
	}

	public void setZeroUPC(long zeroUPC) {
		this.zeroUPC = zeroUPC;
	}

	public long getInvalidCaseValue() {
		return invalidCaseValue;
	}

	public void setInvalidCaseValue(long invalidCaseValue) {
		this.invalidCaseValue = invalidCaseValue;
	}

	public long getEncryptedCaseNum() {
		return encryptedCaseNum;
	}

	public void setEncryptedCaseNum(long encryptedCaseNum) {
		this.encryptedCaseNum = encryptedCaseNum;
	}

	public long getUnknownCases() {
		return unknownCases;
	}

	public void setUnknownCases(long unknownCases) {
		this.unknownCases = unknownCases;
	}
}
