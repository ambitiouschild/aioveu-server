CDN预热失败: Forbidden.RAM : User not authorized to operate on the specified resource, or this API doesn't support RAM.

这个 `Forbidden.RAM`错误说明你代码里使用的 **AccessKey** 权限不够，没有调用 `PushObjectCache`（预热）API 的资格。

要解决这个问题，你需要给这个 AccessKey 对应的账号**授权**。根据你的使用场景，有两种方案：

### 方案一：授予 CDN 管理员权限（推荐，最简单）

如果你不介意给这个账号完整的 CDN 管理权限，直接添加系统策略 `AliyunCDNFullAccess`即可。

**操作步骤：**

1. 登录 **RAM 控制台**（访问控制）。
2. 进入 **用户** 或 **用户组** 页面，找到你代码里用的那个 AccessKey 对应的用户。
3. 点击 **添加权限**。
4. 在 **系统策略** 中搜索并勾选 **AliyunCDNFullAccess**。
5. 点击 **确定** 完成授权。

**等待约 1-2 分钟**，让权限生效，然后你的代码就能正常调用了。

### 方案二：授予最小权限（最安全）

如果你希望遵循“最小权限原则”，只授予预热这一项功能，可以创建一个**自定义策略**。

**操作步骤：**

1. 在 RAM 控制台，进入 **权限管理 > 权限策略**。

2. 点击 **创建权限策略**，选择 **脚本编辑**。

3. 在策略内容中粘贴以下 JSON（注意将 `your-domain.com`替换为你的实际域名）：

   ```
   {
       "Version": "1",
       "Statement": [
           {
               "Effect": "Allow",
               "Action": [
                   "cdn:PushObjectCache"
               ],
               "Resource": [
                   "acs:cdn:*:*:domain/your-domain.com"
               ]
           }
       ]
   }
   ```

4. 保存策略，然后将这个策略授权给你代码使用的 RAM 用户。

### 关于你提到的“不支持 RAM”提示

你看到的报错信息 `this API doesn't support RAM`可能是指 **某些特定的 API 接口** 不支持通过 RAM 进行精细的权限控制（即不支持资源级授权），但 CDN 的 `PushObjectCache`接口是**支持 RAM 授权**的，只是需要你手动去配置一下权限策略。