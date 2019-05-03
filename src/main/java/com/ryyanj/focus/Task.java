package com.ryyanj.focus;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.Set;
import java.util.concurrent.Callable;

public class Task implements Callable {


    private final Plan plan;
    private final Set<String> concurrentSet;

    public Task(Plan plan, Set<String> concurrentSet) {
        this.plan = plan;
        this.concurrentSet = concurrentSet;
    }


    @Override
    public String call() throws Exception {
        plan.execute();
        concurrentSet.remove(plan.filename);
        FileUtils.forceDelete(new File(PathFactory.get(PathEnum.HOME_SERVICE) + plan.filename));
        return "task completed";
    }
}