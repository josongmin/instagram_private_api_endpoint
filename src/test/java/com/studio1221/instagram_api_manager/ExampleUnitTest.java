package com.studio1221.instagram_api_manager;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    System.out.println("A시작");
                    Thread.sleep(3000);
                    System.out.println("A종료");
                }catch(Exception e){
                    ;
                }
            }
        };

        Thread t2 = new Thread(){
            @Override
            public void run() {
                super.run();
                try{
                    System.out.println("B시작");
                    Thread.sleep(1000);
                    System.out.println("B종료");
                }catch(Exception e){
                    ;
                }
            }
        };

        t1.start();
        t2.start();

        t1.join();
        t2.join();
        System.out.println("C종료");
    }
}