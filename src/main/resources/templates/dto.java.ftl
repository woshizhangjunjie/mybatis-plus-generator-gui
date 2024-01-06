package ${author}.dto;<#--无法自定义带参 这里只能用作者暂时替代下包名-->

import com.mixchains.ytboot.common.domain.BasePage;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class ${entity}Dto extends BasePage {

    @ApiModelProperty(value = "创建开始时间")
    private String startCreateTime;

    @ApiModelProperty(value = "创建结束时间")
    private String endCreateTime;
}
