package com.github.jmoalves.qifParser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.github.jmoalves.qifParser.type.QifTypeChain;

public class QifParser implements AutoCloseable {
	private static final String CLOSE_TOKEN = "^";

	private QifTypeChain typeChain = new QifTypeChain();
	
	private BufferedReader reader;
	private QifCallback qifCallback;

	public QifParser(File qif, QifCallback qifCallback) throws FileNotFoundException {
		this.reader = new BufferedReader(new FileReader(qif));
		this.qifCallback = qifCallback;
	}

	@Override
	public void close() throws IOException {
		if (reader == null) {
			return;
		}
		
		reader.close();
		reader = null;
	}

	public boolean readToken() throws IOException {
		String line = reader.readLine();
		if (line == null) {
			return false;
		}

		line = line.trim();
		if ("!Clear:AutoSwitch".equals(line)) {
			return true;
		}

		if ("!Option:AutoSwitch".equals(line)) {
			return true;
		}
		
		List<String> token = new ArrayList<>();
		while (!CLOSE_TOKEN.equals(line)) {
			token.add(line);
			line = reader.readLine();
		}
		token.add(line);
		
		typeChain.handle(token, qifCallback);
		return true;
	}
}
