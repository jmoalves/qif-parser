package com.github.jmoalves.qifParser.type;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QifCategory {
	private static Map<String, QifCategory> categories = new HashMap<>();
	
	public static QifCategory create(List<String> token) {
		QifCategory cat = new QifCategory(token);
		if (categories.containsKey(cat.getName())) {
			return categories.get(cat.getName());
		}
		
		categories.put(cat.getName(), cat);
		return cat;
	}

	public static QifCategory get(String category) {
		return categories.get(category);
	}

	private String name;
	private String description;
	private boolean taxRelated;
	private String r;
	private boolean expense;
	private boolean income;

	private QifCategory(List<String> token) {
		for (String item : token) {
			String type = item.substring(0, 1);
			String content = item.substring(1);
			
			switch (type) {
			case "!":
				break;

			case "^":
				break;
				
			case "N":
				this.name = content;
				break;
			
			case "D":
				this.description = content;
				break;

			case "T":
				this.taxRelated = true;
				break;

			case "R":
				this.r = content;
				break;

			case "E":
				this.expense = true;
				break;

			case "I":
				this.income = true;
				break;
				
			default:
				throw new IllegalArgumentException("Unhandled attribute " + type);
			}
		}
		
		if (expense && income) {
			throw new IllegalStateException("Expense and Income");
		}
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public boolean isTaxRelated() {
		return taxRelated;
	}

	public String getR() {
		return r;
	}

	public boolean isExpense() {
		return expense;
	}

	public boolean isIncome() {
		return income;
	}
	
	public List<String> getHierarchy() {
		return Arrays.asList(name.split(":"));
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QifCategory [");

		if (name != null) {
			builder.append("name=");
			builder.append(name);
			builder.append(", ");
			
			builder.append("hierarchy=");
			builder.append(getHierarchy());
			builder.append(", ");
		}

		if (description != null) {
			builder.append("description=");
			builder.append(description);
			builder.append(", ");
		}
		
		if (taxRelated) {
			builder.append("taxRelated, ");
		}
		
		if (r != null) {
			builder.append("r=");
			builder.append(r);
			builder.append(", ");
		}
		
		if (expense) {
			builder.append("expense");
		}
		
		if (income) {
			builder.append("income");
		}

		builder.append("]");
		return builder.toString();
	}
}
