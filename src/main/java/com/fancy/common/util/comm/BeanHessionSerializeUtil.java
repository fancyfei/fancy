package com.fancy.common.util.comm;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import com.caucho.hessian.io.SerializerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 */
public class BeanHessionSerializeUtil {

    public static Object deserialize(byte[] obj) {
        if(obj==null) {
            return null;
        }
        byte[] buffer = null;
        ByteArrayInputStream inputStream = null;
        Hessian2Input h2i = null;
        try {
            inputStream = new ByteArrayInputStream(obj);
            h2i = new Hessian2Input(inputStream);
            return h2i.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
            if (h2i != null) {
                try {
                    h2i.close();
                } catch (IOException e) {
                }
            }
        }

        return null;
    }

    public static byte[] serialize(Object o) {
        if(o==null) {
            return null;
        }
        byte[] buffer = null;
        ByteArrayOutputStream baos = null;
        Hessian2Output h2o = null;
        try {
            baos = new ByteArrayOutputStream();
            h2o = new Hessian2Output(baos);
            h2o.writeObject(o);
            buffer = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                }
            }
            if (h2o != null) {
                try {
                    h2o.close();
                } catch (IOException e) {
                }
            }
        }

        return buffer;

    }
}
