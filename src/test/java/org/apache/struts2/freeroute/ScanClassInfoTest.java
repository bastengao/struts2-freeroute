package org.apache.struts2.freeroute;

import com.google.common.reflect.ClassPath;
import org.junit.Test;

import java.io.IOException;

/**
 * @author bastengao
 * @date 12-12-16 17:33
 */
public class ScanClassInfoTest {
    @Test
    public void test() throws IOException {
        ClassPath classPath = ClassPath.from(Thread.currentThread().getContextClassLoader());
        System.out.println(classPath);

        System.out.println(classPath.getTopLevelClassesRecursive("com.gaohui.action"));
    }

}
