package com.yy.vokiller.paser;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuanyang(417168602 @ qq.com)
 * @date 2019/3/18 8:47
 */
public class Analyzer {

    private String content;
    private List<Token> tokenList;
    private char[] contentArray;
    private Integer position;
    private boolean matchingNextSeparator;

    /**
     * 初始化参数
     *
     * @param content
     */
    public Analyzer(String content) {
        this.content = content;
        this.contentArray = this.content.toCharArray();
        this.position = 0;
    }

    public List<Token> analyze() {
        List<Token> tokenList = new ArrayList<>(16);
        Token token = null;
        do {
            token = this.getNextToken();
            tokenList.add(token);
        } while (token != null);
        return tokenList;
    }

    private Token getNextToken() {
        if (this.isFinish()) {
            return null;
        }
        Token token = new Token();
        String fieldName = null;
        String fieldValue = null;
        String subField = null;
        boolean stopTag = false;
        boolean equalsTag = false;
        StringBuilder wordBuilder = new StringBuilder();
        StringBuilder tmpBuilder = new StringBuilder();
        while (!stopTag) {
            Character letter = getNextLetter();
            if (letter == null) {
                if (equalsTag) {
                    fieldValue = tmpBuilder.toString();
                    tmpBuilder.setLength(0);
                } else {
                    fieldName = tmpBuilder.toString();
                    tmpBuilder.setLength(0);
                }
                stopTag = true;
                break;
            }
            switch (letter) {
                case '=':
                    //如果在括号里面则忽略，只关心括号外的结构
                    if (this.matchingNextSeparator) {
                        tmpBuilder.append(letter);
                    }
                    //如果是在括号外的等号则认为当前token是有指定赋值的
                    //将指定属性tag标记为true，并且将左边的内容复制为name
                    else {
                        //判断一种情况就是user(sex=sex)=user1
                        //这种情况下fieldName已经被提取
                        //在前一步提取subField的时候已经将tmpBuilder清空
                        //因此会长度为0无需在做处理
                        if (tmpBuilder.length() != 0) {
                            fieldName = tmpBuilder.toString();
                            tmpBuilder.setLength(0);
                        }
                        equalsTag = true;

                    }
                    wordBuilder.append(letter);
                    break;
                case '(':
                    //如果不在寻找右括号，则赋值给filedName
                    if (!this.matchingNextSeparator) {
                        fieldName = tmpBuilder.toString();
                        tmpBuilder.setLength(0);
                    }
                    this.matchingNextSeparator = true;
                    wordBuilder.append(letter);
                    break;
                case ')':
                    //遇到右括号就把内容赋值给subField
                    subField = tmpBuilder.toString();
                    tmpBuilder.setLength(0);
                    this.matchingNextSeparator = false;
                    wordBuilder.append(letter);
                    break;
                case ',':
                    //如果不是在寻找右括号则认为一个token结束
                    if (!this.matchingNextSeparator) {
                        //如果前面有遇到括号外的等号则认为有指定value
                        if (equalsTag) {
                            fieldValue = tmpBuilder.toString();
                            tmpBuilder.setLength(0);
                        } else {
                            fieldName = tmpBuilder.toString();
                            tmpBuilder.setLength(0);
                        }
                        stopTag = true;
                    }
                    //尚未结束，在括号内的
                    else {
                        tmpBuilder.append(letter);
                        wordBuilder.append(letter);
                    }
                    break;
                default:
                    tmpBuilder.append(letter);
                    wordBuilder.append(letter);
                    break;
            }
        }

        String original = wordBuilder.toString();
        token.setOriginal(original == null ? null : original.trim());
        token.setFieldName(fieldName == null ? null : fieldName.trim());
        token.setFieldValue(fieldValue == null ? null : fieldValue.trim());
        token.setOriginalSubField(subField == null ? null : subField.trim());

        if (token.getOriginalSubField() != null) {
            Analyzer analyzer = new Analyzer(token.getOriginalSubField());
            token.setSubFields(analyzer.analyze());
        }
//        System.out.println(token);
        return token;
    }

    private Character getNextLetter() {
        if (position < this.contentArray.length) {
            return this.contentArray[position++];
        }
        return null;
    }

    private Boolean isFinish() {
        return !(position < this.contentArray.length);
    }


    public static void main(String[] args) {
        String s = "user ( sex=男,age , permissionList =  permission)=9,  role ,role=18,  height = 18,weight";
        Analyzer analyzer = new Analyzer(s);
        List<Token> tokenList = analyzer.analyze();
        System.out.println(tokenList);

    }
}
