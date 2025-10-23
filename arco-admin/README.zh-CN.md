## 简介

arco-admin是arco-design-pro-vue的去除国际化版本。（node v18.17.1）
说明：
1、修改了页面上操作（按钮、链接等）的权限控制的方式，改为权限码控制。用法参见列表页面进行了权限控制的“重设”按钮。
2、前端菜单和操作的权限控制是分开的。菜单是通过"/api/user/menu"获取；操作权限是通过"/api/user/info"获取。
3、在src\main.ts注释了“import './mock';”不使用Mock数据，而使用后端接口。
4、src\config\settings.json中把menuFromServer设为true，使用后端接口获取菜单。
5、config\vite.config.dev.ts中配置了代理，指定了后端地址，同时解决跨域问题。

测试账号: admin/admin user/user

## 安装使用

- 安装依赖

```bash
cd arco-admin

pnpm install

```

- 运行

```bash
pnpm run dev
```

- 打包

```bash
pnpm build
```

