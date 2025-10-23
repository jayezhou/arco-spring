import Mock from 'mockjs';
import setupMock, {
  successResponseWrap,
  failResponseWrap,
} from '@/utils/setup-mock';

import { MockParams } from '@/types/mock';
import { isLogin } from '@/utils/auth';

setupMock({
  setup() {
    // Mock.XHR.prototype.withCredentials = true;

    // 用户信息
    Mock.mock(new RegExp('/api/user/info'), () => {
      if (isLogin()) {
        const role = window.localStorage.getItem('userRole') || 'admin';
        return successResponseWrap({
          name: 'admin',
          avatar:
            '//lf1-xgcdn-tos.pstatp.com/obj/vcloud/vadmin/start.8e0e4855ee346a46ccff8ff3e24db27b.png',
          email: 'wangliqun@email.com',
          job: 'frontend',
          jobName: '前端艺术家',
          organization: 'Frontend',
          organizationName: '前端',
          location: 'beijing',
          locationName: '北京',
          introduction: '人潇洒，性温存',
          personalWebsite: 'https://www.arco.design',
          phone: '150****0000',
          registrationDate: '2013-05-10 12:10:00',
          accountId: '15012312300',
          certification: 1,
          role,
          permissions: [
            'list:search-table:reset'
          ],
        });
      }
      return failResponseWrap(null, '未登录', 50008);
    });

    // 登录
    /*
    Mock.mock(new RegExp('/api/user/login'), (params: MockParams) => {
      const { username, password } = JSON.parse(params.body);
      if (!username) {
        return failResponseWrap(null, '用户名不能为空', 50000);
      }
      if (!password) {
        return failResponseWrap(null, '密码不能为空', 50000);
      }
      if (username === 'admin' && password === 'admin') {
        window.localStorage.setItem('userRole', 'admin');
        return successResponseWrap({
          token: '12345',
        });
      }
      if (username === 'user' && password === 'user') {
        window.localStorage.setItem('userRole', 'user');
        return successResponseWrap({
          token: '54321',
        });
      }
      return failResponseWrap(null, '账号或者密码错误', 50000);
    });
    */

    Mock.mock(new RegExp('/api/user/login'), (params: MockParams) => {
        return successResponseWrap(
          {
            "id": 1,
            "username": "adm",
            "email": "adm@abc.com",
            "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG0iLCJERVZJQ0VfVFlQRSI6IkRFVklDRV9CUk9XU0VSIiwiaWF0IjoxNzYwNTI3MjI1LCJleHAiOjE3NjA2MTM2MjV9.Skib8WGRXJnwRwU7JvzA5dAGKsLPlyoJzDqw01krOTA",
            "refreshToken": "5ff0354e-8a17-4a3e-a230-37f7563287ce"
          }
      );
    });

    // 登出
    Mock.mock(new RegExp('/api/user/logout'), () => {
      return successResponseWrap(null);
    });

    // 用户的服务端菜单
    Mock.mock(new RegExp('/api/user/menu'), () => {
      const menuList = [
        {
          path: '/dashboard',
          name: 'dashboard',
          meta: {
            locale: '仪表盘',
            requiresAuth: true,
            icon: 'icon-dashboard',
            order: 1,
          },
          children: [
            {
              path: 'workplace',
              name: 'Workplace',
              meta: {
                locale: '工作台',
                requiresAuth: true,
              },
            },
            {
              path: 'https://arco.design',
              name: 'arcoWebsite',
              meta: {
                locale: 'Arco Design',
                requiresAuth: true,
              },
            },
          ],
        },
      ];
      return successResponseWrap(menuList);
    });
  },
});
