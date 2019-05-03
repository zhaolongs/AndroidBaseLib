package com.studyyoun.androidbaselibrary.utils;


import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by androidlongs on 17/3/12.
 * 站在顶峰，看世界
 * 落在谷底，思人生
 */

public class ExecutorUtils {

    private ExecutorService mExecutorService;

    private ExecutorUtils() {
        createExecutorFunction();


    }

    private void createExecutorFunction() {
        if (mExecutorService == null) {
            //cpu 核数
            int cpuCount = Runtime.getRuntime().availableProcessors();
            //核心线程数
            int corePoolSize = cpuCount + 1;
            //最大线程数
            int maxCorePoolSize = cpuCount * 2 + 1;
            //创建一个线程数固定大小为10的线程池
            mExecutorService = Executors.newFixedThreadPool(maxCorePoolSize);
        }
    }

    private static class SingleExecutor {
        private static ExecutorUtils sExecutorFunction = new ExecutorUtils();
    }

    public static ExecutorUtils getInstance() {
        return SingleExecutor.sExecutorFunction;
    }


    public void addRunnableTask(Runnable runnable) {
        createExecutorFunction();
        //执行一个任务  该任务是 new Runnable() 对象
        //使用这种方式没有办法获取执行 Runnable 之后的结果，如果你希望获取运行之后的返回值，就必须使用 接收 Callable 参数的 execute() 方法
        //当然可以使用接口回调
        mExecutorService.execute(runnable);
    }

    public Future addFutureTask(Runnable runnable) {
        createExecutorFunction();
        //执行一个任务  该任务是 new Runnable() 对象
        Future future = mExecutorService.submit(runnable);
        return future;

    }

    public Future addFutureTask(Callable<String> callable) {
        createExecutorFunction();
        //执行一个任务  该任务是 new Callable() 对象
        /*
            *方法 submit(Callable) 和方法 submit(Runnable) 比较类似，但是区别则在于它们接收不同的参数类型。
            *Callable 的实例与 Runnable 的实例很类似，但是 Callable 的 call() 方法可以返回壹個结果。方法 Runnable.run() 则不能返回结果。
            *Callable 的返回值可以从方法 submit(Callable) 返回的 Future 对象中获取。如下是一个 ExecutorService Callable 的例子：
         */
        Future future = mExecutorService.submit(callable);
        return future;

    }

    public String addCallbleListTaskToString(List<Callable<String>> list) {
        createExecutorFunction();
        /**
         * 方法 invokeAny() 接收一个包含 Callable 对象的集合作为参数。
         * 调用该方法不会返回 Future 对象，而是返回集合中某一个 Callable 对象的结果，而且无法保证调用之后返回的结果是哪一个 Callable，
         * 只知道它是这些 Callable 中一个执行结束的 Callable 对象。如果一个任务运行完毕或者抛出异常，方法会取消其它的 Callable 的执行
         */
        try {
            return mExecutorService.invokeAny(list);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (ExecutionException e1) {
            e1.printStackTrace();
        }
        return null;
    }

    public List<Future<String>> addCallbleListTaskToFuture(List<Callable<String>> list) {
        createExecutorFunction();
        /**
         * 方法 invokeAny() 接收一个包含 Callable 对象的集合作为参数。
         * 调用该方法不会返回 Future 对象，而是返回集合中某一个 Callable 对象的结果，而且无法保证调用之后返回的结果是哪一个 Callable，
         * 只知道它是这些 Callable 中一个执行结束的 Callable 对象。如果一个任务运行完毕或者抛出异常，方法会取消其它的 Callable 的执行
         * executorService.invokeAll( list ) 是返回值。 但是必须是所有的 Callable对象执行完了，才会返回，返回值是一个list, 顺序和 List<Callable>一样
         * 。在执行的过程中，如果任何一个Callable发生异常，程序会崩溃，没有返回值
         */
        List<Future<String>> result = null;
        try {
            result = mExecutorService.invokeAll(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public void close() {
        if (mExecutorService != null) {
            if (!mExecutorService.isShutdown()) {
                mExecutorService.shutdownNow();
                mExecutorService = null;
            }
        }
    }

}
