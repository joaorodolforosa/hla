package br.unifesp.ppgit.hlamodule;

import br.unifesp.ppgit.application.Controller;
import hla.rti1516e.*;
import hla.rti1516e.exceptions.*;
import hla.rti1516e.time.HLAfloat64Time;
import hla.rti1516e.time.HLAfloat64TimeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

/**
 *
 * @author joaorodolforosa
 */
public class HlaInterfaceImpl extends NullFederateAmbassador implements HlaInterface {

    private RTIambassador _ambassador;
    private String _federationName;
    private FederateAmbassador _federateAmbassador;

    public HlaInterfaceImpl(Controller controller) {

    }

    @Override
    public void start(String fomPath, String federationName, String federateName)
            throws hla.rti1516e.exceptions.FederateNotExecutionMember, hla.rti1516e.exceptions.RestoreInProgress, hla.rti1516e.exceptions.SaveInProgress, NotConnected,
            FederateServiceInvocationsAreBeingReportedViaMOM, hla.rti1516e.exceptions.RTIinternalError, ConnectionFailed,
            InvalidLocalSettingsDesignator, ErrorReadingFDD, CouldNotOpenFDD, InconsistentFDD {

        System.out.println();
        System.out.println("Parâmetros recebidos em HlaInterfaceimpl.java");
        System.out.println("fomPath: " + fomPath);
        System.out.println("federationName: " + federationName);
        System.out.println("federateName: " + federateName);
        System.out.println();

        /////////////////////////////////
        // 1. create the RTIambassador //
        /////////////////////////////////
        RtiFactory rtiFactory = RtiFactoryFactory.getRtiFactory();
        _ambassador = rtiFactory.getRtiAmbassador();

        try {
            System.out.println("Tentou conectar");
            System.out.println();
            _ambassador.connect(this, CallbackModel.HLA_IMMEDIATE);
        } catch (AlreadyConnected ignored) {
        } catch (UnsupportedCallbackModel | CallNotAllowedFromWithinCallback e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }

        //////////////////////////////
        // 2. create the federation //
        //////////////////////////////
        // create
        // NOTE: some other federate may have already created the federation,
        //       in that case, we'll just try and join it
        File fom = new File(fomPath);
        URL url = null;
        try {
            url = fom.toURI().toURL();
            System.out.println();
            System.out.println(url);
            System.out.println();
        } catch (MalformedURLException ignored) {
        }
        try {
            _ambassador.createFederationExecution(federationName, url);
        } catch (FederationExecutionAlreadyExists ignored) {
        }

        try {
            boolean joined = false;
            String federateNameSuffix = "";
            int federateNameIndex = 1;
            while (!joined) {
                try {
                    _ambassador.joinFederationExecution(federateName + federateNameSuffix, "Unifesp", federationName, new URL[]{url});
                    joined = true;
                    System.out.println("Federado conectado");
                } catch (FederateNameAlreadyInUse e) {
                    federateNameSuffix = "-" + federateNameIndex++;
                }
            }
        } catch (FederateAlreadyExecutionMember ignored) {
        } catch (CouldNotCreateLogicalTimeFactory | FederationExecutionDoesNotExist | CallNotAllowedFromWithinCallback e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }
    }

    @Override
    public void stop() throws RTIinternalError {

        try {
            try {
                _ambassador.resignFederationExecution(ResignAction.CANCEL_THEN_DELETE_THEN_DIVEST);
            } catch (FederateNotExecutionMember ignored) {
            } catch (FederateOwnsAttributes | OwnershipAcquisitionPending | CallNotAllowedFromWithinCallback | InvalidResignAction e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            }

            if (_federationName != null) {
                try {
                    _ambassador.destroyFederationExecution(_federationName);
                } catch (FederatesCurrentlyJoined | FederationExecutionDoesNotExist ignored) {
                }
            }

            try {
                _ambassador.disconnect();
            } catch (FederateIsExecutionMember | CallNotAllowedFromWithinCallback e) {
                throw new RTIinternalError("HlaInterfaceFailure", e);
            }
        } catch (NotConnected ignored) {
        }
    }

    @Override
    public void advanceTime(double timeStep) throws RTIinternalError {
        try {
            HLAfloat64TimeFactory timeFactory = (HLAfloat64TimeFactory) LogicalTimeFactoryFactory.getLogicalTimeFactory("HLAfloat64Time");

            HLAfloat64Time _hlaFloat64Time = timeFactory.makeTime(timeStep);
            _ambassador.timeAdvanceRequest(_hlaFloat64Time);
            while (true) {
                _ambassador.evokeMultipleCallbacks(1.0, 2.0);
            }
        } catch (CallNotAllowedFromWithinCallback | FederateNotExecutionMember | InTimeAdvancingState | InvalidLogicalTime | LogicalTimeAlreadyPassed | NotConnected | RTIinternalError | RequestForTimeConstrainedPending | RequestForTimeRegulationPending | RestoreInProgress | SaveInProgress e) {
            throw new RTIinternalError("HlaInterfaceFailure", e);
        }

    }
}
