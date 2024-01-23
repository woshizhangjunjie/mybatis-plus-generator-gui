package ${package.Entity}.base;

import ${package.Entity}.base.YtBootException;

/**
 * 断言
 */
public class YTBootAssert {

    public static <T> T notNull(T object, String message) {
        if (object == null) {
            throw new YtBootException(message);
        }
        return object;
    }

}
