package ${package.Entity}.base;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BasePage {

    @NotNull(message = "请传入当前页码")
    @ApiModelProperty(value = "当前页码(必填)",required = true)
    private Integer curPage;

    @NotNull(message = "请传入每页最大条数")
    @ApiModelProperty(value = "每页最大条数(必填)",required = true)
    private Integer maxPage;

}
