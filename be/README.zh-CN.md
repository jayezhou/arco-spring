## 简介

和fev项目配套使用的支持权限控制的后端，技术栈为SpringBoot/Spring Secutiry/JWT/JPA。

注意：
1、往缓存保存token/从缓存中取token/从缓存删除token 这些实际功能在实际项目中完成，这时需要改写FrontendAuthController类和AuthTokenFilter类。
2、前端的权限（包括菜单和页面上的操作按钮）都保存在permissions表中，分别通过"/api/user/menu"和"/api/user/info"获取。
3、src/main/resources/import.sql文件中的数据可以在application.properties中的配置spring.jpa.hibernate.ddl-auto=create-drop时自动导入数据库。