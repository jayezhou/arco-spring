import { defineStore } from 'pinia';
import { Notification } from '@arco-design/web-vue';
import type { NotificationReturn } from '@arco-design/web-vue/es/notification/interface';
import type { RouteRecordNormalized } from 'vue-router';
import defaultSettings from '@/config/settings.json';
import { getMenuList } from '@/api/user';
import { appRoutes } from '@/router/routes';
import { AppState } from './types';

const useAppStore = defineStore('app', {
  state: (): AppState => ({ ...defaultSettings }),

  getters: {
    appCurrentSetting(state: AppState): AppState {
      return { ...state };
    },
    appDevice(state: AppState) {
      return state.device;
    },
    appAsyncMenus(state: AppState): RouteRecordNormalized[] {
      return state.serverMenu as unknown as RouteRecordNormalized[];
    },
  },

  actions: {
    // Update app settings
    updateSettings(partial: Partial<AppState>) {
      // @ts-ignore-next-line
      this.$patch(partial);
    },

    // Change theme color
    toggleTheme(dark: boolean) {
      if (dark) {
        this.theme = 'dark';
        document.body.setAttribute('arco-theme', 'dark');
      } else {
        this.theme = 'light';
        document.body.removeAttribute('arco-theme');
      }
    },
    toggleDevice(device: string) {
      this.device = device;
    },
    toggleMenu(value: boolean) {
      this.hideMenu = value;
    },
    async fetchServerMenuConfig() {
      let notifyInstance: NotificationReturn | null = null;
      try {
        notifyInstance = Notification.info({
          id: 'menuNotice', // Keep the instance id the same
          content: 'loading',
          closable: true,
        });
        const { data } = await getMenuList();
        
        // 处理服务端菜单数据，匹配本地路由组件
        const processedMenus = this.mergeServerMenuWithLocalRoutes(data);
        this.serverMenu = processedMenus;
        
        notifyInstance = Notification.success({
          id: 'menuNotice',
          content: 'success',
          closable: true,
        });
      } catch (error) {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        notifyInstance = Notification.error({
          id: 'menuNotice',
          content: 'error',
          closable: true,
        });
      }
    },
    
    // 合并服务端菜单与本地路由配置
    mergeServerMenuWithLocalRoutes(serverMenus: any[]): RouteRecordNormalized[] {
      const findLocalRoute = (name: string): RouteRecordNormalized | undefined => {
        const findInRoutes = (routes: RouteRecordNormalized[]): RouteRecordNormalized | undefined => {
          // 使用 some 和 find 替代 for...of 循环
          const found = routes.find((route) => route.name === name);
          if (found) return found;
          
          // 递归查找子路由
          return routes.reduce((result: RouteRecordNormalized | undefined, route) => {
            if (result) return result;
            if (route.children && route.children.length > 0) {
              return findInRoutes(route.children as RouteRecordNormalized[]);
            }
            return undefined;
          }, undefined);
        };
        return findInRoutes(appRoutes);
      };

      const processMenu = (menuItem: any): RouteRecordNormalized | null => {
        const localRoute = findLocalRoute(menuItem.name);
        
        if (!localRoute) {
          // eslint-disable-next-line no-console
          console.warn(`未找到与服务端菜单匹配的本地路由: ${menuItem.name}`);
          return null;
        }

        const processedRoute: RouteRecordNormalized = {
          ...localRoute,
          path: menuItem.path,
          name: menuItem.name,
          meta: {
            ...localRoute.meta,
            ...menuItem.meta,
            // 确保 locale 使用服务端返回的值
            locale: menuItem.meta?.locale || localRoute.meta?.locale,
          },
          children: [],
        };

        // 递归处理子菜单
        if (menuItem.children && menuItem.children.length > 0) {
          const processedChildren = menuItem.children
            .map((child: any) => processMenu(child))
            .filter((child: any) => child !== null);
          processedRoute.children = processedChildren as RouteRecordNormalized[];
        }

        return processedRoute;
      };

      return serverMenus
        .map((menu) => processMenu(menu))
        .filter((menu) => menu !== null) as RouteRecordNormalized[];
    },
    clearServerMenu() {
      this.serverMenu = [];
    },
  },
});

export default useAppStore;
