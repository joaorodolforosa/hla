package br.unifesp.ppgit.application;

/**
 *
 * @author joaorodolforosa
 */
public class TestFederate1 {

    private Controller controller;

    private void run() {

        controller = new Controller();
        try {
            controller.start();
            controller.advanceTime();
        } catch (Exception e) {
            System.out.println();
        }
    }

    public static void main(String[] args) {
        TestFederate1 teste = new TestFederate1();
        teste.run();
    }
}
