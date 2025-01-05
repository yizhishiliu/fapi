export default [
  { path: '/', name: '首页', icon: 'smile', component: './Index' },
  { path: '/interface_info/:id', name: '接口详情', icon: 'smile', component: './InterfaceInfo', hideInMenu: true },
  {
    path: '/user',
    layout: false,
    routes: [{ name: '登录', path: '/user/login', component: './User/Login' }],
  },
  // { path: '/welcome', name: '欢迎', icon: 'smile', component: './Index' },
  // {
  //   path: '/Admin',
  //   name: '管理页',
  //   icon: 'crown',
  //   access: 'canAdmin',
  //   routes: [
  //     { path: '/Admin', redirect: '/Admin/sub-page' },
  //     { path: '/Admin/sub-page', name: '二级管理页', component: './Admin' },
  //   ],
  // },
  // { name: '查询表格', icon: 'table', path: '/list', component: './InterfaceInfo' },
  // { path: '/', redirect: '/welcome' },
  // { path: '*', layout: false, component: './404' },
  {
    path: '/admin',
    name: '管理页',
    icon: 'crown',
    access: 'canAdmin',
    routes: [
      { name: '接口管理', icon: 'table', path: '/admin/interface_info', component: './Admin/InterfaceInfo' },
      { name: '接口分析', icon: 'analysis', path: '/admin/interface_analysis', component: './Admin/InterfaceAnalysis' },
    ],
  },
  { path: '*', layout: false, component: './404' },
];
