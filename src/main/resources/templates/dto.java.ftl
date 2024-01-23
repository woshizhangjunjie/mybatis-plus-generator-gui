package ${package.Entity}.dto;

import ${package.Entity}.base.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ${entity}Dto extends BasePage {

    @ApiModelProperty(value = "创建开始时间")
    private String startCreateTime;

    @ApiModelProperty(value = "创建结束时间")
    private String endCreateTime;
}
