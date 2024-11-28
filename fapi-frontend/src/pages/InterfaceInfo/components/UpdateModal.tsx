import { ProColumns, ProTable } from '@ant-design/pro-components';
import '@umijs/max';
import { Modal } from 'antd';
import React, { useEffect, useRef } from 'react';
import {ProFormInstance} from "@ant-design/pro-form/lib";

export type Props = {
  valuse: API.InterfaceInfoUpdateRequest;
  // 列
  columns: ProColumns<API.InterfaceInfoUpdateRequest>[];
  // 点击取消
  onCancel: () => void;
  // 点击确定
  onSubmit: (values: API.InterfaceInfoUpdateRequest) => Promise<void>;
  // 模态框是否可见
  visible: boolean;
};
const UpdateModal: React.FC<Props> = (props) => {
  const { values, columns, visible, onCancel, onSubmit } = props;
  const formRef = useRef<ProFormInstance>();

  useEffect(() => {
    if (formRef) {
      formRef.current?.setFieldsValue(values);
    }
  }, [values]);

  return (
    <Modal visible={visible} footer={null} onCancel={() => onCancel?.()}>
      <ProTable
        type="form"
        columns={columns}
        formRef={formRef}
        onSubmit={async (values) => {
          onSubmit?.(values);
        }}
      />
    </Modal>
  );
};
export default UpdateModal;
