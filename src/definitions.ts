import type { PluginListenerHandle } from '@capacitor/core'

type PartialAny<T, K extends keyof T> = Omit<T, K> & Partial<Pick<T, K>>;

export type PermissionState = 'denied' | 'granted'

export type PermissionKey =
  "android.permission.BLUETOOTH" |
  "android.permission.BLUETOOTH_ADMIN" |
  "android.permission.ACCESS_COARSE_LOCATION" |
  "android.permission.ACCESS_FINE_LOCATION" |
  "android.permission.BLUETOOTH_SCAN" |
  "android.permission.BLUETOOTH_CONNECT"


export type Permissions = {
  [key in PermissionKey]: PermissionState;
}

export interface BluetoothState {
  enabled: boolean;
}

export interface TimeoutOptions {
  timeout?: number
}

export interface ConnectionState {
  connected: boolean
}

export interface NetKey {
  index: number;
  key: string;
  minSecurity: string;
  name: string;
  phase: number;
  timestamp: string;
  oldKey?: string;
}

export interface AppKey {
  index: number
  boundNetKey: number
  key: string
  name: string
}

export interface MeshGroup {
  name: string
  address: number
  addressHex: string
}

export interface MeshNode {
  name: string
  deviceKey: string
  unicastAddress: number
  security: string
  ttl: number
  netkeys: Array<{ index: number, updated: boolean }>
  appkeys: Array<{ index: number, updated: boolean }>
  elements: Array<{
    name: string
    elementAddress: number
    models: Array<{
      modelId: number
      appkeys: Array<number>
      subscriptions: Array<number>
      publication?: number
    }>
  }>
  features?: {
    friend: number
    lowPower: number
    proxy: number
    relay: number
  }
  networkTransmit?: {
    count: number
    interval: number
    steps: number
  }
}

export interface OnOffModel {
  state: boolean
}

export interface SensorValue {
  propertyId: number
  bytes: Array<number>
}

export interface SensorModel {
  values: Array<SensorValue>
}

export interface SensorPropertyModel {
  propertyId: number
  bytes?: Array<number>
}

export interface SensorPropertiesModel {
  properties: Array<Omit<SensorPropertyModel, 'bytes'>>
}

export interface NetKeysResults {
  netkeys: Array<NetKey>
}

export interface NetKeyOptions {
  add?: Omit<NetKey, 'index' | 'key' | 'minSecurity' | 'name' | 'phase' | 'timestamp' | 'oldKey'>
  del?: Omit<NetKey, 'key' | 'minSecurity' | 'name' | 'phase' | 'timestamp' | 'oldKey'>
  get?: Omit<NetKey, 'key' | 'minSecurity' | 'name' | 'phase' | 'timestamp' | 'oldKey'>
}

export interface AppKeysOptions {
  boundNetKey: number
}

export interface AppKeysResults {
  appkeys: Array<AppKey>
}

export interface AppKeyOptions {
  add?: Partial<Omit<AppKey, 'index' | 'key' | 'name'>>
  del?: Omit<AppKey, 'boundNetKey' | 'key' | 'name'>
  get?: Omit<AppKey, 'boundNetKey' | 'key' | 'name'>
}

export interface GroupsResults {
  groups: Array<MeshGroup>
}

export interface GroupOptions {
  add?: Omit<MeshGroup, 'name' | 'addressHex'>
  del?: Omit<MeshGroup, 'name' | 'addressHex'>
  get?: Omit<MeshGroup, 'name' | 'addressHex'>
}

export interface NodesResults {
  nodes: Array<MeshNode>
}

export interface NodeDefaultTTL {
  ttl: number;
}

export interface NodeNetworkTransmit {
  count: number
  intervalSteps: number
}

export interface NodeNetkey {
  netkeyIndex: number
}

export interface NodeNetkeys {
  netkeyIndexes: Array<number>
}

export interface NodeAppkey {
  netkeyIndex: number
  appkeyIndex: number
}

export interface NodeAppkeys {
  netkeyIndex: number
  appkeyIndexes: Array<number>
}

export interface ModelAppkey {
  elementAddress: number;
  modelId: number;
  appkeyIndex: number;
}

export interface ModelAppkeys {
  elementAddress: number;
  modelId: number;
  appkeyIndexs: Array<number>;
}

export interface HeartbeatPub {
  address: number
  count: number
  period: number
  ttl: number
  netkeyIndex: number
  features: {
    friend: number
    proxy: number
    relay: number
    lowPower: number
  }
}

export interface HeartbeatSub {
  srcAddress: number
  dstAddress: number
  period: number
  count: number
  minHops: number
  maxHops: number
}

export interface NodeOptions extends TimeoutOptions {
  unicastAddress: number
  get?: Object
  defaultTTL?: { set?: NodeDefaultTTL }
  networkTransmit?: { set?: NodeNetworkTransmit }
  netkey?: {
    add?: NodeNetkey
    del?: NodeNetkey
    get?: Omit<NodeNetkeys, 'netkeyIndexes'>
  }
  appkey?: {
    add?: Omit<NodeAppkey, 'netkeyIndex'>
    del?: Omit<NodeAppkey, 'netkeyIndex'>
    get?: Partial<Omit<NodeAppkeys, 'appkeyIndexes'>>
  },
  heartbeat?: {
    pub?: { set?: PartialAny<Omit<HeartbeatPub, 'features'>, 'address' | 'count' | 'netkeyIndex'> }
    sub?: { set?: Omit<HeartbeatSub, 'count' | 'minHops' | 'maxHops'> }
  },
  bind?: {
    add?: ModelAppkey
    del?: ModelAppkey
    get?: Omit<ModelAppkeys, "appkeyIndexs">
  }
}

export interface NodeResults {
  get?: MeshNode
  defaultTTL?: NodeDefaultTTL
  networkTransmit?: NodeNetworkTransmit
  netkey?: {
    add?: NodeNetkey
    del?: NodeNetkey
    get?: NodeNetkeys
  }
  appkey?: {
    add?: NodeAppkey
    del?: NodeAppkey
    get?: NodeAppkeys
  },
  heartbeat?: {
    pub?: HeartbeatPub
    sub?: HeartbeatSub
  },
  bind?: {
    add?: ModelAppkey
    del?: ModelAppkey
    get?: ModelAppkeys
  }
}

export interface ModelOptions extends TimeoutOptions {
  elementAddress: number
  appkeyIndex: number
  onoff?: {
    set?: OnOffModel
  },
  sensor?: {
    propertyId: number
    get?: Omit<SensorModel, 'bytes'>
    property?: SensorPropertyModel
    properties?: SensorPropertiesModel
  }
}

export interface ModelResults {
  onoff?: OnOffModel,
  sensor?: {
    get?: SensorModel
    property?: SensorPropertyModel
    properties?: SensorPropertiesModel
  }
}

export interface MeshDevice {
  address: string
  name: string
  provisioned: boolean
  rssi: number
  uuid?: string
}

export interface ScanOptions extends TimeoutOptions {
  filter?: 'all' | 'provisioned' | 'unprovisioned'
}

export interface ScanResults {
  devices: Array<MeshDevice>
}

export interface IdentifyOptions extends TimeoutOptions {
  address: string;
  uuid: string;
}

export interface IdentifyResults {
  numberOfElements: number;
  availableOOBTypes: Array<string>;
  algorithms: number;
  publicKeyType: number;
  staticOobTypes: number;
  outputOobSize: number;
  outputOobActions: number;
  inputOobSize: number;
  inputOobActions: number;
}

export interface ProvisionOptions extends TimeoutOptions {
  address: string;
  uuid: string;
}

export interface ProvisionResults {
  completed: boolean;
  uuid: string;
  unicastAddress?: number;
}

export interface CompositionOptions extends TimeoutOptions {
  unicastAddress: number
}

export interface CompositionResults {
  companyIdentifier: string;
  productIdentifier: string;
  productVersion: string;
  nodeFeaturesSupported: {
    relay: boolean;
    proxy: boolean;
    friend: boolean;
    lowPower: boolean;
  };
  elements: Array<{
    name: string;
    elementAddress: number;
    sigModelCount: number;
    vendorModelCount: number;
    location: number;
    models: Array<{
      modelId: number;
      modelName: string;
      boundAppKeyIndexes: Array<number>;
    }>;
  }>;
}

export interface OnStateChangeEvent {
  action: 'enabled' | 'connected'
  state: boolean
}

export interface OnNodeChangeEvent {
  action: 'del' | 'add' | 'heartbeat'
  unicastAddress: number
}

export interface NrfMeshPlugin {
  checkPermissions(): Promise<Permissions>;
  requestPermissions(): Promise<Permissions>;
  isBluetoothEnabled(): Promise<BluetoothState>;

  isConnected(): Promise<ConnectionState>;
  connect(options?: TimeoutOptions): Promise<void>;
  disconnect(options?: TimeoutOptions): Promise<void>;

  export(): Promise<any>
  import(options: any): Promise<void>

  netkeys(): Promise<NetKeysResults>
  netkey(options: NetKeyOptions): Promise<NetKey>

  appkeys(options?: AppKeysOptions): Promise<AppKeysResults>
  appkey(options: AppKeyOptions): Promise<AppKey>

  groups(): Promise<GroupsResults>
  group(options: GroupOptions): Promise<MeshGroup>

  nodes(): Promise<NodesResults>
  node(options: NodeOptions): Promise<NodeResults>

  model(options: ModelOptions): Promise<ModelResults>

  init(): Promise<any>;
  devices(options?: ScanOptions): Promise<ScanResults>;
  identify(options: IdentifyOptions): Promise<IdentifyResults>;
  provision(options: ProvisionOptions): Promise<ProvisionResults>;
  composition(options: CompositionOptions): Promise<CompositionResults>;

  addListener(
    event: 'state',
    callback: (e: OnStateChangeEvent) => void,
  ): Promise<PluginListenerHandle>;
  addListener(
    event: 'node',
    callback: (e: OnNodeChangeEvent) => void,
  ): Promise<PluginListenerHandle>;
}
