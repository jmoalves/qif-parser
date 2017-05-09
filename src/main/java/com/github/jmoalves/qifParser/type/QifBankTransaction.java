package com.github.jmoalves.qifParser.type;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class QifBankTransaction {

	public class QifSplit {

		private QifCategory category;
		private String memo;
		private BigDecimal value;
		private QifAccount withAccount;

		private QifSplit(String category) {
			if (category.matches("\\[.*\\]")) {
				this.withAccount = QifAccount.get(category.replace("[", "").replace("]", ""));
			} else {
				this.category = QifCategory.get(category);
			}
		}

		public QifCategory getCategory() {
			return category;
		}

		public String getMemo() {
			return memo;
		}

		public BigDecimal getValue() {
			return value;
		}

		public QifAccount getWithAccount() {
			return withAccount;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("QifSplit [");
			if (category != null) {
				builder.append("category=");
				builder.append(category);
				builder.append(", ");
			}
			if (memo != null) {
				builder.append("memo=");
				builder.append(memo);
				builder.append(", ");
			}
			if (value != null) {
				builder.append("value=");
				builder.append(value);
				builder.append(", ");
			}
			if (withAccount != null) {
				builder.append("withAccount=");
				builder.append(withAccount);
			}
			builder.append("]");
			return builder.toString();
		}
	}

	public static QifBankTransaction create(List<String> token, QifAccount account) {
		return new QifBankTransaction(account, token);
	}

	private QifAccount account;
	private LocalDate date;
	private BigDecimal uValue;
	private BigDecimal tValue;
	private String cleared;
	private String payee;
	private String memo;
	private QifCategory category;
	private QifAccount withAccount;
	private String type;
	private List<QifSplit> splits = new ArrayList<>();

	private QifBankTransaction(QifAccount account, List<String> token) {
		this.account = account;
		
		QifSplit split = null;
		
		for (String item : token) {
			String type = item.substring(0, 1);
			String content = item.substring(1);
			
			switch (type) {
			case "!":
				break;

			case "^":
				break;
				
			case "D":
				this.date = LocalDate.parse(content, DateTimeFormatter.ofPattern("M/ppd''yy"));
				break;
				
			case "U":
				this.uValue = new BigDecimal(content.replace(",", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
				break;

			case "T":
				this.tValue = new BigDecimal(content.replace(",", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
				break;

			case "C":
				this.cleared = content;
				break;

			case "P":
				this.payee = content;
				break;

			case "M":
				this.memo = content;
				break;
				
			case "L":
				if (content.matches("\\[.*\\]")) {
					this.withAccount = QifAccount.get(content.replace("[", "").replace("]", ""));
				} else {
					this.category = QifCategory.get(content);
				}
				
				break;
				
			case "N":
				this.type = content;
				break;

			case "S":
				split = new QifSplit(content);
				break;

			case "E":
				split.memo = content;
				break;

			case "$":
				split.value = new BigDecimal(content.replace(",", "")).setScale(2, BigDecimal.ROUND_HALF_UP);
				splits.add(split);
				split = null;
				break;

			default:
				throw new IllegalArgumentException("Unhandled attribute " + type);
			}
		}
		
		if (!uValue.equals(tValue)) {
			throw new IllegalStateException("U != T");
		}
	}

	public LocalDate getDate() {
		return date;
	}

	public BigDecimal getuValue() {
		return uValue;
	}

	public BigDecimal gettValue() {
		return tValue;
	}

	public String getCleared() {
		return cleared;
	}

	public String getPayee() {
		return payee;
	}

	public String getMemo() {
		return memo;
	}

	public QifCategory getCategory() {
		return category;
	}

	public String getType() {
		return type;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("QifBankTransaction [");
		if (account != null) {
			builder.append("account=");
			builder.append(account);
			builder.append(", ");
		}
		if (date != null) {
			builder.append("date=");
			builder.append(date);
			builder.append(", ");
		}
		if (uValue != null) {
			builder.append("uValue=");
			builder.append(uValue);
			builder.append(", ");
		}
//		if (tValue != null) {
//			builder.append("tValue=");
//			builder.append(tValue);
//			builder.append(", ");
//		}
		if (cleared != null) {
			builder.append("cleared=");
			builder.append(cleared);
			builder.append(", ");
		}
		if (payee != null) {
			builder.append("payee=");
			builder.append(payee);
			builder.append(", ");
		}
		if (memo != null) {
			builder.append("memo=");
			builder.append(memo);
			builder.append(", ");
		}
		if (category != null) {
			builder.append("category=");
			builder.append(category);
			builder.append(", ");
		}
		if (withAccount != null) {
			builder.append("withAccount=");
			builder.append(withAccount);
			builder.append(", ");
		}
		if (type != null) {
			builder.append("type=");
			builder.append(type);
			builder.append(", ");
		}
		if (splits != null && !splits.isEmpty()) {
			builder.append("splits=");
			builder.append(splits);
		}
		builder.append("]");
		return builder.toString();
	}

	public QifAccount getAccount() {
		return account;
	}

	public QifAccount getWithAccount() {
		return withAccount;
	}

	public List<QifSplit> getSplits() {
		return splits;
	}
}
