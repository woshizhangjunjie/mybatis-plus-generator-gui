package ${package.Entity}.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 返回参数封装对象
 */
@NoArgsConstructor
@Data
public class ReturnVo<T> {

    /**
     * 返回状态
     */
    @ApiModelProperty(name = "code", value = "返回码 200-成功 401-未登录 403-无权限 500-报错弹窗")
    private String code;
    /**
     * 返回massage
     */
    @ApiModelProperty(name = "message", value = "信息")
    private String message;
    /**
     * 数据对象
     */
    @ApiModelProperty(value = "数据 ", required = true)
    private T data;

    /**
     * 成功构造
     *
     * @param message
     * @param data
     */
    public ReturnVo(String message, T data) {
        this.code = "200";
        this.message = message;
        this.data = data;
    }

    public static<T> ReturnVo<T> ok() {
        ReturnVo<T> r = new ReturnVo<T>();
        r.setCode("200");
        r.setMessage("成功");
        return r;
    }

    public static<T> ReturnVo<T> ok(String message, T data) {
        ReturnVo<T> r = new ReturnVo<T>();
        r.setCode("200");
        r.setMessage(message);
        r.setData(data);
        return r;
    }

    public static<T> ReturnVo<T> ok(T data) {
        ReturnVo<T> r = new ReturnVo<T>();
        r.setCode("200");
        r.setMessage("成功");
        r.setData(data);
        return r;
    }


    public static ReturnVo<Object> error(String msg) {
        ReturnVo<Object> r = new ReturnVo<Object>();
        r.setCode("500");
        r.setMessage(msg);
        return r;
    }
}
