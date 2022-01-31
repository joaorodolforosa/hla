package br.unifesp.ppgit.hlamodule;

import br.unifesp.ppgit.application.Controller;
import hla.rti1516e.exceptions.ConnectionFailed;
import hla.rti1516e.exceptions.CouldNotOpenFDD;
import hla.rti1516e.exceptions.ErrorReadingFDD;
import hla.rti1516e.exceptions.FederateNotExecutionMember;
import hla.rti1516e.exceptions.FederateServiceInvocationsAreBeingReportedViaMOM;
import hla.rti1516e.exceptions.InconsistentFDD;
import hla.rti1516e.exceptions.InvalidLocalSettingsDesignator;
import hla.rti1516e.exceptions.NotConnected;
import hla.rti1516e.exceptions.RTIinternalError;
import hla.rti1516e.exceptions.RestoreInProgress;
import hla.rti1516e.exceptions.SaveInProgress;

/**
 *
 * @author joaorodolforosa
 */
public interface HlaInterface {

    void start(String fomPath, String federationName, String federateName)
            throws FederateNotExecutionMember,
            RestoreInProgress,
            SaveInProgress,
            NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM,
            RTIinternalError,
            ConnectionFailed,
            InvalidLocalSettingsDesignator,
            ErrorReadingFDD,
            CouldNotOpenFDD,
            InconsistentFDD;

    void stop() throws RTIinternalError;

    void advanceTime(double timeStep) throws RTIinternalError;

    public static class Factory {

        public static HlaInterface newInterface(Controller controller) {
            return new HlaInterfaceImpl(controller);
        }
    }
}
