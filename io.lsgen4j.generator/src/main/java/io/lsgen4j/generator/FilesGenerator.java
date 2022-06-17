package io.lsgen4j.generator;

import java.io.Writer;
import java.util.Map;

public class FilesGenerator {

	private final static FilesGenerator INSTANCE = new FilesGenerator();

	public static FilesGenerator getInstance() {
		return INSTANCE;
	}

	private FilesGenerator() {

	}

	public void generate(Map<String, Object> dataModel, String templateName, Writer writer) {

	}

}
