package com.xxf.cardview.test;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description: TODO @XGode
 * @Author: XGod
 * @CreateDate: 2020/11/28 16:18
 */
public class ThreadTest {

    public static class Node {
        public int value;
        public Node next;

        public Node(int value, Node next) {
            this.value = value;
            this.next = next;
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value=" + value +
                    ", next=" + next +
                    '}';
        }
    }

    public static void test2() {
        try {
            String s = "abba";
            for (int i = 0; i < ((s.length() - 1) / 2); i++) {
                if (!TextUtils.equals(s.substring(i, i + 1), s.substring(s.length() - i - 1, s.length() - i))) {
                    System.out.println("===========>不是回文");
                    return;
                }
            }
            System.out.println("===============>是回文");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public static void test() {
        /**
         * 4. Q：（手写算法）用三个线程，顺序打印字母A-Z，输出结果是1A、2B、3C、1D 2E...
         */
        new Thread(runnable, "0").start();
        new Thread(runnable, "1").start();
        new Thread(runnable, "2").start();

        Node head = new Node(1, new Node(2, new Node(3, new Node(4, null))));
        Node reverse = reverse(head);
        System.out.println("===============>reverse:" + reverse);

        test2();
        System.out.println("============>find index:" + findBy2());
        test3();

    }

    static class Product {
        List<Integer> list = new ArrayList<>();

        public synchronized void get() {
            if (list.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Integer remove = list.remove(0);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=================>消费:" + Thread.currentThread().getName() + " " + remove);
            notify();
        }

        public synchronized void put() {
            if (list.size() >= 10) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            int i = new Random().nextInt();
            list.add(i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("=================>生产:" + Thread.currentThread().getName() + " " + i);
            notify();
        }
    }

    public static void test3() {
        final Product product = new Product();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        for (int i = 0; i < 2; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        product.put();
                    }
                }
            });
        }

        for (int i = 0; i < 5; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        product.get();
                    }
                }
            });
        }

    }

    public static int findBy2() {
        int[] arr = new int[]{1, 3, 5, 9, 19, 34};
        int target = 199;
        int low = 0;
        int high = arr.length - 1;
        while (low <= high) {
            int middle = (low + high) / 2;
            if (target > arr[middle]) {
                low = middle + 1;
            } else if (target < arr[middle]) {
                high = middle - 1;
            } else {
                return middle;
            }
        }
        return -1;
    }

    /**
     * 1,2,3,4,5
     * <p>
     * 2,1,3,4,5
     * <p>
     * 3,2,1,4,5
     * <p>
     * <p>
     * 4,3,2,1,5
     * 5,4,3,2,1
     *
     * @param head
     * @return
     */
    private static Node reverse(Node head) {
        Node newHead = null;
        while (head != null) {
            Node tem = head.next;
            head.next = newHead;
            newHead = head;
            head = tem;
        }
        return newHead;
    }


    static Runnable runnable = new Runnable() {
        int charIndex = 0;
        char firstChar = 'a';

        @Override
        public void run() {
            synchronized (this) {
                while (charIndex < 26) {
                    long id = Long.parseLong(Thread.currentThread().getName());
                    if (charIndex % 3 == id) {
                        System.out.println("================>" + (id + 1) + "" + ((char) (charIndex + firstChar)));
                        charIndex++;
                        notifyAll();
                    } else {
                        try {
                            wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    };
}
