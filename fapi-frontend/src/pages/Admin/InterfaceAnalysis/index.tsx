import { listTopInterfaceInvokeUsingGet } from '@/services/fapi-backend/analysisController';
import { PageContainer } from '@ant-design/pro-components';
import { message } from 'antd';
import ReactECharts from 'echarts-for-react';
import React, { useEffect } from 'react';

const InterfaceAnalysis: React.FC = () => {
  // 存储数据的状态
  const [data, setData] = React.useState<API.InvokeInterfaceVO[]>([]);

  // 每次页面加载时执行
  useEffect(() => {
    try {
      // 获取接口调用Top3
      listTopInterfaceInvokeUsingGet().then((res) => {
        setData(res.data);
      });
    } catch (e) {
      console.log(e);
      message.error('数据获取失败，请稍后重试');
    }
  }, []);

  // 映射
  const chartData = data?.map((item) => {
    return {
      value: item.totalTimes,
      name: item.name,
    };
  });

  // 图表配置
  const option = {
    title: {
      text: '接口调用统计',
      subtext: 'Top3',
      left: 'center',
    },
    tooltip: {
      trigger: 'item',
    },
    legend: {
      top: '5%',
      left: 'left',
      orient: 'vertical',
    },
    series: [
      {
        name: '接口调用Top3',
        type: 'pie',
        radius: ['40%', '70%'],
        avoidLabelOverlap: false,
        padAngle: 5,
        itemStyle: {
          borderRadius: 15,
        },
        label: {
          show: false,
          position: 'center',
        },
        emphasis: {
          label: {
            show: true,
            fontSize: 40,
            fontWeight: 'bold',
          },
        },
        labelLine: {
          show: false,
        },
        data: chartData,
      },
    ],
  };
  return (
    <PageContainer>
      <ReactECharts option={option} style={{ width: '100%', height: '100%', minHeight: '400px' }} />
    </PageContainer>
  );
};
export default InterfaceAnalysis;
