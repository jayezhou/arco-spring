// Customized by architect
import { DirectiveBinding } from 'vue';
import { useUserStore } from '@/store';

function checkPermission(el: HTMLElement, binding: DirectiveBinding) {
  const { value } = binding;
  const userStore = useUserStore();
  const { permissions } = userStore;

  // 如果权限数组为空或未定义，或者值为空，则移除元素
  if (!permissions || permissions.length === 0) {
    if (el.parentNode) {
      el.parentNode.removeChild(el);
    }
    return;
  }
  
  if (value === null || value.trim() === "") {
    throw new Error(`value of attribute has-permission is null`);
  }
  
  const hasPermission = permissions.includes(value);
  if (!hasPermission && el.parentNode) {
    el.parentNode.removeChild(el);
  }
}

export default {
  mounted(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
  updated(el: HTMLElement, binding: DirectiveBinding) {
    checkPermission(el, binding);
  },
};
