package ${package.Entity}.base;

/**
 * YtBoot 系统内部异常
 */
public class YtBootException extends RuntimeException {

    private static final long serialVersionUID = -994962710559017255L;

    public YtBootException(String message) {
        super(message);
    }
}
