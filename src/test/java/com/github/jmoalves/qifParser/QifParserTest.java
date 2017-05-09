package com.github.jmoalves.qifParser;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.junit.Test;

import com.github.jmoalves.qifParser.type.QifAccount;
import com.github.jmoalves.qifParser.type.QifBankTransaction;
import com.github.jmoalves.qifParser.type.QifCategory;

import static org.junit.Assert.*;

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
			log.info("QifAccount: " + acc);
			Response res = createAccount(acc.getName(), "BRL");
//			assertEquals(Status.OK, res.getStatus());
		}
		
		@Override
		public void qifCategory(QifCategory cat) {
			log.info("QifCategory: " + cat);
			Response res = createAccount(cat.getName(), "BRL");
		}

		@Override
		public void qifBankTransaction(QifBankTransaction transaction) {
			log.info("QifBankTransaction: " + transaction);
		}

		
		private Response createAccount(String accName, String refName) {
			try {
				URL baseURL = new URL("http://localhost:18080/easyledger-service-0.0.1-SNAPSHOT");
				return ClientBuilder.newClient()
						.target(baseURL.toURI())
						.path("rest")
						.path("account")
						.path(accName)
						.queryParam("reference", refName)
						.request()
						.post(null);
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		}
	}
}
