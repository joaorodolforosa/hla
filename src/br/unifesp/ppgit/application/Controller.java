package br.unifesp.ppgit.application;

import br.unifesp.ppgit.hlamodule.HlaInterface;
import hla.rti1516e.exceptions.RTIexception;
import java.io.IOException;

/**
 *
 * @author joaorodolforosa
 */
public class Controller {

    private HlaInterface _hlaInterface;
    private SimulationConfig _config;

    public void start() {
        try {
            _config = new SimulationConfig("Simulation.config");
        } catch (IOException e) {
            System.err.println("Could not read Simulation.config");
            return;
        }

        _hlaInterface = HlaInterface.Factory.newInterface(this);

        System.out.println(_config.getFom());
        System.out.println(_config.getFederationName());
        System.out.println(_config.getFederateName());

        try {
            _hlaInterface.start(_config.getFom(), _config.getFederationName(), _config.getFederateName());
        } catch (RTIexception e) {
            System.out.println("Could not connect to the RTI using the local settings designator \"");
        }
    }

    public void advanceTime() {
        try {
            _hlaInterface.advanceTime(60.0);
        } catch (RTIexception e) {
            System.out.println("Could not execute the method advanceTime()");
        }
    }

}
