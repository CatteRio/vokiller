package com.yy.vokiller.paser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/17 23:48
 */
public class TypeParser {

    private String vql;
    private String type;

    public TypeParser(String vql) {
        this.vql = vql;
    }


    public void parse() {
        char[] chars = this.vql.toCharArray();
        List<Token> tokens = new ArrayList<>(16);
        for (int i = 0; i < chars.length; i++) {
            Token token = new Token();
            char currentChar = chars[i];
            if (currentChar == '(') {

            }else if(currentChar == ','){

            }else if(currentChar == ' '){

            }else{

            }


        }
    }
}
