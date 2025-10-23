--Role data
INSERT INTO roles(id, name) VALUES(1, 'ROLE_ADMIN');
INSERT INTO roles(id, name) VALUES(2, 'ROLE_MODERATOR');
INSERT INTO roles(id, name) VALUES(3, 'ROLE_USER');

--Backend Authority data
INSERT INTO authorities(id, name) VALUES(1, 'dashboard:workplace:retrieve');
INSERT INTO authorities(id, name) VALUES(2, 'list:search-table:retrieve');
INSERT INTO authorities(id, name) VALUES(3, 'list:search-table:reset');
INSERT INTO authorities(id, name) VALUES(4, 'list:search-table:create');
INSERT INTO authorities(id, name) VALUES(5, 'list:search-table:import');
INSERT INTO authorities(id, name) VALUES(6, 'other:page-header-nav:retrieve');

INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 1);
INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 2);
INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 3);
INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 4);
INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 5);
INSERT INTO role_authorities(role_id, authority_id) VALUES(1, 6);

--Frontend Permissions/Menu data, provided by frontend
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(1, 'dashboard', true, null, '/dashboard', 'dashboard', '仪表盘', true, 'icon-dashboard', 1);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(2, 'dashboard:workplace', true, 1, 'workplace', 'workplace', '工作台', true, null, null);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(3, 'list', true, null, '/list', 'list', '列表页', true, null, 2);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(4, 'list:search-table', true, 3, 'search-table', 'SearchTable', '查询表格', true, null, null);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(5, 'list:search-table:query', false, 4, null, 'query', '查询', true, null, null);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(6, 'list:search-table:reset', false, 4, null, 'reset', '重置', true, null, null);	
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(7, 'list:search-table:add', false, 4, null, 'add', '新建', true, null, null);	
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(8, 'list:search-table:import', false, 4, null, 'import', '批量导入', true, null, null);
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(9, 'other', true, null, '/other', 'other', '其他', true, 'icon-more-vertical', 3);	
INSERT INTO permissions(id, permission, is_menu, parent, path, router_name, locale, requires_auth, icon, sort_order) 
	VALUES(10, 'other:page-header-nav', true, 9, 'page-header-nav', 'PageHeaderNav', '页头导航', true, null, null);	

INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 1);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 2);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 3);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 4);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 5);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 6);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 7);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 8);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 9);
INSERT INTO role_permissions(role_id, permission_id) VALUES(1, 10);

COMMIT;
