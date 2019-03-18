package com.yy.vokiller.core;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.paser.Analyzer;
import com.yy.vokiller.paser.Structure;
import com.yy.vokiller.paser.StructureGenerator;
import com.yy.vokiller.paser.Token;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 10:59
 */
public class VOHandler {
    private Map<Method, Executor> cachedMap;
    public VOHandler() {
        this.cachedMap = new ConcurrentHashMap<>(16);
    }
    public Object invoke(Method method, Object[] args) {
        if (cachedMap.containsKey(method)) {
            return cachedMap.get(method).execute(args);
        }
        Class<?> returnType = method.getReturnType();
        List<Token> tokenList = null;
        if (returnType == Object.class) {
            SelectVO selectVO = method.getDeclaredAnnotation(SelectVO.class);
            String vql = selectVO.vql();
            Analyzer analyzer = new Analyzer(vql);
            tokenList = analyzer.analyze();
        }
        Structure structure = StructureGenerator.generate(tokenList, returnType, args);
        Executor executor = new Executor(structure);
        cachedMap.put(method, executor);
        return executor.execute(args);
    }

}
