package com.fly.basic.test;

public class TestRegex
{
    public static void main(String args[]){
        /*String content = "I am noob " +
          "from runoob.com.";
   
        String pattern = ".*runoob.*";
   
        boolean isMatch = Pattern.matches(pattern, content);
        System.out.println("字符串中是否包含了 'runoob' 子字符串? " + isMatch);*/
        
       /* Pattern p = Pattern.compile("(\\d(\\d))\\2");
        Matcher matcher = p.matcher("322");
        System.out.println(matcher.matches());*/
        
       /* Pattern pattern = Pattern.compile("\\ba\\w*g\\b");
       // Matcher matcher = pattern.matcher("abcdab cccabcd aaacd aaacg");
        Matcher matcher = pattern.matcher("abcdabcccabcdaaacdaaacg");

        int index = 0;
        while (matcher.find()) {
            String res = matcher.group();
            System.out.println(index + ":" + res);
            index++;

        
        }*/
        
       /* String str = "Hello,World! in Java.or";  
        Pattern pattern = Pattern.compile("W(or)(ld!)");  
        Matcher matcher = pattern.matcher(str);  
        while(matcher.find()){  
         System.out.println("Group 0:"+matcher.group(0));//得到第0组——整个匹配  
         System.out.println("Group 1:"+matcher.group(1));//得到第一组匹配——与(or)匹配的  
         System.out.println("Group 2:"+matcher.group(2));//得到第二组匹配——与(ld!)匹配的，组也就是子表达式  
         System.out.println("Start 0:"+matcher.start(0)+" End 0:"+matcher.end(0));//总匹配的索引  
         System.out.println("Start 1:"+matcher.start(1)+" End 1:"+matcher.end(1));//第一组匹配的索引  
         System.out.println("Start 2:"+matcher.start(2)+" End 2:"+matcher.end(2));//第二组匹配的索引  
         System.out.println(str.substring(matcher.start(0),matcher.end(2)));//从总匹配开始索引到第1组匹配的结束索引之间子串——Wor   
        
         System.out.println(matcher.group());
        }*/
        
        /*String str="China12345America678922England342343434Mexica";
        System.out.println(str.replaceAll("\\d+", " "));
        String str1="China|||||America::::::England&&&&&&&Mexica";
        System.out.println(str1.replaceAll("(.)\\1+","2"));*/
       /* String sql = "select * from share_content where username=? 1";
        Pattern pattern = Pattern.compile("\\=\\s*\\?\\s*1");
        // Matcher matcher = pattern.matcher("abcdab cccabcd aaacd aaacg");
         Matcher matcher = pattern.matcher(sql);
        int index = 0;
        while (matcher.find()) {
            String res = matcher.group();
            System.out.println(index + ":" + res);
            index++;

        
        }
        System.out.println(sql.replaceAll("\\=\\s*\\?\\s*1", "='张三'"));*/
        
       
     }
}
