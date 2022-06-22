/**
 * @see https://umijs.org/zh-CN/plugins/plugin-access
 * */
import {ADMIN} from "@/constant/admin";

export default function access(initialState: { currentUser?: API.CurrentUser } | undefined) {
  const { currentUser } = initialState ?? {};
  return {
    canAdmin: currentUser && currentUser.userRole === ADMIN,
  };
}
