package com.github.jmoalves.qifParser;

import com.github.jmoalves.qifParser.type.QifAccount;
import com.github.jmoalves.qifParser.type.QifCategory;

public interface QifCallback {
	default void qifAccount(QifAccount acc) {}
	default void qifCategory(QifCategory cat) {}
}
