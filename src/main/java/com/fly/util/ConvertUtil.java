package com.fly.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.NClob;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConvertUtil {
    private static final String SPECIAL_MARK1 = "_convertutil_special1_";
    private static final String SPECIAL_MARK2 = "_convertutil_special2_";

    public static <T> T convertDataType(Class<T> ptype, Object val) {
        Object result = null;
        if (ptype == String.class) {
            if (StringUtil.isNotBlank(val)) {
                val = val.toString().trim();
            } else {
                val = "";
            }
        } else if (ptype != Boolean.class && ptype != Boolean.TYPE) {
            if (ptype != Integer.class && ptype != Integer.TYPE) {
                if (ptype == Date.class) {
                    if (val != null && (val instanceof Timestamp || !(val instanceof Date))) {
                        String val1 = val.toString().replace("T", " ");
                        val = EpointDateUtil.convertString2DateAuto(val1.toString());
                    }
                } else if (ptype != Double.class && ptype != Double.TYPE) {
                    if (ptype != Long.class && ptype != Long.TYPE) {
                        if (ptype != Float.class && ptype != Float.TYPE) {
                            if (ptype != Short.class && ptype != Short.TYPE) {
                                if (ptype == BigDecimal.class) {
                                    if (StringUtil.isNotBlank(val)) {
                                        if (!(val instanceof Integer) && !(val instanceof String)
                                                && !(val instanceof Double)) {
                                            if (!(val instanceof BigDecimal)) {
                                                val = null;
                                            }
                                        } else {
                                            val = new BigDecimal(String.valueOf(val));
                                        }
                                    }
                                } else if (ptype == byte[].class) {
                                    if (StringUtil.isNotBlank(val) && val instanceof InputStream) {
                                        val = FileManagerUtil.getContentFromInputStream((InputStream) val);
                                    }
                                } else if (ptype == InputStream.class && StringUtil.isNotBlank(val)
                                        && val instanceof byte[]) {
                                    val = new ByteArrayInputStream((byte[]) ((byte[]) val));
                                }
                            } else if (StringUtil.isBlank(val)) {
                                if (ptype == Short.class) {
                                    val = null;
                                } else {
                                    val = Short.valueOf(Short.parseShort("0"));
                                }
                            } else {
                                val = Short.valueOf(Short.parseShort(String.valueOf(val)));
                            }
                        } else if (StringUtil.isBlank(val)) {
                            if (ptype == Float.class) {
                                val = null;
                            } else {
                                val = Float.valueOf(Float.parseFloat("0"));
                            }
                        } else {
                            val = Float.valueOf(Float.parseFloat(String.valueOf(val)));
                        }
                    } else if (StringUtil.isBlank(val)) {
                        if (ptype == Long.class) {
                            val = null;
                        } else {
                            val = Long.valueOf(0L);
                        }
                    } else {
                        val = Long.valueOf(Long.parseLong(String.valueOf(val)));
                    }
                } else if (StringUtil.isBlank(val)) {
                    if (ptype == Double.class) {
                        val = null;
                    } else {
                        val = Double.valueOf(Double.parseDouble("0"));
                    }
                } else {
                    val = Double.valueOf(Double.parseDouble(String.valueOf(val)));
                }
            } else if (StringUtil.isNotBlank(val)) {
                String temp = String.valueOf(val);
                if (temp.toLowerCase().endsWith("px")) {
                    temp = temp.substring(0, temp.indexOf("px"));
                } else if (temp.endsWith("%")) {
                    val = null;
                }

                if (val != null) {
                    val = Integer.valueOf(Integer.parseInt(temp));
                }
            } else if (ptype == Integer.TYPE) {
                val = Integer.valueOf(0);
            } else {
                val = null;
            }
        } else if (StringUtil.isNotBlank(val)) {
            if (!"0".equals(String.valueOf(val)) && !"false".equalsIgnoreCase(String.valueOf(val))) {
                val = Boolean.valueOf(true);
            } else {
                val = Boolean.valueOf(false);
            }
        } else if (ptype == Boolean.TYPE) {
            val = Boolean.valueOf(false);
        } else {
            val = null;
        }

        if (val != null) {
            result = val;
        }

        return (T) result;
    }

   
   
    public static String convertListToString(List<?> inList, String token) {
        if (token == null || token.length() == 0) {
            token = ";";
        }

        if (inList != null) {
            int length = inList.size();
            if (length > 0) {
                StringBuffer sb = new StringBuffer();

                for (int i = 0; i < length; ++i) {
                    sb.append(inList.get(i).toString()).append(token);
                }

                return sb.toString();
            }
        }

        return "";
    }

    public static Object[] convertListToObjectArray(List<?> inList) {
        Object[] result = null;
        if (inList != null) {
            int length = inList.size();
            if (length > 0) {
                result = new Object[length];

                for (int i = 0; i < length; ++i) {
                    result[i] = inList.get(i);
                }
            }
        }

        return result;
    }

    public static String convertArrayToString(Object[] array) {
        StringBuffer result = new StringBuffer();
        if (array != null) {
            Object[] arg1 = array;
            int arg2 = array.length;

            for (int arg3 = 0; arg3 < arg2; ++arg3) {
                Object item = arg1[arg3];
                if (item != null) {
                    result.append(item.toString() + ";");
                } else {
                    result.append("null;");
                }
            }
        }

        return result.toString();
    }

    public static String[] convertListToStringArray(List<?> inList) {
        String[] result = null;
        if (inList != null) {
            int length = inList.size();
            if (length > 0) {
                result = new String[length];

                for (int i = 0; i < length; ++i) {
                    result[i] = (String) inList.get(i);
                }
            }
        }

        return result;
    }

    public static List<String> convertStringToList(String str, String token) {
        if (token == null || token.length() == 0) {
            token = ";";
        }

        ArrayList rntList = new ArrayList();
        if (StringUtil.isNotBlank(str)) {
            String[] a = str.split(token);
            String[] arg3 = a;
            int arg4 = a.length;

            for (int arg5 = 0; arg5 < arg4; ++arg5) {
                String item = arg3[arg5];
                rntList.add(item);
            }
        }

        return rntList;
    }

    public static Integer convertBooleanToInteger(Boolean bool) {
        return bool == null ? Integer.valueOf(0) : (bool.booleanValue() ? Integer.valueOf(1) : Integer.valueOf(0));
    }

    public static Boolean convertIntegerToBoolean(Integer intvalue) {
        return intvalue == null
                ? Boolean.valueOf(false)
                : (intvalue.intValue() > 0 ? Boolean.valueOf(true) : Boolean.valueOf(false));
    }

    public static String convertBooleanToString(Boolean b) {
        return b != null ? b.toString() : null;
    }

    public static Boolean convertStringToBoolean(String s) {
        return s != null ? Boolean.valueOf(s) : null;
    }

    public static String convertLengthToCapacity(long dataSize) {
        return dataSize >= 1073741824L
                ? (double) Math.round((double) dataSize / 1.073741824E9D * 100.0D) / 100.0D + " GB"
                : (dataSize >= 1048576L
                        ? (double) Math.round((double) dataSize / 1048576.0D * 100.0D) / 100.0D + " MB"
                        : (dataSize >= 1024L
                                ? (double) Math.round((double) dataSize / 1024.0D * 100.0D) / 100.0D + " KB"
                                : dataSize + " B"));
    }

    public static String changeClobtoString(Clob clob) {
        String str = "";
        if (clob != null) {
            Reader inStream = null;

            try {
                inStream = clob.getCharacterStream();
                char[] e = new char[(int) clob.length()];
                inStream.read(e);
                str = new String(e);
            } catch (SQLException arg13) {
                arg13.printStackTrace();
            } catch (IOException arg14) {
                arg14.printStackTrace();
            } finally {
                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                } catch (IOException arg12) {
                    arg12.printStackTrace();
                }

            }
        }

        return str;
    }

    public static NClob changeStringToNClob(String str, Connection conn) {
        NClob nclob = null;

        try {
            nclob = (NClob) createOracleLob(conn, "java.sql.NClob");
            Method e = nclob.getClass().getMethod("getCharacterOutputStream", (Class[]) null);
            Writer writer = (Writer) e.invoke(nclob, (Object[]) null);
            writer.write(str);
            writer.close();
        } catch (Exception arg4) {
            arg4.printStackTrace();
        }

        return nclob;
    }

    private static Object createOracleLob(Connection conn, String lobClassName) throws Exception {
        Class lobClass = conn.getClass().getClassLoader().loadClass(lobClassName);
        Integer DURATION_SESSION = new Integer(lobClass.getField("DURATION_SESSION").getInt((Object) null));
        Integer MODE_READWRITE = new Integer(lobClass.getField("MODE_READWRITE").getInt((Object) null));
        Method createTemporary = lobClass.getMethod("createTemporary",
                new Class[]{Connection.class, Boolean.TYPE, Integer.TYPE});
        Object lob = createTemporary.invoke((Object) null,
                new Object[]{conn, Boolean.valueOf(false), DURATION_SESSION});
        Method open = lobClass.getMethod("open", new Class[]{Integer.TYPE});
        open.invoke(lob, new Object[]{MODE_READWRITE});
        return lob;
    }

    public static String changeBlobtoString(Blob blob) {
        String str = "";
        if (blob != null) {
            InputStream inStream = null;

            try {
                inStream = blob.getBinaryStream();
                byte[] e = new byte[inStream.available()];
                inStream.read(e);
                str = new String(e);
            } catch (SQLException arg13) {
                arg13.printStackTrace();
            } catch (IOException arg14) {
                arg14.printStackTrace();
            } finally {
                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                } catch (IOException arg12) {
                    arg12.printStackTrace();
                }

            }
        }

        return str;
    }

    public static byte[] changeBolbToByte(Blob blob) {
        if (blob == null) {
            return null;
        } else {
            int BUFFER_SIZE = 1048576;
            InputStream is = null;
            ByteArrayOutputStream baos = null;

            try {
                is = blob.getBinaryStream();
                baos = new ByteArrayOutputStream();
                boolean e = false;
                byte[] b = new byte[BUFFER_SIZE];

                int e1;
                while ((e1 = is.read(b, 0, BUFFER_SIZE)) != -1) {
                    baos.write(b, 0, e1);
                }
            } catch (SQLException arg15) {
                arg15.printStackTrace();
            } catch (IOException arg16) {
                arg16.printStackTrace();
            } finally {
                try {
                    is.close();
                    baos.close();
                } catch (IOException arg14) {
                    arg14.printStackTrace();
                }

            }

            return baos.toByteArray();
        }
    }

    public static String getStackTrace(Throwable aThrowable) {
        StringWriter result = new StringWriter();
        PrintWriter printWriter = new PrintWriter(result);
        aThrowable.printStackTrace(printWriter);
        return result.toString();
    }

   
}
