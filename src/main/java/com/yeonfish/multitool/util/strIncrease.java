package com.yeonfish.multitool.util;

import java.util.Arrays;

public class strIncrease {
    public static String strIncrease(String str, String[] strList) {
        int strListLen = strList.length;
        String result = "";
        int cutLen = 0;
        String strTemp = str;
        while(true) {
            if (strTemp.equals("")) {
                result = strList[0];
                for(int i=0;i<cutLen;i++){
                    result += strList[0];
                }
                break;
            }
            String lastChar = strTemp.substring(strTemp.length()-1);
            int pos = Arrays.asList(strList).indexOf(lastChar);
            if (lastChar.equals(strList[strListLen-1])) {
                cutLen++;
                strTemp = strTemp.substring(0, strTemp.length()-1);
            }else{
                result = strTemp.substring(0, strTemp.length()-1)+strList[pos+1];
                for(int i=0;i<cutLen;i++){
                    result += strList[0];
                }
                break;
            }
        }
        return result;
    }
}
