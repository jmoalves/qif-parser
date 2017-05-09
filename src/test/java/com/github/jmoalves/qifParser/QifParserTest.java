package com.github.jmoalves.qifParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import org.junit.Test;

import com.github.jmoalves.qifParser.type.QifAccount;
import com.github.jmoalves.qifParser.type.QifBankTransaction;
import com.github.jmoalves.qifParser.type.QifCategory;

public class QifParserTest {
	private static final Logger log = Logger.getLogger(QifParserTest.class.getName());
	
	@Test
	public void qifParse() throws FileNotFoundException, IOException {
		File qif = new File("/home/jmoalves/VMWindows-shared/jmoalves.QIF");
		try (QifParser reader = new QifParser(qif, new QifCallbackTest())) {
			while (reader.readToken()) {
				
			}
		}
	}
	
	private static class QifCallbackTest implements QifCallback {
		@Override
		public void qifAccount(QifAccount acc) {
//			log.info("QifAccount: " + acc);
		}
		
		@Override
		public void qifCategory(QifCategory cat) {
//			log.info("QifCategory: " + cat);
		}

		@Override
		public void qifBankTransaction(QifBankTransaction transaction) {
			log.info("QifBankTransaction: " + transaction);
		}

	}
}
