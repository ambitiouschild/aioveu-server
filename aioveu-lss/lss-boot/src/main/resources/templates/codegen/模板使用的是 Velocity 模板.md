这个模板是 **Velocity 模板**！

## 证据1：文件后缀 `.vm`

- 资源: `codegen/api.ts.vm`
- `.vm`是 **Velocity 模板的标准文件扩展名**

## 证据2：Velocity 语法特征

您的模板使用了典型的 Velocity 语法：

### 1. **变量引用 `${}`**

```
${entityName.toUpperCase()}
${businessName}
${kebabCaseEntityName}
```

### 2. **指令语法 `#directive`**

```
#foreach($fieldConfig in $fieldConfigs)
#if($fieldConfig.isShowInQuery)
#if("$!fieldConfig.fieldComment" != "")
#end
```

### 3. **空值安全操作符 `$!`**

```
#if("$!fieldConfig.fieldComment" != "")
```