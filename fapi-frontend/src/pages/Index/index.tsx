import { listInterfaceInfoVoByPageUsingPost } from '@/services/fapi-backend/interfaceInfoController';
import { PageContainer } from '@ant-design/pro-components';
import { List, message } from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 首页
 * @constructor
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfo[]>([]);
  const [total, setTotal] = useState<number>(0);
  const initCurrent = 1;
  const initPageSize = 5;

  // 加载数据
  const loadData = async (current = initCurrent, pageSize = initPageSize) => {
    setLoading(true);
    try {
      const res = await listInterfaceInfoVoByPageUsingPost({
        current,
        pageSize,
      });
      setList(res?.data?.records ?? []);
      setTotal(res?.data?.total ?? 0);
    } catch (e) {
      message.error('获取数据失败' + e.message);
    }
    setLoading(false);
  };

  // 页面首次加载时调用
  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title="在线API开放平台">
      <List
        className="api-list"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => (
          <List.Item actions={[<a key="list-loadmore-edit">详情</a>]}>
            <List.Item.Meta
              title={<a href="https://ant.design">{item.name}</a>}
              description={item.description}
            />
          </List.Item>
        )}
        // 分页配置
        pagination={{
          // 自定义显示总数
          showTotal(total: number) {
            return '共 ' + total + ' 条';
          },
          // 每页条数
          pageSize: initPageSize,
          // 总条数
          total,
          // 切换页面触发的回调函数
          onChange(page, pageSize) {
            // 加载数据
            loadData(page, pageSize);
          },
        }}
      />
    </PageContainer>
  );
};

export default Index;
