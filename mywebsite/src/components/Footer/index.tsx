import { GithubOutlined } from '@ant-design/icons';
import { DefaultFooter } from '@ant-design/pro-layout';
import {MY_GITHUB} from "@/constant";

const Footer: React.FC = () => {
  const defaultMessage = 'Shinonome出品';
  const currentYear = new Date().getFullYear();
  return (
    <DefaultFooter
      copyright={`${currentYear} ${defaultMessage}`}
      links={[
        {
          key: 'Source Code Gitee',
          title: 'Source Code Gitee',
          href: 'https://gitee.com/shinome/UserCenter',
          blankTarget: true,
        },
        {
          key: 'github',
          title: <GithubOutlined />,
          href: MY_GITHUB,
          blankTarget: true,
        },
        {
          key: 'Source Code GitHub',
          title: 'Source Code GitHub',
          href: 'https://github.com/shinonomeee/UserCenter',
          blankTarget: true,
        },
      ]}
    />
  );
};

export default Footer;
