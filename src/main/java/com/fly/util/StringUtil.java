package com.fly.util;


import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.druid.sql.visitor.functions.Char;

public class StringUtil {
    public static String ATTACH_FILE_SEPARATOR = "▍";
    private static final String[] specialStr = new String[]{"$", "&", "[", "\'", "("};

    public static boolean isSimple(Class<?> clazz) {
        boolean simple = false;
        if (clazz.isPrimitive() || String.class.equals(clazz) || Integer.class.equals(clazz) || Date.class.equals(clazz)
                || Double.class.equals(clazz) || Float.class.equals(clazz) || Long.class.equals(clazz)
                || Short.class.equals(clazz) || Boolean.class.equals(clazz) || Byte.class.equals(clazz)
                || Char.class.equals(clazz)) {
            simple = true;
        }

        return simple;
    }

    public static String[] removeDup(String[] myData) {
        if (myData.length > 0) {
            Arrays.sort(myData);
            int size = 1;

            for (int myTempData = 1; myTempData < myData.length; ++myTempData) {
                if (!myData[myTempData].equals(myData[myTempData - 1])) {
                    ++size;
                }
            }

            String[] arg4 = new String[size];
            byte j = 0;
            int arg5 = j + 1;
            arg4[j] = myData[0];

            for (int i = 1; i < myData.length; ++i) {
                if (!myData[i].equals(myData[i - 1])) {
                    arg4[arg5++] = myData[i];
                }
            }

            return arg4;
        } else {
            return myData;
        }
    }

    public static String[] decrease(String[] include, String[] except) {
        ArrayList AL_IncludeRaw = change2ArrayList(include);
        ArrayList AL_Include = change2ArrayList(include);
        ArrayList AL_Except = change2ArrayList(except);
        Iterator arg4 = AL_IncludeRaw.iterator();

        while (arg4.hasNext()) {
            String Item_Include = (String) arg4.next();
            if (AL_Except.contains(Item_Include)) {
                AL_Include.remove(Item_Include);
            }
        }

        return (String[]) AL_Include.toArray(new String[0]);
    }

    public static ArrayList<String> change2ArrayList(String[] src) {
        ArrayList des = new ArrayList();

        for (int i = 0; i < src.length; ++i) {
            des.add(src[i]);
        }

        return des;
    }

    public static String changeArrayToString(String[] transactors) {
        String strTransactors = "";
        String[] arg1 = transactors;
        int arg2 = transactors.length;

        for (int arg3 = 0; arg3 < arg2; ++arg3) {
            String transactor = arg1[arg3];
            if (transactor != "") {
                strTransactors = strTransactors + transactor + ";";
            }
        }

        return strTransactors.equals("") ? strTransactors : strTransactors.trim();
    }

    public static String convertUrlWithoutApplicationPath(String RawUrl, String ParamList) {
        if (RawUrl != null) {
            String Params = ParamList.trim();
            return Params != null && !Params.equals("")
                    ? (RawUrl.indexOf(63) > 0 ? RawUrl + "&" + Params : RawUrl + "?" + Params)
                    : RawUrl;
        } else {
            return "";
        }
    }

    public static String convertUrl(String RawUrl, String ParamList, String path) {
        if (RawUrl != null) {
            String Params = ParamList.trim();
            return Params != null && !Params.equals("")
                    ? path + (RawUrl.indexOf(63) > 0 ? RawUrl + "&" + Params : RawUrl + "?" + Params)
                    : path + RawUrl;
        } else {
            return "";
        }
    }

    public static String getFixLengthString(String instr, int len) {
        return instr == null ? "" : (instr.length() > len ? instr.substring(0, len) : instr);
    }

    public static String getNotNullString(Object instr) {
        return instr == null ? "" : instr.toString();
    }

    public static String trimPrefix(String str, String prefix) {
        return str.startsWith(prefix) ? str.substring(1) : str;
    }

    private static String splitString(int num, int len, String sMsg) {
        StringBuffer sbf = new StringBuffer();
        StringBuffer sbf1 = new StringBuffer();
        sbf.append("");
        String temp = "";
        int startNum = 0;
        String s = sMsg.replaceAll("[^\\x00-\\xff]", "**");
        int strLen = s.length();

        for (int i = 0; i < num; ++i) {
            int lenc = 0;
            if (i != 0) {
                lenc = sbf1.toString().replaceAll("[^\\x00-\\xff]", "**").length();
            }

            if (strLen > startNum + len) {
                temp = substring2(sMsg, lenc, len, false);
                System.out.println(temp + "##" + temp.replaceAll("[^\\x00-\\xff]", "**").length());
                startNum += len;
            } else {
                temp = substring2(sMsg, lenc, strLen - lenc, true);
                System.out.println(temp + "##" + temp.replaceAll("[^\\x00-\\xff]", "**").length());
            }

            sbf1.append(temp);
            sbf.append(temp).append("ξ");
        }

        return sbf.toString();
    }

    private static String substring2(String str, int srcPos, int specialCharsLength, boolean islast) {
        if (str != null && !"".equals(str) && specialCharsLength >= 1) {
            if (srcPos < 0) {
                srcPos = 0;
            }

            if (specialCharsLength <= 0) {
                return "";
            } else {
                char[] chars = str.toCharArray();
                if (srcPos > chars.length) {
                    ;
                }

                int charsLength = getCharsLength(chars, specialCharsLength);
                System.out.println("charsLength" + charsLength);
                int aa = getCharsLength(chars, srcPos);
                if (islast) {
                    charsLength = chars.length - aa;
                }

                return new String(chars, aa, charsLength);
            }
        } else {
            return "";
        }
    }

    public static String[] getMessages(String str, int len) {
        int msgLen = str.trim().replaceAll(" ", "").replaceAll("[^\\x00-\\xff]", "**").length();
        String finalsMsg = "";
        if (msgLen >= len) {
            String tempArray = str.replaceAll("[^\\x00-\\xff]", "**");
            double a = 0.0D;
            a = Double.parseDouble(tempArray.length() + "") / Double.parseDouble(len + "");
            int a1 = tempArray.length() / len;
            int xm = Integer.valueOf(Math.round((a - (double) a1) * 100.0D) + "").intValue();
            int len1 = a1;
            if (xm > 0) {
                len1 = a1 + 1;
            }

            finalsMsg = splitString(len1, len, str);
        } else {
            finalsMsg = str;
        }

        String[] tempArray1 = finalsMsg.split("ξ");
        return tempArray1;
    }

    public static boolean isLetter(char c) {
        short k = 128;
        return c / k == 0;
    }

    public static int length(String s) {
        if (s == null) {
            return 0;
        } else {
            char[] c = s.toCharArray();
            int len = 0;

            for (int i = 0; i < c.length; ++i) {
                ++len;
                if (!isLetter(c[i])) {
                    ++len;
                }
            }

            return len;
        }
    }

    public static String substring(String origin, int start, int len) {
        if (origin != null && !origin.equals("") && len >= 1) {
            byte[] strByte = new byte[len];
            if (start + len > length(origin)) {
                len = length(origin) - start;
            }

            try {
                System.arraycopy(origin.getBytes("UTF-8"), start, strByte, 0, len);
                int e = 0;

                for (int i = 0; i < len; ++i) {
                    byte value = strByte[i];
                    if (value < 0) {
                        ++e;
                    }
                }

                if (e % 2 != 0) {
                    int arg9999;
                    if (len == 1) {
                        ++len;
                        arg9999 = len;
                    } else {
                        --len;
                        arg9999 = len;
                    }

                    len = arg9999;
                }

                return new String(strByte, 0, len, "UTF-8");
            } catch (UnsupportedEncodingException arg6) {
                throw new RuntimeException(arg6);
            }
        } else {
            return "";
        }
    }

    public static String substring2(String str, int srcPos, int specialCharsLength) {
        if (str != null && !"".equals(str) && specialCharsLength >= 1) {
            if (srcPos < 0) {
                srcPos = 0;
            }

            if (specialCharsLength <= 0) {
                return "";
            } else {
                char[] chars = str.toCharArray();
                if (srcPos > chars.length) {
                    ;
                }

                int charsLength = getCharsLength(chars, specialCharsLength);
                int aa = getCharsLength(chars, srcPos);
                if (charsLength + aa > chars.length) {
                    charsLength = chars.length - aa;
                }

                return new String(chars, aa, charsLength);
            }
        } else {
            return "";
        }
    }

    private static int getCharsLength(char[] chars, int specialCharsLength) {
        int count = 0;
        int normalCharsLength = 0;

        for (int i = 0; i < chars.length; ++i) {
            int specialCharLength = getSpecialCharLength(chars[i]);
            if (count > specialCharsLength - specialCharLength) {
                break;
            }

            count += specialCharLength;
            ++normalCharsLength;
        }

        return normalCharsLength;
    }

    private static int getSpecialCharLength(char c) {
        return isLetter(c) ? 1 : 2;
    }

    public static String middle(String input, int index, int count) {
        count = count > input.length() - index + 1 ? input.length() - index + 1 : count;
        return input.substring(index - 1, index + count - 1);
    }

    public static String changeSingleDigits2Double(String inDigits) {
        int i = 0;

        try {
            i = Integer.parseInt(inDigits);
        } catch (Exception arg2) {
            arg2.printStackTrace();
        }

        return i >= 10 ? "" + i : "0" + i;
    }

    public static boolean isBlank(Object string) {
        boolean blank = false;
        if (string == null) {
            blank = true;
        } else if (string instanceof String) {
            blank = string.toString().trim().length() == 0 || string.toString().equalsIgnoreCase("null");
        }

        return blank;
    }

    public static boolean isNotBlank(Object string) {
        return !isBlank(string);
    }

    public static String getFileNameFromPath(String fileFullPath) {
        int last1 = fileFullPath.lastIndexOf("\\");
        int last2 = fileFullPath.lastIndexOf("/");
        if (last1 < last2) {
            last1 = last2;
        }

        if (last1 != -1) {
            fileFullPath = fileFullPath.substring(last1 + 1, fileFullPath.length());
        }

        return fileFullPath;
    }

    public static String replaceSql(String sql) {
        return sql == null ? "" : sql.replace("\'", "\'\'").trim();
    }

    public static String formatSqlStringParameter(String instr) {
        String rntstr = "";
        if (instr == null) {
            return rntstr;
        } else {
            rntstr = instr.trim().replace("%", "");
            return rntstr;
        }
    }

    public static String getSearchPattern(String param) {
        return param == null ? "%" : '%' + param.replace('*', '%') + '%';
    }

    public static List<String[]> getMatchedFileInfo(String fileContent, String keyword) {
        if (fileContent != null && keyword != null) {
            keyword = keyword.trim();
            String regex_string = "<file\\s*(filename=[^" + ATTACH_FILE_SEPARATOR + "]+)\\s*(fileguid=[^"
                    + ATTACH_FILE_SEPARATOR + "]+)\\s*>[^" + ATTACH_FILE_SEPARATOR + "]*" + keyword + "[^"
                    + ATTACH_FILE_SEPARATOR + "]*" + ATTACH_FILE_SEPARATOR + "";
            Matcher match = Pattern.compile(regex_string).matcher(fileContent);
            ArrayList list = new ArrayList();

            while (match.find()) {
                String[] str = new String[2];
                String s = match.group(1);
                str[0] = s.substring(s.indexOf("=") + 1).replace("\"", "");
                s = match.group(2);
                str[1] = s.substring(s.indexOf("=") + 1).replace("\"", "");
                list.add(str);
            }

            return list;
        } else {
            return null;
        }
    }

    public static String toChinese(String strvalue) {
        try {
            if (strvalue == null) {
                return null;
            } else {
                strvalue = new String(strvalue.getBytes("ISO8859_1"), "GBK");
                return strvalue;
            }
        } catch (Exception arg1) {
            return null;
        }
    }

    public static String formatLinkUrl(String strUrl) {
        if (strUrl == null) {
            return "";
        } else {
            strUrl = strUrl.replace("\\", "/");
            return !strUrl.startsWith("/") && !strUrl.startsWith("http:") && !strUrl.startsWith("\\\\")
                    && !strUrl.startsWith("://") ? "../" + strUrl : strUrl;
        }
    }

    public static String getTypeSettingString(String in, int size) {
        String out = in;
        if (size > 6) {
            char[] names = in.toCharArray();
            StringBuilder sb = new StringBuilder();
            char[] arg4 = names;
            int arg5 = names.length;

            for (int arg6 = 0; arg6 < arg5; ++arg6) {
                char item = arg4[arg6];
                sb.append(item);
                sb.append("</br>");
            }

            out = sb.toString();
            sb = null;
        }

        return out;
    }

    public static String getParameterByNameFromUrl(String url, String name) {
        String parames = url.substring(url.indexOf(name + '=') + 1 + name.length(), url.length());
        if (parames.indexOf(38) != -1) {
            String[] paramvalue = parames.split("&");
            return paramvalue[0];
        } else {
            return parames;
        }
    }

    public static List<String> parseStrByTag(String startTag, String endTag, String content, boolean withTag) {
        ArrayList result = new ArrayList();
        Pattern p1 = Pattern.compile(startTag);

        int start;
        int end;
        for (Matcher m1 = p1.matcher(content); m1.find(); result.add(content.substring(start, end))) {
            start = m1.end();
            if (withTag) {
                start -= startTag.length();
            }

            end = content.indexOf(endTag, start);
            if (withTag) {
                end += endTag.length();
            }
        }

        return result;
    }

    public static String firstSmallStr(String str, int length) {
        String result = str.substring(length, str.length());
        result = result.substring(0, 1).toLowerCase() + result.substring(1);
        return result;
    }

    public static String firstCharToLowerCase(String str) {
        Character firstChar = Character.valueOf(str.charAt(0));
        String tail = str.substring(1);
        str = Character.toLowerCase(firstChar.charValue()) + tail;
        return str;
    }

    public static String firstCharToUpperCase(String str) {
        Character firstChar = Character.valueOf(str.charAt(0));
        String tail = str.substring(1);
        str = Character.toUpperCase(firstChar.charValue()) + tail;
        return str;
    }

    public static String join(String[] ss) {
        return join(ss, ",");
    }

    public static <T> String join(List<T> list) {
        return join(list, ",");
    }

    public static String join(String[] ss, String subStr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (int size = ss.length; i < size; ++i) {
            sb.append(ss[i]);
            if (i != size - 1) {
                sb.append(subStr);
            }
        }

        return sb.toString();
    }

    public static <T> String join(List<T> list, String subStr) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (int size = list.size(); i < size; ++i) {
            sb.append(list.get(i).toString());
            if (isNotBlank(subStr) && i != size - 1) {
                sb.append(subStr);
            }
        }

        return sb.toString();
    }

    public static String joinSql(String[] ss) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (int size = ss.length; i < size; ++i) {
            sb.append("\'").append(ss[i]).append("\'");
            if (i != size - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static <T> String joinSql(List<T> list) {
        StringBuilder sb = new StringBuilder();
        int i = 0;

        for (int size = list.size(); i < size; ++i) {
            sb.append("\'").append(list.get(i).toString()).append("\'");
            if (i != size - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }

    public static String firstUp(String name) {
        return name.substring(0, 1).toUpperCase() + name.substring(1);
    }

    public static int countSameStr(String strSource, String key) {
        int count = 0;
        Pattern p1 = Pattern.compile(key);

        for (Matcher m1 = p1.matcher(strSource); m1.find(); ++count) {
            ;
        }

        return count;
    }

    public static String[] removeNoNeed(String old, String[] args) {
        ArrayList fit = new ArrayList();
        int length = args.length;

        for (int i = 0; i < length; ++i) {
            if (old.startsWith(args[i])) {
                fit.add(args[i]);
                old = old.replace(args[i], "");
            } else {
                boolean less = true;

                int j;
                for (j = i + 1; j < length; ++j) {
                    if (old.startsWith(args[j])) {
                        fit.add(args[j]);
                        old = old.replace(args[j], "");
                        less = false;
                        i = j;
                        break;
                    }
                }

                if (less) {
                    for (j = 0; j < old.length(); ++j) {
                        old = old.substring(1, old.length());
                        if (old.startsWith(args[i])) {
                            fit.add(args[i]);
                            old = old.replace(args[i], "");
                            break;
                        }
                    }
                }
            }
        }

        return (String[]) fit.toArray(new String[0]);
    }

    public static String subStrSql(String start, String to, String tag, String fileContent, List<String> result,
            String uidefine, boolean recure) {
        if (fileContent != null && fileContent.trim().length() > 0) {
            int index = fileContent.indexOf(start);
            if (index != -1) {
                byte extra = 0;
                if (tag.equalsIgnoreCase(start)) {
                    extra = 1;
                }

                int endIndex = getEndIndex(to, tag, fileContent, index + start.length(), extra);
                if (endIndex != -1) {
                    result.add(fileContent.substring(index, endIndex + to.length()));
                    String begin = fileContent.substring(0, index);
                    String end = fileContent.substring(endIndex + to.length());
                    if (isNotBlank(uidefine)) {
                        begin = begin + uidefine;
                    }

                    fileContent = begin + end;
                    if (recure) {
                        fileContent = subStrSql(start, to, tag, fileContent, result, uidefine, recure);
                    }
                }
            }
        }

        return fileContent;
    }

    public static int getEndIndex(String to, String tag, String fileContent, int start, int totalBeforeCount) {
        int endIndex = fileContent.indexOf(to, start);
        if (endIndex != -1) {
            int beforeCount = getIndex(fileContent, tag, start, endIndex).size();
            --beforeCount;
            totalBeforeCount += beforeCount;
            if (totalBeforeCount > 0) {
                endIndex = getEndIndex(to, tag, fileContent, endIndex + to.length(), totalBeforeCount);
            }
        }

        return endIndex;
    }

    public static List<Integer> getIndex(String fileContent, String tag, int start, int end) {
        ArrayList result = new ArrayList();
        int index = fileContent.indexOf(tag, start);
        if (index != -1 && index < end) {
            result.add(Integer.valueOf(index));
            result.addAll(getIndex(fileContent, tag, index + tag.length(), end));
        }

        return result;
    }

    public static String subStrSql(String startBegin, String startMiddle, String startEnd, int from, String to,
            boolean keep, String fileContent, List<String> result, String uidefine) {
        if (fileContent != null && fileContent.trim().length() > 0 && fileContent.indexOf(startBegin) != -1) {
            int startIndex = -1;
            boolean startWidth = true;
            int endIndex = -1;
            int endWidth = -1;
            boolean index = true;
            if (isNotBlank(startMiddle)) {
                int arg20 = fileContent.indexOf(startMiddle);
                if (arg20 != -1) {
                    List toParams = getIndex(fileContent, startBegin, 0, arg20);
                    startIndex = ((Integer) toParams.get(toParams.size() - 1)).intValue();
                }
            } else {
                startIndex = fileContent.indexOf(startBegin);
            }

            int arg19;
            if (isBlank(startEnd)) {
                arg19 = startBegin.length();
            } else {
                int arg21 = fileContent.indexOf(startEnd, startIndex);
                arg19 = arg21 - startIndex + startEnd.length();
            }

            if (startIndex != -1) {
                String[] arg22 = to.split("@");
                String[] begin = arg22;
                int end = arg22.length;

                for (int arg16 = 0; arg16 < end; ++arg16) {
                    String item = begin[arg16];
                    int tempEndIndex = fileContent.indexOf(item, startIndex);
                    if (tempEndIndex != -1 && (endIndex == -1 || tempEndIndex < endIndex)) {
                        endIndex = tempEndIndex;
                        endWidth = item.length();
                    }
                }

                if (endIndex == -1) {
                    endIndex = fileContent.length();
                }

                if (!keep) {
                    result.add(fileContent.substring(startIndex + arg19, endIndex).trim());
                } else {
                    result.add(fileContent.substring(startIndex, endIndex + endWidth).trim());
                }

                String arg23 = fileContent.substring(0, startIndex);
                String arg24 = fileContent.substring(endIndex + endWidth);
                if (isNotBlank(uidefine)) {
                    arg23 = arg23 + uidefine;
                }

                fileContent = arg23 + arg24;
                fileContent = subStrSql(startBegin, startMiddle, startEnd, from, to, keep, fileContent, result,
                        uidefine);
            }
        }

        return fileContent;
    }

    public static String subStrSql(String start, int from, String to, String fileContent, boolean recure) {
        int index = fileContent.indexOf(start);
        if (index != -1) {
            int endIndex = fileContent.indexOf(to, index + start.length());
            if (endIndex == -1) {
                fileContent = "";
            } else {
                fileContent = fileContent.substring(0, index + from) + fileContent.substring(endIndex + to.length());
                if (recure) {
                    fileContent = subStrSql(start, from, to, fileContent, recure);
                }
            }
        }

        return fileContent;
    }

    public static String insertStr(String str, String index, int from, int indexTo, String toStr) {
        int start = str.indexOf(index, from);
        if (start != -1) {
            start += indexTo;
            str = str.substring(0, start) + toStr + str.substring(start);
        }

        return str;
    }

    private static String getSpecial(String str) {
        String result = null;
        String[] arg1 = specialStr;
        int arg2 = arg1.length;

        for (int arg3 = 0; arg3 < arg2; ++arg3) {
            String item = arg1[arg3];
            if (str.indexOf(item) != -1) {
                result = item;
            }
        }

        return result;
    }

    public static String filterSpecialStr(String str) {
        String sReturn = "";
        if (isNotBlank(str)) {
            String special = getSpecial(str);
            if (isNotBlank(special)) {
                while (str.length() > 0) {
                    if (str.indexOf(special, 0) > -1) {
                        sReturn = sReturn + str.subSequence(0, str.indexOf(special, 0));
                        sReturn = sReturn + "\\" + special;
                        str = str.substring(str.indexOf(special, 0) + 1, str.length());
                    } else {
                        sReturn = sReturn + str;
                        str = "";
                    }
                }
            } else {
                sReturn = str;
            }
        }

        return sReturn;
    }

    public static String getXMLAtt(String bs, String att) {
        String result = "";

        try {
            String e = "<" + att + ">";
            String tail = "</" + att + ">";
            result = bs.substring(bs.indexOf(e) + e.length(), bs.indexOf(tail));
            return result;
        } catch (Exception arg4) {
            arg4.printStackTrace();
            return "";
        }
    }

    public static String getXMLAttOut(String bs, String att) {
        try {
            String e = getXMLAtt(bs, att);
            return "<" + att + ">" + e + "</" + att + ">";
        } catch (Exception arg2) {
            arg2.printStackTrace();
            return "";
        }
    }

    public static int countChinese(String str) {
        int result = 0;
        if (isNotBlank(str)) {
            String E1 = "[一-龥]";

            for (int i = 0; i < str.length(); ++i) {
                String temp = String.valueOf(str.charAt(i));
                if (temp.matches(E1)) {
                    ++result;
                }
            }
        }

        return result;
    }

    public static int countEnglish(String str) {
        int result = 0;
        if (isNotBlank(str)) {
            String E1 = "[a-zA-Z]";

            for (int i = 0; i < str.length(); ++i) {
                String temp = String.valueOf(str.charAt(i));
                if (temp.matches(E1)) {
                    ++result;
                }
            }
        }

        return result;
    }

    public static int countNumber(String str) {
        int result = 0;
        if (isNotBlank(str)) {
            String E1 = "[0-9]";

            for (int i = 0; i < str.length(); ++i) {
                String temp = String.valueOf(str.charAt(i));
                if (temp.matches(E1)) {
                    ++result;
                }
            }
        }

        return result;
    }

    public static String replaceSpecialChar(String content) {
        return content == null
                ? ""
                : content.replace("\r\n", "").replace("\r", "").replace("\n", "").replace("\'", "&#39")
                        .replace("\\", "&#92").replace("\"", "&quot;");
    }

    public static boolean isGuidString(String guid) {
        boolean isValid = false;
        if (guid != null) {
            Pattern pattern = Pattern.compile(
                    "^(\\{){0,1}[0-9a-fA-F]{8}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{4}\\-[0-9a-fA-F]{12}(\\}){0,1}$");
            Matcher matcher = pattern.matcher(guid);
            if (matcher.matches()) {
                isValid = true;
            }
        }

        return isValid;
    }

   

    public static boolean isImg(String fileName) {
        boolean img = false;
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".jpg") || fileName.endsWith(".bmp") || fileName.endsWith(".png")
                || fileName.endsWith(".gif")) {
            img = true;
        }

        return img;
    }

    public static String toUtf8String(String s) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            if (c >= 0 && c <= 255) {
                sb.append(c);
            } else {
                byte[] b;
                try {
                    b = Character.toString(c).getBytes("utf-8");
                } catch (Exception arg6) {
                    System.out.println(arg6);
                    b = new byte[0];
                }

                for (int j = 0; j < b.length; ++j) {
                    int k = b[j];
                    if (k < 0) {
                        k += 256;
                    }

                    sb.append("%" + Integer.toHexString(k).toUpperCase());
                }
            }
        }

        return sb.toString();
    }

    public static String replaceWorkFlowSpecialChar(String content) {
        return content == null ? "" : content.replace("<", "&lt;").replace(">", "&gt;").replace("\'", "&apos;");
    }
}