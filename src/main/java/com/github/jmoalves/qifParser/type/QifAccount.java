package com.github.jmoalves.qifParser.type;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QifAccount {
	private static Map<String, QifAccount> accounts = new HashMap<>();
	
	public static QifAccount get(List<String> token) {
		QifAccount acc = new QifAccount(token);
		if (accounts.containsKey(acc.getName())) {
			return accounts.get(acc.getName());
		}
		
		accounts.put(acc.getName(), acc);
		return acc;
	}
	
	private String name;
	private String type;
	private Double limit;

	private QifAccount(List<String> token) {
		for (String item : token) {
			String type = item.substring(0, 1);
			String content = item.substring(1);
			
			switch (type) {
			case "N":
				this.name = content;
				break;
			
			case "T":
				this.type = content;
				break;

			case "L": // CreditCard only
				this.limit = Double.parseDouble(content);
				break;
			}
		}
	}

	public String getName() {
		return name;
	}

	public String getType() {
		return type;
	}

	public double getLimit() {
		return limit;
	}

	@Override
	public String toString() {
		return "Account [name=" + name + ", type=" + type + (limit == null ? "" : ", limit=" + limit) + "]";
	}
}
