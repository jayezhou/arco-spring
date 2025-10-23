import { App } from 'vue';
import permission from './permission';
import hasPermission from './has-permission';

export default {
  install(Vue: App) {
    Vue.directive('permission', permission);
    Vue.directive('has-permission', hasPermission);
  },
};
