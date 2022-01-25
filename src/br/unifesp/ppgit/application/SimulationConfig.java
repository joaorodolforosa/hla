package br.unifesp.ppgit.application;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author joaorodolforosa
 */
public class SimulationConfig {

	private static final String FEDERATION_NAME = "federationName";
	private static final String FEDERATE_NAME = "federateName";

	private static final String FOM = "fom";

	private final String _federationName;
	private final String _federateName;

	private final String _fom;
	public SimulationConfig(String fileName) throws IOException {
		this(new File(fileName));
	}

	public SimulationConfig(File file) throws IOException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(file));

		_federationName = properties.getProperty(FEDERATION_NAME, "");
		_federateName = properties.getProperty(FEDERATE_NAME, "");

		_fom = properties.getProperty(FOM, "Fom.xml");	      
	}


	public String getFederationName() {
		return this._federationName;
	}

	public String getFederateName() {
		return this._federateName;
	}

	public String getFom() {
		return this._fom;
	}
    
}
