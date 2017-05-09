package com.github.jmoalves.qifParser.type;

import java.util.List;
import java.util.logging.Logger;

import com.github.jmoalves.qifParser.QifCallback;

public class QifTypeChain {
	private static final Logger LOG = Logger.getLogger(QifTypeChain.class.getName());
	
	private String lastType = null;
	private QifAccount lastAccount = null;
	
	public void handle(List<String> token, QifCallback callback) {
		if (token.isEmpty()) {
			return;
		}
		
		String tokenTag = token.get(0);
		if (dispatch(token, callback, tokenTag)) {
			lastType = tokenTag;
			return;
		}

		token.add(0, lastType);
		if (!dispatch(token, callback, lastType)) {
			throw new IllegalStateException(token.toString());
		}
	}

	private boolean dispatch(List<String> token, QifCallback callback, String tokenTag) {
		if (tokenTag == null) {
			return false;
		}

		switch (tokenTag) {
		case "!Account":
			lastAccount = handleAccount(token, callback);
			break;
			
		case "!Type:Tag":
			handleTag(token, callback);
			break;
			
		case "!Type:Cat":
			handleCategory(token, callback);
			break;
			
		case "!Type:Security":
			handleSecurity(token, callback);
			break;
			
		case "!Type:Bank": 
			handleBank(token, callback);
			break;
			
		case "!Type:CCard":
			handleCreditCard(token, callback);
			break;
			
		case "!Type:Cash": 
			handleCash(token, callback);
			break;
			
		case "!Type:Invst":
			handleInvestment(token, callback);
			break;
			
		case "!Type:Memorized":
			handleMemorized(token, callback);
			break;
			
		case "!Type:Prices":
			handlePrices(token, callback);
			break;

		default:
			return false;
		}
		
		return true;
	}

	private QifAccount handleAccount(List<String> token, QifCallback callback) {
//		LOG.info("Account: " + token);
		QifAccount acc = QifAccount.get(token);
		callback.qifAccount(acc);
		return acc;
	}

	private void handleTag(List<String> token, QifCallback callback) {
//		LOG.info("Tag: " + token);
	}

	private void handleCategory(List<String> token, QifCallback callback) {
//		LOG.info("Category: " + token);
		QifCategory cat = QifCategory.create(token);
		callback.qifCategory(cat);
	}

	private void handleSecurity(List<String> token, QifCallback callback) {
//		LOG.info("Security: " + token);
	}

	private void handleBank(List<String> token, QifCallback callback) {
//		LOG.info("Bank: " + token + " => " + lastAccount);
		QifBankTransaction bt = QifBankTransaction.create(token, lastAccount);
		callback.qifBankTransaction(bt);
	}

	private void handleCreditCard(List<String> token, QifCallback callback) {
//		LOG.info("CreditCard: " + token + " => " + lastAccount);
	}

	private void handleCash(List<String> token, QifCallback callback) {
//		LOG.info("Cash: " + token + " => " + lastAccount);
	}

	private void handleInvestment(List<String> token, QifCallback callback) {
//		LOG.info("Investment: " + token + " => " + lastAccount);
	}

	private void handleMemorized(List<String> token, QifCallback callback) {
//		LOG.info("Memorized: " + token);
	}

	private void handlePrices(List<String> token, QifCallback callback) {
//		LOG.info("Prices: " + token);
	}
}
