
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Aaron
 */
public class InjectionTest {

    private List<Runnable> injections;
    private Thread consumer;

    public InjectionTest() {
        injections = Collections.synchronizedList(new ArrayList());
        consumer = new Thread(new Runnable() {
            @Override
            public void run() {

                while (true) {
                    while (!injections.isEmpty()) {
                        injections.remove(0).run();
                    }
                    System.out.println("Loop.");
                    sleep(1000);
                }
            }
        });
        consumer.setDaemon(true);
        consumer.start();
    }

    public static void main(String[] args) {

        InjectionTest test = new InjectionTest();

        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("Shit gets done.");
            }
        };
        test.inject(r);
        System.out.println("Ouch.");
        test.injectWait(r);
        System.out.println("Win.");

        sleep(2000);

        test.injectWait(r);
        System.out.println("Fuck Yeah.");

        sleep(2000);

        System.out.println("We Win!");

    }

    public static void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {}
    }


    public void inject(Runnable r) {
        injections.add(r);
    }

    public void injectWait(final Runnable r) {
        final AtomicBoolean b = new AtomicBoolean(false);
        injections.add(new Runnable() {
            @Override
            public void run() {
                r.run();
                b.set(true);
            }
        });
        while (!b.get()) {
            sleep(5);
        }
    }

}
