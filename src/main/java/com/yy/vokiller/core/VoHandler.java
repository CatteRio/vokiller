package com.yy.vokiller.core;

import com.yy.vokiller.annotation.SelectVO;
import com.yy.vokiller.exception.StatusException;
import com.yy.vokiller.parser.Analyzer;
import com.yy.vokiller.parser.Structure;
import com.yy.vokiller.parser.StructureGenerator;
import com.yy.vokiller.parser.Token;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 10:59
 */
public class VoHandler {
    private Map<Method, Executor> cachedMap;

    public VoHandler() {
        this.cachedMap = new ConcurrentHashMap<>(16);
    }

    public Object invoke(Method method, Object[] args) throws StatusException {
        if (cachedMap.containsKey(method)) {
            return cachedMap.get(method).execute(args);
        }
        Class<?> returnType = method.getReturnType();
        List<Token> tokenList = null;
        if (returnType == Object.class) {
            SelectVO selectVo = method.getDeclaredAnnotation(SelectVO.class);
            String vql = selectVo.vql();
            Analyzer analyzer = new Analyzer(vql);
            tokenList = analyzer.analyze();
        }
        Structure structure = StructureGenerator.generate(tokenList, returnType, method, args);
        Executor executor = new Executor(structure, method);
        cachedMap.put(method, executor);
        return executor.execute(args);
    }

}
