# auto-nrfmesh

nrf mesh plugin

## Install

To use npm

```bash
npm install @autorunify/capacitor-nrfmesh
````

To use yarn

```bash
yarn add @autorunify/capacitor-nrfmesh
```

Sync native files

```bash
npx cap sync
```

## API

<docgen-index>

* [`checkPermissions()`](#checkpermissions)
* [`requestPermissions()`](#requestpermissions)
* [`isBluetoothEnabled()`](#isbluetoothenabled)
* [`isConnected()`](#isconnected)
* [`connect(...)`](#connect)
* [`disconnect(...)`](#disconnect)
* [`export()`](#export)
* [`import(...)`](#import)
* [`netkeys()`](#netkeys)
* [`netkey(...)`](#netkey)
* [`appkeys(...)`](#appkeys)
* [`appkey(...)`](#appkey)
* [`groups()`](#groups)
* [`group(...)`](#group)
* [`nodes()`](#nodes)
* [`node(...)`](#node)
* [`model(...)`](#model)
* [`init()`](#init)
* [`kill()`](#kill)
* [`devices(...)`](#devices)
* [`identify(...)`](#identify)
* [`provision(...)`](#provision)
* [`composition(...)`](#composition)
* [`addListener('state', ...)`](#addlistenerstate-)
* [`addListener('node', ...)`](#addlistenernode-)
* [Interfaces](#interfaces)
* [Type Aliases](#type-aliases)

</docgen-index>

<docgen-api>
<!--Update the source file JSDoc comments and rerun docgen to update the docs below-->

### checkPermissions()

```typescript
checkPermissions() => Promise<Permissions>
```

**Returns:** <code>Promise&lt;<a href="#permissions">Permissions</a>&gt;</code>

--------------------


### requestPermissions()

```typescript
requestPermissions() => Promise<Permissions>
```

**Returns:** <code>Promise&lt;<a href="#permissions">Permissions</a>&gt;</code>

--------------------


### isBluetoothEnabled()

```typescript
isBluetoothEnabled() => Promise<BluetoothState>
```

**Returns:** <code>Promise&lt;<a href="#bluetoothstate">BluetoothState</a>&gt;</code>

--------------------


### isConnected()

```typescript
isConnected() => Promise<ConnectionState>
```

**Returns:** <code>Promise&lt;<a href="#connectionstate">ConnectionState</a>&gt;</code>

--------------------


### connect(...)

```typescript
connect(options?: ConnectOptions | undefined) => Promise<void>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code><a href="#connectoptions">ConnectOptions</a></code> |

--------------------


### disconnect(...)

```typescript
disconnect(options?: TimeoutOptions | undefined) => Promise<void>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code><a href="#timeoutoptions">TimeoutOptions</a></code> |

--------------------


### export()

```typescript
export() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### import(...)

```typescript
import(options: any) => Promise<void>
```

| Param         | Type             |
| ------------- | ---------------- |
| **`options`** | <code>any</code> |

--------------------


### netkeys()

```typescript
netkeys() => Promise<NetKeysResults>
```

**Returns:** <code>Promise&lt;<a href="#netkeysresults">NetKeysResults</a>&gt;</code>

--------------------


### netkey(...)

```typescript
netkey(options: NetKeyOptions) => Promise<NetKey>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#netkeyoptions">NetKeyOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#netkey">NetKey</a>&gt;</code>

--------------------


### appkeys(...)

```typescript
appkeys(options?: AppKeysOptions | undefined) => Promise<AppKeysResults>
```

| Param         | Type                                                      |
| ------------- | --------------------------------------------------------- |
| **`options`** | <code><a href="#appkeysoptions">AppKeysOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#appkeysresults">AppKeysResults</a>&gt;</code>

--------------------


### appkey(...)

```typescript
appkey(options: AppKeyOptions) => Promise<AppKey>
```

| Param         | Type                                                    |
| ------------- | ------------------------------------------------------- |
| **`options`** | <code><a href="#appkeyoptions">AppKeyOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#appkey">AppKey</a>&gt;</code>

--------------------


### groups()

```typescript
groups() => Promise<GroupsResults>
```

**Returns:** <code>Promise&lt;<a href="#groupsresults">GroupsResults</a>&gt;</code>

--------------------


### group(...)

```typescript
group(options: GroupOptions) => Promise<MeshGroup>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#groupoptions">GroupOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#meshgroup">MeshGroup</a>&gt;</code>

--------------------


### nodes()

```typescript
nodes() => Promise<NodesResults>
```

**Returns:** <code>Promise&lt;<a href="#nodesresults">NodesResults</a>&gt;</code>

--------------------


### node(...)

```typescript
node(options: NodeOptions) => Promise<NodeResults>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#nodeoptions">NodeOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#noderesults">NodeResults</a>&gt;</code>

--------------------


### model(...)

```typescript
model(options: ModelOptions) => Promise<ModelResults>
```

| Param         | Type                                                  |
| ------------- | ----------------------------------------------------- |
| **`options`** | <code><a href="#modeloptions">ModelOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#modelresults">ModelResults</a>&gt;</code>

--------------------


### init()

```typescript
init() => Promise<any>
```

**Returns:** <code>Promise&lt;any&gt;</code>

--------------------


### kill()

```typescript
kill() => Promise<void>
```

--------------------


### devices(...)

```typescript
devices(options?: ScanOptions | undefined) => Promise<ScanResults>
```

| Param         | Type                                                |
| ------------- | --------------------------------------------------- |
| **`options`** | <code><a href="#scanoptions">ScanOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#scanresults">ScanResults</a>&gt;</code>

--------------------


### identify(...)

```typescript
identify(options: IdentifyOptions) => Promise<IdentifyResults>
```

| Param         | Type                                                        |
| ------------- | ----------------------------------------------------------- |
| **`options`** | <code><a href="#identifyoptions">IdentifyOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#identifyresults">IdentifyResults</a>&gt;</code>

--------------------


### provision(...)

```typescript
provision(options: ProvisionOptions) => Promise<ProvisionResults>
```

| Param         | Type                                                          |
| ------------- | ------------------------------------------------------------- |
| **`options`** | <code><a href="#provisionoptions">ProvisionOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#provisionresults">ProvisionResults</a>&gt;</code>

--------------------


### composition(...)

```typescript
composition(options: CompositionOptions) => Promise<CompositionResults>
```

| Param         | Type                                                              |
| ------------- | ----------------------------------------------------------------- |
| **`options`** | <code><a href="#compositionoptions">CompositionOptions</a></code> |

**Returns:** <code>Promise&lt;<a href="#compositionresults">CompositionResults</a>&gt;</code>

--------------------


### addListener('state', ...)

```typescript
addListener(event: 'state', callback: (e: OnStateChangeEvent) => void) => Promise<PluginListenerHandle>
```

| Param          | Type                                                                              |
| -------------- | --------------------------------------------------------------------------------- |
| **`event`**    | <code>'state'</code>                                                              |
| **`callback`** | <code>(e: <a href="#onstatechangeevent">OnStateChangeEvent</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### addListener('node', ...)

```typescript
addListener(event: 'node', callback: (e: OnNodeChangeEvent) => void) => Promise<PluginListenerHandle>
```

| Param          | Type                                                                            |
| -------------- | ------------------------------------------------------------------------------- |
| **`event`**    | <code>'node'</code>                                                             |
| **`callback`** | <code>(e: <a href="#onnodechangeevent">OnNodeChangeEvent</a>) =&gt; void</code> |

**Returns:** <code>Promise&lt;<a href="#pluginlistenerhandle">PluginListenerHandle</a>&gt;</code>

--------------------


### Interfaces


#### BluetoothState

| Prop          | Type                 |
| ------------- | -------------------- |
| **`enabled`** | <code>boolean</code> |


#### ConnectionState

| Prop            | Type                 |
| --------------- | -------------------- |
| **`connected`** | <code>boolean</code> |


#### ConnectOptions

| Prop          | Type                |
| ------------- | ------------------- |
| **`address`** | <code>string</code> |


#### TimeoutOptions

| Prop          | Type                |
| ------------- | ------------------- |
| **`timeout`** | <code>number</code> |


#### NetKeysResults

| Prop          | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`netkeys`** | <code><a href="#array">Array</a>&lt;<a href="#netkey">NetKey</a>&gt;</code> |


#### Array

| Prop         | Type                | Description                                                                                            |
| ------------ | ------------------- | ------------------------------------------------------------------------------------------------------ |
| **`length`** | <code>number</code> | Gets or sets the length of the array. This is a number one higher than the highest index in the array. |

| Method             | Signature                                                                                                                     | Description                                                                                                                                                                                                                                 |
| ------------------ | ----------------------------------------------------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **toString**       | () =&gt; string                                                                                                               | Returns a string representation of an array.                                                                                                                                                                                                |
| **toLocaleString** | () =&gt; string                                                                                                               | Returns a string representation of an array. The elements are converted to string using their toLocalString methods.                                                                                                                        |
| **pop**            | () =&gt; T \| undefined                                                                                                       | Removes the last element from an array and returns it. If the array is empty, undefined is returned and the array is not modified.                                                                                                          |
| **push**           | (...items: T[]) =&gt; number                                                                                                  | Appends new elements to the end of an array, and returns the new length of the array.                                                                                                                                                       |
| **concat**         | (...items: <a href="#concatarray">ConcatArray</a>&lt;T&gt;[]) =&gt; T[]                                                       | Combines two or more arrays. This method returns a new array without modifying any existing arrays.                                                                                                                                         |
| **concat**         | (...items: (T \| <a href="#concatarray">ConcatArray</a>&lt;T&gt;)[]) =&gt; T[]                                                | Combines two or more arrays. This method returns a new array without modifying any existing arrays.                                                                                                                                         |
| **join**           | (separator?: string \| undefined) =&gt; string                                                                                | Adds all the elements of an array into a string, separated by the specified separator string.                                                                                                                                               |
| **reverse**        | () =&gt; T[]                                                                                                                  | Reverses the elements in an array in place. This method mutates the array and returns a reference to the same array.                                                                                                                        |
| **shift**          | () =&gt; T \| undefined                                                                                                       | Removes the first element from an array and returns it. If the array is empty, undefined is returned and the array is not modified.                                                                                                         |
| **slice**          | (start?: number \| undefined, end?: number \| undefined) =&gt; T[]                                                            | Returns a copy of a section of an array. For both start and end, a negative index can be used to indicate an offset from the end of the array. For example, -2 refers to the second to last element of the array.                           |
| **sort**           | (compareFn?: ((a: T, b: T) =&gt; number) \| undefined) =&gt; this                                                             | Sorts an array in place. This method mutates the array and returns a reference to the same array.                                                                                                                                           |
| **splice**         | (start: number, deleteCount?: number \| undefined) =&gt; T[]                                                                  | Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.                                                                                                                      |
| **splice**         | (start: number, deleteCount: number, ...items: T[]) =&gt; T[]                                                                 | Removes elements from an array and, if necessary, inserts new elements in their place, returning the deleted elements.                                                                                                                      |
| **unshift**        | (...items: T[]) =&gt; number                                                                                                  | Inserts new elements at the start of an array, and returns the new length of the array.                                                                                                                                                     |
| **indexOf**        | (searchElement: T, fromIndex?: number \| undefined) =&gt; number                                                              | Returns the index of the first occurrence of a value in an array, or -1 if it is not present.                                                                                                                                               |
| **lastIndexOf**    | (searchElement: T, fromIndex?: number \| undefined) =&gt; number                                                              | Returns the index of the last occurrence of a specified value in an array, or -1 if it is not present.                                                                                                                                      |
| **every**          | &lt;S extends T&gt;(predicate: (value: T, index: number, array: T[]) =&gt; value is S, thisArg?: any) =&gt; this is S[]       | Determines whether all the members of an array satisfy the specified test.                                                                                                                                                                  |
| **every**          | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; boolean                                 | Determines whether all the members of an array satisfy the specified test.                                                                                                                                                                  |
| **some**           | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; boolean                                 | Determines whether the specified callback function returns true for any element of an array.                                                                                                                                                |
| **forEach**        | (callbackfn: (value: T, index: number, array: T[]) =&gt; void, thisArg?: any) =&gt; void                                      | Performs the specified action for each element in an array.                                                                                                                                                                                 |
| **map**            | &lt;U&gt;(callbackfn: (value: T, index: number, array: T[]) =&gt; U, thisArg?: any) =&gt; U[]                                 | Calls a defined callback function on each element of an array, and returns an array that contains the results.                                                                                                                              |
| **filter**         | &lt;S extends T&gt;(predicate: (value: T, index: number, array: T[]) =&gt; value is S, thisArg?: any) =&gt; S[]               | Returns the elements of an array that meet the condition specified in a callback function.                                                                                                                                                  |
| **filter**         | (predicate: (value: T, index: number, array: T[]) =&gt; unknown, thisArg?: any) =&gt; T[]                                     | Returns the elements of an array that meet the condition specified in a callback function.                                                                                                                                                  |
| **reduce**         | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T) =&gt; T                           | Calls the specified callback function for all the elements in an array. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function.                      |
| **reduce**         | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T, initialValue: T) =&gt; T          |                                                                                                                                                                                                                                             |
| **reduce**         | &lt;U&gt;(callbackfn: (previousValue: U, currentValue: T, currentIndex: number, array: T[]) =&gt; U, initialValue: U) =&gt; U | Calls the specified callback function for all the elements in an array. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function.                      |
| **reduceRight**    | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T) =&gt; T                           | Calls the specified callback function for all the elements in an array, in descending order. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function. |
| **reduceRight**    | (callbackfn: (previousValue: T, currentValue: T, currentIndex: number, array: T[]) =&gt; T, initialValue: T) =&gt; T          |                                                                                                                                                                                                                                             |
| **reduceRight**    | &lt;U&gt;(callbackfn: (previousValue: U, currentValue: T, currentIndex: number, array: T[]) =&gt; U, initialValue: U) =&gt; U | Calls the specified callback function for all the elements in an array, in descending order. The return value of the callback function is the accumulated result, and is provided as an argument in the next call to the callback function. |


#### ConcatArray

| Prop         | Type                |
| ------------ | ------------------- |
| **`length`** | <code>number</code> |

| Method    | Signature                                                          |
| --------- | ------------------------------------------------------------------ |
| **join**  | (separator?: string \| undefined) =&gt; string                     |
| **slice** | (start?: number \| undefined, end?: number \| undefined) =&gt; T[] |


#### NetKey

| Prop              | Type                |
| ----------------- | ------------------- |
| **`index`**       | <code>number</code> |
| **`key`**         | <code>string</code> |
| **`minSecurity`** | <code>string</code> |
| **`name`**        | <code>string</code> |
| **`phase`**       | <code>number</code> |
| **`timestamp`**   | <code>string</code> |
| **`oldKey`**      | <code>string</code> |


#### NetKeyOptions

| Prop      | Type                                                                                                                                                         |
| --------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`add`** | <code><a href="#omit">Omit</a>&lt;<a href="#netkey">NetKey</a>, 'index' \| 'key' \| 'minSecurity' \| 'name' \| 'phase' \| 'timestamp' \| 'oldKey'&gt;</code> |
| **`del`** | <code><a href="#omit">Omit</a>&lt;<a href="#netkey">NetKey</a>, 'key' \| 'minSecurity' \| 'name' \| 'phase' \| 'timestamp' \| 'oldKey'&gt;</code>            |
| **`get`** | <code><a href="#omit">Omit</a>&lt;<a href="#netkey">NetKey</a>, 'key' \| 'minSecurity' \| 'name' \| 'phase' \| 'timestamp' \| 'oldKey'&gt;</code>            |


#### AppKeysResults

| Prop          | Type                                                                        |
| ------------- | --------------------------------------------------------------------------- |
| **`appkeys`** | <code><a href="#array">Array</a>&lt;<a href="#appkey">AppKey</a>&gt;</code> |


#### AppKey

| Prop              | Type                |
| ----------------- | ------------------- |
| **`index`**       | <code>number</code> |
| **`boundNetKey`** | <code>number</code> |
| **`key`**         | <code>string</code> |
| **`name`**        | <code>string</code> |


#### AppKeysOptions

| Prop              | Type                |
| ----------------- | ------------------- |
| **`boundNetKey`** | <code>number</code> |


#### AppKeyOptions

| Prop      | Type                                                                                                                                        |
| --------- | ------------------------------------------------------------------------------------------------------------------------------------------- |
| **`add`** | <code><a href="#partial">Partial</a>&lt;<a href="#omit">Omit</a>&lt;<a href="#appkey">AppKey</a>, 'index' \| 'key' \| 'name'&gt;&gt;</code> |
| **`del`** | <code><a href="#omit">Omit</a>&lt;<a href="#appkey">AppKey</a>, 'boundNetKey' \| 'key' \| 'name'&gt;</code>                                 |
| **`get`** | <code><a href="#omit">Omit</a>&lt;<a href="#appkey">AppKey</a>, 'boundNetKey' \| 'key' \| 'name'&gt;</code>                                 |


#### GroupsResults

| Prop         | Type                                                                              |
| ------------ | --------------------------------------------------------------------------------- |
| **`groups`** | <code><a href="#array">Array</a>&lt;<a href="#meshgroup">MeshGroup</a>&gt;</code> |


#### MeshGroup

| Prop             | Type                |
| ---------------- | ------------------- |
| **`name`**       | <code>string</code> |
| **`address`**    | <code>number</code> |
| **`addressHex`** | <code>string</code> |


#### GroupOptions

| Prop      | Type                                                                                                    |
| --------- | ------------------------------------------------------------------------------------------------------- |
| **`add`** | <code><a href="#omit">Omit</a>&lt;<a href="#meshgroup">MeshGroup</a>, 'name' \| 'addressHex'&gt;</code> |
| **`del`** | <code><a href="#omit">Omit</a>&lt;<a href="#meshgroup">MeshGroup</a>, 'name' \| 'addressHex'&gt;</code> |
| **`get`** | <code><a href="#omit">Omit</a>&lt;<a href="#meshgroup">MeshGroup</a>, 'name' \| 'addressHex'&gt;</code> |


#### NodesResults

| Prop        | Type                                                                            |
| ----------- | ------------------------------------------------------------------------------- |
| **`nodes`** | <code><a href="#array">Array</a>&lt;<a href="#meshnode">MeshNode</a>&gt;</code> |


#### MeshNode

| Prop                  | Type                                                                                                                                                                                                                                                                                |
| --------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`name`**            | <code>string</code>                                                                                                                                                                                                                                                                 |
| **`deviceKey`**       | <code>string</code>                                                                                                                                                                                                                                                                 |
| **`unicastAddress`**  | <code>number</code>                                                                                                                                                                                                                                                                 |
| **`security`**        | <code>string</code>                                                                                                                                                                                                                                                                 |
| **`ttl`**             | <code>number</code>                                                                                                                                                                                                                                                                 |
| **`netkeys`**         | <code><a href="#array">Array</a>&lt;{ index: number, updated: boolean }&gt;</code>                                                                                                                                                                                                  |
| **`appkeys`**         | <code><a href="#array">Array</a>&lt;{ index: number, updated: boolean }&gt;</code>                                                                                                                                                                                                  |
| **`elements`**        | <code><a href="#array">Array</a>&lt;{ name: string elementAddress: number models: <a href="#array">Array</a>&lt;{ modelId: number appkeys: <a href="#array">Array</a>&lt;number&gt; subscriptions: <a href="#array">Array</a>&lt;number&gt; publication?: number }&gt; }&gt;</code> |
| **`features`**        | <code>{ friend: number; lowPower: number; proxy: number; relay: number; }</code>                                                                                                                                                                                                    |
| **`networkTransmit`** | <code>{ count: number; interval: number; steps: number; }</code>                                                                                                                                                                                                                    |


#### NodeResults

| Prop                  | Type                                                                                                                                                         |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`get`**             | <code><a href="#meshnode">MeshNode</a></code>                                                                                                                |
| **`defaultTTL`**      | <code><a href="#nodedefaultttl">NodeDefaultTTL</a></code>                                                                                                    |
| **`networkTransmit`** | <code><a href="#nodenetworktransmit">NodeNetworkTransmit</a></code>                                                                                          |
| **`netkey`**          | <code>{ add?: <a href="#nodenetkey">NodeNetkey</a>; del?: <a href="#nodenetkey">NodeNetkey</a>; get?: <a href="#nodenetkeys">NodeNetkeys</a>; }</code>       |
| **`appkey`**          | <code>{ add?: <a href="#nodeappkey">NodeAppkey</a>; del?: <a href="#nodeappkey">NodeAppkey</a>; get?: <a href="#nodeappkeys">NodeAppkeys</a>; }</code>       |
| **`heartbeat`**       | <code>{ pub?: <a href="#heartbeatpub">HeartbeatPub</a>; sub?: <a href="#heartbeatsub">HeartbeatSub</a>; }</code>                                             |
| **`bind`**            | <code>{ add?: <a href="#modelappkey">ModelAppkey</a>; del?: <a href="#modelappkey">ModelAppkey</a>; get?: <a href="#modelappkeys">ModelAppkeys</a>; }</code> |


#### NodeDefaultTTL

| Prop      | Type                |
| --------- | ------------------- |
| **`ttl`** | <code>number</code> |


#### NodeNetworkTransmit

| Prop                | Type                |
| ------------------- | ------------------- |
| **`count`**         | <code>number</code> |
| **`intervalSteps`** | <code>number</code> |


#### NodeNetkey

| Prop              | Type                |
| ----------------- | ------------------- |
| **`netkeyIndex`** | <code>number</code> |


#### NodeNetkeys

| Prop                | Type                                                  |
| ------------------- | ----------------------------------------------------- |
| **`netkeyIndexes`** | <code><a href="#array">Array</a>&lt;number&gt;</code> |


#### NodeAppkey

| Prop              | Type                |
| ----------------- | ------------------- |
| **`netkeyIndex`** | <code>number</code> |
| **`appkeyIndex`** | <code>number</code> |


#### NodeAppkeys

| Prop                | Type                                                  |
| ------------------- | ----------------------------------------------------- |
| **`netkeyIndex`**   | <code>number</code>                                   |
| **`appkeyIndexes`** | <code><a href="#array">Array</a>&lt;number&gt;</code> |


#### HeartbeatPub

| Prop              | Type                                                                             |
| ----------------- | -------------------------------------------------------------------------------- |
| **`address`**     | <code>number</code>                                                              |
| **`count`**       | <code>number</code>                                                              |
| **`period`**      | <code>number</code>                                                              |
| **`ttl`**         | <code>number</code>                                                              |
| **`netkeyIndex`** | <code>number</code>                                                              |
| **`features`**    | <code>{ friend: number; proxy: number; relay: number; lowPower: number; }</code> |


#### HeartbeatSub

| Prop             | Type                |
| ---------------- | ------------------- |
| **`srcAddress`** | <code>number</code> |
| **`dstAddress`** | <code>number</code> |
| **`period`**     | <code>number</code> |
| **`count`**      | <code>number</code> |
| **`minHops`**    | <code>number</code> |
| **`maxHops`**    | <code>number</code> |


#### ModelAppkey

| Prop                 | Type                |
| -------------------- | ------------------- |
| **`elementAddress`** | <code>number</code> |
| **`modelId`**        | <code>number</code> |
| **`appkeyIndex`**    | <code>number</code> |


#### ModelAppkeys

| Prop                 | Type                                                  |
| -------------------- | ----------------------------------------------------- |
| **`elementAddress`** | <code>number</code>                                   |
| **`modelId`**        | <code>number</code>                                   |
| **`appkeyIndexs`**   | <code><a href="#array">Array</a>&lt;number&gt;</code> |


#### NodeOptions

| Prop                  | Type                                                                                                                                                                                                                                                                                                                                        |
| --------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`unicastAddress`**  | <code>number</code>                                                                                                                                                                                                                                                                                                                         |
| **`get`**             | <code><a href="#object">Object</a></code>                                                                                                                                                                                                                                                                                                   |
| **`defaultTTL`**      | <code>{ set?: <a href="#nodedefaultttl">NodeDefaultTTL</a>; }</code>                                                                                                                                                                                                                                                                        |
| **`networkTransmit`** | <code>{ set?: <a href="#nodenetworktransmit">NodeNetworkTransmit</a>; }</code>                                                                                                                                                                                                                                                              |
| **`netkey`**          | <code>{ add?: <a href="#nodenetkey">NodeNetkey</a>; del?: <a href="#nodenetkey">NodeNetkey</a>; get?: <a href="#omit">Omit</a>&lt;<a href="#nodenetkeys">NodeNetkeys</a>, 'netkeyIndexes'&gt;; }</code>                                                                                                                                     |
| **`appkey`**          | <code>{ add?: <a href="#omit">Omit</a>&lt;<a href="#nodeappkey">NodeAppkey</a>, 'netkeyIndex'&gt;; del?: <a href="#omit">Omit</a>&lt;<a href="#nodeappkey">NodeAppkey</a>, 'netkeyIndex'&gt;; get?: <a href="#partial">Partial</a>&lt;<a href="#omit">Omit</a>&lt;<a href="#nodeappkeys">NodeAppkeys</a>, 'appkeyIndexes'&gt;&gt;; }</code> |
| **`heartbeat`**       | <code>{ pub?: { set?: <a href="#partialany">PartialAny</a>&lt;<a href="#omit">Omit</a>&lt;<a href="#heartbeatpub">HeartbeatPub</a>, 'features'&gt;, 'address' \| 'netkeyIndex' \| 'count'&gt;; }; sub?: { set?: <a href="#omit">Omit</a>&lt;<a href="#heartbeatsub">HeartbeatSub</a>, 'count' \| 'minHops' \| 'maxHops'&gt;; }; }</code>    |
| **`bind`**            | <code>{ add?: <a href="#modelappkey">ModelAppkey</a>; del?: <a href="#modelappkey">ModelAppkey</a>; get?: <a href="#omit">Omit</a>&lt;<a href="#modelappkeys">ModelAppkeys</a>, 'appkeyIndexs'&gt;; }</code>                                                                                                                                |


#### Object

Provides functionality common to all JavaScript objects.

| Prop              | Type                                          | Description                                                                                                                                |
| ----------------- | --------------------------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------ |
| **`constructor`** | <code><a href="#function">Function</a></code> | The initial value of <a href="#object">Object</a>.prototype.constructor is the standard built-in <a href="#object">Object</a> constructor. |

| Method                   | Signature                                                 | Description                                                              |
| ------------------------ | --------------------------------------------------------- | ------------------------------------------------------------------------ |
| **toString**             | () =&gt; string                                           | Returns a string representation of an object.                            |
| **toLocaleString**       | () =&gt; string                                           | Returns a date converted to a string using the current locale.           |
| **valueOf**              | () =&gt; <a href="#object">Object</a>                     | Returns the primitive value of the specified object.                     |
| **hasOwnProperty**       | (v: <a href="#propertykey">PropertyKey</a>) =&gt; boolean | Determines whether an object has a property with the specified name.     |
| **isPrototypeOf**        | (v: <a href="#object">Object</a>) =&gt; boolean           | Determines whether an object exists in another object's prototype chain. |
| **propertyIsEnumerable** | (v: <a href="#propertykey">PropertyKey</a>) =&gt; boolean | Determines whether a specified property is enumerable.                   |


#### Function

Creates a new function.

| Prop            | Type                                          |
| --------------- | --------------------------------------------- |
| **`prototype`** | <code>any</code>                              |
| **`length`**    | <code>number</code>                           |
| **`arguments`** | <code>any</code>                              |
| **`caller`**    | <code><a href="#function">Function</a></code> |

| Method       | Signature                                                                            | Description                                                                                                                                                                                                              |
| ------------ | ------------------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **apply**    | (this: <a href="#function">Function</a>, thisArg: any, argArray?: any) =&gt; any     | Calls the function, substituting the specified object for the this value of the function, and the specified array for the arguments of the function.                                                                     |
| **call**     | (this: <a href="#function">Function</a>, thisArg: any, ...argArray: any[]) =&gt; any | Calls a method of an object, substituting another object for the current object.                                                                                                                                         |
| **bind**     | (this: <a href="#function">Function</a>, thisArg: any, ...argArray: any[]) =&gt; any | For a given function, creates a bound function that has the same body as the original function. The this object of the bound function is associated with the specified object, and has the specified initial parameters. |
| **toString** | () =&gt; string                                                                      | Returns a string representation of a function.                                                                                                                                                                           |


#### FunctionDeclaration

| Prop     | Type                                              | Description                                                                                 |
| -------- | ------------------------------------------------- | ------------------------------------------------------------------------------------------- |
| **`id`** | <code><a href="#identifier">Identifier</a></code> | It is null when a function declaration is a part of the `export default function` statement |


#### Identifier

| Prop       | Type                                                |
| ---------- | --------------------------------------------------- |
| **`type`** | <code>'<a href="#identifier">Identifier</a>'</code> |
| **`name`** | <code>string</code>                                 |


#### FunctionExpression

| Prop       | Type                                                                |
| ---------- | ------------------------------------------------------------------- |
| **`id`**   | <code><a href="#identifier">Identifier</a> \| null</code>           |
| **`type`** | <code>'<a href="#functionexpression">FunctionExpression</a>'</code> |
| **`body`** | <code><a href="#blockstatement">BlockStatement</a></code>           |


#### BlockStatement

| Prop                | Type                                                        |
| ------------------- | ----------------------------------------------------------- |
| **`type`**          | <code>'<a href="#blockstatement">BlockStatement</a>'</code> |
| **`body`**          | <code>Statement[]</code>                                    |
| **`innerComments`** | <code>Comment[]</code>                                      |


#### ExpressionStatement

| Prop             | Type                                                                  |
| ---------------- | --------------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#expressionstatement">ExpressionStatement</a>'</code> |
| **`expression`** | <code><a href="#expression">Expression</a></code>                     |


#### ExpressionMap

| Prop                           | Type                                                                          |
| ------------------------------ | ----------------------------------------------------------------------------- |
| **`ArrayExpression`**          | <code><a href="#arrayexpression">ArrayExpression</a></code>                   |
| **`ArrowFunctionExpression`**  | <code><a href="#arrowfunctionexpression">ArrowFunctionExpression</a></code>   |
| **`AssignmentExpression`**     | <code><a href="#assignmentexpression">AssignmentExpression</a></code>         |
| **`AwaitExpression`**          | <code><a href="#awaitexpression">AwaitExpression</a></code>                   |
| **`BinaryExpression`**         | <code><a href="#binaryexpression">BinaryExpression</a></code>                 |
| **`CallExpression`**           | <code><a href="#callexpression">CallExpression</a></code>                     |
| **`ChainExpression`**          | <code><a href="#chainexpression">ChainExpression</a></code>                   |
| **`ClassExpression`**          | <code><a href="#classexpression">ClassExpression</a></code>                   |
| **`ConditionalExpression`**    | <code><a href="#conditionalexpression">ConditionalExpression</a></code>       |
| **`FunctionExpression`**       | <code><a href="#functionexpression">FunctionExpression</a></code>             |
| **`Identifier`**               | <code><a href="#identifier">Identifier</a></code>                             |
| **`ImportExpression`**         | <code><a href="#importexpression">ImportExpression</a></code>                 |
| **`Literal`**                  | <code><a href="#literal">Literal</a></code>                                   |
| **`LogicalExpression`**        | <code><a href="#logicalexpression">LogicalExpression</a></code>               |
| **`MemberExpression`**         | <code><a href="#memberexpression">MemberExpression</a></code>                 |
| **`MetaProperty`**             | <code><a href="#metaproperty">MetaProperty</a></code>                         |
| **`NewExpression`**            | <code><a href="#newexpression">NewExpression</a></code>                       |
| **`ObjectExpression`**         | <code><a href="#objectexpression">ObjectExpression</a></code>                 |
| **`SequenceExpression`**       | <code><a href="#sequenceexpression">SequenceExpression</a></code>             |
| **`TaggedTemplateExpression`** | <code><a href="#taggedtemplateexpression">TaggedTemplateExpression</a></code> |
| **`TemplateLiteral`**          | <code><a href="#templateliteral">TemplateLiteral</a></code>                   |
| **`ThisExpression`**           | <code><a href="#thisexpression">ThisExpression</a></code>                     |
| **`UnaryExpression`**          | <code><a href="#unaryexpression">UnaryExpression</a></code>                   |
| **`UpdateExpression`**         | <code><a href="#updateexpression">UpdateExpression</a></code>                 |
| **`YieldExpression`**          | <code><a href="#yieldexpression">YieldExpression</a></code>                   |


#### ArrayExpression

| Prop           | Type                                                                                                                                      |
| -------------- | ----------------------------------------------------------------------------------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#arrayexpression">ArrayExpression</a>'</code>                                                                             |
| **`elements`** | <code><a href="#array">Array</a>&lt;<a href="#expression">Expression</a> \| <a href="#spreadelement">SpreadElement</a> \| null&gt;</code> |


#### SpreadElement

| Prop           | Type                                                      |
| -------------- | --------------------------------------------------------- |
| **`type`**     | <code>'<a href="#spreadelement">SpreadElement</a>'</code> |
| **`argument`** | <code><a href="#expression">Expression</a></code>         |


#### ArrowFunctionExpression

| Prop             | Type                                                                                              |
| ---------------- | ------------------------------------------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#arrowfunctionexpression">ArrowFunctionExpression</a>'</code>                     |
| **`expression`** | <code>boolean</code>                                                                              |
| **`body`**       | <code><a href="#expression">Expression</a> \| <a href="#blockstatement">BlockStatement</a></code> |


#### AssignmentExpression

| Prop           | Type                                                                    |
| -------------- | ----------------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#assignmentexpression">AssignmentExpression</a>'</code> |
| **`operator`** | <code><a href="#assignmentoperator">AssignmentOperator</a></code>       |
| **`left`**     | <code><a href="#pattern">Pattern</a></code>                             |
| **`right`**    | <code><a href="#expression">Expression</a></code>                       |


#### ObjectPattern

| Prop             | Type                                                                                                                                          |
| ---------------- | --------------------------------------------------------------------------------------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#objectpattern">ObjectPattern</a>'</code>                                                                                     |
| **`properties`** | <code><a href="#array">Array</a>&lt;<a href="#assignmentproperty">AssignmentProperty</a> \| <a href="#restelement">RestElement</a>&gt;</code> |


#### AssignmentProperty

| Prop         | Type                                        |
| ------------ | ------------------------------------------- |
| **`value`**  | <code><a href="#pattern">Pattern</a></code> |
| **`kind`**   | <code>'init'</code>                         |
| **`method`** | <code>boolean</code>                        |


#### RestElement

| Prop           | Type                                                  |
| -------------- | ----------------------------------------------------- |
| **`type`**     | <code>'<a href="#restelement">RestElement</a>'</code> |
| **`argument`** | <code><a href="#pattern">Pattern</a></code>           |


#### ArrayPattern

| Prop           | Type                                                                                  |
| -------------- | ------------------------------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#arraypattern">ArrayPattern</a>'</code>                               |
| **`elements`** | <code><a href="#array">Array</a>&lt;<a href="#pattern">Pattern</a> \| null&gt;</code> |


#### AssignmentPattern

| Prop        | Type                                                              |
| ----------- | ----------------------------------------------------------------- |
| **`type`**  | <code>'<a href="#assignmentpattern">AssignmentPattern</a>'</code> |
| **`left`**  | <code><a href="#pattern">Pattern</a></code>                       |
| **`right`** | <code><a href="#expression">Expression</a></code>                 |


#### MemberExpression

| Prop           | Type                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#memberexpression">MemberExpression</a>'</code>                                         |
| **`object`**   | <code><a href="#expression">Expression</a> \| <a href="#super">Super</a></code>                         |
| **`property`** | <code><a href="#expression">Expression</a> \| <a href="#privateidentifier">PrivateIdentifier</a></code> |
| **`computed`** | <code>boolean</code>                                                                                    |
| **`optional`** | <code>boolean</code>                                                                                    |


#### Super

| Prop       | Type                                      |
| ---------- | ----------------------------------------- |
| **`type`** | <code>'<a href="#super">Super</a>'</code> |


#### PrivateIdentifier

| Prop       | Type                                                              |
| ---------- | ----------------------------------------------------------------- |
| **`type`** | <code>'<a href="#privateidentifier">PrivateIdentifier</a>'</code> |
| **`name`** | <code>string</code>                                               |


#### AwaitExpression

| Prop           | Type                                                          |
| -------------- | ------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#awaitexpression">AwaitExpression</a>'</code> |
| **`argument`** | <code><a href="#expression">Expression</a></code>             |


#### BinaryExpression

| Prop           | Type                                                                                                    |
| -------------- | ------------------------------------------------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#binaryexpression">BinaryExpression</a>'</code>                                         |
| **`operator`** | <code><a href="#binaryoperator">BinaryOperator</a></code>                                               |
| **`left`**     | <code><a href="#expression">Expression</a> \| <a href="#privateidentifier">PrivateIdentifier</a></code> |
| **`right`**    | <code><a href="#expression">Expression</a></code>                                                       |


#### SimpleCallExpression

| Prop           | Type                                                        |
| -------------- | ----------------------------------------------------------- |
| **`type`**     | <code>'<a href="#callexpression">CallExpression</a>'</code> |
| **`optional`** | <code>boolean</code>                                        |


#### NewExpression

| Prop       | Type                                                      |
| ---------- | --------------------------------------------------------- |
| **`type`** | <code>'<a href="#newexpression">NewExpression</a>'</code> |


#### ChainExpression

| Prop             | Type                                                          |
| ---------------- | ------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#chainexpression">ChainExpression</a>'</code> |
| **`expression`** | <code><a href="#chainelement">ChainElement</a></code>         |


#### ClassExpression

| Prop       | Type                                                          |
| ---------- | ------------------------------------------------------------- |
| **`type`** | <code>'<a href="#classexpression">ClassExpression</a>'</code> |
| **`id`**   | <code><a href="#identifier">Identifier</a> \| null</code>     |


#### ConditionalExpression

| Prop             | Type                                                                      |
| ---------------- | ------------------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#conditionalexpression">ConditionalExpression</a>'</code> |
| **`test`**       | <code><a href="#expression">Expression</a></code>                         |
| **`alternate`**  | <code><a href="#expression">Expression</a></code>                         |
| **`consequent`** | <code><a href="#expression">Expression</a></code>                         |


#### ImportExpression

| Prop          | Type                                                            |
| ------------- | --------------------------------------------------------------- |
| **`type`**    | <code>'<a href="#importexpression">ImportExpression</a>'</code> |
| **`source`**  | <code><a href="#expression">Expression</a></code>               |
| **`options`** | <code><a href="#expression">Expression</a> \| null</code>       |


#### SimpleLiteral

| Prop        | Type                                             |
| ----------- | ------------------------------------------------ |
| **`type`**  | <code>'<a href="#literal">Literal</a>'</code>    |
| **`value`** | <code>string \| number \| boolean \| null</code> |
| **`raw`**   | <code>string</code>                              |


#### RegExpLiteral

| Prop        | Type                                              |
| ----------- | ------------------------------------------------- |
| **`type`**  | <code>'<a href="#literal">Literal</a>'</code>     |
| **`value`** | <code><a href="#regexp">RegExp</a> \| null</code> |
| **`regex`** | <code>{ pattern: string; flags: string; }</code>  |
| **`raw`**   | <code>string</code>                               |


#### RegExp

| Prop             | Type                 | Description                                                                                                                                                          |
| ---------------- | -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`source`**     | <code>string</code>  | Returns a copy of the text of the regular expression pattern. Read-only. The regExp argument is a Regular expression object. It can be a variable name or a literal. |
| **`global`**     | <code>boolean</code> | Returns a Boolean value indicating the state of the global flag (g) used with a regular expression. Default is false. Read-only.                                     |
| **`ignoreCase`** | <code>boolean</code> | Returns a Boolean value indicating the state of the ignoreCase flag (i) used with a regular expression. Default is false. Read-only.                                 |
| **`multiline`**  | <code>boolean</code> | Returns a Boolean value indicating the state of the multiline flag (m) used with a regular expression. Default is false. Read-only.                                  |
| **`lastIndex`**  | <code>number</code>  |                                                                                                                                                                      |

| Method      | Signature                                                                     | Description                                                                                                                   |
| ----------- | ----------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **exec**    | (string: string) =&gt; <a href="#regexpexecarray">RegExpExecArray</a> \| null | Executes a search on a string using a regular expression pattern, and returns an array containing the results of that search. |
| **test**    | (string: string) =&gt; boolean                                                | Returns a Boolean value that indicates whether or not a pattern exists in a searched string.                                  |
| **compile** | () =&gt; this                                                                 |                                                                                                                               |


#### RegExpExecArray

| Prop        | Type                |
| ----------- | ------------------- |
| **`index`** | <code>number</code> |
| **`input`** | <code>string</code> |


#### BigIntLiteral

| Prop         | Type                                          |
| ------------ | --------------------------------------------- |
| **`type`**   | <code>'<a href="#literal">Literal</a>'</code> |
| **`value`**  | <code>bigint \| null</code>                   |
| **`bigint`** | <code>string</code>                           |
| **`raw`**    | <code>string</code>                           |


#### LogicalExpression

| Prop           | Type                                                              |
| -------------- | ----------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#logicalexpression">LogicalExpression</a>'</code> |
| **`operator`** | <code><a href="#logicaloperator">LogicalOperator</a></code>       |
| **`left`**     | <code><a href="#expression">Expression</a></code>                 |
| **`right`**    | <code><a href="#expression">Expression</a></code>                 |


#### MetaProperty

| Prop           | Type                                                    |
| -------------- | ------------------------------------------------------- |
| **`type`**     | <code>'<a href="#metaproperty">MetaProperty</a>'</code> |
| **`meta`**     | <code><a href="#identifier">Identifier</a></code>       |
| **`property`** | <code><a href="#identifier">Identifier</a></code>       |


#### ObjectExpression

| Prop             | Type                                                                                                                          |
| ---------------- | ----------------------------------------------------------------------------------------------------------------------------- |
| **`type`**       | <code>'<a href="#objectexpression">ObjectExpression</a>'</code>                                                               |
| **`properties`** | <code><a href="#array">Array</a>&lt;<a href="#property">Property</a> \| <a href="#spreadelement">SpreadElement</a>&gt;</code> |


#### Property

| Prop            | Type                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| --------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`type`**      | <code>'<a href="#property">Property</a>'</code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                     |
| **`key`**       | <code><a href="#expression">Expression</a> \| <a href="#privateidentifier">PrivateIdentifier</a></code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                             |
| **`value`**     | <code><a href="#classexpression">ClassExpression</a> \| <a href="#arrayexpression">ArrayExpression</a> \| <a href="#arrowfunctionexpression">ArrowFunctionExpression</a> \| <a href="#assignmentexpression">AssignmentExpression</a> \| <a href="#awaitexpression">AwaitExpression</a> \| <a href="#binaryexpression">BinaryExpression</a> \| <a href="#simplecallexpression">SimpleCallExpression</a> \| <a href="#newexpression">NewExpression</a> \| <a href="#chainexpression">ChainExpression</a> \| <a href="#conditionalexpression">ConditionalExpression</a> \| <a href="#functionexpression">FunctionExpression</a> \| <a href="#identifier">Identifier</a> \| <a href="#importexpression">ImportExpression</a> \| <a href="#simpleliteral">SimpleLiteral</a> \| <a href="#regexpliteral">RegExpLiteral</a> \| <a href="#bigintliteral">BigIntLiteral</a> \| <a href="#logicalexpression">LogicalExpression</a> \| <a href="#memberexpression">MemberExpression</a> \| <a href="#metaproperty">MetaProperty</a> \| <a href="#objectexpression">ObjectExpression</a> \| <a href="#sequenceexpression">SequenceExpression</a> \| <a href="#taggedtemplateexpression">TaggedTemplateExpression</a> \| <a href="#templateliteral">TemplateLiteral</a> \| <a href="#thisexpression">ThisExpression</a> \| <a href="#unaryexpression">UnaryExpression</a> \| <a href="#updateexpression">UpdateExpression</a> \| <a href="#yieldexpression">YieldExpression</a> \| <a href="#objectpattern">ObjectPattern</a> \| <a href="#arraypattern">ArrayPattern</a> \| <a href="#restelement">RestElement</a> \| <a href="#assignmentpattern">AssignmentPattern</a></code> |
| **`kind`**      | <code>'init' \| 'get' \| 'set'</code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                               |
| **`method`**    | <code>boolean</code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| **`shorthand`** | <code>boolean</code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |
| **`computed`**  | <code>boolean</code>                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                |


#### SequenceExpression

| Prop              | Type                                                                |
| ----------------- | ------------------------------------------------------------------- |
| **`type`**        | <code>'<a href="#sequenceexpression">SequenceExpression</a>'</code> |
| **`expressions`** | <code>Expression[]</code>                                           |


#### TaggedTemplateExpression

| Prop        | Type                                                                            |
| ----------- | ------------------------------------------------------------------------------- |
| **`type`**  | <code>'<a href="#taggedtemplateexpression">TaggedTemplateExpression</a>'</code> |
| **`tag`**   | <code><a href="#expression">Expression</a></code>                               |
| **`quasi`** | <code><a href="#templateliteral">TemplateLiteral</a></code>                     |


#### TemplateLiteral

| Prop              | Type                                                          |
| ----------------- | ------------------------------------------------------------- |
| **`type`**        | <code>'<a href="#templateliteral">TemplateLiteral</a>'</code> |
| **`quasis`**      | <code>TemplateElement[]</code>                                |
| **`expressions`** | <code>Expression[]</code>                                     |


#### TemplateElement

| Prop        | Type                                                          |
| ----------- | ------------------------------------------------------------- |
| **`type`**  | <code>'<a href="#templateelement">TemplateElement</a>'</code> |
| **`tail`**  | <code>boolean</code>                                          |
| **`value`** | <code>{ cooked?: string \| null; raw: string; }</code>        |


#### ThisExpression

| Prop       | Type                                                        |
| ---------- | ----------------------------------------------------------- |
| **`type`** | <code>'<a href="#thisexpression">ThisExpression</a>'</code> |


#### UnaryExpression

| Prop           | Type                                                          |
| -------------- | ------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#unaryexpression">UnaryExpression</a>'</code> |
| **`operator`** | <code><a href="#unaryoperator">UnaryOperator</a></code>       |
| **`prefix`**   | <code>true</code>                                             |
| **`argument`** | <code><a href="#expression">Expression</a></code>             |


#### UpdateExpression

| Prop           | Type                                                            |
| -------------- | --------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#updateexpression">UpdateExpression</a>'</code> |
| **`operator`** | <code><a href="#updateoperator">UpdateOperator</a></code>       |
| **`argument`** | <code><a href="#expression">Expression</a></code>               |
| **`prefix`**   | <code>boolean</code>                                            |


#### YieldExpression

| Prop           | Type                                                          |
| -------------- | ------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#yieldexpression">YieldExpression</a>'</code> |
| **`argument`** | <code><a href="#expression">Expression</a> \| null</code>     |
| **`delegate`** | <code>boolean</code>                                          |


#### StaticBlock

| Prop       | Type                                                  |
| ---------- | ----------------------------------------------------- |
| **`type`** | <code>'<a href="#staticblock">StaticBlock</a>'</code> |


#### EmptyStatement

| Prop       | Type                                                        |
| ---------- | ----------------------------------------------------------- |
| **`type`** | <code>'<a href="#emptystatement">EmptyStatement</a>'</code> |


#### DebuggerStatement

| Prop       | Type                                                              |
| ---------- | ----------------------------------------------------------------- |
| **`type`** | <code>'<a href="#debuggerstatement">DebuggerStatement</a>'</code> |


#### WithStatement

| Prop         | Type                                                      |
| ------------ | --------------------------------------------------------- |
| **`type`**   | <code>'<a href="#withstatement">WithStatement</a>'</code> |
| **`object`** | <code><a href="#expression">Expression</a></code>         |
| **`body`**   | <code><a href="#statement">Statement</a></code>           |


#### ReturnStatement

| Prop           | Type                                                          |
| -------------- | ------------------------------------------------------------- |
| **`type`**     | <code>'<a href="#returnstatement">ReturnStatement</a>'</code> |
| **`argument`** | <code><a href="#expression">Expression</a> \| null</code>     |


#### LabeledStatement

| Prop        | Type                                                            |
| ----------- | --------------------------------------------------------------- |
| **`type`**  | <code>'<a href="#labeledstatement">LabeledStatement</a>'</code> |
| **`label`** | <code><a href="#identifier">Identifier</a></code>               |
| **`body`**  | <code><a href="#statement">Statement</a></code>                 |


#### BreakStatement

| Prop        | Type                                                        |
| ----------- | ----------------------------------------------------------- |
| **`type`**  | <code>'<a href="#breakstatement">BreakStatement</a>'</code> |
| **`label`** | <code><a href="#identifier">Identifier</a> \| null</code>   |


#### ContinueStatement

| Prop        | Type                                                              |
| ----------- | ----------------------------------------------------------------- |
| **`type`**  | <code>'<a href="#continuestatement">ContinueStatement</a>'</code> |
| **`label`** | <code><a href="#identifier">Identifier</a> \| null</code>         |


#### IfStatement

| Prop             | Type                                                    |
| ---------------- | ------------------------------------------------------- |
| **`type`**       | <code>'<a href="#ifstatement">IfStatement</a>'</code>   |
| **`test`**       | <code><a href="#expression">Expression</a></code>       |
| **`consequent`** | <code><a href="#statement">Statement</a></code>         |
| **`alternate`**  | <code><a href="#statement">Statement</a> \| null</code> |


#### SwitchStatement

| Prop               | Type                                                          |
| ------------------ | ------------------------------------------------------------- |
| **`type`**         | <code>'<a href="#switchstatement">SwitchStatement</a>'</code> |
| **`discriminant`** | <code><a href="#expression">Expression</a></code>             |
| **`cases`**        | <code>SwitchCase[]</code>                                     |


#### SwitchCase

| Prop             | Type                                                      |
| ---------------- | --------------------------------------------------------- |
| **`type`**       | <code>'<a href="#switchcase">SwitchCase</a>'</code>       |
| **`test`**       | <code><a href="#expression">Expression</a> \| null</code> |
| **`consequent`** | <code>Statement[]</code>                                  |


#### ThrowStatement

| Prop           | Type                                                        |
| -------------- | ----------------------------------------------------------- |
| **`type`**     | <code>'<a href="#throwstatement">ThrowStatement</a>'</code> |
| **`argument`** | <code><a href="#expression">Expression</a></code>           |


#### TryStatement

| Prop            | Type                                                              |
| --------------- | ----------------------------------------------------------------- |
| **`type`**      | <code>'<a href="#trystatement">TryStatement</a>'</code>           |
| **`block`**     | <code><a href="#blockstatement">BlockStatement</a></code>         |
| **`handler`**   | <code><a href="#catchclause">CatchClause</a> \| null</code>       |
| **`finalizer`** | <code><a href="#blockstatement">BlockStatement</a> \| null</code> |


#### CatchClause

| Prop        | Type                                                      |
| ----------- | --------------------------------------------------------- |
| **`type`**  | <code>'<a href="#catchclause">CatchClause</a>'</code>     |
| **`param`** | <code><a href="#pattern">Pattern</a> \| null</code>       |
| **`body`**  | <code><a href="#blockstatement">BlockStatement</a></code> |


#### WhileStatement

| Prop       | Type                                                        |
| ---------- | ----------------------------------------------------------- |
| **`type`** | <code>'<a href="#whilestatement">WhileStatement</a>'</code> |
| **`test`** | <code><a href="#expression">Expression</a></code>           |
| **`body`** | <code><a href="#statement">Statement</a></code>             |


#### DoWhileStatement

| Prop       | Type                                                            |
| ---------- | --------------------------------------------------------------- |
| **`type`** | <code>'<a href="#dowhilestatement">DoWhileStatement</a>'</code> |
| **`body`** | <code><a href="#statement">Statement</a></code>                 |
| **`test`** | <code><a href="#expression">Expression</a></code>               |


#### ForStatement

| Prop         | Type                                                                                                                |
| ------------ | ------------------------------------------------------------------------------------------------------------------- |
| **`type`**   | <code>'<a href="#forstatement">ForStatement</a>'</code>                                                             |
| **`init`**   | <code><a href="#expression">Expression</a> \| <a href="#variabledeclaration">VariableDeclaration</a> \| null</code> |
| **`test`**   | <code><a href="#expression">Expression</a> \| null</code>                                                           |
| **`update`** | <code><a href="#expression">Expression</a> \| null</code>                                                           |
| **`body`**   | <code><a href="#statement">Statement</a></code>                                                                     |


#### VariableDeclaration

| Prop               | Type                                                                  |
| ------------------ | --------------------------------------------------------------------- |
| **`type`**         | <code>'<a href="#variabledeclaration">VariableDeclaration</a>'</code> |
| **`declarations`** | <code>VariableDeclarator[]</code>                                     |
| **`kind`**         | <code>'var' \| 'let' \| 'const' \| 'using' \| 'await using'</code>    |


#### VariableDeclarator

| Prop       | Type                                                                |
| ---------- | ------------------------------------------------------------------- |
| **`type`** | <code>'<a href="#variabledeclarator">VariableDeclarator</a>'</code> |
| **`id`**   | <code><a href="#pattern">Pattern</a></code>                         |
| **`init`** | <code><a href="#expression">Expression</a> \| null</code>           |


#### ForInStatement

| Prop       | Type                                                        |
| ---------- | ----------------------------------------------------------- |
| **`type`** | <code>'<a href="#forinstatement">ForInStatement</a>'</code> |


#### ForOfStatement

| Prop        | Type                                                        |
| ----------- | ----------------------------------------------------------- |
| **`type`**  | <code>'<a href="#forofstatement">ForOfStatement</a>'</code> |
| **`await`** | <code>boolean</code>                                        |


#### ClassDeclaration

| Prop     | Type                                              | Description                                                                           |
| -------- | ------------------------------------------------- | ------------------------------------------------------------------------------------- |
| **`id`** | <code><a href="#identifier">Identifier</a></code> | It is null when a class declaration is a part of the `export default class` statement |


#### Comment

| Prop        | Type                           |
| ----------- | ------------------------------ |
| **`type`**  | <code>'Line' \| 'Block'</code> |
| **`value`** | <code>string</code>            |


#### ModelResults

| Prop         | Type                                                                                                                                                                                             |
| ------------ | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`onoff`**  | <code><a href="#onoffmodel">OnOffModel</a></code>                                                                                                                                                |
| **`sensor`** | <code>{ get?: <a href="#sensormodel">SensorModel</a>; property?: <a href="#sensorrawvalue">SensorRawValue</a>; properties?: <a href="#sensorpropertiesmodel">SensorPropertiesModel</a>; }</code> |


#### OnOffModel

| Prop        | Type                 |
| ----------- | -------------------- |
| **`state`** | <code>boolean</code> |


#### SensorModel

| Prop         | Type                                                                                        |
| ------------ | ------------------------------------------------------------------------------------------- |
| **`values`** | <code><a href="#array">Array</a>&lt;<a href="#sensorrawvalue">SensorRawValue</a>&gt;</code> |


#### SensorRawValue

| Prop             | Type                                                  |
| ---------------- | ----------------------------------------------------- |
| **`propertyId`** | <code>number</code>                                   |
| **`bytes`**      | <code><a href="#array">Array</a>&lt;number&gt;</code> |


#### SensorPropertiesModel

| Prop             | Type                                                  |
| ---------------- | ----------------------------------------------------- |
| **`properties`** | <code><a href="#array">Array</a>&lt;number&gt;</code> |


#### ModelOptions

| Prop                 | Type                                                                                                                                                                                                                                                                                                                 |
| -------------------- | -------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| **`elementAddress`** | <code>number</code>                                                                                                                                                                                                                                                                                                  |
| **`appkeyIndex`**    | <code>number</code>                                                                                                                                                                                                                                                                                                  |
| **`onoff`**          | <code>{ set?: <a href="#onoffmodel">OnOffModel</a>; }</code>                                                                                                                                                                                                                                                         |
| **`sensor`**         | <code>{ propertyId?: number; get?: <a href="#omit">Omit</a>&lt;<a href="#sensormodel">SensorModel</a>, 'values'&gt;; property?: <a href="#partialany">PartialAny</a>&lt;<a href="#sensorrawvalue">SensorRawValue</a>, 'bytes'&gt;; properties?: <a href="#sensorpropertiesmodel">SensorPropertiesModel</a>; }</code> |


#### ScanResults

| Prop          | Type                                                                                |
| ------------- | ----------------------------------------------------------------------------------- |
| **`devices`** | <code><a href="#array">Array</a>&lt;<a href="#meshdevice">MeshDevice</a>&gt;</code> |


#### MeshDevice

| Prop              | Type                 |
| ----------------- | -------------------- |
| **`address`**     | <code>string</code>  |
| **`name`**        | <code>string</code>  |
| **`provisioned`** | <code>boolean</code> |
| **`rssi`**        | <code>number</code>  |
| **`uuid`**        | <code>string</code>  |


#### ScanOptions

| Prop         | Type                                                   |
| ------------ | ------------------------------------------------------ |
| **`filter`** | <code>'all' \| 'provisioned' \| 'unprovisioned'</code> |
| **`max`**    | <code>number</code>                                    |


#### IdentifyResults

| Prop                    | Type                                                  |
| ----------------------- | ----------------------------------------------------- |
| **`numberOfElements`**  | <code>number</code>                                   |
| **`availableOOBTypes`** | <code><a href="#array">Array</a>&lt;string&gt;</code> |
| **`algorithms`**        | <code>number</code>                                   |
| **`publicKeyType`**     | <code>number</code>                                   |
| **`staticOobTypes`**    | <code>number</code>                                   |
| **`outputOobSize`**     | <code>number</code>                                   |
| **`outputOobActions`**  | <code>number</code>                                   |
| **`inputOobSize`**      | <code>number</code>                                   |
| **`inputOobActions`**   | <code>number</code>                                   |


#### IdentifyOptions

| Prop          | Type                |
| ------------- | ------------------- |
| **`address`** | <code>string</code> |
| **`uuid`**    | <code>string</code> |


#### ProvisionResults

| Prop                 | Type                 |
| -------------------- | -------------------- |
| **`completed`**      | <code>boolean</code> |
| **`uuid`**           | <code>string</code>  |
| **`unicastAddress`** | <code>number</code>  |


#### ProvisionOptions

| Prop          | Type                |
| ------------- | ------------------- |
| **`address`** | <code>string</code> |
| **`uuid`**    | <code>string</code> |


#### CompositionResults

| Prop                        | Type                                                                                                                                                                                                                                                                                                         |
| --------------------------- | ------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| **`companyIdentifier`**     | <code>string</code>                                                                                                                                                                                                                                                                                          |
| **`productIdentifier`**     | <code>string</code>                                                                                                                                                                                                                                                                                          |
| **`productVersion`**        | <code>string</code>                                                                                                                                                                                                                                                                                          |
| **`nodeFeaturesSupported`** | <code>{ relay: boolean; proxy: boolean; friend: boolean; lowPower: boolean; }</code>                                                                                                                                                                                                                         |
| **`elements`**              | <code><a href="#array">Array</a>&lt;{ name: string; elementAddress: number; sigModelCount: number; vendorModelCount: number; location: number; models: <a href="#array">Array</a>&lt;{ modelId: number; modelName: string; boundAppKeyIndexes: <a href="#array">Array</a>&lt;number&gt;; }&gt;; }&gt;</code> |


#### CompositionOptions

| Prop                 | Type                |
| -------------------- | ------------------- |
| **`unicastAddress`** | <code>number</code> |


#### PluginListenerHandle

| Prop         | Type                                      |
| ------------ | ----------------------------------------- |
| **`remove`** | <code>() =&gt; Promise&lt;void&gt;</code> |


#### OnStateChangeEvent

| Prop         | Type                                  |
| ------------ | ------------------------------------- |
| **`action`** | <code>'enabled' \| 'connected'</code> |
| **`state`**  | <code>boolean</code>                  |


#### OnNodeChangeEvent

| Prop                 | Type                                       |
| -------------------- | ------------------------------------------ |
| **`action`**         | <code>'del' \| 'add' \| 'heartbeat'</code> |
| **`unicastAddress`** | <code>number</code>                        |


### Type Aliases


#### Permissions

<code>{ [key in PermissionKey]: <a href="#permissionstate">PermissionState</a>; }</code>


#### PermissionKey

<code>"android.permission.BLUETOOTH" | "android.permission.BLUETOOTH_ADMIN" | "android.permission.ACCESS_COARSE_LOCATION" | "android.permission.ACCESS_FINE_LOCATION" | "android.permission.BLUETOOTH_SCAN" | "android.permission.BLUETOOTH_CONNECT"</code>


#### PermissionState

<code>'prompt' | 'prompt-with-rationale' | 'granted' | 'denied'</code>


#### Omit

Construct a type with the properties of T except for those in type K.

<code><a href="#pick">Pick</a>&lt;T, <a href="#exclude">Exclude</a>&lt;keyof T, K&gt;&gt;</code>


#### Pick

From T, pick a set of properties whose keys are in the union K

<code>{ [P in K]: T[P]; }</code>


#### Exclude

<a href="#exclude">Exclude</a> from T those types that are assignable to U

<code>T extends U ? never : T</code>


#### Partial

Make all properties in T optional

<code>{ [P in keyof T]?: T[P]; }</code>


#### PropertyKey

<code>string | number | symbol</code>


#### Function

<code><a href="#functiondeclaration">FunctionDeclaration</a> | <a href="#functionexpression">FunctionExpression</a> | <a href="#arrowfunctionexpression">ArrowFunctionExpression</a></code>


#### Statement

<code><a href="#expressionstatement">ExpressionStatement</a> | <a href="#blockstatement">BlockStatement</a> | <a href="#staticblock">StaticBlock</a> | <a href="#emptystatement">EmptyStatement</a> | <a href="#debuggerstatement">DebuggerStatement</a> | <a href="#withstatement">WithStatement</a> | <a href="#returnstatement">ReturnStatement</a> | <a href="#labeledstatement">LabeledStatement</a> | <a href="#breakstatement">BreakStatement</a> | <a href="#continuestatement">ContinueStatement</a> | <a href="#ifstatement">IfStatement</a> | <a href="#switchstatement">SwitchStatement</a> | <a href="#throwstatement">ThrowStatement</a> | <a href="#trystatement">TryStatement</a> | <a href="#whilestatement">WhileStatement</a> | <a href="#dowhilestatement">DoWhileStatement</a> | <a href="#forstatement">ForStatement</a> | <a href="#forinstatement">ForInStatement</a> | <a href="#forofstatement">ForOfStatement</a> | <a href="#declaration">Declaration</a></code>


#### Expression

<code>ExpressionMap[keyof ExpressionMap]</code>


#### AssignmentOperator

<code>"=" | "+=" | "-=" | "*=" | "/=" | "%=" | "**=" | "&lt;&lt;=" | "&gt;&gt;=" | "&gt;&gt;&gt;=" | "|=" | "^=" | "&=" | "||=" | "&&=" | "??="</code>


#### Pattern

<code><a href="#identifier">Identifier</a> | <a href="#objectpattern">ObjectPattern</a> | <a href="#arraypattern">ArrayPattern</a> | <a href="#restelement">RestElement</a> | <a href="#assignmentpattern">AssignmentPattern</a> | <a href="#memberexpression">MemberExpression</a></code>


#### BinaryOperator

<code>"==" | "!=" | "===" | "!==" | "&lt;" | "&lt;=" | "&gt;" | "&gt;=" | "&lt;&lt;" | "&gt;&gt;" | "&gt;&gt;&gt;" | "+" | "-" | "*" | "/" | "%" | "**" | "|" | "^" | "&" | "in" | "instanceof"</code>


#### CallExpression

<code><a href="#simplecallexpression">SimpleCallExpression</a> | <a href="#newexpression">NewExpression</a></code>


#### ChainElement

<code><a href="#simplecallexpression">SimpleCallExpression</a> | <a href="#memberexpression">MemberExpression</a></code>


#### Literal

<code><a href="#simpleliteral">SimpleLiteral</a> | <a href="#regexpliteral">RegExpLiteral</a> | <a href="#bigintliteral">BigIntLiteral</a></code>


#### LogicalOperator

<code>"||" | "&&" | "??"</code>


#### UnaryOperator

<code>"-" | "+" | "!" | "~" | "typeof" | "void" | "delete"</code>


#### UpdateOperator

<code>"++" | "--"</code>


#### Declaration

<code><a href="#functiondeclaration">FunctionDeclaration</a> | <a href="#variabledeclaration">VariableDeclaration</a> | <a href="#classdeclaration">ClassDeclaration</a></code>


#### PartialAny

<code><a href="#omit">Omit</a>&lt;T, K&gt; & <a href="#partial">Partial</a>&lt;<a href="#pick">Pick</a>&lt;T, K&gt;&gt;</code>

</docgen-api>
