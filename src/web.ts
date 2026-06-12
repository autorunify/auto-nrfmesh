import { WebPlugin } from '@capacitor/core';

import type {
  NrfMeshPlugin,
  Permissions,
  BluetoothState,
  ScanResults,
  IdentifyResults,
  ConnectionState,
  ProvisionResults,
  CompositionResults,
  NetKeysResults,
  NetKey,
  AppKeysResults,
  AppKey,
  GroupsResults,
  MeshGroup,
  NodesResults,
  NodeResults,
  ModelResults,
} from './definitions';

export class NrfMeshWeb extends WebPlugin implements NrfMeshPlugin {
  async checkPermissions(): Promise<Permissions> { return {} as Permissions }
  async requestPermissions(): Promise<Permissions> { return {} as Permissions }
  async isBluetoothEnabled(): Promise<BluetoothState> { return {} as BluetoothState }

  async isConnected(): Promise<ConnectionState> { return {} as ConnectionState }
  async connect(): Promise<void> { }
  async disconnect(): Promise<void> { }

  async export(): Promise<any> { }
  async import(): Promise<void> { }

  async netkeys(): Promise<NetKeysResults> { return {} as NetKeysResults }
  async netkey(): Promise<NetKey> { return {} as NetKey }

  async appkeys(): Promise<AppKeysResults> { return {} as AppKeysResults }
  async appkey(): Promise<AppKey> { return {} as AppKey }

  async groups(): Promise<GroupsResults> { return {} as GroupsResults }
  async group(): Promise<MeshGroup> { return {} as MeshGroup }

  async nodes(): Promise<NodesResults> { return {} as NodesResults }
  async node(): Promise<NodeResults> { return {} as NodeResults }

  async model(): Promise<ModelResults> { return {} as ModelResults }

  async init(): Promise<void> { }
  async kill(): Promise<void> { }
  async devices(): Promise<ScanResults> { return {} as ScanResults }
  async identify(): Promise<IdentifyResults> { return {} as IdentifyResults };
  async provision(): Promise<ProvisionResults> { return {} as ProvisionResults }
  async composition(): Promise<CompositionResults> { return {} as CompositionResults }
}
