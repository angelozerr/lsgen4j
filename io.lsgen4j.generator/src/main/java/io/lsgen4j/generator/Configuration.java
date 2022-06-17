package io.lsgen4j.generator;

public class Configuration {

	private String projectName;

	private String projectType;

	private String outDir;

	public Configuration() {
		setProjectName("com.youcompany.youls");
		setProjectType(DefaultProjectType.SERVER_LSP4J_BASIC.getPath());
		setOutDir("target");
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getTemplateProjectBaseDir() {
		return projectType;
	}

	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}

	public String getOutDir() {
		return outDir;
	}

	public void setOutDir(String outDir) {
		this.outDir = outDir;
	}
}
