package com.fly.util;


import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class FileManagerUtil {
    private static long maxSmallAttachSize = 50L;

    public static InputStream getInput(InputStream in) {
        Object result = in;

        try {
            if ((long) in.available() < maxSmallAttachSize * 1024L * 1024L) {
                result = new ByteArrayInputStream(getContentFromInputStream(in));
            } else {
                File e = File.createTempFile(UUID.randomUUID().toString(), ".gridfs_or_attach");
                e.deleteOnExit();
                writeContentToFileByStream(e, in);
                result = new FileInputStream(e);
            }
        } catch (IOException arg2) {
            arg2.printStackTrace();
        }

        return (InputStream) result;
    }

    public static byte[] getContentFromSystem(String filePath) {
        byte[] content = null;
        File newfile = new File(filePath);
        if (newfile.exists()) {
            FileInputStream fis = null;

            try {
                fis = new FileInputStream(newfile);
                content = getContentFromInputStream(fis);
            } catch (FileNotFoundException arg4) {
                arg4.printStackTrace();
            }
        }

        return content;
    }

    public static String getContentFromProject(String fileName) {
        InputStream is = FileManagerUtil.class.getResourceAsStream("/" + fileName);
        String value = null;
        if (is != null) {
            byte[] content = getContentFromInputStream(is);

            try {
                value = new String(content, "utf-8");
            } catch (UnsupportedEncodingException arg4) {
                arg4.printStackTrace();
            }
        }

        return value;
    }

    public static byte[] getContentFromInputStream(InputStream fis) {
        byte[] content = null;
        ByteArrayOutputStream baos = null;

        try {
            byte[] e = new byte[4096];
            baos = new ByteArrayOutputStream();
            boolean ch = false;

            int ch1;
            while ((ch1 = fis.read(e)) != -1) {
                baos.write(e, 0, ch1);
            }

            content = baos.toByteArray();
        } catch (IOException arg12) {
            arg12.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (baos != null) {
                    baos.close();
                }
            } catch (IOException arg11) {
                arg11.printStackTrace();
            }

        }

        return content;
    }

    public static void writeContentToFile(String path, String fileName, String content) {
        try {
            writeContentToFile(path, fileName, content.getBytes("utf-8"));
        } catch (UnsupportedEncodingException arg3) {
            arg3.printStackTrace();
        }

    }

    public static String getContentFromSystemByReader(String fileName, String encode) {
        String value = null;
        if (isExist(fileName, false)) {
            byte[] content = getContentFromSystem(fileName);

            try {
                value = new String(content, encode);
            } catch (UnsupportedEncodingException arg4) {
                arg4.printStackTrace();
            }
        }

        return value;
    }

    public static String getContentFromSystemByReader(String fileName) {
        return getContentFromSystemByReader(fileName, "UTF-8");
    }

    public static String[] getContentFromSystemByLineNumber(String fileName, int lineNumber) {
        String[] lineOf = new String[lineNumber];
        if (isExist(fileName, false)) {
            String line = null;

            try {
                BufferedReader e = new BufferedReader(new FileReader(new File(fileName)));

                for (int count = 0; count < lineNumber && (line = e.readLine()) != null; ++count) {
                    lineOf[count] = line;
                }

                e.close();
            } catch (FileNotFoundException arg5) {
                arg5.printStackTrace();
            } catch (IOException arg6) {
                arg6.printStackTrace();
            }
        }

        return lineOf;
    }

    public static List<String> getContentFromSystemByLine(String fileName) {
        ArrayList lineOf = new ArrayList();
        if (isExist(fileName, false)) {
            String line = null;

            try {
                BufferedReader e = new BufferedReader(new FileReader(new File(fileName)));

                while ((line = e.readLine()) != null) {
                    lineOf.add(line);
                }

                e.close();
            } catch (FileNotFoundException arg3) {
                arg3.printStackTrace();
            } catch (IOException arg4) {
                arg4.printStackTrace();
            }
        }

        return lineOf;
    }

    public static String getContentFromReader(Reader reader) {
        BufferedReader br = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();

        try {
            for (String e = br.readLine(); e != null; e = br.readLine()) {
                sb.append(e);
            }
        } catch (Exception arg11) {
            arg11.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException arg10) {
                arg10.printStackTrace();
            }

        }

        return sb.toString();
    }

    public static BufferedWriter writeContentToFile(String fileName, String lineData, boolean end,
            BufferedWriter write) {
        try {
            if (write == null) {
                isExist(fileName, true);
                write = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "utf-8"));
            }

            write.newLine();
            if (StringUtil.isNotBlank(lineData)) {
                write.write(lineData);
            }

            if (end) {
                write.flush();
                write.close();
                write = null;
            }

            return write;
        } catch (Exception arg4) {
            throw new RuntimeException("写文件到指定文件中(一行行的填写)执行发生了异常  sorry -_-", arg4);
        }
    }

    public static void writeContentToFileByWriter(String fileContent, String fileName, String encode) {
        OutputStreamWriter writer = null;

        try {
            isExist(fileName, true);
            writer = new OutputStreamWriter(new FileOutputStream(new File(fileName)), encode);
            writer.write(fileContent);
            writer.flush();
        } catch (IOException arg11) {
            throw new RuntimeException("将内容写入文件(用字符流写)执行发生了异常  sorry -_-", arg11);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException arg10) {
                    arg10.printStackTrace();
                }
            }

        }

    }

    public static void writeContentToFileByWriter(String fileContent, String fileName) {
        writeContentToFileByWriter(fileContent, fileName, "UTF-8");
    }

    public static void writeContentToFile(String path, String fileName, byte[] byteContent) {
        File parentFile = new File(path);
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }

        File file = new File(path + fileName);
        FileOutputStream fos = null;

        try {
            fos = new FileOutputStream(file);
            int e = byteContent.length;

            for (int i = 0; i < e; ++i) {
                int start = i;
                i += 1024;
                int conLength = 1024;
                if (i >= e) {
                    conLength = e - (i - 1024);
                }

                byte[] con = new byte[conLength];

                for (int j = 0; j < conLength; ++j) {
                    con[j] = byteContent[start + j];
                }

                fos.write(con, 0, conLength);
                --i;
            }
        } catch (Exception arg18) {
            throw new RuntimeException("将输入流中的内容写入指定路径指定名字的文件中执行发生了异常  sorry -_-", arg18);
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException arg17) {
                    arg17.printStackTrace();
                }
            }

        }

    }

    public static void writeContentToFileByStream(String path, String fileName, InputStream in) {
        File parentFile = new File(path);
        if (!parentFile.exists()) {
            boolean file = parentFile.mkdirs();
            if (!file) {
                throw new RuntimeException("将输入流中的内容写入指定路径指定名字的文件中时创建父目录发生了异常  sorry -_-");
            }
        }

        File file1 = new File(path + fileName);
        writeContentToFileByStream(file1, in);
    }

    public static void writeContentToFileByStream(File file, InputStream in) {
        FileOutputStream fos = null;

        try {
            byte[] e = new byte[4096];
            fos = new FileOutputStream(file);
            boolean ch = false;

            int ch1;
            while ((ch1 = in.read(e)) != -1) {
                fos.write(e, 0, ch1);
            }
        } catch (Exception arg11) {
            throw new RuntimeException("将输入流中的内容写入指定路径指定名字的文件中执行发生了异常  sorry -_-", arg11);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (fos != null) {
                    fos.close();
                }
            } catch (IOException arg10) {
                arg10.printStackTrace();
            }

        }

    }

    public static boolean isExist(String fileName, boolean create) {
        File file = new File(fileName);
        boolean exist = file.exists();
        if (!exist && create) {
            file.mkdirs();
            if (fileName.indexOf(".") != -1) {
                file.delete();
            }
        }

        return exist;
    }

    public static boolean isExistFileDir(String filePath, boolean create) {
        File file = new File(filePath);
        boolean exist = file.exists();
        if (!exist && create) {
            file.mkdirs();
        }

        return exist;
    }

    public static byte[] copyFile(String actuallyInpath, String outPath, String outFileName, boolean isDel) {
        byte[] fileContent = null;
        FileInputStream fis = null;
        BufferedInputStream in = null;
        ByteArrayOutputStream out = null;
        FileOutputStream fos = null;
        File fi = null;
        boolean success = false;

        try {
            fi = new File(actuallyInpath);
            if (fi.exists()) {
                File e = new File(outPath);
                if (!e.exists()) {
                    e.mkdirs();
                }

                File write = new File(outPath + outFileName);
                if (write.exists()) {
                    write.delete();
                }

                fis = new FileInputStream(fi);
                in = new BufferedInputStream(fis);
                out = new ByteArrayOutputStream(1024);
                byte[] temp = new byte[1024];
                boolean size = false;

                int size1;
                while ((size1 = in.read(temp)) != -1) {
                    out.write(temp, 0, size1);
                }

                fileContent = out.toByteArray();
                File file = new File(outPath + outFileName);
                fos = new FileOutputStream(file);
                fos.write(fileContent, 0, fileContent.length);
                success = true;
            }
        } catch (Exception arg22) {
            throw new RuntimeException("复制文件执行发生了异常  sorry -_-", arg22);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }

                if (fis != null) {
                    fis.close();
                }

                if (fos != null) {
                    fos.close();
                }

                if (out != null) {
                    out.close();
                }

                if (success && isDel) {
                    fi.delete();
                }
            } catch (IOException arg21) {
                arg21.printStackTrace();
            }

        }

        return fileContent;
    }

   
    public static int getFileCount(String fileName) {
        if (fileName != null) {
            File file = new File(fileName);
            if (file.exists()) {
                return file.listFiles().length;
            }
        }

        return 0;
    }

    public static void additionalFile(String fileName, String content, int index) {
        try {
            RandomAccessFile e = new RandomAccessFile(fileName, "rw");
            e.seek((long) index);
            e.writeBytes(content);
            e.close();
        } catch (IOException arg3) {
            arg3.printStackTrace();
        }

    }

    public static void additionalFileToFileEnd(String fileName, String content, int index) {
        try {
            RandomAccessFile e = new RandomAccessFile(fileName, "rw");
            e.seek(e.length() - (long) index);
            e.write(content.getBytes("utf-8"));
            e.close();
        } catch (IOException arg3) {
            arg3.printStackTrace();
        }

    }

    public static void deleteFile(String path) {
        File myDelFile = new File(path);
        if (myDelFile.exists()) {
            if (myDelFile.isFile()) {
                myDelFile.delete();
            } else if (myDelFile.isDirectory()) {
                String[] list = myDelFile.list();
                String[] arg2 = list;
                int arg3 = list.length;

                for (int arg4 = 0; arg4 < arg3; ++arg4) {
                    String s = arg2[arg4];
                    String directory = path + "/" + s;
                    deleteFile(directory);
                }

                myDelFile.delete();
            }
        }

    }

    public static List<File> getFileListByDirectory(String directory, String fileType, List<String> excludeFileName) {
        ArrayList lstJavaFile = new ArrayList();
        listJavaFile(directory, lstJavaFile, fileType, excludeFileName);
        return lstJavaFile;
    }

    public static void copyFolder(String oldPath, String newPath) {
        try {
            (new File(newPath)).mkdirs();
            File e = new File(oldPath);
            String[] file = e.list();
            File temp = null;

            for (int i = 0; i < file.length; ++i) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" + temp.getName().toString());
                    byte[] b = new byte[5120];

                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }

                    output.flush();
                    output.close();
                    input.close();
                }

                if (temp.isDirectory()) {
                    copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
                }
            }
        } catch (Exception arg9) {
            arg9.printStackTrace();
        }

    }

    private static boolean checkExclude(String directory, List<String> excludeFileName) {
        if (excludeFileName != null) {
            Iterator arg1 = excludeFileName.iterator();

            while (arg1.hasNext()) {
                String item = (String) arg1.next();
                if (directory.endsWith(item)) {
                    return true;
                }
            }
        }

        return false;
    }

    private static void listJavaFile(String directory, List<File> lstJavaFile, String fileType,
            List<String> excludeFileName) {
        if (!checkExclude(directory, excludeFileName)) {
            File file = new File(directory);
            String fileName = "";
            if (!file.isDirectory()) {
                fileName = file.getName();
                if (StringUtil.isNotBlank(fileType)) {
                    if (fileName.endsWith(fileType)) {
                        lstJavaFile.add(file);
                    }
                } else {
                    lstJavaFile.add(file);
                }

                return;
            }

            if (file.isDirectory()) {
                File[] lstFile = file.listFiles();
                if (null != lstFile && 0 != lstFile.length) {
                    for (int i = 0; i < lstFile.length; ++i) {
                        file = lstFile[i];
                        if (file.isDirectory()) {
                            listJavaFile(file.getAbsolutePath(), lstJavaFile, fileType, excludeFileName);
                        } else {
                            fileName = file.getName();
                            if (StringUtil.isNotBlank(fileType)) {
                                if (fileName.endsWith(fileType)) {
                                    lstJavaFile.add(file);
                                }
                            } else {
                                lstJavaFile.add(file);
                            }
                        }
                    }
                }
            }
        }

    }

   

    public static void changeANSI2UTF8(String file) {
        BufferedReader buf = null;
        OutputStreamWriter pw = null;

        try {
            String e = null;
            String allstr = "";
            byte[] c = new byte[]{13, 10};
            String t = new String(c);

            for (buf = new BufferedReader(new InputStreamReader(new FileInputStream(file),
                    "GBK")); (e = buf.readLine()) != null; allstr = allstr + e + t) {
                ;
            }

            pw = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            pw.write(allstr);
        } catch (IOException arg14) {
            arg14.printStackTrace();
        } finally {
            try {
                if (buf != null) {
                    buf.close();
                }

                if (pw != null) {
                    pw.close();
                }
            } catch (IOException arg13) {
                arg13.printStackTrace();
            }

        }

    }

    public static void changeBOM2UTF8(String file) {
        String content = null;
        FileInputStream fis = null;
        ByteArrayOutputStream baos = null;

        try {
            fis = new FileInputStream(file);
            byte[] e = new byte[4096];
            baos = new ByteArrayOutputStream();
            boolean ch = false;
            boolean first = true;

            boolean bom;
            int arg23;
            for (bom = false; (arg23 = fis.read(e)) != -1; baos.write(e, 0, arg23)) {
                if (first) {
                    if (e[0] != -17 || e[1] != -69 || e[2] != -65) {
                        break;
                    }

                    bom = true;
                    byte[] bufferTemp = new byte[4093];
                    int i = 0;
                    byte[] arg9 = e;
                    int arg10 = e.length;

                    for (int arg11 = 0; arg11 < arg10; ++arg11) {
                        byte item = arg9[arg11];
                        if (i > 2) {
                            bufferTemp[i - 3] = item;
                        }

                        ++i;
                    }

                    e = bufferTemp;
                    arg23 -= 3;
                    first = false;
                }
            }

            if (bom) {
                content = new String(baos.toByteArray(), "utf-8");
            }
        } catch (IOException arg21) {
            arg21.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (baos != null) {
                    baos.close();
                }
            } catch (IOException arg20) {
                arg20.printStackTrace();
            }

        }

        if (StringUtil.isNotBlank(content)) {
            writeContentToFileByWriter(content, file);
        }

    }

   
}
