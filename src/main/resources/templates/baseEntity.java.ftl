package ${package.Entity}.base;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class BaseEntity implements Serializable {

    @JsonIgnore
    @ApiModelProperty(value = "id")
    private Long id;

    @TableField(fill = FieldFill.INSERT) //自动填充
    @ApiModelProperty(value = "修改操作时必填 前端操作数据统一uuid")
    private String uuid;

    @TableField(fill = FieldFill.INSERT)
    private String createUsername;

    @TableField(fill = FieldFill.INSERT)
    @ApiModelProperty(value = "创建ID")
    private Long createUserId;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改人")
    private String modifyUsername;

    @TableField(fill = FieldFill.UPDATE)
    @ApiModelProperty(value = "修改人ID")
    private Long modifyUserId;

    @TableField(fill = FieldFill.INSERT)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "创建时间", hidden = true)
    private LocalDateTime createTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @ApiModelProperty(value = "更新时间", hidden = true)
    @TableField(update = "now()")
    private LocalDateTime modifyTime;

    @JsonIgnore
    @TableLogic
    @ApiModelProperty(value = "逻辑删除 0-未删除 1-已删除", hidden = true)
    private Boolean deleted;
}
