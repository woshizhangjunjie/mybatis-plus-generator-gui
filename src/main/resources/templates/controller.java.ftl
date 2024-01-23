package ${package.Controller};


import ${package.Service}.${table.serviceName};
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import io.swagger.annotations.Api;
<#if restControllerStyle>
import org.springframework.web.bind.annotation.RestController;
<#else>
import org.springframework.stereotype.Controller;
</#if>
<#if superControllerClassPackage??>
import ${superControllerClassPackage};
</#if>
import ${package.Entity}.base.ReturnVo;
import ${package.Entity}.dto.${entity}Dto;
import com.github.pagehelper.PageInfo;
import ${package.Entity}.${entity};

import java.util.Arrays;
import java.util.List;

/**
* @author ${author}
*/
@Api(tags = "${table.comment!}接口")
<#if restControllerStyle>
@RestController
<#else>
@Controller
</#if>
@RequestMapping("<#if package.ModuleName??>/${package.ModuleName}</#if>/<#if controllerMappingHyphenStyle??>${controllerMappingHyphen}<#else>${table.entityPath}</#if>")
<#if kotlin>
class ${table.controllerName}<#if superControllerClass??> : ${superControllerClass}()</#if>
<#else>
<#if superControllerClass??>
public class ${table.controllerName} extends ${superControllerClass} {
<#else>
public class ${table.controllerName} {
</#if>

    @Autowired
    private I${entity}Service ${table.entityPath}Service;

    @RequestMapping(value = "list${entity}", method = RequestMethod.POST)
    @ApiOperation(value = "${table.comment!}列表", notes = "${table.comment!}列表", httpMethod = "POST")
    public ReturnVo<PageInfo<${entity}>> list${entity}(@RequestBody ${entity}Dto ${table.entityPath}Dto) {
        return ${table.entityPath}Service.list${entity}(${table.entityPath}Dto);
    }

    @RequestMapping(value = "select${entity}ByUuid/{uuid}", method = RequestMethod.POST)
    @ApiOperation(value = "根据uuid查询${table.comment!}详情", notes = "根据uuid查询${table.comment!}详情", httpMethod = "POST")
    public ReturnVo<${entity}> select${entity}ByUuid(@PathVariable String uuid) {
        return ${table.entityPath}Service.select${entity}ByUuid(uuid);
    }

    @RequestMapping(value = "save${entity}", method = RequestMethod.POST)
    @ApiOperation(value = "新增${table.comment!}", notes = "新增${table.comment!}", httpMethod = "POST")
    public ReturnVo save${entity}(@RequestBody ${entity} ${table.entityPath}) {
        return ${table.entityPath}Service.save${entity}(${table.entityPath});
    }

    @RequestMapping(value = "update${entity}", method = RequestMethod.POST)
    @ApiOperation(value = "修改${table.comment!}", notes = "修改${table.comment!}", httpMethod = "POST")
    public ReturnVo update${entity}(@RequestBody ${entity} ${table.entityPath}) {
        return ${table.entityPath}Service.update${entity}(${table.entityPath});
    }

    @RequestMapping(value = "delete${entity}/{uuids}", method = RequestMethod.POST)
    @ApiOperation(value = "删除${table.comment!}", notes = "删除${table.comment!}", httpMethod = "POST")
    public ReturnVo delete${entity}(@PathVariable String uuids) {
        List<String> uuidList = Arrays.asList(uuids.split(","));
        return ${table.entityPath}Service.delete${entity}(uuidList);
    }
}
</#if>
