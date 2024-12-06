import { ProColumns, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React from 'react';

export type Props = {
  // 列
  columns: ProColumns<API.InterfaceInfoAddRequest>[];
  // 点击取消
  onCancel: () => void;
  // 点击确定
  onSubmit: (values: API.InterfaceInfoAddRequest) => Promise<void>;
  // 模态框是否可见
  visible: boolean;
};
const CreateModal: React.FC<Props> = (props) => {
  const { columns, visible, onCancel, onSubmit } = props;
  return (
    <Modal visible={visible} footer={null} onCancel={() => onCancel?.()}>
      <ProTable
        type="form"
        columns={columns}
        onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};
export default CreateModal;
