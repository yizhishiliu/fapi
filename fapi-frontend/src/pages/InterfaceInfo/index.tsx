import {
  getInterfaceInfoVoByIdUsingGet,
  invokeInterfaceInfoUsingPost
} from '@/services/fapi-backend/interfaceInfoController';
import { useParams } from '@@/exports';
import { PageContainer } from '@ant-design/pro-components';
import {Button, Card, Descriptions, Divider, Form, Input, message} from 'antd';
import React, { useEffect, useState } from 'react';

/**
 * 接口文档
 * @constructor
 */
const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [invokeLoading, setInvokeLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  const [invokeResult, setInvokeResult] = useState<any>();
  // 使用 useParams 钩子函数获取动态路由参数
  const params = useParams();

  // 加载数据
  const loadData = async () => {
    // 检查路由参数是否存在
    if (!params.id) {
      message.error('参数错误');
      return;
    }
    setLoading(true);
    try {
      // 请求后端接口
      const res = await getInterfaceInfoVoByIdUsingGet({
        id: Number(params.id),
      });
      // 将后端返回的接口信息设置到 data 状态中
      setData(res.data);
    } catch (e) {
      message.error('获取数据失败' + e.message);
    }
    setLoading(false);
  };

  // 页面首次加载时调用
  useEffect(() => {
    loadData();
  }, []);

  // 调用接口
  const onFinish = async (values) => {
    if (!params.id) {
      message.error('参数错误');
      return;
    }
    setInvokeLoading(true);
    try {
      const res = await invokeInterfaceInfoUsingPost({
        ...values,
        id: Number(params.id),
      });
      setInvokeResult(res.data);
      message.success('调用成功！')
    } catch (e) {
      message.error('调用接口失败' + e.message);
    }
    setInvokeLoading(false);
  };

  return (
    <PageContainer title="API接口文档">
      <Card loading={loading}>
        {data ? (
          <Descriptions title={data.name} column={1}>
            <Descriptions.Item label="描述">{data.description}</Descriptions.Item>
            <Descriptions.Item label="请求地址">{data.url}</Descriptions.Item>
            <Descriptions.Item label="请求参数">{data.requestParams}</Descriptions.Item>
            <Descriptions.Item label="状态">{data.status ? '正常' : '关闭'}</Descriptions.Item>
            <Descriptions.Item label="请求方法">{data.method}</Descriptions.Item>
            <Descriptions.Item label="请求头">{data.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="响应头">{data.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="创建时间">{data.createTime}</Descriptions.Item>
            <Descriptions.Item label="更新时间">{data.updateTime}</Descriptions.Item>
          </Descriptions>
        ) : (
          <>接口不存在！</>
        )}
      </Card>
      <Divider/>
      <Card title="在线测试">
        <Form
          name="invoke"
          layout="vertical"
          labelCol={{ span: 8 }}
          wrapperCol={{ span: 16 }}
          style={{ maxWidth: 600 }}
          initialValues={{ remember: true }}
          onFinish={onFinish}
          autoComplete="off"
        >
          <Form.Item label="请求参数：" name="userRequestParams">
            <Input.TextArea />
          </Form.Item>

          <Form.Item wrapperCol={{ span: 16 }}>
            <Button type="primary" htmlType="submit">
              发送
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Card title="调用结果" loading={invokeLoading}>
        {invokeResult}
      </Card>
    </PageContainer>
  );
};

export default Index;
