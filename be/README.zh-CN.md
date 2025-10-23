## 简介

- 和arco-admin项目配套使用的支持权限控制的后端，技术栈为SpringBoot/Spring Secutiry/JWT/JPA。
- 项目中同时使用了JPA和MyBatis，利用JPA自动数据库迁移功能（自动建表修改表、自动增减字段）和实体的简单CRUD，利用MyBatis来完成复杂查询。UserMenuService中使用了roleRepository(JPA)或roleMapper（Mybatis）来实现菜单的生成，从中可看出对于复杂查询MyBatis实现起来较为简单。

注意：
1、往缓存保存token/从缓存中取token/从缓存删除token 这些实际功能在实际项目中完成，这时需要改写FrontendAuthController类和AuthTokenFilter类。
2、前端的权限（包括菜单和页面上的操作按钮）都保存在permissions表中，分别通过"/api/user/menu"和"/api/user/info"获取。
3、src/main/resources/import.sql文件中的数据可以在application.properties中的配置spring.jpa.hibernate.ddl-auto=create-drop时自动导入数据库。