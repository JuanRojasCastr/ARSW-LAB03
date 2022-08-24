/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.eci.arst.concprg.prodcons;

import java.util.Queue;

/**
 *
 * @author hcadavid
 */
public class Consumer extends Thread{
    
    private Queue<Integer> queue;
    
    
    public Consumer(Queue<Integer> queue){
        this.queue=queue;        
    }
    
    @Override
    public void run() {
        while (true) {

            try{
                synchronized (queue){
                    while (queue.isEmpty())
                    {
                        System.out.println("Queue is empty " + Thread.currentThread().getName() + " is waiting , size: " + queue.size());
                        queue.wait();
                    }
                    int elem=queue.poll();
                    System.out.println("Consumer consumes "+elem);
                    queue.notifyAll();
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
            
        }
    }
}
