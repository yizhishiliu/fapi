/**
 * @see https://umijs.org/docs/max/access#access
 * */
export default function access(initialState: InitialState) {
  const { loginUser } = initialState ?? {};
  return {
    // 普通用户权限，登录即可访问
    canUser: loginUser,
    // 管理员权限
    canAdmin: loginUser?.userRole === 'admin',
  };
}
