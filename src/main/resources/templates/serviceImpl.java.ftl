package ${package.ServiceImpl};

import ${package.Entity}.${entity};
import ${package.Mapper}.${table.mapperName};
import ${package.Service}.${table.serviceName};
import ${superServiceImplClassPackage};
import ${author}.dto.${entity}Dto;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mixchains.ytboot.common.vo.ReturnVO;
import com.mixchains.ytboot.common.utils.YTBootAssert;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;

/**
* @author ${author}
*/
@Service
<#if kotlin>
open class ${table.serviceImplName} : ${superServiceImplClass}<${table.mapperName}, ${entity}>(), ${table.serviceName} {

}
<#else>
public class ${table.serviceImplName} extends ${superServiceImplClass}<${table.mapperName}, ${entity}> implements ${table.serviceName} {

    @Autowired
    private ${table.mapperName} ${table.entityPath}Mapper;

    @Override
    public ReturnVO<PageInfo<${entity}>> list${entity}(${entity}Dto ${table.entityPath}Dto) {
        LambdaQueryWrapper<${entity}> ${table.entityPath}Wrapper = Wrappers.lambdaQuery();
        ${table.entityPath}Wrapper.orderByDesc(${entity}::getCreateTime);
        //创建时间筛选
        if (StringUtils.isNotBlank(${table.entityPath}Dto.getStartCreateTime()) && StringUtils.isNotBlank(${table.entityPath}Dto.getEndCreateTime())) {
            ${table.entityPath}Wrapper.between(${entity}::getCreateTime, ${table.entityPath}Dto.getStartCreateTime(), ${table.entityPath}Dto.getEndCreateTime());
        }
        PageHelper.startPage(${table.entityPath}Dto.getCurPage(),${table.entityPath}Dto.getMaxPage());
        List<${entity}> ${table.entityPath}List = ${table.entityPath}Mapper.selectList(${table.entityPath}Wrapper);
        return ReturnVO.ok("${table.comment!}列表", PageInfo.of(${table.entityPath}List));
    }

    @Override
    public ReturnVO<${entity}> select${entity}ByUuid(String uuid) {
        ${entity} ${table.entityPath}Info = YTBootAssert.notNull(${table.entityPath}Mapper.selectByUuid(uuid), "该uuid对应${table.comment!}不存在");
        return ReturnVO.ok("根据uuid查询${table.comment!}详情", ${table.entityPath}Info);
    }

    @Override
    public ReturnVO save${entity}(${entity} ${table.entityPath}) {
        ${table.entityPath}Mapper.insert(${table.entityPath});
        return ReturnVO.ok("新增${table.comment!}", ${table.entityPath}.getUuid());
    }

    @Override
    public ReturnVO update${entity}(${entity} ${table.entityPath}) {
        ${entity} ${table.entityPath}Info = YTBootAssert.notNull(${table.entityPath}Mapper.selectByUuid(${table.entityPath}.getUuid()), "该uuid对应${table.comment!}不存在");
        ${table.entityPath}.setId(${table.entityPath}Info.getId());
        ${table.entityPath}Mapper.updateById(${table.entityPath});
        return ReturnVO.ok("修改${table.comment!}", ${table.entityPath}.getUuid());
    }

    @Transactional
    @Override
    public ReturnVO delete${entity}(List<String> uuidList) {
        uuidList.stream()
                .map(${table.entityPath}Mapper::selectByUuid)
                .filter(Objects::nonNull)
                .forEach(${table.entityPath} -> ${table.entityPath}Mapper.deleteById(${table.entityPath}.getId()));
        return ReturnVO.ok("删除${table.comment!}", uuidList);
     }
}
</#if>
