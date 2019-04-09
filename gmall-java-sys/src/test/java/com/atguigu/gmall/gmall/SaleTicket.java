package com.atguigu.gmall.gmall;

import com.sun.org.apache.bcel.internal.generic.NEW;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Ticket{
    private  int number=30;
    private Lock lock=new ReentrantLock();
    public void sale(){
        lock.lock();
        try {
            if (number>0){
                System.out.println(Thread.currentThread().getName()+"\t卖出第："+(number--)+"\t还剩下"+number);
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }
}

/**
 * 1 线程 操作 资源类
 * 2 高内聚 低耦合
 */
/*NEW新建  RUNNABLE就绪 可运行 BOLOCKED阻塞
         * WAITING 等待不见不散 TIME_WAITING 过时不候 TERMINATED结束*/
public class SaleTicket {
    public static void main(String[] args) {
        Ticket ticket = new Ticket();

        new Thread(()->{for (int i = 1; i <40 ; i++) {ticket.sale();}},"A").start();
        new Thread(()->{for (int i = 1; i <40 ; i++) {ticket.sale();}},"B").start();
        new Thread(()->{for (int i = 1; i <40 ; i++) {ticket.sale();}},"C").start();

    }
}
