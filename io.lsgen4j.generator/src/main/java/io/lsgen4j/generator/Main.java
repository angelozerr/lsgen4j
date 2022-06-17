package io.lsgen4j.generator;

import java.io.IOException;

public class Main {

	public static void main(String[] args) {
		Configuration configuration = new Configuration();
		
		ProjectGenerator projectGenerator = new ProjectGenerator(configuration);
		try {
			projectGenerator.generate();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
