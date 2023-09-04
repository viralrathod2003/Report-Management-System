import { createRouter, createWebHistory } from 'vue-router';
import { useAuthStore } from '@/stores/auth';

import EmployeeHome from '@/views/employee/EmployeeHome.vue';
import ApproverView from '@/views/approvers/ApproverHome.vue';
import AdminHome from '@/views/admin/AdminHome.vue';
import Login from '@/views/login.vue';


const routes = [
  {
    path: '/',
    name: 'login',
    component: Login
  },
  {
    path: '/admin',
    name: 'admin',
    component: AdminHome,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/employee',
    name: 'employee',
    component: EmployeeHome,
    meta: {
      requiresAuth: true
    }
  },
  {
    path: '/approver',
    name: 'approver',
    component: ApproverView,
    meta: {
      requiresAuth: true
    }
  }
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  const isAuthenticatedValue = useAuthStore().isAuthenticated.value;

  if (to.matched.some(route => route.meta.requiresAuth)) {
    if (!isAuthenticatedValue) {
      next('/');
    } else {
      next();
    }
  } else {
    next();
  }
});

export default router;
